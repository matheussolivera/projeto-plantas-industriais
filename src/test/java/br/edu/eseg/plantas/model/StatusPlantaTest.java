package br.edu.eseg.plantas.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusPlantaTest {

    @Test
    @DisplayName("Deve permitir transicoes validas a partir de ATIVA")
    void devePermitirTransicoesValidasDeAtiva() {
        assertTrue(StatusPlanta.ATIVA.podeTransicionarPara(StatusPlanta.EM_MANUTENCAO));
        assertTrue(StatusPlanta.ATIVA.podeTransicionarPara(StatusPlanta.SUSPENSA));
        assertTrue(StatusPlanta.ATIVA.podeTransicionarPara(StatusPlanta.DESATIVADA));
    }

    @Test
    @DisplayName("Deve bloquear retorno de ATIVA para EM_IMPLANTACAO e autotransicao")
    void deveBloquearTransicoesInvalidasDeAtiva() {
        assertFalse(StatusPlanta.ATIVA.podeTransicionarPara(StatusPlanta.EM_IMPLANTACAO));
        assertFalse(StatusPlanta.ATIVA.podeTransicionarPara(StatusPlanta.ATIVA));
        assertFalse(StatusPlanta.ATIVA.podeTransicionarPara(null));
    }

    @Test
    @DisplayName("Estado terminal DESATIVADA nao deve permitir nenhuma transicao")
    void desativadaDeveSerEstadoTerminal() {
        assertTrue(StatusPlanta.DESATIVADA.getTransicoesPermitidas().isEmpty());
        for (StatusPlanta destino : StatusPlanta.values()) {
            assertFalse(StatusPlanta.DESATIVADA.podeTransicionarPara(destino));
        }
    }

    @Test
    @DisplayName("EM_IMPLANTACAO deve permitir apenas ATIVA ou DESATIVADA")
    void emImplantacaoDevePermitirApenasAtivaOuDesativada() {
        assertTrue(StatusPlanta.EM_IMPLANTACAO.podeTransicionarPara(StatusPlanta.ATIVA));
        assertTrue(StatusPlanta.EM_IMPLANTACAO.podeTransicionarPara(StatusPlanta.DESATIVADA));
        assertFalse(StatusPlanta.EM_IMPLANTACAO.podeTransicionarPara(StatusPlanta.EM_MANUTENCAO));
        assertFalse(StatusPlanta.EM_IMPLANTACAO.podeTransicionarPara(StatusPlanta.SUSPENSA));
    }
}
