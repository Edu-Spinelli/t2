package br.ufscar.dc.dsw.EstagioT2.repository;

import br.ufscar.dc.dsw.EstagioT2.domain.Candidatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {

}
