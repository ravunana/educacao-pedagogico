package com.ravunana.educacao.pedagogico.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PlanoCurricular.
 */
@Entity
@Table(name = "plano_curricular")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "planocurricular")
public class PlanoCurricular implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @Column(name = "terminal", nullable = false)
    private Boolean terminal;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "tempo_semanal", nullable = false)
    private Integer tempoSemanal;

    @Column(name = "periodo_lectivo")
    private String periodoLectivo;

    @NotNull
    @Column(name = "componente", nullable = false)
    private String componente;

    @NotNull
    @Column(name = "disciplina", nullable = false)
    private String disciplina;

    @NotNull
    @Column(name = "classe", nullable = false)
    private Integer classe;

    @OneToMany(mappedBy = "curriculo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PlanoAula> planoAulas = new HashSet<>();

    @OneToMany(mappedBy = "curriulo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Dosificacao> dosificacaos = new HashSet<>();

    @OneToMany(mappedBy = "curriculo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Nota> notas = new HashSet<>();

    @OneToMany(mappedBy = "curriulo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Aula> aulas = new HashSet<>();

    @OneToMany(mappedBy = "curriculo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Horario> horarios = new HashSet<>();

    @OneToMany(mappedBy = "curriculo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TesteConhecimento> testeConhecimentos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("planoCurriculars")
    private Curso curso;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public PlanoCurricular descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean isTerminal() {
        return terminal;
    }

    public PlanoCurricular terminal(Boolean terminal) {
        this.terminal = terminal;
        return this;
    }

    public void setTerminal(Boolean terminal) {
        this.terminal = terminal;
    }

    public Integer getTempoSemanal() {
        return tempoSemanal;
    }

    public PlanoCurricular tempoSemanal(Integer tempoSemanal) {
        this.tempoSemanal = tempoSemanal;
        return this;
    }

    public void setTempoSemanal(Integer tempoSemanal) {
        this.tempoSemanal = tempoSemanal;
    }

    public String getPeriodoLectivo() {
        return periodoLectivo;
    }

    public PlanoCurricular periodoLectivo(String periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
        return this;
    }

    public void setPeriodoLectivo(String periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
    }

    public String getComponente() {
        return componente;
    }

    public PlanoCurricular componente(String componente) {
        this.componente = componente;
        return this;
    }

    public void setComponente(String componente) {
        this.componente = componente;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public PlanoCurricular disciplina(String disciplina) {
        this.disciplina = disciplina;
        return this;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public Integer getClasse() {
        return classe;
    }

    public PlanoCurricular classe(Integer classe) {
        this.classe = classe;
        return this;
    }

    public void setClasse(Integer classe) {
        this.classe = classe;
    }

    public Set<PlanoAula> getPlanoAulas() {
        return planoAulas;
    }

    public PlanoCurricular planoAulas(Set<PlanoAula> planoAulas) {
        this.planoAulas = planoAulas;
        return this;
    }

    public PlanoCurricular addPlanoAula(PlanoAula planoAula) {
        this.planoAulas.add(planoAula);
        planoAula.setCurriculo(this);
        return this;
    }

    public PlanoCurricular removePlanoAula(PlanoAula planoAula) {
        this.planoAulas.remove(planoAula);
        planoAula.setCurriculo(null);
        return this;
    }

    public void setPlanoAulas(Set<PlanoAula> planoAulas) {
        this.planoAulas = planoAulas;
    }

    public Set<Dosificacao> getDosificacaos() {
        return dosificacaos;
    }

    public PlanoCurricular dosificacaos(Set<Dosificacao> dosificacaos) {
        this.dosificacaos = dosificacaos;
        return this;
    }

    public PlanoCurricular addDosificacao(Dosificacao dosificacao) {
        this.dosificacaos.add(dosificacao);
        dosificacao.setCurriulo(this);
        return this;
    }

    public PlanoCurricular removeDosificacao(Dosificacao dosificacao) {
        this.dosificacaos.remove(dosificacao);
        dosificacao.setCurriulo(null);
        return this;
    }

    public void setDosificacaos(Set<Dosificacao> dosificacaos) {
        this.dosificacaos = dosificacaos;
    }

    public Set<Nota> getNotas() {
        return notas;
    }

    public PlanoCurricular notas(Set<Nota> notas) {
        this.notas = notas;
        return this;
    }

    public PlanoCurricular addNota(Nota nota) {
        this.notas.add(nota);
        nota.setCurriculo(this);
        return this;
    }

    public PlanoCurricular removeNota(Nota nota) {
        this.notas.remove(nota);
        nota.setCurriculo(null);
        return this;
    }

    public void setNotas(Set<Nota> notas) {
        this.notas = notas;
    }

    public Set<Aula> getAulas() {
        return aulas;
    }

    public PlanoCurricular aulas(Set<Aula> aulas) {
        this.aulas = aulas;
        return this;
    }

    public PlanoCurricular addAula(Aula aula) {
        this.aulas.add(aula);
        aula.setCurriulo(this);
        return this;
    }

    public PlanoCurricular removeAula(Aula aula) {
        this.aulas.remove(aula);
        aula.setCurriulo(null);
        return this;
    }

    public void setAulas(Set<Aula> aulas) {
        this.aulas = aulas;
    }

    public Set<Horario> getHorarios() {
        return horarios;
    }

    public PlanoCurricular horarios(Set<Horario> horarios) {
        this.horarios = horarios;
        return this;
    }

    public PlanoCurricular addHorario(Horario horario) {
        this.horarios.add(horario);
        horario.setCurriculo(this);
        return this;
    }

    public PlanoCurricular removeHorario(Horario horario) {
        this.horarios.remove(horario);
        horario.setCurriculo(null);
        return this;
    }

    public void setHorarios(Set<Horario> horarios) {
        this.horarios = horarios;
    }

    public Set<TesteConhecimento> getTesteConhecimentos() {
        return testeConhecimentos;
    }

    public PlanoCurricular testeConhecimentos(Set<TesteConhecimento> testeConhecimentos) {
        this.testeConhecimentos = testeConhecimentos;
        return this;
    }

    public PlanoCurricular addTesteConhecimento(TesteConhecimento testeConhecimento) {
        this.testeConhecimentos.add(testeConhecimento);
        testeConhecimento.setCurriculo(this);
        return this;
    }

    public PlanoCurricular removeTesteConhecimento(TesteConhecimento testeConhecimento) {
        this.testeConhecimentos.remove(testeConhecimento);
        testeConhecimento.setCurriculo(null);
        return this;
    }

    public void setTesteConhecimentos(Set<TesteConhecimento> testeConhecimentos) {
        this.testeConhecimentos = testeConhecimentos;
    }

    public Curso getCurso() {
        return curso;
    }

    public PlanoCurricular curso(Curso curso) {
        this.curso = curso;
        return this;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanoCurricular)) {
            return false;
        }
        return id != null && id.equals(((PlanoCurricular) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PlanoCurricular{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", terminal='" + isTerminal() + "'" +
            ", tempoSemanal=" + getTempoSemanal() +
            ", periodoLectivo='" + getPeriodoLectivo() + "'" +
            ", componente='" + getComponente() + "'" +
            ", disciplina='" + getDisciplina() + "'" +
            ", classe=" + getClasse() +
            "}";
    }
}
