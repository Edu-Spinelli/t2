package br.ufscar.dc.dsw.EstagioT2.controller;

import br.ufscar.dc.dsw.EstagioT2.domain.Empresa;
import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import br.ufscar.dc.dsw.EstagioT2.domain.Usuario;
import br.ufscar.dc.dsw.EstagioT2.domain.Vaga;
import br.ufscar.dc.dsw.EstagioT2.service.EmpresaService;
import br.ufscar.dc.dsw.EstagioT2.service.ProfissionalService;
import br.ufscar.dc.dsw.EstagioT2.service.UsuarioService;
import br.ufscar.dc.dsw.EstagioT2.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private UsuarioService usuarioService;

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

    @PostMapping("/profissional/save/{id}")
    public String salvarProfissional(@PathVariable("id") Long id, @ModelAttribute("profissional") Profissional profissional, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            // Em caso de erro, retorna à página de edição com o objeto "profissional"
            return "admin/editarProfissional";
        }

        try {
            // Busca o profissional atual no banco de dados usando o ID
            Profissional profissionalExistente = profissionalService.buscarPorId(id);
            Usuario usuarioExistente = profissionalExistente.getUsuario();

            // Verifica se o email do formulário é diferente do email atual no banco de dados
            if (!usuarioExistente.getEmail().equals(profissional.getUsuario().getEmail())) {
                // Verifica se o novo email já está em uso por outro profissional
                Optional<Usuario> usuarioComNovoEmail = usuarioService.buscarPorEmail(profissional.getUsuario().getEmail());

                if (usuarioComNovoEmail.isPresent()) {
                    // Se o email já está em uso por outro usuário, retorna erro
                    attributes.addFlashAttribute("erro", "O email já está em uso por outro profissional.");
                    return "redirect:/admin/editarProfissional/" + id;
                } else {
                    // Se o email não está em uso, atualiza o email
                    usuarioExistente.setEmail(profissional.getUsuario().getEmail());
                }
            }

            // Atualiza a senha apenas se o campo não estiver vazio
            if (profissional.getUsuario().getSenha() != null && !profissional.getUsuario().getSenha().isEmpty()) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String senhaCriptografada = passwordEncoder.encode(profissional.getUsuario().getSenha());
                usuarioExistente.setSenha(senhaCriptografada); // Criptografa e atualiza a senha
            }

            // Atualiza os demais dados do profissional
            profissionalExistente.setNome(profissional.getNome());
            profissionalExistente.setCpf(profissional.getCpf());
            profissionalExistente.setTelefone(profissional.getTelefone());

            // Salva as atualizações no banco de dados
            profissionalService.salvar(profissionalExistente);
            attributes.addFlashAttribute("sucesso", "Profissional salvo com sucesso!");

        } catch (Exception e) {
            // Lida com exceções e redireciona de volta à página de edição
            attributes.addFlashAttribute("erro", "Erro ao salvar o profissional: " + e.getMessage());
            return "redirect:/admin/editarProfissional/" + id;
        }

        return "redirect:/admin/profissional";
    }

    @GetMapping("/excluirProfissional/{id}")
    public String excluirProfissional(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            // Busca o profissional pelo ID
            Profissional profissional = profissionalService.buscarPorId(id);

            if (profissional != null) {
                // Busca o usuário associado ao profissional
                Usuario usuario = profissional.getUsuario();

                // Exclui o profissional
                profissionalService.excluir(id);

                // Exclui o usuário associado, se existir
                if (usuario != null) {
                    usuarioService.excluir(usuario.getId());
                }

                attributes.addFlashAttribute("sucesso", "Profissional e usuário excluídos com sucesso!");
            } else {
                attributes.addFlashAttribute("erro", "Profissional não encontrado.");
            }

        } catch (Exception e) {
            // Lida com exceções
            attributes.addFlashAttribute("erro", "Erro ao excluir o profissional: " + e.getMessage());
        }

        // Redireciona para a lista de profissionais após a exclusão
        return "redirect:/admin/profissional";
    }




    @GetMapping("/editarEmpresa/{id}")
    public String editarEmpresa(@PathVariable("id") Long id, Model model) {
        Empresa empresa = empresaService.buscarPorId(id);
        model.addAttribute("empresa", empresa);
        return "admin/editarEmpresa";  // Certifique-se de que o template exista
    }

    @PostMapping("/empresa/save/{id}")
    public String salvarEmpresa(@PathVariable("id") Long id, @ModelAttribute("empresa") Empresa empresa, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            // Em caso de erro, retorna à página de edição com o objeto "empresa"
            return "admin/editarEmpresa";
        }

        try {
            // Busca a empresa atual no banco de dados usando o ID
            Empresa empresaExistente = empresaService.buscarPorId(id);
            Usuario usuarioExistente = empresaExistente.getUsuario();

            // Verifica se o email do formulário é diferente do email atual no banco de dados
            if (!usuarioExistente.getEmail().equals(empresa.getUsuario().getEmail())) {
                // Verifica se o novo email já está em uso por outra empresa
                Optional<Usuario> usuarioComNovoEmail = usuarioService.buscarPorEmail(empresa.getUsuario().getEmail());

                if (usuarioComNovoEmail.isPresent()) {
                    // Se o email já está em uso por outra empresa, retorna erro
                    attributes.addFlashAttribute("erro", "O email já está em uso por outra empresa.");
                    return "redirect:/admin/editarEmpresa/" + id;
                } else {
                    // Se o email não está em uso, atualiza o email
                    usuarioExistente.setEmail(empresa.getUsuario().getEmail());
                }
            }

            // Atualiza a senha apenas se o campo não estiver vazio
            if (empresa.getUsuario().getSenha() != null && !empresa.getUsuario().getSenha().isEmpty()) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String senhaCriptografada = passwordEncoder.encode(empresa.getUsuario().getSenha());
                usuarioExistente.setSenha(senhaCriptografada); // Criptografa e atualiza a senha
            }

            // Atualiza os demais dados da empresa
            empresaExistente.setNome(empresa.getNome());
            empresaExistente.setCnpj(empresa.getCnpj());
            empresaExistente.setDescricao(empresa.getDescricao());
            empresaExistente.setCidade(empresa.getCidade());

            // Salva as atualizações no banco de dados
            empresaService.salvar(empresaExistente);
            attributes.addFlashAttribute("sucesso", "Empresa salva com sucesso!");

        } catch (Exception e) {
            // Lida com exceções e redireciona de volta à página de edição
            attributes.addFlashAttribute("erro", "Erro ao salvar a empresa");
            return "redirect:/admin/editarEmpresa/" + id;
        }
        return "redirect:/admin/empresa";
    }

    @GetMapping("/excluirEmpresa/{id}")
    public String excluirEmpresa(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            // Busca a empresa pelo ID
            Empresa empresa = empresaService.buscarPorId(id);

            if (empresa != null) {
                // Busca o usuário associado à empresa
                Usuario usuario = empresa.getUsuario();

                // Exclui a empresa
                empresaService.excluir(id);

                // Exclui o usuário associado, se existir
                if (usuario != null) {
                    usuarioService.excluir(usuario.getId());
                }

                attributes.addFlashAttribute("sucesso", "Empresa e usuário excluídos com sucesso!");
            } else {
                attributes.addFlashAttribute("erro", "Empresa não encontrada.");
            }

        } catch (Exception e) {
            // Lida com exceções
            attributes.addFlashAttribute("erro", "Erro ao excluir a empresa: " + e.getMessage());
        }

        // Redireciona para a lista de empresas após a exclusão
        return "redirect:/admin/empresa";
    }


    @GetMapping("/editarVaga/{id}")
    public String editarVaga(@PathVariable("id") Long id, Model model){
        Vaga vaga = vagaService.buscarPorId(id);
        model.addAttribute("vaga", vaga);
        return "admin/editarVaga";
    }

    @PostMapping("/vaga/save/{id}")
    public String salvarVaga(@PathVariable("id") Long id, @ModelAttribute("vaga") Vaga vaga, BindingResult result, RedirectAttributes attributes) {
        System.out.println("Iniciando o processo de salvar vaga");

        if (result.hasErrors()) {
            System.out.println("Erros de validação encontrados:");
            result.getAllErrors().forEach(error -> System.out.println(error.toString())); // Log de todos os erros de validação
            return "admin/editarVaga";
        }

        try {
            System.out.println("Buscando vaga com ID: " + id);
            Vaga vagaExistente = vagaService.buscarPorId(id);

            if (vagaExistente != null) {
                System.out.println("Vaga encontrada, atualizando informações");

                // Atualizando a descrição, remuneração e cidade
                vagaExistente.setDescricao(vaga.getDescricao());
                vagaExistente.setRemuneracao(vaga.getRemuneracao());
                vagaExistente.setCidade(vaga.getCidade());

                // Convertendo a data do formulário (String) para java.sql.Date, se a data for válida
                if (vaga.getDataLimiteInscricao() != null) {
                    System.out.println("Atualizando a data limite de inscrição");
                    vagaExistente.setDataLimiteInscricao(new java.sql.Date(vaga.getDataLimiteInscricao().getTime()));
                }

                System.out.println("Salvando vaga atualizada");
                vagaService.salvar(vagaExistente);

                attributes.addFlashAttribute("sucesso", "Vaga salva com sucesso!");
                System.out.println("Vaga salva com sucesso");
            } else {
                System.out.println("Vaga não encontrada");
                attributes.addFlashAttribute("erro", "Vaga não encontrada.");
                return "redirect:/admin/vaga";
            }

        } catch (Exception e) {
            System.out.println("Erro ao salvar vaga: " + e.getMessage());
            e.printStackTrace(); // Loga a exceção completa no console
            attributes.addFlashAttribute("erro", "Erro ao salvar a vaga: " + e.getMessage());
            return "redirect:/admin/editarVaga/" + id;
        }

        return "redirect:/admin/vaga";
    }

    @GetMapping("/excluirVaga/{id}")
    public String excluirVaga(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            // Busca a vaga pelo ID
            Vaga vaga = vagaService.buscarPorId(id);

            if (vaga != null) {
                // Exclui a vaga
                vagaService.excluir(id);
                attributes.addFlashAttribute("sucesso", "Vaga excluída com sucesso!");
            } else {
                attributes.addFlashAttribute("erro", "Vaga não encontrada.");
            }

        } catch (Exception e) {
            // Lida com exceções
            attributes.addFlashAttribute("erro", "Erro ao excluir a vaga: " + e.getMessage());
        }

        // Redireciona para a lista de vagas após a exclusão
        return "redirect:/admin/vaga";
    }








}
