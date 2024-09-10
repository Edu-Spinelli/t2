package br.ufscar.dc.dsw.EstagioT2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.ufscar.dc.dsw.EstagioT2.domain.Empresa;
import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import br.ufscar.dc.dsw.EstagioT2.domain.Usuario;
import br.ufscar.dc.dsw.EstagioT2.service.EmpresaService;
import br.ufscar.dc.dsw.EstagioT2.service.ProfissionalService;
import br.ufscar.dc.dsw.EstagioT2.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/register")
public class RegisterController implements WebMvcConfigurer {

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
    public String showRegisterEmpresaForm(Empresa empresa) {

        return "register/registerEmpresa"; // Página HTML de registro de empresa
    }

    // Lida com o envio do formulário de registro de empresa
    @PostMapping("/empresa")
    public String registerEmpresa(@Valid @ModelAttribute("empresa") Empresa empresa, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register/registerEmpresa"; // Retorna ao formulário se houver erros
        }

        Optional<Usuario> usuarioExistente = usuarioService.buscarPorEmail(empresa.getUsuario().getEmail());
        if (usuarioExistente.isPresent()) {
            // Adiciona a mensagem de erro ao modelo e retorna ao formulário
            model.addAttribute("errorMessage", "Já existe um usuário com o email informado.");
            return "register/registerEmpresa";
        }


        Usuario usuario = empresa.getUsuario();
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha())); // Criptografa a senha
        usuario.setTipo(Usuario.TipoUsuario.empresa); // Define o tipo de usuário como empresa
        usuarioService.salvar(usuario);
        empresaService.salvar(empresa);

        redirectAttributes.addFlashAttribute("sucesso", "Registro realizado com sucesso! Por favor, faça login.");
        return "redirect:/login";
    }

    // Exibe o formulário de registro para profissional
    @GetMapping("/profissional")
    public String showRegisterProfissionalForm(Profissional profissional) {
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

