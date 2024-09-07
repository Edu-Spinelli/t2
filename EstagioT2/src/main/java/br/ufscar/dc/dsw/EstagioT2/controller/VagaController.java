package br.ufscar.dc.dsw.EstagioT2.controller;

import br.ufscar.dc.dsw.EstagioT2.domain.Vaga;
import br.ufscar.dc.dsw.EstagioT2.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vagas")
public class VagaController {
    @Autowired
    private VagaService vagaService;

    @GetMapping
    public String listarVagas(Model model) {
        // Busca todas as vagas disponíveis
        List<Vaga> vagas = vagaService.listarTodas();

        // Filtra as vagas cuja data limite de inscrição ainda não passou
        List<Vaga> vagasDisponiveis = vagas.stream()
                .filter(vaga -> vaga.getDataLimiteInscricao().after(new Date())) // Verifica se a data limite é posterior à data atual
                .collect(Collectors.toList());

        // Adiciona as vagas disponíveis ao modelo
        model.addAttribute("vagas", vagasDisponiveis);

        return "vaga/listarVagas"; // Aponta para o HTML da lista de vagas
    }


    @PostMapping
    public String buscarVagasPorCidade(@RequestParam("cidade") String cidade, Model model) {
        List<Vaga> vagas;

        // Filtra as vagas pela cidade se foi fornecida uma cidade, senão exibe todas
        if (cidade != null && !cidade.isEmpty()) {
            vagas = vagaService.buscarPorCidade(cidade);
        } else {
            vagas = vagaService.listarTodas(); // Se não for fornecida cidade, lista todas
        }

        // Filtra as vagas cuja data limite ainda não passou
        List<Vaga> vagasDisponiveis = vagas.stream()
                .filter(vaga -> vaga.getDataLimiteInscricao().after(new Date()))
                .collect(Collectors.toList());

        model.addAttribute("vagas", vagasDisponiveis);
        model.addAttribute("cidadePesquisada", cidade); // Passa a cidade digitada de volta para o frontend
        return "vaga/listarVagas";
    }

}