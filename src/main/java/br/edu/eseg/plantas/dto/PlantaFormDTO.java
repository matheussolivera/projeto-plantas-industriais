package br.edu.eseg.plantas.dto;

import br.edu.eseg.plantas.model.NivelAutomacao;
import br.edu.eseg.plantas.model.PlantaIndustrial;
import br.edu.eseg.plantas.model.StatusPlanta;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class PlantaFormDTO {

    private Long id;

    @NotBlank(message = "O código da planta é obrigatório (ex.: PLT-SP-011).")
    @Size(min = 3, max = 20, message = "O código deve ter entre 3 e 20 caracteres.")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Use apenas letras maiúsculas, números e hífen.")
    private String codigoPlanta;

    @NotBlank(message = "O nome da planta é obrigatório.")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres.")
    private String nome;

    @NotBlank(message = "A cidade é obrigatória.")
    @Size(min = 2, max = 100, message = "A cidade deve ter entre 2 e 100 caracteres.")
    private String cidade;

    @NotNull(message = "Selecione uma Unidade Federativa (UF).")
    private Long ufId;

    @NotNull(message = "A latitude é obrigatória.")
    @DecimalMin(value = "-90.0", message = "Latitude mínima é -90.0.")
    @DecimalMax(value = "90.0", message = "Latitude máxima é 90.0.")
    private BigDecimal latitude;

    @NotNull(message = "A longitude é obrigatória.")
    @DecimalMin(value = "-180.0", message = "Longitude mínima é -180.0.")
    @DecimalMax(value = "180.0", message = "Longitude máxima é 180.0.")
    private BigDecimal longitude;

    @NotNull(message = "O ano de início de operação é obrigatório.")
    @Min(value = 1900, message = "Ano inválido (mínimo 1900).")
    @Max(value = 2030, message = "Ano inválido (máximo 2030).")
    private Integer anoInicioOperacao;

    @NotNull(message = "A área em m² é obrigatória.")
    @Positive(message = "A área deve ser maior que zero.")
    private Integer areaM2;

    @NotNull(message = "O número de funcionários é obrigatório.")
    @Min(value = 0, message = "O número de funcionários não pode ser negativo.")
    private Integer funcionarios;

    @NotNull(message = "Selecione o nível de automação.")
    private NivelAutomacao nivelAutomacao;

    @NotNull(message = "Informe a quantidade de linhas automatizadas.")
    @Min(value = 0, message = "Não pode ser negativo.")
    private Integer linhasAutomatizadas;

    @NotNull(message = "Informe se possui certificação ISO 9001.")
    private Boolean certificacaoIso9001 = Boolean.FALSE;

    private StatusPlanta statusInicial = StatusPlanta.EM_IMPLANTACAO;

    public static PlantaFormDTO fromEntity(PlantaIndustrial p) {
        PlantaFormDTO dto = new PlantaFormDTO();
        dto.setId(p.getId());
        dto.setCodigoPlanta(p.getCodigoPlanta());
        dto.setNome(p.getNome());
        dto.setCidade(p.getCidade());
        dto.setUfId(p.getUf() != null ? p.getUf().getId() : null);
        dto.setLatitude(p.getLatitude());
        dto.setLongitude(p.getLongitude());
        dto.setAnoInicioOperacao(p.getAnoInicioOperacao());
        dto.setAreaM2(p.getAreaM2());
        dto.setFuncionarios(p.getFuncionarios());
        dto.setNivelAutomacao(p.getNivelAutomacao());
        dto.setLinhasAutomatizadas(p.getLinhasAutomatizadas());
        dto.setCertificacaoIso9001(p.getCertificacaoIso9001());
        dto.setStatusInicial(p.getStatus());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoPlanta() { return codigoPlanta; }
    public void setCodigoPlanta(String codigoPlanta) { this.codigoPlanta = codigoPlanta; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public Long getUfId() { return ufId; }
    public void setUfId(Long ufId) { this.ufId = ufId; }

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

    public StatusPlanta getStatusInicial() { return statusInicial; }
    public void setStatusInicial(StatusPlanta statusInicial) { this.statusInicial = statusInicial; }
}
