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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.ravunana.educacao.pedagogico.domain.Turma} entity. This class is used
 * in {@link com.ravunana.educacao.pedagogico.web.rest.TurmaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /turmas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TurmaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter descricao;

    private IntegerFilter anoLectivo;

    private ZonedDateTimeFilter data;

    private LocalDateFilter abertura;

    private LocalDateFilter encerramento;

    private IntegerFilter lotacao;

    private BooleanFilter aberta;

    private StringFilter periodoLectivo;

    private StringFilter turno;

    private IntegerFilter sala;

    private IntegerFilter classe;

    private LongFilter horarioId;

    private LongFilter planoActividadeId;

    private LongFilter notaId;

    private LongFilter aulaId;

    private LongFilter testeConhecimentoId;

    private LongFilter cursoId;

    private LongFilter coordenadorId;

    private LongFilter planoAulaTurmaId;

    public TurmaCriteria(){
    }

    public TurmaCriteria(TurmaCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.anoLectivo = other.anoLectivo == null ? null : other.anoLectivo.copy();
        this.data = other.data == null ? null : other.data.copy();
        this.abertura = other.abertura == null ? null : other.abertura.copy();
        this.encerramento = other.encerramento == null ? null : other.encerramento.copy();
        this.lotacao = other.lotacao == null ? null : other.lotacao.copy();
        this.aberta = other.aberta == null ? null : other.aberta.copy();
        this.periodoLectivo = other.periodoLectivo == null ? null : other.periodoLectivo.copy();
        this.turno = other.turno == null ? null : other.turno.copy();
        this.sala = other.sala == null ? null : other.sala.copy();
        this.classe = other.classe == null ? null : other.classe.copy();
        this.horarioId = other.horarioId == null ? null : other.horarioId.copy();
        this.planoActividadeId = other.planoActividadeId == null ? null : other.planoActividadeId.copy();
        this.notaId = other.notaId == null ? null : other.notaId.copy();
        this.aulaId = other.aulaId == null ? null : other.aulaId.copy();
        this.testeConhecimentoId = other.testeConhecimentoId == null ? null : other.testeConhecimentoId.copy();
        this.cursoId = other.cursoId == null ? null : other.cursoId.copy();
        this.coordenadorId = other.coordenadorId == null ? null : other.coordenadorId.copy();
        this.planoAulaTurmaId = other.planoAulaTurmaId == null ? null : other.planoAulaTurmaId.copy();
    }

    @Override
    public TurmaCriteria copy() {
        return new TurmaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescricao() {
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public IntegerFilter getAnoLectivo() {
        return anoLectivo;
    }

    public void setAnoLectivo(IntegerFilter anoLectivo) {
        this.anoLectivo = anoLectivo;
    }

    public ZonedDateTimeFilter getData() {
        return data;
    }

    public void setData(ZonedDateTimeFilter data) {
        this.data = data;
    }

    public LocalDateFilter getAbertura() {
        return abertura;
    }

    public void setAbertura(LocalDateFilter abertura) {
        this.abertura = abertura;
    }

    public LocalDateFilter getEncerramento() {
        return encerramento;
    }

    public void setEncerramento(LocalDateFilter encerramento) {
        this.encerramento = encerramento;
    }

    public IntegerFilter getLotacao() {
        return lotacao;
    }

    public void setLotacao(IntegerFilter lotacao) {
        this.lotacao = lotacao;
    }

    public BooleanFilter getAberta() {
        return aberta;
    }

    public void setAberta(BooleanFilter aberta) {
        this.aberta = aberta;
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

    public IntegerFilter getSala() {
        return sala;
    }

    public void setSala(IntegerFilter sala) {
        this.sala = sala;
    }

    public IntegerFilter getClasse() {
        return classe;
    }

    public void setClasse(IntegerFilter classe) {
        this.classe = classe;
    }

    public LongFilter getHorarioId() {
        return horarioId;
    }

    public void setHorarioId(LongFilter horarioId) {
        this.horarioId = horarioId;
    }

    public LongFilter getPlanoActividadeId() {
        return planoActividadeId;
    }

    public void setPlanoActividadeId(LongFilter planoActividadeId) {
        this.planoActividadeId = planoActividadeId;
    }

    public LongFilter getNotaId() {
        return notaId;
    }

    public void setNotaId(LongFilter notaId) {
        this.notaId = notaId;
    }

    public LongFilter getAulaId() {
        return aulaId;
    }

    public void setAulaId(LongFilter aulaId) {
        this.aulaId = aulaId;
    }

    public LongFilter getTesteConhecimentoId() {
        return testeConhecimentoId;
    }

    public void setTesteConhecimentoId(LongFilter testeConhecimentoId) {
        this.testeConhecimentoId = testeConhecimentoId;
    }

    public LongFilter getCursoId() {
        return cursoId;
    }

    public void setCursoId(LongFilter cursoId) {
        this.cursoId = cursoId;
    }

    public LongFilter getCoordenadorId() {
        return coordenadorId;
    }

    public void setCoordenadorId(LongFilter coordenadorId) {
        this.coordenadorId = coordenadorId;
    }

    public LongFilter getPlanoAulaTurmaId() {
        return planoAulaTurmaId;
    }

    public void setPlanoAulaTurmaId(LongFilter planoAulaTurmaId) {
        this.planoAulaTurmaId = planoAulaTurmaId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TurmaCriteria that = (TurmaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(anoLectivo, that.anoLectivo) &&
            Objects.equals(data, that.data) &&
            Objects.equals(abertura, that.abertura) &&
            Objects.equals(encerramento, that.encerramento) &&
            Objects.equals(lotacao, that.lotacao) &&
            Objects.equals(aberta, that.aberta) &&
            Objects.equals(periodoLectivo, that.periodoLectivo) &&
            Objects.equals(turno, that.turno) &&
            Objects.equals(sala, that.sala) &&
            Objects.equals(classe, that.classe) &&
            Objects.equals(horarioId, that.horarioId) &&
            Objects.equals(planoActividadeId, that.planoActividadeId) &&
            Objects.equals(notaId, that.notaId) &&
            Objects.equals(aulaId, that.aulaId) &&
            Objects.equals(testeConhecimentoId, that.testeConhecimentoId) &&
            Objects.equals(cursoId, that.cursoId) &&
            Objects.equals(coordenadorId, that.coordenadorId) &&
            Objects.equals(planoAulaTurmaId, that.planoAulaTurmaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        descricao,
        anoLectivo,
        data,
        abertura,
        encerramento,
        lotacao,
        aberta,
        periodoLectivo,
        turno,
        sala,
        classe,
        horarioId,
        planoActividadeId,
        notaId,
        aulaId,
        testeConhecimentoId,
        cursoId,
        coordenadorId,
        planoAulaTurmaId
        );
    }

    @Override
    public String toString() {
        return "TurmaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (descricao != null ? "descricao=" + descricao + ", " : "") +
                (anoLectivo != null ? "anoLectivo=" + anoLectivo + ", " : "") +
                (data != null ? "data=" + data + ", " : "") +
                (abertura != null ? "abertura=" + abertura + ", " : "") +
                (encerramento != null ? "encerramento=" + encerramento + ", " : "") +
                (lotacao != null ? "lotacao=" + lotacao + ", " : "") +
                (aberta != null ? "aberta=" + aberta + ", " : "") +
                (periodoLectivo != null ? "periodoLectivo=" + periodoLectivo + ", " : "") +
                (turno != null ? "turno=" + turno + ", " : "") +
                (sala != null ? "sala=" + sala + ", " : "") +
                (classe != null ? "classe=" + classe + ", " : "") +
                (horarioId != null ? "horarioId=" + horarioId + ", " : "") +
                (planoActividadeId != null ? "planoActividadeId=" + planoActividadeId + ", " : "") +
                (notaId != null ? "notaId=" + notaId + ", " : "") +
                (aulaId != null ? "aulaId=" + aulaId + ", " : "") +
                (testeConhecimentoId != null ? "testeConhecimentoId=" + testeConhecimentoId + ", " : "") +
                (cursoId != null ? "cursoId=" + cursoId + ", " : "") +
                (coordenadorId != null ? "coordenadorId=" + coordenadorId + ", " : "") +
                (planoAulaTurmaId != null ? "planoAulaTurmaId=" + planoAulaTurmaId + ", " : "") +
            "}";
    }

}
