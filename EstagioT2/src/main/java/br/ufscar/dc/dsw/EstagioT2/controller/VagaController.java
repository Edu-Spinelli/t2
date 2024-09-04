package br.ufscar.dc.dsw.EstagioT2.controller;

import br.ufscar.dc.dsw.EstagioT2.domain.Vaga;
import br.ufscar.dc.dsw.EstagioT2.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vagas")
public class VagaController {

    @Autowired
    private VagaService vagaService;

    @GetMapping
    public String listarVagas(Model model) {
        model.addAttribute("vagas", vagaService.listarTodas());
        return "vaga/lista";
    }

    @GetMapping("/nova")
    public String novaVaga(Model model) {
        model.addAttribute("vaga", new Vaga());
        return "vaga/form";
    }

    @PostMapping
    public String salvarVaga(@ModelAttribute Vaga vaga) {
        vagaService.salvar(vaga);
        return "redirect:/vagas";
    }

    @GetMapping("/{id}")
    public String editarVaga(@PathVariable Long id, Model model) {
        model.addAttribute("vaga", vagaService.buscarPorId(id));
        return "vaga/form";
    }

    @GetMapping("/{id}/excluir")
    public String excluirVaga(@PathVariable Long id) {
        vagaService.excluir(id);
        return "redirect:/vagas";
    }
}