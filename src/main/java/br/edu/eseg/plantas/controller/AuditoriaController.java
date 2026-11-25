package br.edu.eseg.plantas.controller;

import br.edu.eseg.plantas.service.PlantaIndustrialService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auditoria")
@PreAuthorize("hasRole('ADMIN')")
public class AuditoriaController {

    private final PlantaIndustrialService plantaService;

    public AuditoriaController(PlantaIndustrialService plantaService) {
        this.plantaService = plantaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("paginaAtual", "auditoria");
        model.addAttribute("registros", plantaService.listarHistoricoGeral());
        return "auditoria/lista";
    }
}
