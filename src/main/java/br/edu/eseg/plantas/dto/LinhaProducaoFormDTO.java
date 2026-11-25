package br.edu.eseg.plantas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class LinhaProducaoFormDTO {

    @NotBlank(message = "Informe o código da linha (ex.: LN-01).")
    @Size(min = 2, max = 30, message = "Código deve ter entre 2 e 30 caracteres.")
    private String codigoLinha;

    @NotBlank(message = "Informe o nome da linha de produção.")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "Informe o tipo de automação (ex.: Robótica Colaborativa, CLP/SCADA).")
    @Size(min = 2, max = 60, message = "Tipo de automação deve ter entre 2 e 60 caracteres.")
    private String tipoAutomacao;

    @NotNull(message = "Informe a capacidade produtiva por hora.")
    @Positive(message = "A capacidade por hora deve ser maior que zero.")
    private Integer capacidadeHora;

    public String getCodigoLinha() { return codigoLinha; }
    public void setCodigoLinha(String codigoLinha) { this.codigoLinha = codigoLinha; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipoAutomacao() { return tipoAutomacao; }
    public void setTipoAutomacao(String tipoAutomacao) { this.tipoAutomacao = tipoAutomacao; }

    public Integer getCapacidadeHora() { return capacidadeHora; }
    public void setCapacidadeHora(Integer capacidadeHora) { this.capacidadeHora = capacidadeHora; }
}
