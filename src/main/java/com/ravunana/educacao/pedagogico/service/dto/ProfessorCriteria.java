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
 * Criteria class for the {@link com.ravunana.educacao.pedagogico.domain.Professor} entity. This class is used
 * in {@link com.ravunana.educacao.pedagogico.web.rest.ProfessorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /professors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProfessorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter sexo;

    private StringFilter contacto;

    private StringFilter email;

    private StringFilter residencia;

    private StringFilter numeroAgente;

    private StringFilter utilizadorId;

    private StringFilter grauAcademico;

    private StringFilter cursoAcademico;

    private BooleanFilter ativo;

    private LongFilter horarioId;

    private LongFilter turmaId;

    private LongFilter planoAulaId;

    private LongFilter notaId;

    private LongFilter testeConhecimentoId;

    public ProfessorCriteria(){
    }

    public ProfessorCriteria(ProfessorCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.sexo = other.sexo == null ? null : other.sexo.copy();
        this.contacto = other.contacto == null ? null : other.contacto.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.residencia = other.residencia == null ? null : other.residencia.copy();
        this.numeroAgente = other.numeroAgente == null ? null : other.numeroAgente.copy();
        this.utilizadorId = other.utilizadorId == null ? null : other.utilizadorId.copy();
        this.grauAcademico = other.grauAcademico == null ? null : other.grauAcademico.copy();
        this.cursoAcademico = other.cursoAcademico == null ? null : other.cursoAcademico.copy();
        this.ativo = other.ativo == null ? null : other.ativo.copy();
        this.horarioId = other.horarioId == null ? null : other.horarioId.copy();
        this.turmaId = other.turmaId == null ? null : other.turmaId.copy();
        this.planoAulaId = other.planoAulaId == null ? null : other.planoAulaId.copy();
        this.notaId = other.notaId == null ? null : other.notaId.copy();
        this.testeConhecimentoId = other.testeConhecimentoId == null ? null : other.testeConhecimentoId.copy();
    }

    @Override
    public ProfessorCriteria copy() {
        return new ProfessorCriteria(this);
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

    public StringFilter getSexo() {
        return sexo;
    }

    public void setSexo(StringFilter sexo) {
        this.sexo = sexo;
    }

    public StringFilter getContacto() {
        return contacto;
    }

    public void setContacto(StringFilter contacto) {
        this.contacto = contacto;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getResidencia() {
        return residencia;
    }

    public void setResidencia(StringFilter residencia) {
        this.residencia = residencia;
    }

    public StringFilter getNumeroAgente() {
        return numeroAgente;
    }

    public void setNumeroAgente(StringFilter numeroAgente) {
        this.numeroAgente = numeroAgente;
    }

    public StringFilter getUtilizadorId() {
        return utilizadorId;
    }

    public void setUtilizadorId(StringFilter utilizadorId) {
        this.utilizadorId = utilizadorId;
    }

    public StringFilter getGrauAcademico() {
        return grauAcademico;
    }

    public void setGrauAcademico(StringFilter grauAcademico) {
        this.grauAcademico = grauAcademico;
    }

    public StringFilter getCursoAcademico() {
        return cursoAcademico;
    }

    public void setCursoAcademico(StringFilter cursoAcademico) {
        this.cursoAcademico = cursoAcademico;
    }

    public BooleanFilter getAtivo() {
        return ativo;
    }

    public void setAtivo(BooleanFilter ativo) {
        this.ativo = ativo;
    }

    public LongFilter getHorarioId() {
        return horarioId;
    }

    public void setHorarioId(LongFilter horarioId) {
        this.horarioId = horarioId;
    }

    public LongFilter getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(LongFilter turmaId) {
        this.turmaId = turmaId;
    }

    public LongFilter getPlanoAulaId() {
        return planoAulaId;
    }

    public void setPlanoAulaId(LongFilter planoAulaId) {
        this.planoAulaId = planoAulaId;
    }

    public LongFilter getNotaId() {
        return notaId;
    }

    public void setNotaId(LongFilter notaId) {
        this.notaId = notaId;
    }

    public LongFilter getTesteConhecimentoId() {
        return testeConhecimentoId;
    }

    public void setTesteConhecimentoId(LongFilter testeConhecimentoId) {
        this.testeConhecimentoId = testeConhecimentoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProfessorCriteria that = (ProfessorCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(sexo, that.sexo) &&
            Objects.equals(contacto, that.contacto) &&
            Objects.equals(email, that.email) &&
            Objects.equals(residencia, that.residencia) &&
            Objects.equals(numeroAgente, that.numeroAgente) &&
            Objects.equals(utilizadorId, that.utilizadorId) &&
            Objects.equals(grauAcademico, that.grauAcademico) &&
            Objects.equals(cursoAcademico, that.cursoAcademico) &&
            Objects.equals(ativo, that.ativo) &&
            Objects.equals(horarioId, that.horarioId) &&
            Objects.equals(turmaId, that.turmaId) &&
            Objects.equals(planoAulaId, that.planoAulaId) &&
            Objects.equals(notaId, that.notaId) &&
            Objects.equals(testeConhecimentoId, that.testeConhecimentoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nome,
        sexo,
        contacto,
        email,
        residencia,
        numeroAgente,
        utilizadorId,
        grauAcademico,
        cursoAcademico,
        ativo,
        horarioId,
        turmaId,
        planoAulaId,
        notaId,
        testeConhecimentoId
        );
    }

    @Override
    public String toString() {
        return "ProfessorCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (sexo != null ? "sexo=" + sexo + ", " : "") +
                (contacto != null ? "contacto=" + contacto + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (residencia != null ? "residencia=" + residencia + ", " : "") +
                (numeroAgente != null ? "numeroAgente=" + numeroAgente + ", " : "") +
                (utilizadorId != null ? "utilizadorId=" + utilizadorId + ", " : "") +
                (grauAcademico != null ? "grauAcademico=" + grauAcademico + ", " : "") +
                (cursoAcademico != null ? "cursoAcademico=" + cursoAcademico + ", " : "") +
                (ativo != null ? "ativo=" + ativo + ", " : "") +
                (horarioId != null ? "horarioId=" + horarioId + ", " : "") +
                (turmaId != null ? "turmaId=" + turmaId + ", " : "") +
                (planoAulaId != null ? "planoAulaId=" + planoAulaId + ", " : "") +
                (notaId != null ? "notaId=" + notaId + ", " : "") +
                (testeConhecimentoId != null ? "testeConhecimentoId=" + testeConhecimentoId + ", " : "") +
            "}";
    }

}
