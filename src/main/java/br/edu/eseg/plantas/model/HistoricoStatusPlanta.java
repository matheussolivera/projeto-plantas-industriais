package br.edu.eseg.plantas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "historico_status_planta",
    indexes = {
        @Index(name = "idx_historico_planta", columnList = "planta_id"),
        @Index(name = "idx_historico_data", columnList = "alterado_em")
    }
)
public class HistoricoStatusPlanta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "planta_id", nullable = false, foreignKey = @ForeignKey(name = "fk_historico_planta"))
    private PlantaIndustrial planta;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior", length = 20)
    private StatusPlanta statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false, length = 20)
    private StatusPlanta statusNovo;

    @Column(nullable = false, length = 300)
    private String justificativa;

    @Column(name = "alterado_por", nullable = false, length = 80)
    private String alteradoPor;

    @Column(name = "alterado_em", nullable = false, updatable = false)
    private LocalDateTime alteradoEm;

    public HistoricoStatusPlanta() {
    }

    public HistoricoStatusPlanta(PlantaIndustrial planta, StatusPlanta statusAnterior,
                                 StatusPlanta statusNovo, String justificativa, String alteradoPor) {
        this.planta = planta;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.justificativa = justificativa;
        this.alteradoPor = alteradoPor;
    }

    @PrePersist
    protected void onCreate() {
        this.alteradoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PlantaIndustrial getPlanta() { return planta; }
    public void setPlanta(PlantaIndustrial planta) { this.planta = planta; }

    public StatusPlanta getStatusAnterior() { return statusAnterior; }
    public void setStatusAnterior(StatusPlanta statusAnterior) { this.statusAnterior = statusAnterior; }

    public StatusPlanta getStatusNovo() { return statusNovo; }
    public void setStatusNovo(StatusPlanta statusNovo) { this.statusNovo = statusNovo; }

    public String getJustificativa() { return justificativa; }
    public void setJustificativa(String justificativa) { this.justificativa = justificativa; }

    public String getAlteradoPor() { return alteradoPor; }
    public void setAlteradoPor(String alteradoPor) { this.alteradoPor = alteradoPor; }

    public LocalDateTime getAlteradoEm() { return alteradoEm; }
    public void setAlteradoEm(LocalDateTime alteradoEm) { this.alteradoEm = alteradoEm; }
}
