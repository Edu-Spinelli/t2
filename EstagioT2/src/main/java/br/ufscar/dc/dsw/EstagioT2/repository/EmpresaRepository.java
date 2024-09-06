package br.ufscar.dc.dsw.EstagioT2.repository;

import br.ufscar.dc.dsw.EstagioT2.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByUsuarioEmail(String email);




}
