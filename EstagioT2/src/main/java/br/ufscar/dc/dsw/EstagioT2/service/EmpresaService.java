package br.ufscar.dc.dsw.EstagioT2.service;

import br.ufscar.dc.dsw.EstagioT2.domain.Empresa;
import br.ufscar.dc.dsw.EstagioT2.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    public Optional<Empresa> buscarPorEmail(String email) {
        return empresaRepository.findByUsuarioEmail(email);
    }

    public void salvar(Empresa empresa) {
        empresaRepository.save(empresa);
    }

    public Empresa buscarPorId(Long id) {
        Optional<Empresa> empresa = empresaRepository.findById(id);
        return empresa.orElse(null);
    }

    public void excluir(Long id) {
        empresaRepository.deleteById(id);
    }
}
