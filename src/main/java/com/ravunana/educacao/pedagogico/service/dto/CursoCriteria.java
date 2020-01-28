package com.ravunana.educacao.pedagogico.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ravunana.educacao.pedagogico.domain.Curso} entity. This class is used
 * in {@link com.ravunana.educacao.pedagogico.web.rest.CursoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cursos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CursoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter sigla;

    private StringFilter areaFormacao;

    private LongFilter planoCurricularId;

    private LongFilter turmaId;

    private LongFilter planoActividadeId;

    private LongFilter dosificacaoCursoId;

    public CursoCriteria(){
    }

    public CursoCriteria(CursoCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.sigla = other.sigla == null ? null : other.sigla.copy();
        this.areaFormacao = other.areaFormacao == null ? null : other.areaFormacao.copy();
        this.planoCurricularId = other.planoCurricularId == null ? null : other.planoCurricularId.copy();
        this.turmaId = other.turmaId == null ? null : other.turmaId.copy();
        this.planoActividadeId = other.planoActividadeId == null ? null : other.planoActividadeId.copy();
        this.dosificacaoCursoId = other.dosificacaoCursoId == null ? null : other.dosificacaoCursoId.copy();
    }

    @Override
    public CursoCriteria copy() {
        return new CursoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getSigla() {
        return sigla;
    }

    public void setSigla(StringFilter sigla) {
        this.sigla = sigla;
    }

    public StringFilter getAreaFormacao() {
        return areaFormacao;
    }

    public void setAreaFormacao(StringFilter areaFormacao) {
        this.areaFormacao = areaFormacao;
    }

    public LongFilter getPlanoCurricularId() {
        return planoCurricularId;
    }

    public void setPlanoCurricularId(LongFilter planoCurricularId) {
        this.planoCurricularId = planoCurricularId;
    }

    public LongFilter getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(LongFilter turmaId) {
        this.turmaId = turmaId;
    }

    public LongFilter getPlanoActividadeId() {
        return planoActividadeId;
    }

    public void setPlanoActividadeId(LongFilter planoActividadeId) {
        this.planoActividadeId = planoActividadeId;
    }

    public LongFilter getDosificacaoCursoId() {
        return dosificacaoCursoId;
    }

    public void setDosificacaoCursoId(LongFilter dosificacaoCursoId) {
        this.dosificacaoCursoId = dosificacaoCursoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CursoCriteria that = (CursoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(sigla, that.sigla) &&
            Objects.equals(areaFormacao, that.areaFormacao) &&
            Objects.equals(planoCurricularId, that.planoCurricularId) &&
            Objects.equals(turmaId, that.turmaId) &&
            Objects.equals(planoActividadeId, that.planoActividadeId) &&
            Objects.equals(dosificacaoCursoId, that.dosificacaoCursoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nome,
        sigla,
        areaFormacao,
        planoCurricularId,
        turmaId,
        planoActividadeId,
        dosificacaoCursoId
        );
    }

    @Override
    public String toString() {
        return "CursoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (sigla != null ? "sigla=" + sigla + ", " : "") +
                (areaFormacao != null ? "areaFormacao=" + areaFormacao + ", " : "") +
                (planoCurricularId != null ? "planoCurricularId=" + planoCurricularId + ", " : "") +
                (turmaId != null ? "turmaId=" + turmaId + ", " : "") +
                (planoActividadeId != null ? "planoActividadeId=" + planoActividadeId + ", " : "") +
                (dosificacaoCursoId != null ? "dosificacaoCursoId=" + dosificacaoCursoId + ", " : "") +
            "}";
    }

}
