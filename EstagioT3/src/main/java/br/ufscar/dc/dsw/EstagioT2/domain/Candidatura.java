package br.ufscar.dc.dsw.EstagioT2.domain;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Lob
    @Column(name = "curriculo", columnDefinition = "LONGBLOB")
    private byte[] curriculo;

    @Column(name = "curriculo_nome", length = 255)
    private String curriculoNome;


    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false)
    private StatusCandidatura status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCandidatura;

    private String entrevistaLink;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")

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

    public byte[] getCurriculo() {
        return curriculo;
    }

    public void setCurriculo(byte[] curriculo) {
        this.curriculo = curriculo;
    }

    public String getCurriculoNome() {
        return curriculoNome;
    }

    public void setCurriculoNome(String curriculoNome) {
        this.curriculoNome = curriculoNome;
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