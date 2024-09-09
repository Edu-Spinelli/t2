package br.ufscar.dc.dsw.EstagioT2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String homeRedirect() {
        return "redirect:/login";
    }
}
