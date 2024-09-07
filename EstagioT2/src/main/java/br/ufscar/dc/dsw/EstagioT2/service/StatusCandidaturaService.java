package br.ufscar.dc.dsw.EstagioT2.service;

import br.ufscar.dc.dsw.EstagioT2.domain.StatusCandidatura;
import br.ufscar.dc.dsw.EstagioT2.repository.StatusCandidaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusCandidaturaService {

    @Autowired
    private StatusCandidaturaRepository statusCandidaturaRepository;

    public StatusCandidatura buscarPorDescricao(String descricao) {
        Optional<StatusCandidatura> status = statusCandidaturaRepository.findByDescricao(descricao);
        return status.orElse(null); // Retorna null se o status não for encontrado
    }

    public void salvar(StatusCandidatura status) {
        statusCandidaturaRepository.save(status);
    }


    public List<StatusCandidatura> listarTodos() {
        return statusCandidaturaRepository.findAll();
    }
}
