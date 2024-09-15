package br.ufscar.dc.dsw.EstagioT2.service;

import br.ufscar.dc.dsw.EstagioT2.domain.Candidatura;
import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import br.ufscar.dc.dsw.EstagioT2.domain.Vaga;
import br.ufscar.dc.dsw.EstagioT2.repository.CandidaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidaturaService {

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    public List<Candidatura> listarTodas() {
        return candidaturaRepository.findAll();
    }

    public void salvar(Candidatura candidatura) {
        candidaturaRepository.save(candidatura);
    }

    public Candidatura buscarPorId(Long id) {
        Optional<Candidatura> candidatura = candidaturaRepository.findById(id);
        return candidatura.orElse(null);
    }

    public void excluir(Long id) {
        candidaturaRepository.deleteById(id);
    }

    public List<Candidatura> buscarPorProfissionalEVaga(Profissional profissional, Vaga vaga) {
        return candidaturaRepository.findByProfissionalAndVaga(profissional, vaga);
    }

    public Candidatura buscarPorVaga(Vaga vaga) {
        return candidaturaRepository.findFirstByVaga(vaga);
    }

    public List<Candidatura> buscarPorProfissional(Profissional profissional) {
        return candidaturaRepository.findByProfissional(profissional);
    }

    public List<Candidatura> buscarPorVaga1(Vaga vaga) {
        return candidaturaRepository.findByVaga(vaga);
    }



}
