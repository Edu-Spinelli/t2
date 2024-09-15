package br.ufscar.dc.dsw.EstagioT2.service;

import br.ufscar.dc.dsw.EstagioT2.domain.Empresa;
import br.ufscar.dc.dsw.EstagioT2.domain.Vaga;
import br.ufscar.dc.dsw.EstagioT2.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;

    public List<Vaga> listarTodas() {
        return vagaRepository.findAll();
    }

    public Vaga buscarPorId(Long id) {
        return vagaRepository.findById(id).orElse(null);
    }

    public Vaga salvar(Vaga vaga) {
        return vagaRepository.save(vaga);
    }

    public void excluir(Long id) {
        vagaRepository.deleteById(id);
    }

    public List<Vaga> buscarPorEmpresa(Empresa empresa) {
        return vagaRepository.findByEmpresa(empresa);
    }



    public List<Vaga> buscarPorCidade(String cidade) {
        return vagaRepository.findByCidadeContainingIgnoreCase(cidade); // Esse método deve ser implementado no seu repository
    }


}