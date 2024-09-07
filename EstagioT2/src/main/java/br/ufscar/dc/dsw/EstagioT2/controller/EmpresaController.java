package br.ufscar.dc.dsw.EstagioT2.controller;

import br.ufscar.dc.dsw.EstagioT2.domain.Candidatura;
import br.ufscar.dc.dsw.EstagioT2.domain.Empresa;
import br.ufscar.dc.dsw.EstagioT2.domain.Vaga;
import br.ufscar.dc.dsw.EstagioT2.security.UsuarioDetails;
import br.ufscar.dc.dsw.EstagioT2.service.CandidaturaService;
import br.ufscar.dc.dsw.EstagioT2.service.EmpresaService;
import br.ufscar.dc.dsw.EstagioT2.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    VagaService vagaService;

    @Autowired
    CandidaturaService candidaturaService;


    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal UsuarioDetails user) {
        Empresa empresa = empresaService.buscarPorEmail(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada: " + user.getUsername()));
        model.addAttribute("empresa", empresa);
        return "empresa/home";
    }


    @GetMapping("/cadastrarVaga")
    public String showCadastroVagaForm(Model model, Authentication authentication) {
        String email = authentication.getName();

        Optional<Empresa> optionalEmpresa = empresaService.buscarPorEmail(email);
        if (optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();

            Vaga vaga = new Vaga();
            vaga.setEmpresa(empresa); // Define a empresa associada à vaga

            model.addAttribute("vaga", vaga);
            model.addAttribute("empresa", empresa); // Passa a empresa para preencher o CNPJ no form

            return "empresa/cadastrarVaga"; // Retorna o template HTML para cadastrar vaga
        } else {
            // Se a empresa não for encontrada, redireciona ou exibe uma mensagem de erro
            return "redirect:/erro"; // Redireciona para uma página de erro ou faz outro tratamento
        }
    }

    // Lida com o envio do formulário de cadastro de vaga
    @PostMapping("/cadastrarVaga")
    public String cadastrarVaga(@ModelAttribute("vaga") Vaga vaga, Authentication authentication, RedirectAttributes attributes) {
        // Valida a data limite da vaga (não pode ser anterior à data atual)
        if (vaga.getDataLimiteInscricao().before(new Date())) {
            attributes.addFlashAttribute("erro", "A data limite de inscrição não pode ser anterior à data atual.");
            return "redirect:/empresa/cadastrarVaga"; // Redireciona com erro se a data estiver incorreta
        }

        // Define a empresa associada à vaga com base no usuário logado
        String email = authentication.getName();
        Optional<Empresa> optionalEmpresa = empresaService.buscarPorEmail(email);
        if (optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();
            vaga.setEmpresa(empresa);

            vagaService.salvar(vaga); // Salva a vaga no banco de dados
            return "redirect:/empresa/home"; // Redireciona para a página inicial da empresa
        } else {
            // Se a empresa não for encontrada, redireciona ou exibe uma mensagem de erro
            attributes.addFlashAttribute("erro", "Empresa não encontrada.");
            return "redirect:/empresa/vaga/cadastrar"; // Redireciona de volta ao formulário de cadastro
        }


    }


    @GetMapping("/listaVagas")
    public String listarVagas(Model model, Authentication authentication) {
        // Recupera o email do usuário autenticado
        String email = authentication.getName();

        // Busca a empresa pelo email do usuário autenticado
        Optional<Empresa> optionalEmpresa = empresaService.buscarPorEmail(email);

        if (optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();

            // Busca todas as vagas associadas à empresa
            List<Vaga> vagas = vagaService.buscarPorEmpresa(empresa);

            // Adiciona a lista de vagas ao modelo para exibição na página
            model.addAttribute("vagas", vagas);

            return "empresa/listaVagas"; // Página HTML para listar as vagas
        } else {
            // Caso a empresa não seja encontrada, redireciona para a página de erro ou inicial
            return "redirect:/empresa/home";
        }
    }

    @GetMapping("/editarVaga/{id}")
    public String showEditarVagaForm(@PathVariable("id") Long id, Model model, Authentication authentication) {
        // Recupera o email do usuário autenticado
        String email = authentication.getName();

        // Busca a empresa pelo email do usuário autenticado
        Optional<Empresa> optionalEmpresa = empresaService.buscarPorEmail(email);

        if (optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();

            // Busca a vaga pelo ID
            Vaga vaga = vagaService.buscarPorId(id);

            // Verifica se a vaga pertence à empresa logada
            if (vaga != null && vaga.getEmpresa().getId().equals(empresa.getId())) {
                // Adiciona a vaga ao modelo para preencher o formulário de edição
                model.addAttribute("vaga", vaga);
                return "empresa/editarVaga"; // Retorna o template HTML de edição de vaga
            } else {
                // Se a vaga não pertence à empresa ou não existe, redireciona para a lista de vagas
                return "redirect:/empresa/listaVagas";
            }
        } else {
            // Caso a empresa não seja encontrada, redireciona para a página inicial
            return "redirect:/empresa/home";
        }
    }



    @PostMapping("/editarVaga/{id}")
    public String editarVaga(@PathVariable("id") Long id, @ModelAttribute("vaga") Vaga vagaEditada, Authentication authentication, RedirectAttributes attributes) {
        // Recupera o email do usuário autenticado
        String email = authentication.getName();

        // Busca a empresa pelo email do usuário autenticado
        Optional<Empresa> optionalEmpresa = empresaService.buscarPorEmail(email);

        if (optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();

            // Busca a vaga pelo ID
            Vaga vaga = vagaService.buscarPorId(id);

            // Verifica se a vaga pertence à empresa logada
            if (vaga != null && vaga.getEmpresa().getId().equals(empresa.getId())) {
                // Atualiza os campos da vaga com os valores do formulário
                vaga.setDescricao(vagaEditada.getDescricao());
                vaga.setRemuneracao(vagaEditada.getRemuneracao());
                vaga.setDataLimiteInscricao(vagaEditada.getDataLimiteInscricao());
                vaga.setCidade(vagaEditada.getCidade());

                // Salva a vaga atualizada no banco de dados
                vagaService.salvar(vaga);

                attributes.addFlashAttribute("sucesso", "Vaga atualizada com sucesso!");
                return "redirect:/empresa/listaVagas"; // Redireciona para a lista de vagas após a edição
            } else {
                return "redirect:/empresa/listaVagas"; // Redireciona se a vaga não pertencer à empresa
            }
        } else {
            return "redirect:/empresa/home"; // Redireciona para a página inicial se a empresa não for encontrada
        }
    }


    @GetMapping("/excluirVaga/{id}")
    public String excluirVaga(@PathVariable("id") Long id, Authentication authentication, RedirectAttributes attributes) {
        // Recupera o email do usuário autenticado
        String email = authentication.getName();

        // Busca a empresa pelo email do usuário autenticado
        Optional<Empresa> optionalEmpresa = empresaService.buscarPorEmail(email);

        if (optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();

            // Busca a vaga pelo ID
            Vaga vaga = vagaService.buscarPorId(id);

            // Verifica se a vaga pertence à empresa logada
            if (vaga != null && vaga.getEmpresa().getId().equals(empresa.getId())) {
                vagaService.excluir(id);  // Exclui a vaga do banco de dados
                attributes.addFlashAttribute("sucesso", "Vaga excluída com sucesso!");
            } else {
                attributes.addFlashAttribute("erro", "Vaga não encontrada ou você não tem permissão para excluí-la.");
            }
        } else {
            attributes.addFlashAttribute("erro", "Empresa não encontrada.");
        }

        return "redirect:/empresa/listaVagas"; // Redireciona para a lista de vagas após a exclusão
    }

    @GetMapping("/verCandidatos/{id}")
    public String verCandidatos(@PathVariable("id") Long id, Model model, Authentication authentication, RedirectAttributes attributes) {
        // Recupera o email do usuário autenticado
        String email = authentication.getName();

        // Busca a empresa pelo email do usuário autenticado
        Optional<Empresa> optionalEmpresa = empresaService.buscarPorEmail(email);
        if (optionalEmpresa.isEmpty()) {
            attributes.addFlashAttribute("erro", "Empresa não encontrada.");
            return "redirect:/empresa/listaVagas";
        }

        Empresa empresa = optionalEmpresa.get();

        // Busca a vaga pelo ID
        Vaga vaga = vagaService.buscarPorId(id);

        // Verifica se a vaga pertence à empresa logada
        if (vaga == null || !vaga.getEmpresa().getId().equals(empresa.getId())) {
            attributes.addFlashAttribute("erro", "Vaga não encontrada ou você não tem permissão para visualizá-la.");
            return "redirect:/empresa/listaVagas";
        }

        // Busca as candidaturas para essa vaga
        List<Candidatura> candidaturas = candidaturaService.buscarPorVaga1(vaga);

        // Adiciona a vaga e as candidaturas ao modelo
        model.addAttribute("vaga", vaga);
        model.addAttribute("candidaturas", candidaturas);

        return "empresa/verCandidatos"; // Retorna o template HTML para listar os candidatos
    }

    @GetMapping("/downloadCurriculo/{candidaturaId}")
    public ResponseEntity<byte[]> downloadCurriculo(@PathVariable Long candidaturaId) {
        // Busca a candidatura pelo ID
        Candidatura candidatura = candidaturaService.buscarPorId(candidaturaId);
        if (candidatura == null || candidatura.getCurriculo() == null) {
            return ResponseEntity.notFound().build();
        }

        // Configura os headers da resposta
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", candidatura.getCurriculoNome());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        // Retorna o arquivo PDF do currículo
        return new ResponseEntity<>(candidatura.getCurriculo(), headers, HttpStatus.OK);
    }




}