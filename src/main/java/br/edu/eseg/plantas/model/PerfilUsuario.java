package br.edu.eseg.plantas.model;

public enum PerfilUsuario {
    ADMIN("Gestor Industrial (ADMIN)"),
    OPERADOR("Operador de Planta (OPERADOR)");

    private final String descricao;

    PerfilUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
