package br.edu.eseg.plantas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "linhas_producao",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_linha_planta_codigo", columnNames = {"planta_id", "codigo_linha"})
    },
    indexes = {
        @Index(name = "idx_linha_planta", columnList = "planta_id")
    }
)
public class LinhaProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_linha", nullable = false, length = 30)
    private String codigoLinha;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "tipo_automacao", nullable = false, length = 60)
    private String tipoAutomacao;

    @Column(name = "capacidade_hora", nullable = false)
    private Integer capacidadeHora;

    @Column(nullable = false)
    private Boolean ativa = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    // Lado proprietario da associacao: detem a coluna FK planta_id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "planta_id", nullable = false, foreignKey = @ForeignKey(name = "fk_linha_planta"))
    private PlantaIndustrial planta;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
        if (this.ativa == null) {
            this.ativa = true;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoLinha() { return codigoLinha; }
    public void setCodigoLinha(String codigoLinha) { this.codigoLinha = codigoLinha; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipoAutomacao() { return tipoAutomacao; }
    public void setTipoAutomacao(String tipoAutomacao) { this.tipoAutomacao = tipoAutomacao; }

    public Integer getCapacidadeHora() { return capacidadeHora; }
    public void setCapacidadeHora(Integer capacidadeHora) { this.capacidadeHora = capacidadeHora; }

    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public PlantaIndustrial getPlanta() { return planta; }
    public void setPlanta(PlantaIndustrial planta) { this.planta = planta; }
}
