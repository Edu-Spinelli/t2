package br.ufscar.dc.dsw.EstagioT2.controller;

import br.ufscar.dc.dsw.EstagioT2.domain.Candidatura;
import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import br.ufscar.dc.dsw.EstagioT2.domain.StatusCandidatura;
import br.ufscar.dc.dsw.EstagioT2.domain.Vaga;
import br.ufscar.dc.dsw.EstagioT2.security.UsuarioDetails;
import br.ufscar.dc.dsw.EstagioT2.service.CandidaturaService;
import br.ufscar.dc.dsw.EstagioT2.service.ProfissionalService;
import br.ufscar.dc.dsw.EstagioT2.service.StatusCandidaturaService;
import br.ufscar.dc.dsw.EstagioT2.service.VagaService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profissional")
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private VagaService vagaService;

    @Autowired
    private CandidaturaService candidaturaService;

    @Autowired
    private StatusCandidaturaService statusCandidaturaService;



    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuario = auth.getName(); // Isso retorna o email ou nome de usuário

        // Aqui buscamos o profissional pelo email, lidando com o Optional
        Optional<Profissional> optionalProfissional = profissionalService.buscarPorEmail(emailUsuario);

        if (optionalProfissional.isPresent()) {
            Profissional profissional = optionalProfissional.get();
            model.addAttribute("nomeUsuario", profissional.getNome()); // Adiciona o nome ao modelo
        } else {
            model.addAttribute("nomeUsuario", "Usuário"); // Valor padrão caso o profissional não seja encontrado
        }

        return "profissional/home"; // Retorna o template home.html
    }

    @GetMapping("/vagas")
    public String listarVagas(Model model) {
        List<Vaga> vagas = vagaService.listarTodas(); // Busca todas as vagas disponíveis
        model.addAttribute("vagas", vagaService.listarTodas());
        return "profissional/listaVagas"; // Aponta para o HTML da lista de vagas
    }

    @GetMapping("/inscreverVaga/{id}")
    public String exibirFormularioInscricao(@PathVariable("id") Long id, Model model, Authentication authentication) {
        // Busca a vaga pelo ID
        Vaga vaga = vagaService.buscarPorId(id);
        if (vaga == null) {
            return "redirect:/profissional/vagas";
        }

        // Busca o profissional logado
        String email = authentication.getName();
        Optional<Profissional> optionalProfissional = profissionalService.buscarPorEmail(email);
        if (!optionalProfissional.isPresent()) {
            return "redirect:/profissional/vagas";
        }

        Profissional profissional = optionalProfissional.get();


        List<Candidatura> candidaturasExistentes = candidaturaService.buscarPorProfissionalEVaga(profissional, vaga);
        if (!candidaturasExistentes.isEmpty()) {
            // Se já houver uma candidatura, redireciona para a lista de vagas com uma mensagem
            return "redirect:/profissional/vagas";
        }

        // Cria uma nova candidatura e preenche com a vaga e o profissional
        Candidatura candidatura = new Candidatura();
        candidatura.setVaga(vaga);
        candidatura.setProfissional(profissional);

        // Adiciona a vaga e a candidatura ao modelo para o Thymeleaf
        model.addAttribute("vaga", vaga);
        model.addAttribute("candidatura", candidatura);

        return "profissional/inscreverVaga";
    }



    @PostMapping("/inscreverVaga/{id}")
    public String inscreverVaga(@PathVariable("id") Long id, @ModelAttribute("candidatura") Candidatura candidatura, Authentication authentication, RedirectAttributes attributes) {

        // Busca da vaga
        Vaga vaga = vagaService.buscarPorId(id);
        if (vaga == null) {
            attributes.addFlashAttribute("erro", "Vaga não encontrada.");
            return "redirect:/profissional/vagas";
        }

        // Busca do profissional logado
        String email = authentication.getName();
        Optional<Profissional> optionalProfissional = profissionalService.buscarPorEmail(email);
        if (!optionalProfissional.isPresent()) {
            attributes.addFlashAttribute("erro", "Profissional não encontrado.");
            return "redirect:/profissional/vagas";
        }
        Profissional profissional = optionalProfissional.get();

        // Verificar se o profissional já se candidatou a essa vaga
        List<Candidatura> candidaturasExistentes = candidaturaService.buscarPorProfissionalEVaga(profissional, vaga);
        if (!candidaturasExistentes.isEmpty()) {
            attributes.addFlashAttribute("erro", "Você já se inscreveu nesta vaga.");
            return "redirect:/profissional/vagas";
        }

        // Definindo vaga, profissional e data de candidatura
        candidatura.setVaga(vaga);
        candidatura.setProfissional(profissional);
        candidatura.setDataCandidatura(new Date());

        // Definindo o status padrão da candidatura (exemplo: PENDENTE)
        StatusCandidatura statusPendente = statusCandidaturaService.buscarPorDescricao("ABERTO");
        if (statusPendente == null) {
            attributes.addFlashAttribute("erro", "Status de candidatura não encontrado.");
            return "redirect:/profissional/vagas";
        }
        candidatura.setStatus(statusPendente);

        // Salvando a candidatura
        candidaturaService.salvar(candidatura);

        attributes.addFlashAttribute("sucesso", "Inscrição realizada com sucesso!");

        return "redirect:/profissional/vagas";
    }









}