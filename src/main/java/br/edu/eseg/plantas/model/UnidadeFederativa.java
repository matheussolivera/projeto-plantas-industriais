package br.edu.eseg.plantas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "unidades_federativas", uniqueConstraints = {
        @UniqueConstraint(name = "uk_uf_sigla", columnNames = "sigla")
})
public class UnidadeFederativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2)
    private String sigla;

    @Column(nullable = false, length = 60)
    private String nome;

    @Column(nullable = false, length = 20)
    private String regiao;

    public UnidadeFederativa() {
    }

    public UnidadeFederativa(String sigla, String nome, String regiao) {
        this.sigla = sigla;
        this.nome = nome;
        this.regiao = regiao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSigla() { return sigla; }
    public void setSigla(String sigla) { this.sigla = sigla; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getRegiao() { return regiao; }
    public void setRegiao(String regiao) { this.regiao = regiao; }
}
