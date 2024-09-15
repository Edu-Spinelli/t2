package br.ufscar.dc.dsw.EstagioT2.repository;

import br.ufscar.dc.dsw.EstagioT2.domain.Empresa;
import br.ufscar.dc.dsw.EstagioT2.domain.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VagaRepository extends JpaRepository<Vaga, Long> {
    List<Vaga> findByEmpresa(Empresa empresa);
    List<Vaga> findByCidade(String cidade); // Esse método faz a busca por cidade
    List<Vaga> findByCidadeContainingIgnoreCase(String cidade); // Esse método faz a busca por cidade
}