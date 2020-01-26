package com.ravunana.educacao.pedagogico.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Curso.
 */
@Entity
@Table(name = "curso")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "curso")
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Size(min = 10)
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @NotNull
    @Size(min = 3, max = 12)
    @Column(name = "sigla", length = 12, nullable = false, unique = true)
    private String sigla;

    @Lob
    @Column(name = "competencias")
    private String competencias;

    @NotNull
    @Column(name = "area_formacao", nullable = false)
    private String areaFormacao;

    @OneToMany(mappedBy = "curso")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PlanoCurricular> planoCurriculars = new HashSet<>();

    @OneToMany(mappedBy = "curso")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Turma> turmas = new HashSet<>();

    @OneToMany(mappedBy = "curso")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PlanoActividade> planoActividades = new HashSet<>();

    @ManyToMany(mappedBy = "cursos")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Dosificacao> dosificacaoCursos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Curso nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public Curso sigla(String sigla) {
        this.sigla = sigla;
        return this;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getCompetencias() {
        return competencias;
    }

    public Curso competencias(String competencias) {
        this.competencias = competencias;
        return this;
    }

    public void setCompetencias(String competencias) {
        this.competencias = competencias;
    }

    public String getAreaFormacao() {
        return areaFormacao;
    }

    public Curso areaFormacao(String areaFormacao) {
        this.areaFormacao = areaFormacao;
        return this;
    }

    public void setAreaFormacao(String areaFormacao) {
        this.areaFormacao = areaFormacao;
    }

    public Set<PlanoCurricular> getPlanoCurriculars() {
        return planoCurriculars;
    }

    public Curso planoCurriculars(Set<PlanoCurricular> planoCurriculars) {
        this.planoCurriculars = planoCurriculars;
        return this;
    }

    public Curso addPlanoCurricular(PlanoCurricular planoCurricular) {
        this.planoCurriculars.add(planoCurricular);
        planoCurricular.setCurso(this);
        return this;
    }

    public Curso removePlanoCurricular(PlanoCurricular planoCurricular) {
        this.planoCurriculars.remove(planoCurricular);
        planoCurricular.setCurso(null);
        return this;
    }

    public void setPlanoCurriculars(Set<PlanoCurricular> planoCurriculars) {
        this.planoCurriculars = planoCurriculars;
    }

    public Set<Turma> getTurmas() {
        return turmas;
    }

    public Curso turmas(Set<Turma> turmas) {
        this.turmas = turmas;
        return this;
    }

    public Curso addTurma(Turma turma) {
        this.turmas.add(turma);
        turma.setCurso(this);
        return this;
    }

    public Curso removeTurma(Turma turma) {
        this.turmas.remove(turma);
        turma.setCurso(null);
        return this;
    }

    public void setTurmas(Set<Turma> turmas) {
        this.turmas = turmas;
    }

    public Set<PlanoActividade> getPlanoActividades() {
        return planoActividades;
    }

    public Curso planoActividades(Set<PlanoActividade> planoActividades) {
        this.planoActividades = planoActividades;
        return this;
    }

    public Curso addPlanoActividade(PlanoActividade planoActividade) {
        this.planoActividades.add(planoActividade);
        planoActividade.setCurso(this);
        return this;
    }

    public Curso removePlanoActividade(PlanoActividade planoActividade) {
        this.planoActividades.remove(planoActividade);
        planoActividade.setCurso(null);
        return this;
    }

    public void setPlanoActividades(Set<PlanoActividade> planoActividades) {
        this.planoActividades = planoActividades;
    }

    public Set<Dosificacao> getDosificacaoCursos() {
        return dosificacaoCursos;
    }

    public Curso dosificacaoCursos(Set<Dosificacao> dosificacaos) {
        this.dosificacaoCursos = dosificacaos;
        return this;
    }

    public Curso addDosificacaoCurso(Dosificacao dosificacao) {
        this.dosificacaoCursos.add(dosificacao);
        dosificacao.getCursos().add(this);
        return this;
    }

    public Curso removeDosificacaoCurso(Dosificacao dosificacao) {
        this.dosificacaoCursos.remove(dosificacao);
        dosificacao.getCursos().remove(this);
        return this;
    }

    public void setDosificacaoCursos(Set<Dosificacao> dosificacaos) {
        this.dosificacaoCursos = dosificacaos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Curso)) {
            return false;
        }
        return id != null && id.equals(((Curso) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Curso{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", sigla='" + getSigla() + "'" +
            ", competencias='" + getCompetencias() + "'" +
            ", areaFormacao='" + getAreaFormacao() + "'" +
            "}";
    }
}
