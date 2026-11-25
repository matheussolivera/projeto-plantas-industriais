package br.edu.eseg.plantas.dto;

import java.util.Map;

public class DashboardKPIsDTO {
    private long totalPlantas;
    private long plantasAtivas;
    private double percentualIso9001;
    private long totalFuncionarios;
    private long totalLinhasAutomatizadas;
    private long areaTotalM2;
    private Map<String, Long> porRegiao;
    private Map<String, Long> porNivelAutomacao;
    private Map<String, Long> porStatus;

    public long getTotalPlantas() { return totalPlantas; }
    public void setTotalPlantas(long totalPlantas) { this.totalPlantas = totalPlantas; }

    public long getPlantasAtivas() { return plantasAtivas; }
    public void setPlantasAtivas(long plantasAtivas) { this.plantasAtivas = plantasAtivas; }

    public double getPercentualIso9001() { return percentualIso9001; }
    public void setPercentualIso9001(double percentualIso9001) { this.percentualIso9001 = percentualIso9001; }

    public long getTotalFuncionarios() { return totalFuncionarios; }
    public void setTotalFuncionarios(long totalFuncionarios) { this.totalFuncionarios = totalFuncionarios; }

    public long getTotalLinhasAutomatizadas() { return totalLinhasAutomatizadas; }
    public void setTotalLinhasAutomatizadas(long totalLinhasAutomatizadas) { this.totalLinhasAutomatizadas = totalLinhasAutomatizadas; }

    public long getAreaTotalM2() { return areaTotalM2; }
    public void setAreaTotalM2(long areaTotalM2) { this.areaTotalM2 = areaTotalM2; }

    public Map<String, Long> getPorRegiao() { return porRegiao; }
    public void setPorRegiao(Map<String, Long> porRegiao) { this.porRegiao = porRegiao; }

    public Map<String, Long> getPorNivelAutomacao() { return porNivelAutomacao; }
    public void setPorNivelAutomacao(Map<String, Long> porNivelAutomacao) { this.porNivelAutomacao = porNivelAutomacao; }

    public Map<String, Long> getPorStatus() { return porStatus; }
    public void setPorStatus(Map<String, Long> porStatus) { this.porStatus = porStatus; }
}
