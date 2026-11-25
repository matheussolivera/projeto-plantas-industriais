package br.edu.eseg.plantas.controller;

import br.edu.eseg.plantas.dto.LinhaProducaoFormDTO;
import br.edu.eseg.plantas.dto.PlantaFormDTO;
import br.edu.eseg.plantas.dto.TransicaoStatusDTO;
import br.edu.eseg.plantas.exception.RegraNegocioException;
import br.edu.eseg.plantas.model.NivelAutomacao;
import br.edu.eseg.plantas.model.PlantaIndustrial;
import br.edu.eseg.plantas.model.StatusPlanta;
import br.edu.eseg.plantas.service.LinhaProducaoService;
import br.edu.eseg.plantas.service.PlantaIndustrialService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/plantas")
public class PlantaIndustrialController {

    private final PlantaIndustrialService plantaService;
    private final LinhaProducaoService linhaService;

    public PlantaIndustrialController(PlantaIndustrialService plantaService,
                                      LinhaProducaoService linhaService) {
        this.plantaService = plantaService;
        this.linhaService = linhaService;
    }

    @GetMapping
    public String listar(
            @RequestParam(name = "termo", required = false) String termo,
            @RequestParam(name = "ufId", required = false) Long ufId,
            @RequestParam(name = "nivel", required = false) NivelAutomacao nivel,
            @RequestParam(name = "status", required = false) StatusPlanta status,
            @RequestParam(name = "iso9001", required = false) Boolean iso9001,
            Model model) {

        model.addAttribute("paginaAtual", "plantas");
        model.addAttribute("plantas", plantaService.listarComFiltros(termo, ufId, nivel, status, iso9001));
        model.addAttribute("ufs", plantaService.listarUfs());
        model.addAttribute("niveis", NivelAutomacao.values());
        model.addAttribute("statuses", StatusPlanta.values());

        model.addAttribute("filtroTermo", termo);
        model.addAttribute("filtroUfId", ufId);
        model.addAttribute("filtroNivel", nivel);
        model.addAttribute("filtroStatus", status);
        model.addAttribute("filtroIso9001", iso9001);

        return "plantas/lista";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable("id") Long id, Model model) {
        PlantaIndustrial planta = plantaService.buscarPorId(id);
        carregarModelDetalhes(model, planta);
        if (!model.containsAttribute("transicaoForm")) {
            model.addAttribute("transicaoForm", new TransicaoStatusDTO());
        }
        if (!model.containsAttribute("linhaForm")) {
            model.addAttribute("linhaForm", new LinhaProducaoFormDTO());
        }
        return "plantas/detalhes";
    }

    @GetMapping("/nova")
    @PreAuthorize("hasRole('ADMIN')")
    public String formularioNova(Model model) {
        model.addAttribute("paginaAtual", "plantas");
        model.addAttribute("plantaForm", new PlantaFormDTO());
        model.addAttribute("modoEdicao", false);
        carregarListasFormulario(model);
        return "plantas/form";
    }

    @PostMapping("/nova")
    @PreAuthorize("hasRole('ADMIN')")
    public String criar(@Valid @ModelAttribute("plantaForm") PlantaFormDTO dto,
                        BindingResult result,
                        Authentication auth,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("paginaAtual", "plantas");
            model.addAttribute("modoEdicao", false);
            carregarListasFormulario(model);
            return "plantas/form";
        }

        try {
            PlantaIndustrial salva = plantaService.criar(dto, auth.getName());
            redirectAttributes.addFlashAttribute("sucesso",
                    "Planta '" + salva.getNome() + "' cadastrada com sucesso!");
            return "redirect:/plantas/" + salva.getId();
        } catch (RegraNegocioException ex) {
            model.addAttribute("paginaAtual", "plantas");
            model.addAttribute("modoEdicao", false);
            model.addAttribute("erroFormulario", ex.getMessage());
            carregarListasFormulario(model);
            return "plantas/form";
        }
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String formularioEditar(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        PlantaIndustrial planta = plantaService.buscarPorId(id);
        if (planta.getStatus() == StatusPlanta.DESATIVADA) {
            redirectAttributes.addFlashAttribute("erro",
                    "Plantas com status DESATIVADA não podem ser editadas.");
            return "redirect:/plantas/" + id;
        }
        model.addAttribute("paginaAtual", "plantas");
        model.addAttribute("plantaForm", PlantaFormDTO.fromEntity(planta));
        model.addAttribute("modoEdicao", true);
        carregarListasFormulario(model);
        return "plantas/form";
    }

