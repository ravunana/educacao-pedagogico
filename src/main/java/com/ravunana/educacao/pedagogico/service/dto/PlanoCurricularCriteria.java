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
 * Criteria class for the {@link com.ravunana.educacao.pedagogico.domain.PlanoCurricular} entity. This class is used
 * in {@link com.ravunana.educacao.pedagogico.web.rest.PlanoCurricularResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plano-curriculars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlanoCurricularCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter descricao;

    private BooleanFilter terminal;

    private IntegerFilter tempoSemanal;

    private StringFilter periodoLectivo;

    private StringFilter componente;

    private StringFilter disciplina;

    private IntegerFilter classe;

    private LongFilter planoAulaId;

    private LongFilter dosificacaoId;

    private LongFilter notaId;

    private LongFilter aulaId;

    private LongFilter horarioId;

    private LongFilter testeConhecimentoId;

    private LongFilter cursoId;

    public PlanoCurricularCriteria(){
    }

    public PlanoCurricularCriteria(PlanoCurricularCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.terminal = other.terminal == null ? null : other.terminal.copy();
        this.tempoSemanal = other.tempoSemanal == null ? null : other.tempoSemanal.copy();
        this.periodoLectivo = other.periodoLectivo == null ? null : other.periodoLectivo.copy();
        this.componente = other.componente == null ? null : other.componente.copy();
        this.disciplina = other.disciplina == null ? null : other.disciplina.copy();
        this.classe = other.classe == null ? null : other.classe.copy();
        this.planoAulaId = other.planoAulaId == null ? null : other.planoAulaId.copy();
        this.dosificacaoId = other.dosificacaoId == null ? null : other.dosificacaoId.copy();
        this.notaId = other.notaId == null ? null : other.notaId.copy();
        this.aulaId = other.aulaId == null ? null : other.aulaId.copy();
        this.horarioId = other.horarioId == null ? null : other.horarioId.copy();
        this.testeConhecimentoId = other.testeConhecimentoId == null ? null : other.testeConhecimentoId.copy();
        this.cursoId = other.cursoId == null ? null : other.cursoId.copy();
    }

    @Override
    public PlanoCurricularCriteria copy() {
        return new PlanoCurricularCriteria(this);
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

    public BooleanFilter getTerminal() {
        return terminal;
    }

    public void setTerminal(BooleanFilter terminal) {
        this.terminal = terminal;
    }

    public IntegerFilter getTempoSemanal() {
        return tempoSemanal;
    }

    public void setTempoSemanal(IntegerFilter tempoSemanal) {
        this.tempoSemanal = tempoSemanal;
    }

    public StringFilter getPeriodoLectivo() {
        return periodoLectivo;
    }

    public void setPeriodoLectivo(StringFilter periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
    }

    public StringFilter getComponente() {
        return componente;
    }

    public void setComponente(StringFilter componente) {
        this.componente = componente;
    }

    public StringFilter getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(StringFilter disciplina) {
        this.disciplina = disciplina;
    }

    public IntegerFilter getClasse() {
        return classe;
    }

    public void setClasse(IntegerFilter classe) {
        this.classe = classe;
    }

    public LongFilter getPlanoAulaId() {
        return planoAulaId;
    }

    public void setPlanoAulaId(LongFilter planoAulaId) {
        this.planoAulaId = planoAulaId;
    }

    public LongFilter getDosificacaoId() {
        return dosificacaoId;
    }

    public void setDosificacaoId(LongFilter dosificacaoId) {
        this.dosificacaoId = dosificacaoId;
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

    public LongFilter getHorarioId() {
        return horarioId;
    }

    public void setHorarioId(LongFilter horarioId) {
        this.horarioId = horarioId;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlanoCurricularCriteria that = (PlanoCurricularCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(terminal, that.terminal) &&
            Objects.equals(tempoSemanal, that.tempoSemanal) &&
            Objects.equals(periodoLectivo, that.periodoLectivo) &&
            Objects.equals(componente, that.componente) &&
            Objects.equals(disciplina, that.disciplina) &&
            Objects.equals(classe, that.classe) &&
            Objects.equals(planoAulaId, that.planoAulaId) &&
            Objects.equals(dosificacaoId, that.dosificacaoId) &&
            Objects.equals(notaId, that.notaId) &&
            Objects.equals(aulaId, that.aulaId) &&
            Objects.equals(horarioId, that.horarioId) &&
            Objects.equals(testeConhecimentoId, that.testeConhecimentoId) &&
            Objects.equals(cursoId, that.cursoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        descricao,
        terminal,
        tempoSemanal,
        periodoLectivo,
        componente,
        disciplina,
        classe,
        planoAulaId,
        dosificacaoId,
        notaId,
        aulaId,
        horarioId,
        testeConhecimentoId,
        cursoId
        );
    }

    @Override
    public String toString() {
        return "PlanoCurricularCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (descricao != null ? "descricao=" + descricao + ", " : "") +
                (terminal != null ? "terminal=" + terminal + ", " : "") +
                (tempoSemanal != null ? "tempoSemanal=" + tempoSemanal + ", " : "") +
                (periodoLectivo != null ? "periodoLectivo=" + periodoLectivo + ", " : "") +
                (componente != null ? "componente=" + componente + ", " : "") +
                (disciplina != null ? "disciplina=" + disciplina + ", " : "") +
                (classe != null ? "classe=" + classe + ", " : "") +
                (planoAulaId != null ? "planoAulaId=" + planoAulaId + ", " : "") +
                (dosificacaoId != null ? "dosificacaoId=" + dosificacaoId + ", " : "") +
                (notaId != null ? "notaId=" + notaId + ", " : "") +
                (aulaId != null ? "aulaId=" + aulaId + ", " : "") +
                (horarioId != null ? "horarioId=" + horarioId + ", " : "") +
                (testeConhecimentoId != null ? "testeConhecimentoId=" + testeConhecimentoId + ", " : "") +
                (cursoId != null ? "cursoId=" + cursoId + ", " : "") +
            "}";
    }

}
