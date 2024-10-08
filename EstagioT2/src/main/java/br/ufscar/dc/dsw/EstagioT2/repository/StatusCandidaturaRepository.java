package br.ufscar.dc.dsw.EstagioT2.repository;

import br.ufscar.dc.dsw.EstagioT2.domain.StatusCandidatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusCandidaturaRepository extends JpaRepository<StatusCandidatura, Long> {

    Optional<StatusCandidatura> findByDescricao(String descricao);


}
