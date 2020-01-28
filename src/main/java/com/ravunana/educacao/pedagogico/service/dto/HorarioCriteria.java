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
 * Criteria class for the {@link com.ravunana.educacao.pedagogico.domain.Horario} entity. This class is used
 * in {@link com.ravunana.educacao.pedagogico.web.rest.HorarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /horarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HorarioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter inicio;

    private StringFilter fim;

    private ZonedDateTimeFilter data;

    private IntegerFilter anoLectivo;

    private StringFilter diaSemana;

    private StringFilter categoria;

    private LongFilter turmaId;

    private LongFilter professorId;

    private LongFilter curriculoId;

    public HorarioCriteria(){
    }

    public HorarioCriteria(HorarioCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.inicio = other.inicio == null ? null : other.inicio.copy();
        this.fim = other.fim == null ? null : other.fim.copy();
        this.data = other.data == null ? null : other.data.copy();
        this.anoLectivo = other.anoLectivo == null ? null : other.anoLectivo.copy();
        this.diaSemana = other.diaSemana == null ? null : other.diaSemana.copy();
        this.categoria = other.categoria == null ? null : other.categoria.copy();
        this.turmaId = other.turmaId == null ? null : other.turmaId.copy();
        this.professorId = other.professorId == null ? null : other.professorId.copy();
        this.curriculoId = other.curriculoId == null ? null : other.curriculoId.copy();
    }

    @Override
    public HorarioCriteria copy() {
        return new HorarioCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getInicio() {
        return inicio;
    }

    public void setInicio(StringFilter inicio) {
        this.inicio = inicio;
    }

    public StringFilter getFim() {
        return fim;
    }

    public void setFim(StringFilter fim) {
        this.fim = fim;
    }

    public ZonedDateTimeFilter getData() {
        return data;
    }

    public void setData(ZonedDateTimeFilter data) {
        this.data = data;
    }

    public IntegerFilter getAnoLectivo() {
        return anoLectivo;
    }

    public void setAnoLectivo(IntegerFilter anoLectivo) {
        this.anoLectivo = anoLectivo;
    }

    public StringFilter getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(StringFilter diaSemana) {
        this.diaSemana = diaSemana;
    }

    public StringFilter getCategoria() {
        return categoria;
    }

    public void setCategoria(StringFilter categoria) {
        this.categoria = categoria;
    }

    public LongFilter getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(LongFilter turmaId) {
        this.turmaId = turmaId;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HorarioCriteria that = (HorarioCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(inicio, that.inicio) &&
            Objects.equals(fim, that.fim) &&
            Objects.equals(data, that.data) &&
            Objects.equals(anoLectivo, that.anoLectivo) &&
            Objects.equals(diaSemana, that.diaSemana) &&
            Objects.equals(categoria, that.categoria) &&
            Objects.equals(turmaId, that.turmaId) &&
            Objects.equals(professorId, that.professorId) &&
            Objects.equals(curriculoId, that.curriculoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        inicio,
        fim,
        data,
        anoLectivo,
        diaSemana,
        categoria,
        turmaId,
        professorId,
        curriculoId
        );
    }

    @Override
    public String toString() {
        return "HorarioCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (inicio != null ? "inicio=" + inicio + ", " : "") +
                (fim != null ? "fim=" + fim + ", " : "") +
                (data != null ? "data=" + data + ", " : "") +
                (anoLectivo != null ? "anoLectivo=" + anoLectivo + ", " : "") +
                (diaSemana != null ? "diaSemana=" + diaSemana + ", " : "") +
                (categoria != null ? "categoria=" + categoria + ", " : "") +
                (turmaId != null ? "turmaId=" + turmaId + ", " : "") +
                (professorId != null ? "professorId=" + professorId + ", " : "") +
                (curriculoId != null ? "curriculoId=" + curriculoId + ", " : "") +
            "}";
    }

}
