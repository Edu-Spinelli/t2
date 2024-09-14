package br.ufscar.dc.dsw.EstagioT2.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.EstagioT2.domain.Candidatura;
import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import br.ufscar.dc.dsw.EstagioT2.domain.StatusCandidatura;
import br.ufscar.dc.dsw.EstagioT2.domain.Vaga;
import br.ufscar.dc.dsw.EstagioT2.service.CandidaturaService;
import br.ufscar.dc.dsw.EstagioT2.service.ProfissionalService;
import br.ufscar.dc.dsw.EstagioT2.service.StatusCandidaturaService;
import br.ufscar.dc.dsw.EstagioT2.service.VagaService;

@Controller
@RequestMapping("/profissional")
public class ProfissionalController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

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
        // Busca todas as vagas disponíveis
        List<Vaga> vagas = vagaService.listarTodas();

        // Filtra as vagas cuja data limite de inscrição ainda não passou
        List<Vaga> vagasDisponiveis = vagas.stream()
                .filter(vaga -> vaga.getDataLimiteInscricao().after(new Date())) // Verifica se a data limite é posterior à data atual
                .collect(Collectors.toList());

        // Adiciona as vagas disponíveis ao modelo
        model.addAttribute("vagas", vagasDisponiveis);

        return "profissional/listaVagas"; // Aponta para o HTML da lista de vagas
    }

    @GetMapping("/inscreverVaga/{id}")
    public String exibirFormularioInscricao(@PathVariable("id") Long id, Model model, Authentication authentication, RedirectAttributes attributes, Locale locale) {
        // Busca a vaga pelo ID
        Vaga vaga = vagaService.buscarPorId(id);
        if (vaga == null) {
            attributes.addFlashAttribute("erro", messageSource.getMessage("vaga.not_found", null, locale));
            return "redirect:/profissional/vagas";
        }

        // Busca o profissional logado
        String email = authentication.getName();
        Optional<Profissional> optionalProfissional = profissionalService.buscarPorEmail(email);
        if (!optionalProfissional.isPresent()) {
            attributes.addFlashAttribute("erro", messageSource.getMessage("profissional.not_found", null, locale));

            return "redirect:/profissional/vagas";
        }

        Profissional profissional = optionalProfissional.get();

        List<Candidatura> candidaturasExistentes = candidaturaService.buscarPorProfissionalEVaga(profissional, vaga);
        if (!candidaturasExistentes.isEmpty()) {
            // Se já houver uma candidatura, redireciona para a lista de vagas com uma mensagem
            attributes.addFlashAttribute("erro", messageSource.getMessage("candidatura.already_exists", null, locale));
            return "redirect:/profissional/vagas";
        }


        // Cria uma nova candidatura e preenche com a vaga e o profissional
        Candidatura candidatura = new Candidatura();
        candidatura.setVaga(vaga);
        candidatura.setProfissional(profissional);

        // Adiciona a vaga, profissional e candidatura ao modelo para o Thymeleaf
        model.addAttribute("vaga", vaga);
        model.addAttribute("profissional", profissional);
        model.addAttribute("candidatura", candidatura);

        return "profissional/inscreverVaga";
    }




    @PostMapping("/inscreverVaga/{id}")
    public String inscreverVaga(@PathVariable("id") Long id,
                                @RequestParam("curriculoFile") MultipartFile curriculoFile,
                                Authentication authentication,
                                RedirectAttributes attributes, Locale locale) {

        // Busca da vaga
        Vaga vaga = vagaService.buscarPorId(id);
        if (vaga == null) {
            attributes.addFlashAttribute("erro", messageSource.getMessage("vaga.not_found", null, locale));

            return "redirect:/profissional/vagas";
        }

        // Busca do profissional logado
        String email = authentication.getName();
        Optional<Profissional> optionalProfissional = profissionalService.buscarPorEmail(email);
        if (!optionalProfissional.isPresent()) {
            attributes.addFlashAttribute("erro", messageSource.getMessage("profissional.not_found", null, locale));
            return "redirect:/profissional/vagas";
        }
        Profissional profissional = optionalProfissional.get();

        // Verificar se o profissional já se candidatou a essa vaga
        List<Candidatura> candidaturasExistentes = candidaturaService.buscarPorProfissionalEVaga(profissional, vaga);
        if (!candidaturasExistentes.isEmpty()) {
            attributes.addFlashAttribute("erro", "Você já se inscreveu nesta vaga.");
            return "redirect:/profissional/vagas";
        }

        // Verificar e processar o upload do arquivo PDF
        if (curriculoFile.isEmpty()) {
            attributes.addFlashAttribute("erro", messageSource.getMessage("curriculo.empty", null, locale));
            return "redirect:/profissional/vagas";
        }

        if (!curriculoFile.getContentType().equals("application/pdf")) {
            attributes.addFlashAttribute("erro", messageSource.getMessage("curriculo.not_pdf", null, locale));

            return "redirect:/profissional/vagas";
        }

        Candidatura novaCandidatura = new Candidatura();

        try {
            novaCandidatura.setCurriculo(curriculoFile.getBytes());
            novaCandidatura.setCurriculoNome(curriculoFile.getOriginalFilename());
        } catch (IOException e) {
            attributes.addFlashAttribute("erro", messageSource.getMessage("curriculo.process_error", null, locale));

            return "redirect:/profissional/vagas";
        }

        // Definindo vaga, profissional e data de candidatura
        novaCandidatura.setVaga(vaga);
        novaCandidatura.setProfissional(profissional);
        novaCandidatura.setDataCandidatura(new Date());

        // Definindo o status padrão da candidatura (exemplo: PENDENTE)
        StatusCandidatura statusPendente = statusCandidaturaService.buscarPorDescricao("ABERTO");
        if (statusPendente == null) {
            attributes.addFlashAttribute("erro", messageSource.getMessage("status.not_found", null, locale));
            return "redirect:/profissional/vagas";
        }
        novaCandidatura.setStatus(statusPendente);

        // Salvando a candidatura
        candidaturaService.salvar(novaCandidatura);

        attributes.addFlashAttribute("sucesso", messageSource.getMessage("candidatura.success", null, locale));


        return "redirect:/profissional/vagas";
    }


    @GetMapping("/downloadCurriculo/{vagaId}")
    public ResponseEntity<byte[]> downloadCurriculo(@PathVariable Long vagaId) {
        try {
            // Buscar a vaga pelo ID
            Vaga vaga = vagaService.buscarPorId(vagaId);
            if (vaga == null) {
                return ResponseEntity.notFound().build();
            }

            // Buscar a candidatura associada à vaga (assumindo que existe apenas uma por vaga)
            Candidatura candidatura = candidaturaService.buscarPorVaga(vaga);
            if (candidatura == null || candidatura.getCurriculo() == null) {
                return ResponseEntity.notFound().build();
            }

            // Configurar os headers da resposta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", candidatura.getCurriculoNome());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            // Retornar o arquivo PDF
            return new ResponseEntity<>(candidatura.getCurriculo(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @GetMapping("/vagasInscritas")
    public String listarVagasInscritas(Model model, Authentication authentication) {
        // Obtém o email do usuário logado
        String email = authentication.getName();

        // Busca o profissional logado pelo email
        Optional<Profissional> optionalProfissional = profissionalService.buscarPorEmail(email);
        if (!optionalProfissional.isPresent()) {
            return "redirect:/profissional/vagas"; // Redireciona se o profissional não for encontrado
        }
        Profissional profissional = optionalProfissional.get();

        // Busca todas as candidaturas do profissional
        List<Candidatura> candidaturas = candidaturaService.buscarPorProfissional(profissional);

        // Adiciona as candidaturas ao modelo
        model.addAttribute("candidaturas", candidaturas);

        return "profissional/vagasInscritas"; // Retorna a view com a lista de vagas inscritas
    }







}