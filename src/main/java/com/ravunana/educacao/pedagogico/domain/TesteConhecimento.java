package com.ravunana.educacao.pedagogico.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A TesteConhecimento.
 */
@Entity
@Table(name = "teste_conhecimento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "testeconhecimento")
public class TesteConhecimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "categoria", nullable = false)
    private String categoria;

    @NotNull
    @Column(name = "periodo_lectivo", nullable = false)
    private String periodoLectivo;

    @NotNull
    @Min(value = 0)
    @Column(name = "duracao", nullable = false)
    private Integer duracao;

    @Column(name = "data")
    private ZonedDateTime data;

    @OneToMany(mappedBy = "teste")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<QuestaoTeste> questaoTestes = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("testeConhecimentos")
    private PlanoCurricular curriculo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("testeConhecimentos")
    private Turma turma;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("testeConhecimentos")
    private Professor professor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public TesteConhecimento categoria(String categoria) {
        this.categoria = categoria;
        return this;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPeriodoLectivo() {
        return periodoLectivo;
    }

    public TesteConhecimento periodoLectivo(String periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
        return this;
    }

    public void setPeriodoLectivo(String periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public TesteConhecimento duracao(Integer duracao) {
        this.duracao = duracao;
        return this;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public TesteConhecimento data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public Set<QuestaoTeste> getQuestaoTestes() {
        return questaoTestes;
    }

    public TesteConhecimento questaoTestes(Set<QuestaoTeste> questaoTestes) {
        this.questaoTestes = questaoTestes;
        return this;
    }

    public TesteConhecimento addQuestaoTeste(QuestaoTeste questaoTeste) {
        this.questaoTestes.add(questaoTeste);
        questaoTeste.setTeste(this);
        return this;
    }

    public TesteConhecimento removeQuestaoTeste(QuestaoTeste questaoTeste) {
        this.questaoTestes.remove(questaoTeste);
        questaoTeste.setTeste(null);
        return this;
    }

    public void setQuestaoTestes(Set<QuestaoTeste> questaoTestes) {
        this.questaoTestes = questaoTestes;
    }

    public PlanoCurricular getCurriculo() {
        return curriculo;
    }

    public TesteConhecimento curriculo(PlanoCurricular planoCurricular) {
        this.curriculo = planoCurricular;
        return this;
    }

    public void setCurriculo(PlanoCurricular planoCurricular) {
        this.curriculo = planoCurricular;
    }

    public Turma getTurma() {
        return turma;
    }

    public TesteConhecimento turma(Turma turma) {
        this.turma = turma;
        return this;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Professor getProfessor() {
        return professor;
    }

    public TesteConhecimento professor(Professor professor) {
        this.professor = professor;
        return this;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TesteConhecimento)) {
            return false;
        }
        return id != null && id.equals(((TesteConhecimento) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TesteConhecimento{" +
            "id=" + getId() +
            ", categoria='" + getCategoria() + "'" +
            ", periodoLectivo='" + getPeriodoLectivo() + "'" +
            ", duracao=" + getDuracao() +
            ", data='" + getData() + "'" +
            "}";
    }
}
