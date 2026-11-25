package br.edu.eseg.plantas.controller;

import br.edu.eseg.plantas.service.PlantaIndustrialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final PlantaIndustrialService plantaService;

    public DashboardController(PlantaIndustrialService plantaService) {
        this.plantaService = plantaService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("paginaAtual", "dashboard");
        model.addAttribute("kpis", plantaService.calcularKPIs());
        return "dashboard";
    }
}
