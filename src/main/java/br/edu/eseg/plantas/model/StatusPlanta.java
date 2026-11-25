package br.edu.eseg.plantas.model;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Maquina de estados do ciclo de vida operacional de uma planta industrial.
 * Previne estados inconsistentes e bloqueia transicoes invalidas no servidor.
 */
public enum StatusPlanta {
    EM_IMPLANTACAO("Em Implantação", "info"),
    ATIVA("Ativa", "success"),
    EM_MANUTENCAO("Em Manutenção", "warning"),
    SUSPENSA("Suspensa", "danger"),
    DESATIVADA("Desativada", "dark");

    private final String descricao;
    private final String badgeCss;

    StatusPlanta(String descricao, String badgeCss) {
        this.descricao = descricao;
        this.badgeCss = badgeCss;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getBadgeCss() {
        return badgeCss;
    }

    public Set<StatusPlanta> getTransicoesPermitidas() {
        return switch (this) {
            case EM_IMPLANTACAO -> EnumSet.of(ATIVA, DESATIVADA);
            case ATIVA -> EnumSet.of(EM_MANUTENCAO, SUSPENSA, DESATIVADA);
            case EM_MANUTENCAO -> EnumSet.of(ATIVA, SUSPENSA, DESATIVADA);
            case SUSPENSA -> EnumSet.of(ATIVA, EM_MANUTENCAO, DESATIVADA);
            case DESATIVADA -> Collections.emptySet();
        };
    }

    public boolean podeTransicionarPara(StatusPlanta destino) {
        if (destino == null || destino == this) {
            return false;
        }
        return getTransicoesPermitidas().contains(destino);
    }
}
