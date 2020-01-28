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
 * Criteria class for the {@link com.ravunana.educacao.pedagogico.domain.Nota} entity. This class is used
 * in {@link com.ravunana.educacao.pedagogico.web.rest.NotaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NotaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numeroProcesso;

    private StringFilter nomeAluno;

    private StringFilter disciplina;

    private StringFilter peridoLectivo;

    private IntegerFilter anoLectivo;

    private IntegerFilter faltaJustificada;

    private IntegerFilter faltaInjustificada;

    private DoubleFilter avaliacaoContinuca;

    private DoubleFilter primeiraProva;

    private DoubleFilter segundaProva;

    private DoubleFilter exame;

    private DoubleFilter recurso;

    private DoubleFilter exameEspecial;

    private StringFilter situacao;

    private LongFilter turmaId;

    private LongFilter curriculoId;

    private LongFilter professorId;

    public NotaCriteria(){
    }

    public NotaCriteria(NotaCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.numeroProcesso = other.numeroProcesso == null ? null : other.numeroProcesso.copy();
        this.nomeAluno = other.nomeAluno == null ? null : other.nomeAluno.copy();
        this.disciplina = other.disciplina == null ? null : other.disciplina.copy();
        this.peridoLectivo = other.peridoLectivo == null ? null : other.peridoLectivo.copy();
        this.anoLectivo = other.anoLectivo == null ? null : other.anoLectivo.copy();
        this.faltaJustificada = other.faltaJustificada == null ? null : other.faltaJustificada.copy();
        this.faltaInjustificada = other.faltaInjustificada == null ? null : other.faltaInjustificada.copy();
        this.avaliacaoContinuca = other.avaliacaoContinuca == null ? null : other.avaliacaoContinuca.copy();
        this.primeiraProva = other.primeiraProva == null ? null : other.primeiraProva.copy();
        this.segundaProva = other.segundaProva == null ? null : other.segundaProva.copy();
        this.exame = other.exame == null ? null : other.exame.copy();
        this.recurso = other.recurso == null ? null : other.recurso.copy();
        this.exameEspecial = other.exameEspecial == null ? null : other.exameEspecial.copy();
        this.situacao = other.situacao == null ? null : other.situacao.copy();
        this.turmaId = other.turmaId == null ? null : other.turmaId.copy();
        this.curriculoId = other.curriculoId == null ? null : other.curriculoId.copy();
        this.professorId = other.professorId == null ? null : other.professorId.copy();
    }

    @Override
    public NotaCriteria copy() {
        return new NotaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(StringFilter numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public StringFilter getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(StringFilter nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public StringFilter getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(StringFilter disciplina) {
        this.disciplina = disciplina;
    }

    public StringFilter getPeridoLectivo() {
        return peridoLectivo;
    }

    public void setPeridoLectivo(StringFilter peridoLectivo) {
        this.peridoLectivo = peridoLectivo;
    }

    public IntegerFilter getAnoLectivo() {
        return anoLectivo;
    }

    public void setAnoLectivo(IntegerFilter anoLectivo) {
        this.anoLectivo = anoLectivo;
    }

    public IntegerFilter getFaltaJustificada() {
        return faltaJustificada;
    }

    public void setFaltaJustificada(IntegerFilter faltaJustificada) {
        this.faltaJustificada = faltaJustificada;
    }

    public IntegerFilter getFaltaInjustificada() {
        return faltaInjustificada;
    }

    public void setFaltaInjustificada(IntegerFilter faltaInjustificada) {
        this.faltaInjustificada = faltaInjustificada;
    }

    public DoubleFilter getAvaliacaoContinuca() {
        return avaliacaoContinuca;
    }

    public void setAvaliacaoContinuca(DoubleFilter avaliacaoContinuca) {
        this.avaliacaoContinuca = avaliacaoContinuca;
    }

    public DoubleFilter getPrimeiraProva() {
        return primeiraProva;
    }

    public void setPrimeiraProva(DoubleFilter primeiraProva) {
        this.primeiraProva = primeiraProva;
    }

    public DoubleFilter getSegundaProva() {
        return segundaProva;
    }

    public void setSegundaProva(DoubleFilter segundaProva) {
        this.segundaProva = segundaProva;
    }

    public DoubleFilter getExame() {
        return exame;
    }

    public void setExame(DoubleFilter exame) {
        this.exame = exame;
    }

    public DoubleFilter getRecurso() {
        return recurso;
    }

    public void setRecurso(DoubleFilter recurso) {
        this.recurso = recurso;
    }

    public DoubleFilter getExameEspecial() {
        return exameEspecial;
    }

    public void setExameEspecial(DoubleFilter exameEspecial) {
        this.exameEspecial = exameEspecial;
    }

    public StringFilter getSituacao() {
        return situacao;
    }

    public void setSituacao(StringFilter situacao) {
        this.situacao = situacao;
    }

    public LongFilter getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(LongFilter turmaId) {
        this.turmaId = turmaId;
    }

    public LongFilter getCurriculoId() {
        return curriculoId;
    }

    public void setCurriculoId(LongFilter curriculoId) {
        this.curriculoId = curriculoId;
    }

    public LongFilter getProfessorId() {
        return professorId;
    }

    public void setProfessorId(LongFilter professorId) {
        this.professorId = professorId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotaCriteria that = (NotaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(numeroProcesso, that.numeroProcesso) &&
            Objects.equals(nomeAluno, that.nomeAluno) &&
            Objects.equals(disciplina, that.disciplina) &&
            Objects.equals(peridoLectivo, that.peridoLectivo) &&
            Objects.equals(anoLectivo, that.anoLectivo) &&
            Objects.equals(faltaJustificada, that.faltaJustificada) &&
            Objects.equals(faltaInjustificada, that.faltaInjustificada) &&
            Objects.equals(avaliacaoContinuca, that.avaliacaoContinuca) &&
            Objects.equals(primeiraProva, that.primeiraProva) &&
            Objects.equals(segundaProva, that.segundaProva) &&
            Objects.equals(exame, that.exame) &&
            Objects.equals(recurso, that.recurso) &&
            Objects.equals(exameEspecial, that.exameEspecial) &&
            Objects.equals(situacao, that.situacao) &&
            Objects.equals(turmaId, that.turmaId) &&
            Objects.equals(curriculoId, that.curriculoId) &&
            Objects.equals(professorId, that.professorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        numeroProcesso,
        nomeAluno,
        disciplina,
        peridoLectivo,
        anoLectivo,
        faltaJustificada,
        faltaInjustificada,
        avaliacaoContinuca,
        primeiraProva,
        segundaProva,
        exame,
        recurso,
        exameEspecial,
        situacao,
        turmaId,
        curriculoId,
        professorId
        );
    }

    @Override
    public String toString() {
        return "NotaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (numeroProcesso != null ? "numeroProcesso=" + numeroProcesso + ", " : "") +
                (nomeAluno != null ? "nomeAluno=" + nomeAluno + ", " : "") +
                (disciplina != null ? "disciplina=" + disciplina + ", " : "") +
                (peridoLectivo != null ? "peridoLectivo=" + peridoLectivo + ", " : "") +
                (anoLectivo != null ? "anoLectivo=" + anoLectivo + ", " : "") +
                (faltaJustificada != null ? "faltaJustificada=" + faltaJustificada + ", " : "") +
                (faltaInjustificada != null ? "faltaInjustificada=" + faltaInjustificada + ", " : "") +
                (avaliacaoContinuca != null ? "avaliacaoContinuca=" + avaliacaoContinuca + ", " : "") +
                (primeiraProva != null ? "primeiraProva=" + primeiraProva + ", " : "") +
                (segundaProva != null ? "segundaProva=" + segundaProva + ", " : "") +
                (exame != null ? "exame=" + exame + ", " : "") +
                (recurso != null ? "recurso=" + recurso + ", " : "") +
                (exameEspecial != null ? "exameEspecial=" + exameEspecial + ", " : "") +
                (situacao != null ? "situacao=" + situacao + ", " : "") +
                (turmaId != null ? "turmaId=" + turmaId + ", " : "") +
                (curriculoId != null ? "curriculoId=" + curriculoId + ", " : "") +
                (professorId != null ? "professorId=" + professorId + ", " : "") +
            "}";
    }

}
