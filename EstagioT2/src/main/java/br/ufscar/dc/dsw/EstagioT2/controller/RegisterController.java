package br.ufscar.dc.dsw.EstagioT2.controller;

import br.ufscar.dc.dsw.EstagioT2.domain.Empresa;
import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import br.ufscar.dc.dsw.EstagioT2.domain.Usuario;
import br.ufscar.dc.dsw.EstagioT2.service.EmpresaService;
import br.ufscar.dc.dsw.EstagioT2.service.ProfissionalService;
import br.ufscar.dc.dsw.EstagioT2.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Exibe o formulário de registro para empresa
    @GetMapping("/empresa")
    public String showRegisterEmpresaForm(Model model) {
        model.addAttribute("empresa", new Empresa());
        return "register/registerEmpresa"; // Página HTML de registro de empresa
    }

    // Lida com o envio do formulário de registro de empresa
    @PostMapping("/empresa")
    public String registerEmpresa(@Valid @ModelAttribute("empresa") Empresa empresa, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register/registerEmpresa"; // Retorna ao formulário se houver erros
        }

        Usuario usuario = empresa.getUsuario();
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha())); // Criptografa a senha
        usuario.setTipo(Usuario.TipoUsuario.empresa); // Define o tipo de usuário como empresa
        usuarioService.salvar(usuario);
        empresaService.salvar(empresa);
        return "redirect:/login"; // Redireciona para a página de login após o registro
    }

    // Exibe o formulário de registro para profissional
    @GetMapping("/profissional")
    public String showRegisterProfissionalForm(Model model) {
        model.addAttribute("profissional", new Profissional());
        return "register/registerProfissional"; // Página HTML de registro de profissional
    }

    // Lida com o envio do formulário de registro de profissional
    @PostMapping("/profissional")
    public String registerProfissional(@Valid @ModelAttribute("profissional") Profissional profissional, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register/registerProfissional"; // Retorna ao formulário se houver erros
        }

        Usuario usuario = profissional.getUsuario();
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha())); // Criptografa a senha
        usuario.setTipo(Usuario.TipoUsuario.profissional); // Define o tipo de usuário como profissional
        usuarioService.salvar(usuario);
        profissionalService.salvar(profissional);
        return "redirect:/login"; // Redireciona para a página de login após o registro
    }
}

