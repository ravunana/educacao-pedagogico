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
 * Criteria class for the {@link com.ravunana.educacao.pedagogico.domain.Aula} entity. This class is used
 * in {@link com.ravunana.educacao.pedagogico.web.rest.AulaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /aulas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AulaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter data;

    private StringFilter sumario;

    private IntegerFilter licao;

    private BooleanFilter dada;

    private LongFilter chamadaId;

    private LongFilter planoAulaId;

    private LongFilter turmaId;

    private LongFilter curriuloId;

    public AulaCriteria(){
    }

    public AulaCriteria(AulaCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.data = other.data == null ? null : other.data.copy();
        this.sumario = other.sumario == null ? null : other.sumario.copy();
        this.licao = other.licao == null ? null : other.licao.copy();
        this.dada = other.dada == null ? null : other.dada.copy();
        this.chamadaId = other.chamadaId == null ? null : other.chamadaId.copy();
        this.planoAulaId = other.planoAulaId == null ? null : other.planoAulaId.copy();
        this.turmaId = other.turmaId == null ? null : other.turmaId.copy();
        this.curriuloId = other.curriuloId == null ? null : other.curriuloId.copy();
    }

    @Override
    public AulaCriteria copy() {
        return new AulaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getData() {
        return data;
    }

    public void setData(ZonedDateTimeFilter data) {
        this.data = data;
    }

    public StringFilter getSumario() {
        return sumario;
    }

    public void setSumario(StringFilter sumario) {
        this.sumario = sumario;
    }

    public IntegerFilter getLicao() {
        return licao;
    }

    public void setLicao(IntegerFilter licao) {
        this.licao = licao;
    }

    public BooleanFilter getDada() {
        return dada;
    }

    public void setDada(BooleanFilter dada) {
        this.dada = dada;
    }

    public LongFilter getChamadaId() {
        return chamadaId;
    }

    public void setChamadaId(LongFilter chamadaId) {
        this.chamadaId = chamadaId;
    }

    public LongFilter getPlanoAulaId() {
        return planoAulaId;
    }

    public void setPlanoAulaId(LongFilter planoAulaId) {
        this.planoAulaId = planoAulaId;
    }

    public LongFilter getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(LongFilter turmaId) {
        this.turmaId = turmaId;
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
        final AulaCriteria that = (AulaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(data, that.data) &&
            Objects.equals(sumario, that.sumario) &&
            Objects.equals(licao, that.licao) &&
            Objects.equals(dada, that.dada) &&
            Objects.equals(chamadaId, that.chamadaId) &&
            Objects.equals(planoAulaId, that.planoAulaId) &&
            Objects.equals(turmaId, that.turmaId) &&
            Objects.equals(curriuloId, that.curriuloId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        data,
        sumario,
        licao,
        dada,
        chamadaId,
        planoAulaId,
        turmaId,
        curriuloId
        );
    }

    @Override
    public String toString() {
        return "AulaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (data != null ? "data=" + data + ", " : "") +
                (sumario != null ? "sumario=" + sumario + ", " : "") +
                (licao != null ? "licao=" + licao + ", " : "") +
                (dada != null ? "dada=" + dada + ", " : "") +
                (chamadaId != null ? "chamadaId=" + chamadaId + ", " : "") +
                (planoAulaId != null ? "planoAulaId=" + planoAulaId + ", " : "") +
                (turmaId != null ? "turmaId=" + turmaId + ", " : "") +
                (curriuloId != null ? "curriuloId=" + curriuloId + ", " : "") +
            "}";
    }

}
