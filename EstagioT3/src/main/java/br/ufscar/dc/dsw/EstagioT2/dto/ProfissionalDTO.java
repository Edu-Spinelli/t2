package br.ufscar.dc.dsw.EstagioT2.dto;

import br.ufscar.dc.dsw.EstagioT2.domain.Profissional;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ProfissionalDTO {

    private String nome;
    private String cpf;
    private String telefone;
    @Enumerated(EnumType.STRING)
    private Profissional.Sexo sexo;    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Define o formato da data

    private Date dataNascimento;    private String email;
    private String senha;

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Profissional.Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Profissional.Sexo sexo) {
        this.sexo = sexo;
    }


    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
