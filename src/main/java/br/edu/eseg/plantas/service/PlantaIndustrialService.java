package br.edu.eseg.plantas.service;

import br.edu.eseg.plantas.dto.DashboardKPIsDTO;
import br.edu.eseg.plantas.dto.PlantaFormDTO;
import br.edu.eseg.plantas.dto.TransicaoStatusDTO;
import br.edu.eseg.plantas.exception.RecursoNaoEncontradoException;
import br.edu.eseg.plantas.exception.RegraNegocioException;
import br.edu.eseg.plantas.exception.TransicaoStatusInvalidaException;
import br.edu.eseg.plantas.model.*;
import br.edu.eseg.plantas.repository.*;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PlantaIndustrialService {

    private final PlantaIndustrialRepository plantaRepository;
    private final UnidadeFederativaRepository ufRepository;
    private final LinhaProducaoRepository linhaRepository;
    private final HistoricoStatusPlantaRepository historicoRepository;

    public PlantaIndustrialService(PlantaIndustrialRepository plantaRepository,
                                   UnidadeFederativaRepository ufRepository,
                                   LinhaProducaoRepository linhaRepository,
                                   HistoricoStatusPlantaRepository historicoRepository) {
        this.plantaRepository = plantaRepository;
        this.ufRepository = ufRepository;
        this.linhaRepository = linhaRepository;
        this.historicoRepository = historicoRepository;
    }

    @Transactional(readOnly = true)
    public List<PlantaIndustrial> listarComFiltros(String termo, Long ufId, NivelAutomacao nivel,
                                                   StatusPlanta status, Boolean iso9001) {
        Specification<PlantaIndustrial> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (termo != null && !termo.isBlank()) {
                String pattern = "%" + termo.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("nome")), pattern),
                        cb.like(cb.lower(root.get("cidade")), pattern),
                        cb.like(cb.lower(root.get("codigoPlanta")), pattern)
                ));
            }
            if (ufId != null) {
                predicates.add(cb.equal(root.get("uf").get("id"), ufId));
            }
            if (nivel != null) {
                predicates.add(cb.equal(root.get("nivelAutomacao"), nivel));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (iso9001 != null) {
                predicates.add(cb.equal(root.get("certificacaoIso9001"), iso9001));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return plantaRepository.findAll(spec, Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional(readOnly = true)
    public PlantaIndustrial buscarPorId(Long id) {
        return plantaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Planta industrial não encontrada com ID: " + id));
    }

    @Transactional
    public PlantaIndustrial criar(PlantaFormDTO dto, String usuarioResponsavel) {
        String codigoNormalizado = dto.getCodigoPlanta().trim().toUpperCase();
        String nomeNormalizado = dto.getNome().trim();

        if (plantaRepository.existsByCodigoPlantaIgnoreCase(codigoNormalizado)) {
            throw new RegraNegocioException("Já existe uma planta cadastrada com o código '" + codigoNormalizado + "'.");
        }
        if (plantaRepository.existsByNomeIgnoreCase(nomeNormalizado)) {
            throw new RegraNegocioException("Já existe uma planta cadastrada com o nome '" + nomeNormalizado + "'.");
        }

        UnidadeFederativa uf = ufRepository.findById(dto.getUfId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("UF inválida selecionada."));

        PlantaIndustrial planta = new PlantaIndustrial();
        aplicarDadosForm(planta, dto, uf, codigoNormalizado, nomeNormalizado);

        StatusPlanta statusInicial = (dto.getStatusInicial() == StatusPlanta.ATIVA
                || dto.getStatusInicial() == StatusPlanta.EM_IMPLANTACAO)
                ? dto.getStatusInicial()
                : StatusPlanta.EM_IMPLANTACAO;
        planta.setStatus(statusInicial);

        PlantaIndustrial salva = plantaRepository.save(planta);

        HistoricoStatusPlanta historico = new HistoricoStatusPlanta(
                salva,
                null,
                statusInicial,
                "Cadastro inicial da planta industrial no sistema.",
                usuarioResponsavel
        );
        historicoRepository.save(historico);

        return salva;
    }

    @Transactional
    public PlantaIndustrial atualizar(Long id, PlantaFormDTO dto) {
        PlantaIndustrial planta = buscarPorId(id);

        if (planta.getStatus() == StatusPlanta.DESATIVADA) {
            throw new RegraNegocioException("Não é permitido editar dados cadastrais de uma planta DESATIVADA.");
        }

        String codigoNormalizado = dto.getCodigoPlanta().trim().toUpperCase();
        String nomeNormalizado = dto.getNome().trim();

        if (plantaRepository.existsByCodigoPlantaIgnoreCaseAndIdNot(codigoNormalizado, id)) {
            throw new RegraNegocioException("Outro registro já utiliza o código '" + codigoNormalizado + "'.");
        }
        if (plantaRepository.existsByNomeIgnoreCaseAndIdNot(nomeNormalizado, id)) {
            throw new RegraNegocioException("Outro registro já utiliza o nome '" + nomeNormalizado + "'.");
        }

        UnidadeFederativa uf = ufRepository.findById(dto.getUfId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("UF inválida selecionada."));

        aplicarDadosForm(planta, dto, uf, codigoNormalizado, nomeNormalizado);
        return plantaRepository.save(planta);
    }

    @Transactional
    public PlantaIndustrial alterarStatus(Long id, TransicaoStatusDTO dto, String usuarioResponsavel) {
        PlantaIndustrial planta = buscarPorId(id);
        StatusPlanta statusAtual = planta.getStatus();
        StatusPlanta novoStatus = dto.getNovoStatus();

        if (!statusAtual.podeTransicionarPara(novoStatus)) {
            throw new TransicaoStatusInvalidaException(
                    "Transição inválida: não é permitido alterar o status de '"
                            + statusAtual.getDescricao() + "' para '"
                            + (novoStatus != null ? novoStatus.getDescricao() : "Nulo") + "'."
            );
        }

        // Regra de integridade de dominio: nao desativar planta com linhas de producao ativas
        if (novoStatus == StatusPlanta.DESATIVADA) {
            long linhasAtivas = linhaRepository.countByPlantaIdAndAtivaTrue(id);
            if (linhasAtivas > 0) {
                throw new RegraNegocioException(
                        "Não é possível desativar a planta enquanto houver " + linhasAtivas
                                + " linha(s) de produção ativa(s). Inative as linhas primeiro."
                );
            }
        }

        planta.setStatus(novoStatus);
        PlantaIndustrial atualizada = plantaRepository.save(planta);

        HistoricoStatusPlanta registro = new HistoricoStatusPlanta(
                atualizada,
                statusAtual,
                novoStatus,
                dto.getJustificativa().trim(),
                usuarioResponsavel
        );
        historicoRepository.save(registro);

        return atualizada;
    }

    @Transactional
    public void excluir(Long id) {
        PlantaIndustrial planta = buscarPorId(id);

        if (planta.getStatus() != StatusPlanta.DESATIVADA) {
            throw new RegraNegocioException(
                    "Por segurança operacional, somente plantas com status DESATIVADA podem ser excluídas. "
                            + "Status atual: " + planta.getStatus().getDescricao() + "."
            );
        }

        long linhasAtivas = linhaRepository.countByPlantaIdAndAtivaTrue(id);
        if (linhasAtivas > 0) {
            throw new RegraNegocioException("Não é possível excluir planta com linhas de produção ativas.");
        }

        plantaRepository.delete(planta);
    }

    @Transactional(readOnly = true)
    public DashboardKPIsDTO calcularKPIs() {
        List<PlantaIndustrial> todas = plantaRepository.findAll();
        DashboardKPIsDTO kpis = new DashboardKPIsDTO();

        long total = todas.size();
        kpis.setTotalPlantas(total);
        kpis.setPlantasAtivas(todas.stream().filter(p -> p.getStatus() == StatusPlanta.ATIVA).count());

        long isoCount = todas.stream().filter(p -> Boolean.TRUE.equals(p.getCertificacaoIso9001())).count();
        double pctIso = total == 0 ? 0.0 : Math.round((isoCount * 1000.0) / total) / 10.0;
        kpis.setPercentualIso9001(pctIso);

        kpis.setTotalFuncionarios(todas.stream().mapToLong(PlantaIndustrial::getFuncionarios).sum());
        kpis.setTotalLinhasAutomatizadas(todas.stream().mapToLong(PlantaIndustrial::getLinhasAutomatizadas).sum());
        kpis.setAreaTotalM2(todas.stream().mapToLong(PlantaIndustrial::getAreaM2).sum());

        Map<String, Long> porRegiao = new LinkedHashMap<>();
        Map<String, Long> porNivel = new LinkedHashMap<>();
        Map<String, Long> porStatus = new LinkedHashMap<>();

        for (NivelAutomacao n : NivelAutomacao.values()) {
            porNivel.put(n.getDescricao(), 0L);
        }
        for (StatusPlanta s : StatusPlanta.values()) {
            porStatus.put(s.getDescricao(), 0L);
        }

        for (PlantaIndustrial p : todas) {
            String regiao = p.getUf().getRegiao();
            porRegiao.merge(regiao, 1L, Long::sum);
            porNivel.merge(p.getNivelAutomacao().getDescricao(), 1L, Long::sum);
            porStatus.merge(p.getStatus().getDescricao(), 1L, Long::sum);
        }

        kpis.setPorRegiao(porRegiao);
        kpis.setPorNivelAutomacao(porNivel);
        kpis.setPorStatus(porStatus);
        return kpis;
    }

    @Transactional(readOnly = true)
    public List<UnidadeFederativa> listarUfs() {
        return ufRepository.findAllByOrderBySiglaAsc();
    }

    @Transactional(readOnly = true)
    public List<HistoricoStatusPlanta> listarHistoricoDaPlanta(Long plantaId) {
        return historicoRepository.findByPlantaIdOrderByAlteradoEmDesc(plantaId);
    }

    @Transactional(readOnly = true)
    public List<HistoricoStatusPlanta> listarHistoricoGeral() {
        return historicoRepository.findAllByOrderByAlteradoEmDesc();
    }

    private void aplicarDadosForm(PlantaIndustrial planta, PlantaFormDTO dto, UnidadeFederativa uf,
                                  String codigoNormalizado, String nomeNormalizado) {
        planta.setCodigoPlanta(codigoNormalizado);
        planta.setNome(nomeNormalizado);
        planta.setCidade(dto.getCidade().trim());
        planta.setUf(uf);
        planta.setLatitude(dto.getLatitude());
        planta.setLongitude(dto.getLongitude());
        planta.setAnoInicioOperacao(dto.getAnoInicioOperacao());
        planta.setAreaM2(dto.getAreaM2());
        planta.setFuncionarios(dto.getFuncionarios());
        planta.setNivelAutomacao(dto.getNivelAutomacao());
        planta.setLinhasAutomatizadas(dto.getLinhasAutomatizadas());
        planta.setCertificacaoIso9001(Boolean.TRUE.equals(dto.getCertificacaoIso9001()));
    }
}
