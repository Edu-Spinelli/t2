package br.ufscar.dc.dsw.EstagioT2.controller;

import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import br.ufscar.dc.dsw.EstagioT2.security.UsuarioDetails;
import br.ufscar.dc.dsw.EstagioT2.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profissional")
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal UsuarioDetails user) {
        Profissional profissional = profissionalService.buscarPorEmail(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada: " + user.getUsername()));
        model.addAttribute("profissional", profissional);
        return "profissional/home";
    }


    @GetMapping
    public String listarProfissionais(Model model) {
        model.addAttribute("profissionais", profissionalService.listarTodos());
        return "profissional/lista";
    }

    @GetMapping("/novo")
    public String novoProfissional(Model model) {
        model.addAttribute("profissional", new Profissional());
        return "profissional/form";
    }

    @PostMapping
    public String salvarProfissional(@ModelAttribute Profissional profissional) {
        profissionalService.salvar(profissional);
        return "redirect:/profissionais";
    }

    @GetMapping("/{id}")
    public String editarProfissional(@PathVariable Long id, Model model) {
        model.addAttribute("profissional", profissionalService.buscarPorId(id));
        return "profissional/form";
    }

    @GetMapping("/{id}/excluir")
    public String excluirProfissional(@PathVariable Long id) {
        profissionalService.excluir(id);
        return "redirect:/profissionais";
    }
}