package br.edu.eseg.plantas.config;

import br.edu.eseg.plantas.model.*;
import br.edu.eseg.plantas.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Carga inicial idempotente que realiza a migracao fiel dos 10 registros da base legada
 * Plantas_Industriais.csv para o modelo normalizado, alem de cadastrar UFs, usuarios e
 * linhas de producao demonstrativas.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UnidadeFederativaRepository ufRepository;
    private final PlantaIndustrialRepository plantaRepository;
    private final LinhaProducaoRepository linhaRepository;
    private final HistoricoStatusPlantaRepository historicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UnidadeFederativaRepository ufRepository,
                           PlantaIndustrialRepository plantaRepository,
                           LinhaProducaoRepository linhaRepository,
                           HistoricoStatusPlantaRepository historicoRepository,
                           UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.ufRepository = ufRepository;
        this.plantaRepository = plantaRepository;
        this.linhaRepository = linhaRepository;
        this.historicoRepository = historicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Map<String, UnidadeFederativa> ufs = carregarUfs();
        carregarUsuarios();
        carregarPlantasLegadas(ufs);
    }

    private Map<String, UnidadeFederativa> carregarUfs() {
        if (ufRepository.count() == 0) {
            List<UnidadeFederativa> lista = List.of(
                new UnidadeFederativa("AC", "Acre", "Norte"),
                new UnidadeFederativa("AL", "Alagoas", "Nordeste"),
                new UnidadeFederativa("AP", "Amapá", "Norte"),
                new UnidadeFederativa("AM", "Amazonas", "Norte"),
                new UnidadeFederativa("BA", "Bahia", "Nordeste"),
                new UnidadeFederativa("CE", "Ceará", "Nordeste"),
                new UnidadeFederativa("DF", "Distrito Federal", "Centro-Oeste"),
                new UnidadeFederativa("ES", "Espírito Santo", "Sudeste"),
                new UnidadeFederativa("GO", "Goiás", "Centro-Oeste"),
                new UnidadeFederativa("MA", "Maranhão", "Nordeste"),
                new UnidadeFederativa("MT", "Mato Grosso", "Centro-Oeste"),
                new UnidadeFederativa("MS", "Mato Grosso do Sul", "Centro-Oeste"),
                new UnidadeFederativa("MG", "Minas Gerais", "Sudeste"),
                new UnidadeFederativa("PA", "Pará", "Norte"),
                new UnidadeFederativa("PB", "Paraíba", "Nordeste"),
                new UnidadeFederativa("PR", "Paraná", "Sul"),
                new UnidadeFederativa("PE", "Pernambuco", "Nordeste"),
                new UnidadeFederativa("PI", "Piauí", "Nordeste"),
                new UnidadeFederativa("RJ", "Rio de Janeiro", "Sudeste"),
                new UnidadeFederativa("RN", "Rio Grande do Norte", "Nordeste"),
                new UnidadeFederativa("RS", "Rio Grande do Sul", "Sul"),
                new UnidadeFederativa("RO", "Rondônia", "Norte"),
                new UnidadeFederativa("RR", "Roraima", "Norte"),
                new UnidadeFederativa("SC", "Santa Catarina", "Sul"),
                new UnidadeFederativa("SP", "São Paulo", "Sudeste"),
                new UnidadeFederativa("SE", "Sergipe", "Nordeste"),
                new UnidadeFederativa("TO", "Tocantins", "Norte")
            );
            ufRepository.saveAll(lista);
        }

        Map<String, UnidadeFederativa> map = new HashMap<>();
        for (UnidadeFederativa uf : ufRepository.findAll()) {
            map.put(uf.getSigla(), uf);
        }
        return map;
    }

    private void carregarUsuarios() {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(new Usuario(
                "admin",
                "Gestor Industrial",
                passwordEncoder.encode("admin123"),
                PerfilUsuario.ADMIN
            ));
            usuarioRepository.save(new Usuario(
                "operador",
                "Operador de Planta",
                passwordEncoder.encode("operador123"),
                PerfilUsuario.OPERADOR
            ));
        }
    }

    private void carregarPlantasLegadas(Map<String, UnidadeFederativa> ufs) {
        if (plantaRepository.count() == 0) {
            criarPlantaLegada("PLT-SP-001", "Automax Industrial", "São Bernardo do Campo", "SP", new BigDecimal("-23.69"), new BigDecimal("-46.56"), 2014, 57921, 500, NivelAutomacao.MEDIO, 25, true, ufs);
            criarPlantaLegada("PLT-SP-002", "Fábrica Digital ABC", "São Paulo", "SP", new BigDecimal("-23.55"), new BigDecimal("-46.63"), 2012, 56435, 1653, NivelAutomacao.ALTO, 16, false, ufs);
            criarPlantaLegada("PLT-SP-003", "Indústria Conecta", "Campinas", "SP", new BigDecimal("-22.91"), new BigDecimal("-47.06"), 2008, 46232, 1114, NivelAutomacao.ALTO, 9, true, ufs);
            criarPlantaLegada("PLT-SC-004", "NeoAutomação", "Joinville", "SC", new BigDecimal("-26.30"), new BigDecimal("-48.85"), 2007, 54478, 1498, NivelAutomacao.ALTO, 24, true, ufs);
            criarPlantaLegada("PLT-PR-005", "SmartFactory Sul", "Curitiba", "PR", new BigDecimal("-25.43"), new BigDecimal("-49.27"), 2004, 36483, 735, NivelAutomacao.MEDIO, 10, true, ufs);
            criarPlantaLegada("PLT-RS-006", "Robótica Vale", "Caxias do Sul", "RS", new BigDecimal("-29.17"), new BigDecimal("-51.18"), 2018, 12631, 980, NivelAutomacao.MEDIO, 4, true, ufs);
            criarPlantaLegada("PLT-MG-007", "TecnoProcessos", "Betim", "MG", new BigDecimal("-19.97"), new BigDecimal("-44.20"), 2005, 35528, 662, NivelAutomacao.MEDIO, 5, true, ufs);
            criarPlantaLegada("PLT-SP-008", "Linha 4.0 Brasil", "Sorocaba", "SP", new BigDecimal("-23.50"), new BigDecimal("-47.45"), 2003, 20707, 2106, NivelAutomacao.MUITO_ALTO, 7, true, ufs);
            criarPlantaLegada("PLT-AM-009", "ControleMax", "Manaus", "AM", new BigDecimal("-3.12"), new BigDecimal("-60.02"), 2004, 70174, 403, NivelAutomacao.MUITO_ALTO, 21, false, ufs);
            criarPlantaLegada("PLT-PE-010", "Sistemas Industriais Norte", "Recife", "PE", new BigDecimal("-8.05"), new BigDecimal("-34.88"), 2014, 51686, 756, NivelAutomacao.MUITO_ALTO, 17, false, ufs);

            // Linhas de producao de exemplo vinculadas as primeiras plantas para demonstrar relacionamento JPA 1:N
            List<PlantaIndustrial> plantas = plantaRepository.findAll();
            if (plantas.size() >= 3) {
                criarLinhaExemplo(plantas.get(0), "LN-AMX-01", "Montagem Robotizada Eixo A", "Robótica 6 Eixos + CLP", 240);
                criarLinhaExemplo(plantas.get(0), "LN-AMX-02", "Inspeção Óptica Automatizada", "Visão Computacional", 320);
                criarLinhaExemplo(plantas.get(1), "LN-FDA-01", "Estamparia Digital Contínua", "SCADA + IIoT", 180);
                criarLinhaExemplo(plantas.get(2), "LN-INC-01", "Empacotamento Autônomo", "AGV + CLP Siemens", 410);
            }
        }
    }

    private void criarPlantaLegada(String codigo, String nome, String cidade, String siglaUf,
                                   BigDecimal lat, BigDecimal lon, int ano, int area,
                                   int funcionarios, NivelAutomacao nivel, int linhas,
                                   boolean iso9001, Map<String, UnidadeFederativa> ufs) {
        PlantaIndustrial p = new PlantaIndustrial();
        p.setCodigoPlanta(codigo);
        p.setNome(nome);
        p.setCidade(cidade);
        p.setUf(ufs.get(siglaUf));
        p.setLatitude(lat);
        p.setLongitude(lon);
        p.setAnoInicioOperacao(ano);
        p.setAreaM2(area);
        p.setFuncionarios(funcionarios);
        p.setNivelAutomacao(nivel);
        p.setLinhasAutomatizadas(linhas);
        p.setCertificacaoIso9001(iso9001);
        p.setStatus(StatusPlanta.ATIVA);

        PlantaIndustrial salva = plantaRepository.save(p);
        historicoRepository.save(new HistoricoStatusPlanta(
            salva,
            null,
            StatusPlanta.ATIVA,
            "Migração inicial da base legada Plantas_Industriais.csv com normalização de tipos e UF.",
            "sistema-migracao"
        ));
    }

    private void criarLinhaExemplo(PlantaIndustrial planta, String codigo, String nome,
                                   String tipo, int capacidade) {
        LinhaProducao linha = new LinhaProducao();
        linha.setPlanta(planta);
        linha.setCodigoLinha(codigo);
        linha.setNome(nome);
        linha.setTipoAutomacao(tipo);
        linha.setCapacidadeHora(capacidade);
        linha.setAtiva(true);
        linhaRepository.save(linha);
    }
}
