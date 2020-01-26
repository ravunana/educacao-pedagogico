package com.ravunana.educacao.pedagogico.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.Horario} entity.
 */
public class HorarioDTO implements Serializable {

    private Long id;

    @NotNull
    private String inicio;

    @NotNull
    private String fim;

    @NotNull
    private ZonedDateTime data;

    @NotNull
    private Integer anoLectivo;

    @NotNull
    private String diaSemana;

    @NotNull
    private String categoria;


    private Long turmaId;

    private String turmaDescricao;

    private Long professorId;

    private String professorNumeroAgente;

    private Long curriculoId;

    private String curriculoDescricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFim() {
        return fim;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public Integer getAnoLectivo() {
        return anoLectivo;
    }

    public void setAnoLectivo(Integer anoLectivo) {
        this.anoLectivo = anoLectivo;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Long getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }

    public String getTurmaDescricao() {
        return turmaDescricao;
    }

    public void setTurmaDescricao(String turmaDescricao) {
        this.turmaDescricao = turmaDescricao;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }

    public String getProfessorNumeroAgente() {
        return professorNumeroAgente;
    }

    public void setProfessorNumeroAgente(String professorNumeroAgente) {
        this.professorNumeroAgente = professorNumeroAgente;
    }

    public Long getCurriculoId() {
        return curriculoId;
    }

    public void setCurriculoId(Long planoCurricularId) {
        this.curriculoId = planoCurricularId;
    }

    public String getCurriculoDescricao() {
        return curriculoDescricao;
    }

    public void setCurriculoDescricao(String planoCurricularDescricao) {
        this.curriculoDescricao = planoCurricularDescricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HorarioDTO horarioDTO = (HorarioDTO) o;
        if (horarioDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), horarioDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HorarioDTO{" +
            "id=" + getId() +
            ", inicio='" + getInicio() + "'" +
            ", fim='" + getFim() + "'" +
            ", data='" + getData() + "'" +
            ", anoLectivo=" + getAnoLectivo() +
            ", diaSemana='" + getDiaSemana() + "'" +
            ", categoria='" + getCategoria() + "'" +
            ", turmaId=" + getTurmaId() +
            ", turmaDescricao='" + getTurmaDescricao() + "'" +
            ", professorId=" + getProfessorId() +
            ", professorNumeroAgente='" + getProfessorNumeroAgente() + "'" +
            ", curriculoId=" + getCurriculoId() +
            ", curriculoDescricao='" + getCurriculoDescricao() + "'" +
            "}";
    }
}
