package com.ravunana.educacao.pedagogico.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A CriterioAvaliacao.
 */
@Entity
@Table(name = "criterio_avaliacao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "criterioavaliacao")
public class CriterioAvaliacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "aprova_com", nullable = false)
    private Integer aprovaCom;

    @NotNull
    @Min(value = 0)
    @Column(name = "reporva_com", nullable = false)
    private Integer reporvaCom;

    @NotNull
    @Min(value = 0)
    @Column(name = "recurso_com", nullable = false)
    private Integer recursoCom;

    @NotNull
    @Column(name = "faz_exame", nullable = false)
    private Boolean fazExame;

    @NotNull
    @Column(name = "faz_recurso", nullable = false)
    private Boolean fazRecurso;

    @NotNull
    @Column(name = "faz_exame_especial", nullable = false)
    private Boolean fazExameEspecial;

    @NotNull
    @Min(value = 0)
    @Column(name = "numero_falta_reprova", nullable = false)
    private Integer numeroFaltaReprova;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "nota_minima_aprovacao", nullable = false)
    private Double notaMinimaAprovacao;

    @OneToOne
    @JoinColumn(unique = true)
    private PlanoCurricular planoCurricular;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAprovaCom() {
        return aprovaCom;
    }

    public CriterioAvaliacao aprovaCom(Integer aprovaCom) {
        this.aprovaCom = aprovaCom;
        return this;
    }

    public void setAprovaCom(Integer aprovaCom) {
        this.aprovaCom = aprovaCom;
    }

    public Integer getReporvaCom() {
        return reporvaCom;
    }

    public CriterioAvaliacao reporvaCom(Integer reporvaCom) {
        this.reporvaCom = reporvaCom;
        return this;
    }

    public void setReporvaCom(Integer reporvaCom) {
        this.reporvaCom = reporvaCom;
    }

    public Integer getRecursoCom() {
        return recursoCom;
    }

    public CriterioAvaliacao recursoCom(Integer recursoCom) {
        this.recursoCom = recursoCom;
        return this;
    }

    public void setRecursoCom(Integer recursoCom) {
        this.recursoCom = recursoCom;
    }

    public Boolean isFazExame() {
        return fazExame;
    }

    public CriterioAvaliacao fazExame(Boolean fazExame) {
        this.fazExame = fazExame;
        return this;
    }

    public void setFazExame(Boolean fazExame) {
        this.fazExame = fazExame;
    }

    public Boolean isFazRecurso() {
        return fazRecurso;
    }

    public CriterioAvaliacao fazRecurso(Boolean fazRecurso) {
        this.fazRecurso = fazRecurso;
        return this;
    }

    public void setFazRecurso(Boolean fazRecurso) {
        this.fazRecurso = fazRecurso;
    }

    public Boolean isFazExameEspecial() {
        return fazExameEspecial;
    }

    public CriterioAvaliacao fazExameEspecial(Boolean fazExameEspecial) {
        this.fazExameEspecial = fazExameEspecial;
        return this;
    }

    public void setFazExameEspecial(Boolean fazExameEspecial) {
        this.fazExameEspecial = fazExameEspecial;
    }

    public Integer getNumeroFaltaReprova() {
        return numeroFaltaReprova;
    }

    public CriterioAvaliacao numeroFaltaReprova(Integer numeroFaltaReprova) {
        this.numeroFaltaReprova = numeroFaltaReprova;
        return this;
    }

    public void setNumeroFaltaReprova(Integer numeroFaltaReprova) {
        this.numeroFaltaReprova = numeroFaltaReprova;
    }

    public Double getNotaMinimaAprovacao() {
        return notaMinimaAprovacao;
    }

    public CriterioAvaliacao notaMinimaAprovacao(Double notaMinimaAprovacao) {
        this.notaMinimaAprovacao = notaMinimaAprovacao;
        return this;
    }

    public void setNotaMinimaAprovacao(Double notaMinimaAprovacao) {
        this.notaMinimaAprovacao = notaMinimaAprovacao;
    }

    public PlanoCurricular getPlanoCurricular() {
        return planoCurricular;
    }

    public CriterioAvaliacao planoCurricular(PlanoCurricular planoCurricular) {
        this.planoCurricular = planoCurricular;
        return this;
    }

    public void setPlanoCurricular(PlanoCurricular planoCurricular) {
        this.planoCurricular = planoCurricular;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CriterioAvaliacao)) {
            return false;
        }
        return id != null && id.equals(((CriterioAvaliacao) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CriterioAvaliacao{" +
            "id=" + getId() +
            ", aprovaCom=" + getAprovaCom() +
            ", reporvaCom=" + getReporvaCom() +
            ", recursoCom=" + getRecursoCom() +
            ", fazExame='" + isFazExame() + "'" +
            ", fazRecurso='" + isFazRecurso() + "'" +
            ", fazExameEspecial='" + isFazExameEspecial() + "'" +
            ", numeroFaltaReprova=" + getNumeroFaltaReprova() +
            ", notaMinimaAprovacao=" + getNotaMinimaAprovacao() +
            "}";
    }
}
