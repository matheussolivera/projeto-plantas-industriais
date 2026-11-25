package br.edu.eseg.plantas.dto;

import br.edu.eseg.plantas.model.StatusPlanta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TransicaoStatusDTO {

    @NotNull(message = "Selecione o novo status.")
    private StatusPlanta novoStatus;

    @NotBlank(message = "Informe uma justificativa para a mudança de status.")
    @Size(min = 5, max = 300, message = "A justificativa deve ter entre 5 e 300 caracteres.")
    private String justificativa;

    public StatusPlanta getNovoStatus() { return novoStatus; }
    public void setNovoStatus(StatusPlanta novoStatus) { this.novoStatus = novoStatus; }

    public String getJustificativa() { return justificativa; }
    public void setJustificativa(String justificativa) { this.justificativa = justificativa; }
}
