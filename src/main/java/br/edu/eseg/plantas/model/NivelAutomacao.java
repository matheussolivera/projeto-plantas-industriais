package br.edu.eseg.plantas.model;

public enum NivelAutomacao {
    BAIXO("Baixo", "secondary"),
    MEDIO("Médio", "info"),
    ALTO("Alto", "primary"),
    MUITO_ALTO("Muito Alto", "success");

    private final String descricao;
    private final String badgeCss;

    NivelAutomacao(String descricao, String badgeCss) {
        this.descricao = descricao;
        this.badgeCss = badgeCss;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getBadgeCss() {
        return badgeCss;
    }
}