    @PostMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String atualizar(@PathVariable("id") Long id,
                            @Valid @ModelAttribute("plantaForm") PlantaFormDTO dto,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        dto.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("paginaAtual", "plantas");
            model.addAttribute("modoEdicao", true);
            carregarListasFormulario(model);
            return "plantas/form";
        }

        try {
            PlantaIndustrial atualizada = plantaService.atualizar(id, dto);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Dados da planta '" + atualizada.getNome() + "' atualizados com sucesso!");
            return "redirect:/plantas/" + id;
        } catch (RegraNegocioException ex) {
            model.addAttribute("paginaAtual", "plantas");
            model.addAttribute("modoEdicao", true);
            model.addAttribute("erroFormulario", ex.getMessage());
            carregarListasFormulario(model);
            return "plantas/form";
        }
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public String alterarStatus(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("transicaoForm") TransicaoStatusDTO dto,
                                BindingResult result,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("erro",
                    "Informe um status válido e uma justificativa com pelo menos 5 caracteres.");
            return "redirect:/plantas/" + id;
        }

        try {
            plantaService.alterarStatus(id, dto, auth.getName());
            redirectAttributes.addFlashAttribute("sucesso",
                    "Status atualizado para '" + dto.getNovoStatus().getDescricao() + "' e registrado na auditoria.");
        } catch (RegraNegocioException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/plantas/" + id;
    }

    @PostMapping("/{id}/excluir")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            plantaService.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Planta industrial excluída com sucesso.");
            return "redirect:/plantas";
        } catch (RegraNegocioException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
            return "redirect:/plantas/" + id;
        }
    }

    @PostMapping("/{id}/linhas")
    public String adicionarLinha(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("linhaForm") LinhaProducaoFormDTO dto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("erro",
                    "Verifique os dados da linha de produção: código, nome, tipo de automação e capacidade (> 0).");
            return "redirect:/plantas/" + id;
        }

        try {
            linhaService.adicionarLinha(id, dto);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Linha de produção '" + dto.getCodigoLinha().toUpperCase() + "' adicionada com sucesso.");
        } catch (RegraNegocioException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/plantas/" + id;
    }

    @PostMapping("/{id}/linhas/{linhaId}/alternar")
    public String alternarLinha(@PathVariable("id") Long id,
                                @PathVariable("linhaId") Long linhaId,
                                RedirectAttributes redirectAttributes) {
        try {
            linhaService.alternarStatusAtiva(id, linhaId);
            redirectAttributes.addFlashAttribute("sucesso", "Situação operacional da linha atualizada.");
        } catch (RegraNegocioException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/plantas/" + id;
    }

    @PostMapping("/{id}/linhas/{linhaId}/excluir")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluirLinha(@PathVariable("id") Long id,
                               @PathVariable("linhaId") Long linhaId,
                               RedirectAttributes redirectAttributes) {
        try {
            linhaService.excluirLinha(id, linhaId);
            redirectAttributes.addFlashAttribute("sucesso", "Linha de produção removida.");
        } catch (RegraNegocioException ex) {
            redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/plantas/" + id;
    }

    private void carregarListasFormulario(Model model) {
        model.addAttribute("ufs", plantaService.listarUfs());
        model.addAttribute("niveis", NivelAutomacao.values());
        model.addAttribute("statusesIniciais", new StatusPlanta[]{StatusPlanta.EM_IMPLANTACAO, StatusPlanta.ATIVA});
    }

    private void carregarModelDetalhes(Model model, PlantaIndustrial planta) {
        model.addAttribute("paginaAtual", "plantas");
        model.addAttribute("planta", planta);
        model.addAttribute("linhas", linhaService.listarPorPlanta(planta.getId()));
        model.addAttribute("historico", plantaService.listarHistoricoDaPlanta(planta.getId()));
        model.addAttribute("transicoesPermitidas", planta.getStatus().getTransicoesPermitidas());
    }
}
