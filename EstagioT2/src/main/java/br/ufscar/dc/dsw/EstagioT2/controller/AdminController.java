package br.ufscar.dc.dsw.EstagioT2.controller;

import br.ufscar.dc.dsw.EstagioT2.domain.Empresa;
import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import br.ufscar.dc.dsw.EstagioT2.domain.Vaga;
import br.ufscar.dc.dsw.EstagioT2.service.EmpresaService;
import br.ufscar.dc.dsw.EstagioT2.service.ProfissionalService;
import br.ufscar.dc.dsw.EstagioT2.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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



}
