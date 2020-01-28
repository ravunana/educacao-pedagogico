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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.ravunana.educacao.pedagogico.domain.PlanoActividade} entity. This class is used
 * in {@link com.ravunana.educacao.pedagogico.web.rest.PlanoActividadeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plano-actividades?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlanoActividadeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter numeroActividade;

    private StringFilter atividade;

    private LocalDateFilter de;

    private LocalDateFilter ate;

    private StringFilter responsavel;

    private StringFilter local;

    private StringFilter participantes;

    private StringFilter coResponsavel;

    private IntegerFilter anoLectivo;

    private StringFilter statusActividade;

    private StringFilter periodoLectivo;

    private StringFilter turno;

    private IntegerFilter classe;

    private LongFilter cursoId;

    private LongFilter turmaId;

    public PlanoActividadeCriteria(){
    }

    public PlanoActividadeCriteria(PlanoActividadeCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.numeroActividade = other.numeroActividade == null ? null : other.numeroActividade.copy();
        this.atividade = other.atividade == null ? null : other.atividade.copy();
        this.de = other.de == null ? null : other.de.copy();
        this.ate = other.ate == null ? null : other.ate.copy();
        this.responsavel = other.responsavel == null ? null : other.responsavel.copy();
        this.local = other.local == null ? null : other.local.copy();
        this.participantes = other.participantes == null ? null : other.participantes.copy();
        this.coResponsavel = other.coResponsavel == null ? null : other.coResponsavel.copy();
        this.anoLectivo = other.anoLectivo == null ? null : other.anoLectivo.copy();
        this.statusActividade = other.statusActividade == null ? null : other.statusActividade.copy();
        this.periodoLectivo = other.periodoLectivo == null ? null : other.periodoLectivo.copy();
        this.turno = other.turno == null ? null : other.turno.copy();
        this.classe = other.classe == null ? null : other.classe.copy();
        this.cursoId = other.cursoId == null ? null : other.cursoId.copy();
        this.turmaId = other.turmaId == null ? null : other.turmaId.copy();
    }

    @Override
    public PlanoActividadeCriteria copy() {
        return new PlanoActividadeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getNumeroActividade() {
        return numeroActividade;
    }

    public void setNumeroActividade(IntegerFilter numeroActividade) {
        this.numeroActividade = numeroActividade;
    }

    public StringFilter getAtividade() {
        return atividade;
    }

    public void setAtividade(StringFilter atividade) {
        this.atividade = atividade;
    }

    public LocalDateFilter getDe() {
        return de;
    }

    public void setDe(LocalDateFilter de) {
        this.de = de;
    }

    public LocalDateFilter getAte() {
        return ate;
    }

    public void setAte(LocalDateFilter ate) {
        this.ate = ate;
    }

    public StringFilter getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(StringFilter responsavel) {
        this.responsavel = responsavel;
    }

    public StringFilter getLocal() {
        return local;
    }

    public void setLocal(StringFilter local) {
        this.local = local;
    }

    public StringFilter getParticipantes() {
        return participantes;
    }

    public void setParticipantes(StringFilter participantes) {
        this.participantes = participantes;
    }

    public StringFilter getCoResponsavel() {
        return coResponsavel;
    }

    public void setCoResponsavel(StringFilter coResponsavel) {
        this.coResponsavel = coResponsavel;
    }

    public IntegerFilter getAnoLectivo() {
        return anoLectivo;
    }

    public void setAnoLectivo(IntegerFilter anoLectivo) {
        this.anoLectivo = anoLectivo;
    }

    public StringFilter getStatusActividade() {
        return statusActividade;
    }

    public void setStatusActividade(StringFilter statusActividade) {
        this.statusActividade = statusActividade;
    }

    public StringFilter getPeriodoLectivo() {
        return periodoLectivo;
    }

    public void setPeriodoLectivo(StringFilter periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
    }

    public StringFilter getTurno() {
        return turno;
    }

    public void setTurno(StringFilter turno) {
        this.turno = turno;
    }

    public IntegerFilter getClasse() {
        return classe;
    }

    public void setClasse(IntegerFilter classe) {
        this.classe = classe;
    }

    public LongFilter getCursoId() {
        return cursoId;
    }

    public void setCursoId(LongFilter cursoId) {
        this.cursoId = cursoId;
    }

    public LongFilter getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(LongFilter turmaId) {
        this.turmaId = turmaId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlanoActividadeCriteria that = (PlanoActividadeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(numeroActividade, that.numeroActividade) &&
            Objects.equals(atividade, that.atividade) &&
            Objects.equals(de, that.de) &&
            Objects.equals(ate, that.ate) &&
            Objects.equals(responsavel, that.responsavel) &&
            Objects.equals(local, that.local) &&
            Objects.equals(participantes, that.participantes) &&
            Objects.equals(coResponsavel, that.coResponsavel) &&
            Objects.equals(anoLectivo, that.anoLectivo) &&
            Objects.equals(statusActividade, that.statusActividade) &&
            Objects.equals(periodoLectivo, that.periodoLectivo) &&
            Objects.equals(turno, that.turno) &&
            Objects.equals(classe, that.classe) &&
            Objects.equals(cursoId, that.cursoId) &&
            Objects.equals(turmaId, that.turmaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        numeroActividade,
        atividade,
        de,
        ate,
        responsavel,
        local,
        participantes,
        coResponsavel,
        anoLectivo,
        statusActividade,
        periodoLectivo,
        turno,
        classe,
        cursoId,
        turmaId
        );
    }

    @Override
    public String toString() {
        return "PlanoActividadeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (numeroActividade != null ? "numeroActividade=" + numeroActividade + ", " : "") +
                (atividade != null ? "atividade=" + atividade + ", " : "") +
                (de != null ? "de=" + de + ", " : "") +
                (ate != null ? "ate=" + ate + ", " : "") +
                (responsavel != null ? "responsavel=" + responsavel + ", " : "") +
                (local != null ? "local=" + local + ", " : "") +
                (participantes != null ? "participantes=" + participantes + ", " : "") +
                (coResponsavel != null ? "coResponsavel=" + coResponsavel + ", " : "") +
                (anoLectivo != null ? "anoLectivo=" + anoLectivo + ", " : "") +
                (statusActividade != null ? "statusActividade=" + statusActividade + ", " : "") +
                (periodoLectivo != null ? "periodoLectivo=" + periodoLectivo + ", " : "") +
                (turno != null ? "turno=" + turno + ", " : "") +
                (classe != null ? "classe=" + classe + ", " : "") +
                (cursoId != null ? "cursoId=" + cursoId + ", " : "") +
                (turmaId != null ? "turmaId=" + turmaId + ", " : "") +
            "}";
    }

}
