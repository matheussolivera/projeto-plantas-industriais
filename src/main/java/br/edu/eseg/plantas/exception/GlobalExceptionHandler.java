package br.edu.eseg.plantas.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public String handleNotFound(RecursoNaoEncontradoException ex, Model model, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        model.addAttribute("codigo", 404);
        model.addAttribute("titulo", "Recurso não encontrado");
        model.addAttribute("mensagem", ex.getMessage());
        return "erro/erro";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return "erro/acesso-negado";
    }

    @ExceptionHandler(RegraNegocioException.class)
    public String handleBusinessError(RegraNegocioException ex, Model model, HttpServletResponse response) {
        response.setStatus(422);
        model.addAttribute("codigo", 422);
        model.addAttribute("titulo", "Violação de Regra de Negócio");
        model.addAttribute("mensagem", ex.getMessage());
        return "erro/erro";
    }
}
