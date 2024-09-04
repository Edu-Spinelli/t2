package br.ufscar.dc.dsw.EstagioT2.controller;

import br.ufscar.dc.dsw.EstagioT2.domain.Candidatura;
import br.ufscar.dc.dsw.EstagioT2.service.CandidaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/candidaturas")
public class CandidaturaController {

    @Autowired
    private CandidaturaService candidaturaService;

    @GetMapping
    public String listarCandidaturas(Model model) {
        model.addAttribute("candidaturas", candidaturaService.listarTodas());
        return "candidatura/lista";
    }

    @GetMapping("/nova")
    public String novaCandidatura(Model model) {
        model.addAttribute("candidatura", new Candidatura());
        return "candidatura/form";
    }

    @PostMapping
    public String salvarCandidatura(@ModelAttribute Candidatura candidatura) {
        candidaturaService.salvar(candidatura);
        return "redirect:/candidaturas";
    }

    @GetMapping("/{id}")
    public String editarCandidatura(@PathVariable Long id, Model model) {
        model.addAttribute("candidatura", candidaturaService.buscarPorId(id));
        return "candidatura/form";
    }

    @GetMapping("/{id}/excluir")
    public String excluirCandidatura(@PathVariable Long id) {
        candidaturaService.excluir(id);
        return "redirect:/candidaturas";
    }
}