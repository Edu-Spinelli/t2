package br.ufscar.dc.dsw.EstagioT2.controller;

import br.ufscar.dc.dsw.EstagioT2.domain.Empresa;
import br.ufscar.dc.dsw.EstagioT2.security.UsuarioDetails;
import br.ufscar.dc.dsw.EstagioT2.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;


    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal UsuarioDetails user) {
        Empresa empresa = empresaService.buscarPorEmail(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada: " + user.getUsername()));
        model.addAttribute("empresa", empresa);
        return "empresa/home";
    }

    @GetMapping
    public String listarEmpresas(Model model) {
        model.addAttribute("empresas", empresaService.listarTodas());
        return "empresa/lista";
    }

    @GetMapping("/nova")
    public String novaEmpresa(Model model) {
        model.addAttribute("empresa", new Empresa());
        return "empresa/form";
    }

    @PostMapping
    public String salvarEmpresa(@ModelAttribute Empresa empresa) {
        empresaService.salvar(empresa);
        return "redirect:/empresas";
    }

    @GetMapping("/{id}")
    public String editarEmpresa(@PathVariable Long id, Model model) {
        model.addAttribute("empresa", empresaService.buscarPorId(id));
        return "empresa/form";
    }

    @GetMapping("/{id}/excluir")
    public String excluirEmpresa(@PathVariable Long id) {
        empresaService.excluir(id);
        return "redirect:/empresas";
    }
}