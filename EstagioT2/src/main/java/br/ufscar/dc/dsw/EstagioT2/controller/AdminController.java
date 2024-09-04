package br.ufscar.dc.dsw.EstagioT2.controller;

import br.ufscar.dc.dsw.EstagioT2.domain.Empresa;
import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import br.ufscar.dc.dsw.EstagioT2.domain.Vaga;
import br.ufscar.dc.dsw.EstagioT2.service.EmpresaService;
import br.ufscar.dc.dsw.EstagioT2.service.ProfissionalService;
import br.ufscar.dc.dsw.EstagioT2.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private VagaService vagaService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/home")
    public String admin() {
        return "admin/home";
    }


    @GetMapping("/profissional")
    public String listarProfissionais(Model model) {
        List<Profissional> profissionais = profissionalService.listarTodos();
        model.addAttribute("profissionais", profissionais);
        return "admin/listaProfissionais";  // Certifique-se de que o template exista
    }

    @GetMapping("/empresa")
    public String listarEmpresas(Model model) {
        List<Empresa> empresas = empresaService.listarTodas();
        model.addAttribute("empresas", empresas);
        return "admin/listaEmpresas";  // Certifique-se de que o template exista
    }

    @GetMapping("/vaga")
    public String listarVagas(Model model) {
        List<Vaga> vagas = vagaService.listarTodas();
        model.addAttribute("vagas", vagaService.listarTodas());
        return "admin/listaVagas";  // Certifique-se de que o template exista
    }


    @GetMapping("/editarProfissional/{id}")
    public String editarProfissional(@PathVariable("id") Long id, Model model) {
        Profissional profissionais = profissionalService.buscarPorId(id);
        model.addAttribute("profissionais", profissionais);
        return "admin/editarProfissional";  // Certifique-se de que o template exista
    }

    @PostMapping("/profissional/save")
    public String salvarProfissional(@ModelAttribute("profissional") Profissional profissional, Model model) {
        // Verifica se o campo senha está vazio
        if (profissional.getUsuario().getSenha() != null && !profissional.getUsuario().getSenha().isEmpty()) {
            // Codifica a nova senha antes de salvar
            String encodedPassword = passwordEncoder.encode(profissional.getUsuario().getSenha());
            profissional.getUsuario().setSenha(encodedPassword);
        } else {
            // Caso a senha não tenha sido alterada, mantém a senha antiga
            Profissional existingProfissional = profissionalService.buscarPorId(profissional.getId());
            profissional.getUsuario().setSenha(existingProfissional.getUsuario().getSenha());
        }

        // Salva o profissional com as alterações
        profissionalService.salvar(profissional);

        // Redireciona de volta para a lista de profissionais
        return "redirect:/admin/profissional";
    }



}
