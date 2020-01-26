package com.ravunana.educacao.pedagogico.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Horario.
 */
@Entity
@Table(name = "horario")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "horario")
public class Horario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "inicio", nullable = false)
    private String inicio;

    @NotNull
    @Column(name = "fim", nullable = false)
    private String fim;

    @NotNull
    @Column(name = "data", nullable = false)
    private ZonedDateTime data;

    @NotNull
    @Column(name = "ano_lectivo", nullable = false)
    private Integer anoLectivo;

    @NotNull
    @Column(name = "dia_semana", nullable = false)
    private String diaSemana;

    @NotNull
    @Column(name = "categoria", nullable = false)
    private String categoria;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("horarios")
    private Turma turma;

    @ManyToOne
    @JsonIgnoreProperties("horarios")
    private Professor professor;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("horarios")
    private PlanoCurricular curriculo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInicio() {
        return inicio;
    }

    public Horario inicio(String inicio) {
        this.inicio = inicio;
        return this;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFim() {
        return fim;
    }

    public Horario fim(String fim) {
        this.fim = fim;
        return this;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public Horario data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public Integer getAnoLectivo() {
        return anoLectivo;
    }

    public Horario anoLectivo(Integer anoLectivo) {
        this.anoLectivo = anoLectivo;
        return this;
    }

    public void setAnoLectivo(Integer anoLectivo) {
        this.anoLectivo = anoLectivo;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public Horario diaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
        return this;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getCategoria() {
        return categoria;
    }

    public Horario categoria(String categoria) {
        this.categoria = categoria;
        return this;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Turma getTurma() {
        return turma;
    }

    public Horario turma(Turma turma) {
        this.turma = turma;
        return this;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Horario professor(Professor professor) {
        this.professor = professor;
        return this;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public PlanoCurricular getCurriculo() {
        return curriculo;
    }

    public Horario curriculo(PlanoCurricular planoCurricular) {
        this.curriculo = planoCurricular;
        return this;
    }

    public void setCurriculo(PlanoCurricular planoCurricular) {
        this.curriculo = planoCurricular;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Horario)) {
            return false;
        }
        return id != null && id.equals(((Horario) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Horario{" +
            "id=" + getId() +
            ", inicio='" + getInicio() + "'" +
            ", fim='" + getFim() + "'" +
            ", data='" + getData() + "'" +
            ", anoLectivo=" + getAnoLectivo() +
            ", diaSemana='" + getDiaSemana() + "'" +
            ", categoria='" + getCategoria() + "'" +
            "}";
    }
}
