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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.ravunana.educacao.pedagogico.domain.PlanoAula} entity. This class is used
 * in {@link com.ravunana.educacao.pedagogico.web.rest.PlanoAulaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plano-aulas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlanoAulaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter tempo;

    private LongFilter turmaId;

    private LongFilter dosificacaoId;

    private LongFilter professorId;

    private LongFilter curriculoId;

    private LongFilter aulaPlanoAulaId;

    public PlanoAulaCriteria(){
    }

    public PlanoAulaCriteria(PlanoAulaCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.tempo = other.tempo == null ? null : other.tempo.copy();
        this.turmaId = other.turmaId == null ? null : other.turmaId.copy();
        this.dosificacaoId = other.dosificacaoId == null ? null : other.dosificacaoId.copy();
        this.professorId = other.professorId == null ? null : other.professorId.copy();
        this.curriculoId = other.curriculoId == null ? null : other.curriculoId.copy();
        this.aulaPlanoAulaId = other.aulaPlanoAulaId == null ? null : other.aulaPlanoAulaId.copy();
    }

    @Override
    public PlanoAulaCriteria copy() {
        return new PlanoAulaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getTempo() {
        return tempo;
    }

    public void setTempo(ZonedDateTimeFilter tempo) {
        this.tempo = tempo;
    }

    public LongFilter getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(LongFilter turmaId) {
        this.turmaId = turmaId;
    }

    public LongFilter getDosificacaoId() {
        return dosificacaoId;
    }

    public void setDosificacaoId(LongFilter dosificacaoId) {
        this.dosificacaoId = dosificacaoId;
    }

    public LongFilter getProfessorId() {
        return professorId;
    }

    public void setProfessorId(LongFilter professorId) {
        this.professorId = professorId;
    }

    public LongFilter getCurriculoId() {
        return curriculoId;
    }

    public void setCurriculoId(LongFilter curriculoId) {
        this.curriculoId = curriculoId;
    }

    public LongFilter getAulaPlanoAulaId() {
        return aulaPlanoAulaId;
    }

    public void setAulaPlanoAulaId(LongFilter aulaPlanoAulaId) {
        this.aulaPlanoAulaId = aulaPlanoAulaId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlanoAulaCriteria that = (PlanoAulaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(tempo, that.tempo) &&
            Objects.equals(turmaId, that.turmaId) &&
            Objects.equals(dosificacaoId, that.dosificacaoId) &&
            Objects.equals(professorId, that.professorId) &&
            Objects.equals(curriculoId, that.curriculoId) &&
            Objects.equals(aulaPlanoAulaId, that.aulaPlanoAulaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        tempo,
        turmaId,
        dosificacaoId,
        professorId,
        curriculoId,
        aulaPlanoAulaId
        );
    }

    @Override
    public String toString() {
        return "PlanoAulaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tempo != null ? "tempo=" + tempo + ", " : "") +
                (turmaId != null ? "turmaId=" + turmaId + ", " : "") +
                (dosificacaoId != null ? "dosificacaoId=" + dosificacaoId + ", " : "") +
                (professorId != null ? "professorId=" + professorId + ", " : "") +
                (curriculoId != null ? "curriculoId=" + curriculoId + ", " : "") +
                (aulaPlanoAulaId != null ? "aulaPlanoAulaId=" + aulaPlanoAulaId + ", " : "") +
            "}";
    }

}
