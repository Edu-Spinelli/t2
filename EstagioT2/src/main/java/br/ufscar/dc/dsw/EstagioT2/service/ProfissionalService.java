package br.ufscar.dc.dsw.EstagioT2.service;

import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import br.ufscar.dc.dsw.EstagioT2.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfissionalService {

    @Autowired
    private  ProfissionalRepository profissionalRepository;

    public  List<Profissional> listarTodos() {
        return profissionalRepository.findAll();
    }

    public void salvar(Profissional profissional) {
        profissionalRepository.save(profissional);
    }

    public Optional<Profissional> buscarPorEmail(String email) {
        return profissionalRepository.findByUsuarioEmail(email);
    }


    public Profissional buscarPorId(Long id) {
        Optional<Profissional> profissional = profissionalRepository.findById(id);
        return profissional.orElse(null);
    }

    public void excluir(Long id) {
        profissionalRepository.deleteById(id);
    }
}
