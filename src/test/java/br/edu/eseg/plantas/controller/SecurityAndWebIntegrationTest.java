package br.edu.eseg.plantas.controller;

import br.edu.eseg.plantas.model.PlantaIndustrial;
import br.edu.eseg.plantas.repository.PlantaIndustrialRepository;
import br.edu.eseg.plantas.repository.UnidadeFederativaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAndWebIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantaIndustrialRepository plantaRepository;

    @Autowired
    private UnidadeFederativaRepository ufRepository;

    @Test
    @DisplayName("Carga inicial deve ter migrado exatamente as 10 plantas da base legada")
    void deveCarregarDezPlantasLegadas() {
        assertEquals(10, plantaRepository.count());
    }

    @Test
    @DisplayName("Usuario nao autenticado deve ser redirecionado para /login")
    void usuarioAnonimoDeveIrParaLogin() throws Exception {
        mockMvc.perform(get("/plantas"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "operador", roles = {"OPERADOR"})
    @DisplayName("OPERADOR pode consultar dashboard e listagem de plantas")
    void operadorPodeAcessarConsulta() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Dashboard de Plantas Industriais")));

        mockMvc.perform(get("/plantas"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Automax Industrial")));
    }

    @Test
    @WithMockUser(username = "operador", roles = {"OPERADOR"})
    @DisplayName("Servidor deve retornar 403 Forbidden quando OPERADOR tenta acessar rotas de ADMIN")
    void operadorDeveReceber403EmOperacoesAdmin() throws Exception {
        PlantaIndustrial planta = plantaRepository.findAll().get(0);

        mockMvc.perform(get("/plantas/nova"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/auditoria"))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/plantas/" + planta.getId() + "/status")
                        .with(csrf())
                        .param("novoStatus", "EM_MANUTENCAO")
                        .param("justificativa", "Tentativa sem permissao"))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/plantas/" + planta.getId() + "/excluir")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("ADMIN pode acessar formulario de cadastro e pagina de auditoria")
    void adminPodeAcessarRotasAdministrativas() throws Exception {
        mockMvc.perform(get("/plantas/nova"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Cadastrar Nova Planta Industrial")));

        mockMvc.perform(get("/auditoria"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Trilha de Auditoria")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Cadastro com coordenadas invalidas deve ser barrado por Bean Validation no servidor")
    void deveValidarCoordenadasForaDoIntervalo() throws Exception {
        Long ufId = ufRepository.findBySiglaIgnoreCase("SP").orElseThrow().getId();

        mockMvc.perform(post("/plantas/nova")
                        .with(csrf())
                        .param("codigoPlanta", "PLT-SP-888")
                        .param("nome", "Planta Coordenada Invalida")
                        .param("cidade", "São Paulo")
                        .param("ufId", String.valueOf(ufId))
                        .param("latitude", "-120.50") // invalido (< -90)
                        .param("longitude", "-46.63")
                        .param("anoInicioOperacao", "2024")
                        .param("areaM2", "10000")
                        .param("funcionarios", "100")
                        .param("nivelAutomacao", "ALTO")
                        .param("linhasAutomatizadas", "5")
                        .param("certificacaoIso9001", "true"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("plantaForm", "latitude"));
    }
}
