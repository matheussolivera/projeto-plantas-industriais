package br.edu.eseg.plantas.service;

import br.edu.eseg.plantas.dto.PlantaFormDTO;
import br.edu.eseg.plantas.dto.TransicaoStatusDTO;
import br.edu.eseg.plantas.exception.RegraNegocioException;
import br.edu.eseg.plantas.exception.TransicaoStatusInvalidaException;
import br.edu.eseg.plantas.model.*;
import br.edu.eseg.plantas.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlantaIndustrialServiceTest {

    @Mock
    private PlantaIndustrialRepository plantaRepository;

    @Mock
    private UnidadeFederativaRepository ufRepository;

    @Mock
    private LinhaProducaoRepository linhaRepository;

    @Mock
    private HistoricoStatusPlantaRepository historicoRepository;

    @InjectMocks
    private PlantaIndustrialService service;

    private UnidadeFederativa ufSp;
    private PlantaIndustrial plantaAtiva;

    @BeforeEach
    void setup() {
        ufSp = new UnidadeFederativa("SP", "São Paulo", "Sudeste");
        ufSp.setId(1L);

        plantaAtiva = new PlantaIndustrial();
        plantaAtiva.setId(10L);
        plantaAtiva.setCodigoPlanta("PLT-SP-001");
        plantaAtiva.setNome("Automax Industrial");
        plantaAtiva.setCidade("São Bernardo do Campo");
        plantaAtiva.setUf(ufSp);
        plantaAtiva.setLatitude(new BigDecimal("-23.69"));
        plantaAtiva.setLongitude(new BigDecimal("-46.56"));
        plantaAtiva.setAnoInicioOperacao(2014);
        plantaAtiva.setAreaM2(57921);
        plantaAtiva.setFuncionarios(500);
        plantaAtiva.setNivelAutomacao(NivelAutomacao.MEDIO);
        plantaAtiva.setLinhasAutomatizadas(25);
        plantaAtiva.setCertificacaoIso9001(true);
        plantaAtiva.setStatus(StatusPlanta.ATIVA);
    }

    @Test
    @DisplayName("Deve impedir cadastro quando codigo da planta ja existe")
    void deveImpedirCadastroCodigoDuplicado() {
        PlantaFormDTO dto = criarDtoValido();
        when(plantaRepository.existsByCodigoPlantaIgnoreCase("PLT-SP-099")).thenReturn(true);

        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> service.criar(dto, "admin"));

        assertTrue(ex.getMessage().contains("código"));
        verify(plantaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve realizar transicao valida de status e registrar historico de auditoria")
    void deveAlterarStatusValidoERegistrarHistorico() {
        TransicaoStatusDTO dto = new TransicaoStatusDTO();
        dto.setNovoStatus(StatusPlanta.EM_MANUTENCAO);
        dto.setJustificativa("Parada programada para calibragem de CLPs.");

        when(plantaRepository.findById(10L)).thenReturn(Optional.of(plantaAtiva));
        when(plantaRepository.save(any(PlantaIndustrial.class))).thenAnswer(inv -> inv.getArgument(0));

        PlantaIndustrial atualizada = service.alterarStatus(10L, dto, "admin");

        assertEquals(StatusPlanta.EM_MANUTENCAO, atualizada.getStatus());
        verify(historicoRepository, times(1)).save(any(HistoricoStatusPlanta.class));
    }

    @Test
    @DisplayName("Deve bloquear transicao invalida de status (ex.: ATIVA -> EM_IMPLANTACAO)")
    void deveBloquearTransicaoStatusInvalida() {
        TransicaoStatusDTO dto = new TransicaoStatusDTO();
        dto.setNovoStatus(StatusPlanta.EM_IMPLANTACAO);
        dto.setJustificativa("Tentativa invalida de regressao de ciclo.");

        when(plantaRepository.findById(10L)).thenReturn(Optional.of(plantaAtiva));

        assertThrows(TransicaoStatusInvalidaException.class,
                () -> service.alterarStatus(10L, dto, "admin"));

        verify(plantaRepository, never()).save(any());
        verify(historicoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve bloquear desativacao de planta enquanto houver linhas de producao ativas")
    void deveBloquearDesativacaoComLinhasAtivas() {
        TransicaoStatusDTO dto = new TransicaoStatusDTO();
        dto.setNovoStatus(StatusPlanta.DESATIVADA);
        dto.setJustificativa("Encerramento da unidade industrial.");

        when(plantaRepository.findById(10L)).thenReturn(Optional.of(plantaAtiva));
        when(linhaRepository.countByPlantaIdAndAtivaTrue(10L)).thenReturn(2L);

        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> service.alterarStatus(10L, dto, "admin"));

        assertTrue(ex.getMessage().contains("linha(s) de produção ativa(s)"));
        verify(plantaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve bloquear exclusao de planta que nao esteja no status DESATIVADA")
    void deveBloquearExclusaoPlantaAtiva() {
        when(plantaRepository.findById(10L)).thenReturn(Optional.of(plantaAtiva));

        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> service.excluir(10L));

        assertTrue(ex.getMessage().contains("DESATIVADA"));
        verify(plantaRepository, never()).delete(any(PlantaIndustrial.class));
    }

    @Test
    @DisplayName("Deve permitir exclusao de planta DESATIVADA sem linhas ativas")
    void devePermitirExclusaoPlantaDesativada() {
        plantaAtiva.setStatus(StatusPlanta.DESATIVADA);
        when(plantaRepository.findById(10L)).thenReturn(Optional.of(plantaAtiva));
        when(linhaRepository.countByPlantaIdAndAtivaTrue(10L)).thenReturn(0L);

        assertDoesNotThrow(() -> service.excluir(10L));
        verify(plantaRepository, times(1)).delete(plantaAtiva);
    }

    private PlantaFormDTO criarDtoValido() {
        PlantaFormDTO dto = new PlantaFormDTO();
        dto.setCodigoPlanta("PLT-SP-099");
        dto.setNome("Nova Planta Teste");
        dto.setCidade("São Paulo");
        dto.setUfId(1L);
        dto.setLatitude(new BigDecimal("-23.550000"));
        dto.setLongitude(new BigDecimal("-46.630000"));
        dto.setAnoInicioOperacao(2025);
        dto.setAreaM2(15000);
        dto.setFuncionarios(200);
        dto.setNivelAutomacao(NivelAutomacao.ALTO);
        dto.setLinhasAutomatizadas(8);
        dto.setCertificacaoIso9001(true);
        return dto;
    }
}
