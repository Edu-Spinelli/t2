package br.ufscar.dc.dsw.EstagioT2.repository;

import br.ufscar.dc.dsw.EstagioT2.domain.Candidatura;
import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import br.ufscar.dc.dsw.EstagioT2.domain.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {
    List<Candidatura> findByProfissionalAndVaga(Profissional profissional, Vaga vaga);

    Candidatura findFirstByVaga(Vaga vaga);



    List<Candidatura> findByProfissional(Profissional profissional);

}


