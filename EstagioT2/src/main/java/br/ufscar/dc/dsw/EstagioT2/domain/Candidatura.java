package br.ufscar.dc.dsw.EstagioT2.domain;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Candidatura")
public class Candidatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_profissional", nullable = false)
    private Profissional profissional;

    @ManyToOne
    @JoinColumn(name = "id_vaga", nullable = false)
    private Vaga vaga;

    @Column(nullable = false)
    private String curriculo;

    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false)
    private StatusCandidatura status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCandidatura;

    private String entrevistaLink;

    @Temporal(TemporalType.TIMESTAMP)
    private Date entrevistaDataHora;

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }

    public String getCurriculo() {
        return curriculo;
    }

    public void setCurriculo(String curriculo) {
        this.curriculo = curriculo;
    }

    public StatusCandidatura getStatus() {
        return status;
    }

    public void setStatus(StatusCandidatura status) {
        this.status = status;
    }

    public Date getDataCandidatura() {
        return dataCandidatura;
    }

    public void setDataCandidatura(Date dataCandidatura) {
        this.dataCandidatura = dataCandidatura;
    }

    public String getEntrevistaLink() {
        return entrevistaLink;
    }

    public void setEntrevistaLink(String entrevistaLink) {
        this.entrevistaLink = entrevistaLink;
    }

    public Date getEntrevistaDataHora() {
        return entrevistaDataHora;
    }

    public void setEntrevistaDataHora(Date entrevistaDataHora) {
        this.entrevistaDataHora = entrevistaDataHora;
    }


}