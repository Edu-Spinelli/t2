package br.ufscar.dc.dsw.EstagioT2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model) {
        // Você pode adicionar atributos ao modelo se necessário, como mensagens de erro, etc.
        return "login/login"; // Retorna a view login/login.html
    }
}
