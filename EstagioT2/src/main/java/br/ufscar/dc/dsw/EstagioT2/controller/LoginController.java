package br.ufscar.dc.dsw.EstagioT2.controller;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
public class LoginController {

    private final MessageSource messageSource;

    public LoginController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }



    @GetMapping("/login")
    public String login(@RequestParam(name = "error", required = false) String error,
                        @RequestParam(name = "logout", required = false) String logout,
                        Model model, Locale locale, RedirectAttributes attributes) {
        if (error != null) {
            // Busca mensagem internacionalizada para erro
            String errorMessage = messageSource.getMessage("login.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
        }
        if (logout != null) {
            // Busca mensagem internacionalizada para logout
            String logoutMessage = messageSource.getMessage("login.logout", null, locale);
            model.addAttribute("logoutMessage", logoutMessage);
        }
        return "login/login";
    }
}
