package br.edu.eseg.plantas.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "plantas_industriais",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_planta_codigo", columnNames = "codigo_planta"),
        @UniqueConstraint(name = "uk_planta_nome", columnNames = "nome")
    },
    indexes = {
        @Index(name = "idx_planta_uf", columnList = "uf_id"),
        @Index(name = "idx_planta_status", columnList = "status"),
        @Index(name = "idx_planta_nivel_automacao", columnList = "nivel_automacao")
    }
)
public class PlantaIndustrial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_planta", nullable = false, length = 20)
    private String codigoPlanta;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 100)
    private String cidade;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "uf_id", nullable = false, foreignKey = @ForeignKey(name = "fk_planta_uf"))
    private UnidadeFederativa uf;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "ano_inicio_operacao", nullable = false)
    private Integer anoInicioOperacao;

    @Column(name = "area_m2", nullable = false)
    private Integer areaM2;

    @Column(nullable = false)
    private Integer funcionarios;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_automacao", nullable = false, length = 20)
    private NivelAutomacao nivelAutomacao;

    @Column(name = "linhas_automatizadas", nullable = false)
    private Integer linhasAutomatizadas;

    @Column(name = "certificacao_iso9001", nullable = false)
    private Boolean certificacaoIso9001;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPlanta status = StatusPlanta.ATIVA;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    // Lado inverso (mappedBy): a FK planta_id pertence a entidade LinhaProducao
    @OneToMany(mappedBy = "planta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LinhaProducao> linhasProducao = new ArrayList<>();

    // Lado inverso (mappedBy): a FK planta_id pertence a entidade HistoricoStatusPlanta
    @OneToMany(mappedBy = "planta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoStatusPlanta> historicoStatus = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        LocalDateTime agora = LocalDateTime.now();
        this.criadoEm = agora;
        this.atualizadoEm = agora;
        if (this.status == null) {
            this.status = StatusPlanta.ATIVA;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public BigDecimal getAreaPorFuncionario() {
        if (funcionarios == null || funcionarios == 0 || areaM2 == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(areaM2)
                .divide(BigDecimal.valueOf(funcionarios), 2, RoundingMode.HALF_UP);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoPlanta() { return codigoPlanta; }
    public void setCodigoPlanta(String codigoPlanta) { this.codigoPlanta = codigoPlanta; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public UnidadeFederativa getUf() { return uf; }
    public void setUf(UnidadeFederativa uf) { this.uf = uf; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

    public Integer getAnoInicioOperacao() { return anoInicioOperacao; }
    public void setAnoInicioOperacao(Integer anoInicioOperacao) { this.anoInicioOperacao = anoInicioOperacao; }

    public Integer getAreaM2() { return areaM2; }
    public void setAreaM2(Integer areaM2) { this.areaM2 = areaM2; }

    public Integer getFuncionarios() { return funcionarios; }
    public void setFuncionarios(Integer funcionarios) { this.funcionarios = funcionarios; }

    public NivelAutomacao getNivelAutomacao() { return nivelAutomacao; }
    public void setNivelAutomacao(NivelAutomacao nivelAutomacao) { this.nivelAutomacao = nivelAutomacao; }

    public Integer getLinhasAutomatizadas() { return linhasAutomatizadas; }
    public void setLinhasAutomatizadas(Integer linhasAutomatizadas) { this.linhasAutomatizadas = linhasAutomatizadas; }

    public Boolean getCertificacaoIso9001() { return certificacaoIso9001; }
    public void setCertificacaoIso9001(Boolean certificacaoIso9001) { this.certificacaoIso9001 = certificacaoIso9001; }

    public StatusPlanta getStatus() { return status; }
    public void setStatus(StatusPlanta status) { this.status = status; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    public List<LinhaProducao> getLinhasProducao() { return linhasProducao; }
    public void setLinhasProducao(List<LinhaProducao> linhasProducao) { this.linhasProducao = linhasProducao; }

    public List<HistoricoStatusPlanta> getHistoricoStatus() { return historicoStatus; }
    public void setHistoricoStatus(List<HistoricoStatusPlanta> historicoStatus) { this.historicoStatus = historicoStatus; }
}
