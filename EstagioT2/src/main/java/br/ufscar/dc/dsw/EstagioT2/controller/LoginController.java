package br.ufscar.dc.dsw.EstagioT2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(name = "error", required = false) String error,
                        @RequestParam(name = "logout", required = false) String logout,
                        Model model, RedirectAttributes attributes) {
        if (error != null) {
            model.addAttribute("errorMessage", "Usuário ou senha incorretos!");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Você saiu com sucesso.");
        }
        return "login/login";
    }
}
