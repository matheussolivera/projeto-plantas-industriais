package br.edu.eseg.plantas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "erro/acesso-negado";
    }
}
