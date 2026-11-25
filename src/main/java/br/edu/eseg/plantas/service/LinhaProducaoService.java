package br.edu.eseg.plantas.service;

import br.edu.eseg.plantas.dto.LinhaProducaoFormDTO;
import br.edu.eseg.plantas.exception.RecursoNaoEncontradoException;
import br.edu.eseg.plantas.exception.RegraNegocioException;
import br.edu.eseg.plantas.model.LinhaProducao;
import br.edu.eseg.plantas.model.PlantaIndustrial;
import br.edu.eseg.plantas.model.StatusPlanta;
import br.edu.eseg.plantas.repository.LinhaProducaoRepository;
import br.edu.eseg.plantas.repository.PlantaIndustrialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LinhaProducaoService {

    private final LinhaProducaoRepository linhaRepository;
    private final PlantaIndustrialRepository plantaRepository;

    public LinhaProducaoService(LinhaProducaoRepository linhaRepository,
                                PlantaIndustrialRepository plantaRepository) {
        this.linhaRepository = linhaRepository;
        this.plantaRepository = plantaRepository;
    }

    @Transactional(readOnly = true)
    public List<LinhaProducao> listarPorPlanta(Long plantaId) {
        return linhaRepository.findByPlantaIdOrderByCodigoLinhaAsc(plantaId);
    }

    @Transactional
    public LinhaProducao adicionarLinha(Long plantaId, LinhaProducaoFormDTO dto) {
        PlantaIndustrial planta = plantaRepository.findById(plantaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Planta não encontrada com ID: " + plantaId));

        if (planta.getStatus() == StatusPlanta.DESATIVADA) {
            throw new RegraNegocioException("Não é possível adicionar linhas de produção em uma planta DESATIVADA.");
        }

        String codigo = dto.getCodigoLinha().trim().toUpperCase();
        if (linhaRepository.existsByPlantaIdAndCodigoLinhaIgnoreCase(plantaId, codigo)) {
            throw new RegraNegocioException("Já existe uma linha com o código '" + codigo + "' nesta planta.");
        }

        LinhaProducao linha = new LinhaProducao();
        linha.setCodigoLinha(codigo);
        linha.setNome(dto.getNome().trim());
        linha.setTipoAutomacao(dto.getTipoAutomacao().trim());
        linha.setCapacidadeHora(dto.getCapacidadeHora());
        linha.setAtiva(true);
        linha.setPlanta(planta);

        return linhaRepository.save(linha);
    }

    @Transactional
    public LinhaProducao alternarStatusAtiva(Long plantaId, Long linhaId) {
        LinhaProducao linha = linhaRepository.findById(linhaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Linha de produção não encontrada."));

        if (!linha.getPlanta().getId().equals(plantaId)) {
            throw new RegraNegocioException("A linha informada não pertence à planta especificada.");
        }
        if (linha.getPlanta().getStatus() == StatusPlanta.DESATIVADA && !Boolean.TRUE.equals(linha.getAtiva())) {
            throw new RegraNegocioException("Não é possível reativar uma linha em uma planta DESATIVADA.");
        }

        linha.setAtiva(!Boolean.TRUE.equals(linha.getAtiva()));
        return linhaRepository.save(linha);
    }

    @Transactional
    public void excluirLinha(Long plantaId, Long linhaId) {
        LinhaProducao linha = linhaRepository.findById(linhaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Linha de produção não encontrada."));

        if (!linha.getPlanta().getId().equals(plantaId)) {
            throw new RegraNegocioException("A linha informada não pertence à planta especificada.");
        }
        linhaRepository.delete(linha);
    }
}
