package com.ravunana.educacao.pedagogico.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.Professor} entity.
 */
public class ProfessorDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private String sexo;

    @Lob
    private byte[] fotografia;

    private String fotografiaContentType;
    @NotNull
    private String contacto;

    
    private String email;

    @NotNull
    private String residencia;

    @NotNull
    private String numeroAgente;

    private String utilizadorId;

    private String grauAcademico;

    private String cursoAcademico;

    @Lob
    private String observacao;

    private Boolean ativo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public byte[] getFotografia() {
        return fotografia;
    }

    public void setFotografia(byte[] fotografia) {
        this.fotografia = fotografia;
    }

    public String getFotografiaContentType() {
        return fotografiaContentType;
    }

    public void setFotografiaContentType(String fotografiaContentType) {
        this.fotografiaContentType = fotografiaContentType;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResidencia() {
        return residencia;
    }

    public void setResidencia(String residencia) {
        this.residencia = residencia;
    }

    public String getNumeroAgente() {
        return numeroAgente;
    }

    public void setNumeroAgente(String numeroAgente) {
        this.numeroAgente = numeroAgente;
    }

    public String getUtilizadorId() {
        return utilizadorId;
    }

    public void setUtilizadorId(String utilizadorId) {
        this.utilizadorId = utilizadorId;
    }

    public String getGrauAcademico() {
        return grauAcademico;
    }

    public void setGrauAcademico(String grauAcademico) {
        this.grauAcademico = grauAcademico;
    }

    public String getCursoAcademico() {
        return cursoAcademico;
    }

    public void setCursoAcademico(String cursoAcademico) {
        this.cursoAcademico = cursoAcademico;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProfessorDTO professorDTO = (ProfessorDTO) o;
        if (professorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), professorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfessorDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", fotografia='" + getFotografia() + "'" +
            ", contacto='" + getContacto() + "'" +
            ", email='" + getEmail() + "'" +
            ", residencia='" + getResidencia() + "'" +
            ", numeroAgente='" + getNumeroAgente() + "'" +
            ", utilizadorId='" + getUtilizadorId() + "'" +
            ", grauAcademico='" + getGrauAcademico() + "'" +
            ", cursoAcademico='" + getCursoAcademico() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", ativo='" + isAtivo() + "'" +
            "}";
    }
}
