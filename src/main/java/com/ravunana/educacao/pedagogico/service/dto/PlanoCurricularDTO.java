package com.ravunana.educacao.pedagogico.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.PlanoCurricular} entity.
 */
public class PlanoCurricularDTO implements Serializable {

    private Long id;

    private String descricao;

    @NotNull
    private Boolean terminal;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private Integer tempoSemanal;

    private String periodoLectivo;

    @NotNull
    private String componente;

    @NotNull
    private String disciplina;

    @NotNull
    private Integer classe;


    private Long cursoId;

    private String cursoNome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(Boolean terminal) {
        this.terminal = terminal;
    }

    public Integer getTempoSemanal() {
        return tempoSemanal;
    }

    public void setTempoSemanal(Integer tempoSemanal) {
        this.tempoSemanal = tempoSemanal;
    }

    public String getPeriodoLectivo() {
        return periodoLectivo;
    }

    public void setPeriodoLectivo(String periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
    }

    public String getComponente() {
        return componente;
    }

    public void setComponente(String componente) {
        this.componente = componente;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public Integer getClasse() {
        return classe;
    }

    public void setClasse(Integer classe) {
        this.classe = classe;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public String getCursoNome() {
        return cursoNome;
    }

    public void setCursoNome(String cursoNome) {
        this.cursoNome = cursoNome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlanoCurricularDTO planoCurricularDTO = (PlanoCurricularDTO) o;
        if (planoCurricularDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), planoCurricularDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlanoCurricularDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", terminal='" + isTerminal() + "'" +
            ", tempoSemanal=" + getTempoSemanal() +
            ", periodoLectivo='" + getPeriodoLectivo() + "'" +
            ", componente='" + getComponente() + "'" +
            ", disciplina='" + getDisciplina() + "'" +
            ", classe=" + getClasse() +
            ", cursoId=" + getCursoId() +
            ", cursoNome='" + getCursoNome() + "'" +
            "}";
    }
}
