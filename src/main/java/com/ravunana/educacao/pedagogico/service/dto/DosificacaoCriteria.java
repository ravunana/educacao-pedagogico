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
 * Criteria class for the {@link com.ravunana.educacao.pedagogico.domain.Dosificacao} entity. This class is used
 * in {@link com.ravunana.educacao.pedagogico.web.rest.DosificacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dosificacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DosificacaoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter peridoLective;

    private IntegerFilter semanaLectiva;

    private LocalDateFilter de;

    private LocalDateFilter ate;

    private StringFilter unidadeTematica;

    private IntegerFilter tempoAula;

    private StringFilter formaAvaliacao;

    private LongFilter planoAulaId;

    private LongFilter cursoId;

    private LongFilter curriuloId;

    public DosificacaoCriteria(){
    }

    public DosificacaoCriteria(DosificacaoCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.peridoLective = other.peridoLective == null ? null : other.peridoLective.copy();
        this.semanaLectiva = other.semanaLectiva == null ? null : other.semanaLectiva.copy();
        this.de = other.de == null ? null : other.de.copy();
        this.ate = other.ate == null ? null : other.ate.copy();
        this.unidadeTematica = other.unidadeTematica == null ? null : other.unidadeTematica.copy();
        this.tempoAula = other.tempoAula == null ? null : other.tempoAula.copy();
        this.formaAvaliacao = other.formaAvaliacao == null ? null : other.formaAvaliacao.copy();
        this.planoAulaId = other.planoAulaId == null ? null : other.planoAulaId.copy();
        this.cursoId = other.cursoId == null ? null : other.cursoId.copy();
        this.curriuloId = other.curriuloId == null ? null : other.curriuloId.copy();
    }

    @Override
    public DosificacaoCriteria copy() {
        return new DosificacaoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPeridoLective() {
        return peridoLective;
    }

    public void setPeridoLective(StringFilter peridoLective) {
        this.peridoLective = peridoLective;
    }

    public IntegerFilter getSemanaLectiva() {
        return semanaLectiva;
    }

    public void setSemanaLectiva(IntegerFilter semanaLectiva) {
        this.semanaLectiva = semanaLectiva;
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

    public StringFilter getUnidadeTematica() {
        return unidadeTematica;
    }

    public void setUnidadeTematica(StringFilter unidadeTematica) {
        this.unidadeTematica = unidadeTematica;
    }

    public IntegerFilter getTempoAula() {
        return tempoAula;
    }

    public void setTempoAula(IntegerFilter tempoAula) {
        this.tempoAula = tempoAula;
    }

    public StringFilter getFormaAvaliacao() {
        return formaAvaliacao;
    }

    public void setFormaAvaliacao(StringFilter formaAvaliacao) {
        this.formaAvaliacao = formaAvaliacao;
    }

    public LongFilter getPlanoAulaId() {
        return planoAulaId;
    }

    public void setPlanoAulaId(LongFilter planoAulaId) {
        this.planoAulaId = planoAulaId;
    }

    public LongFilter getCursoId() {
        return cursoId;
    }

    public void setCursoId(LongFilter cursoId) {
        this.cursoId = cursoId;
    }

    public LongFilter getCurriuloId() {
        return curriuloId;
    }

    public void setCurriuloId(LongFilter curriuloId) {
        this.curriuloId = curriuloId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DosificacaoCriteria that = (DosificacaoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(peridoLective, that.peridoLective) &&
            Objects.equals(semanaLectiva, that.semanaLectiva) &&
            Objects.equals(de, that.de) &&
            Objects.equals(ate, that.ate) &&
            Objects.equals(unidadeTematica, that.unidadeTematica) &&
            Objects.equals(tempoAula, that.tempoAula) &&
            Objects.equals(formaAvaliacao, that.formaAvaliacao) &&
            Objects.equals(planoAulaId, that.planoAulaId) &&
            Objects.equals(cursoId, that.cursoId) &&
            Objects.equals(curriuloId, that.curriuloId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        peridoLective,
        semanaLectiva,
        de,
        ate,
        unidadeTematica,
        tempoAula,
        formaAvaliacao,
        planoAulaId,
        cursoId,
        curriuloId
        );
    }

    @Override
    public String toString() {
        return "DosificacaoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (peridoLective != null ? "peridoLective=" + peridoLective + ", " : "") +
                (semanaLectiva != null ? "semanaLectiva=" + semanaLectiva + ", " : "") +
                (de != null ? "de=" + de + ", " : "") +
                (ate != null ? "ate=" + ate + ", " : "") +
                (unidadeTematica != null ? "unidadeTematica=" + unidadeTematica + ", " : "") +
                (tempoAula != null ? "tempoAula=" + tempoAula + ", " : "") +
                (formaAvaliacao != null ? "formaAvaliacao=" + formaAvaliacao + ", " : "") +
                (planoAulaId != null ? "planoAulaId=" + planoAulaId + ", " : "") +
                (cursoId != null ? "cursoId=" + cursoId + ", " : "") +
                (curriuloId != null ? "curriuloId=" + curriuloId + ", " : "") +
            "}";
    }

}
