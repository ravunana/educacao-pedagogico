package com.ravunana.educacao.pedagogico.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.CriterioAvaliacao} entity.
 */
public class CriterioAvaliacaoDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private Integer aprovaCom;

    @NotNull
    @Min(value = 0)
    private Integer reporvaCom;

    @NotNull
    @Min(value = 0)
    private Integer recursoCom;

    @NotNull
    private Boolean fazExame;

    @NotNull
    private Boolean fazRecurso;

    @NotNull
    private Boolean fazExameEspecial;

    @NotNull
    @Min(value = 0)
    private Integer numeroFaltaReprova;

    @NotNull
    @DecimalMin(value = "0")
    private Double notaMinimaAprovacao;


    private Long planoCurricularId;

    private String planoCurricularDescricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAprovaCom() {
        return aprovaCom;
    }

    public void setAprovaCom(Integer aprovaCom) {
        this.aprovaCom = aprovaCom;
    }

    public Integer getReporvaCom() {
        return reporvaCom;
    }

    public void setReporvaCom(Integer reporvaCom) {
        this.reporvaCom = reporvaCom;
    }

    public Integer getRecursoCom() {
        return recursoCom;
    }

    public void setRecursoCom(Integer recursoCom) {
        this.recursoCom = recursoCom;
    }

    public Boolean isFazExame() {
        return fazExame;
    }

    public void setFazExame(Boolean fazExame) {
        this.fazExame = fazExame;
    }

    public Boolean isFazRecurso() {
        return fazRecurso;
    }

    public void setFazRecurso(Boolean fazRecurso) {
        this.fazRecurso = fazRecurso;
    }

    public Boolean isFazExameEspecial() {
        return fazExameEspecial;
    }

    public void setFazExameEspecial(Boolean fazExameEspecial) {
        this.fazExameEspecial = fazExameEspecial;
    }

    public Integer getNumeroFaltaReprova() {
        return numeroFaltaReprova;
    }

    public void setNumeroFaltaReprova(Integer numeroFaltaReprova) {
        this.numeroFaltaReprova = numeroFaltaReprova;
    }

    public Double getNotaMinimaAprovacao() {
        return notaMinimaAprovacao;
    }

    public void setNotaMinimaAprovacao(Double notaMinimaAprovacao) {
        this.notaMinimaAprovacao = notaMinimaAprovacao;
    }

    public Long getPlanoCurricularId() {
        return planoCurricularId;
    }

    public void setPlanoCurricularId(Long planoCurricularId) {
        this.planoCurricularId = planoCurricularId;
    }

    public String getPlanoCurricularDescricao() {
        return planoCurricularDescricao;
    }

    public void setPlanoCurricularDescricao(String planoCurricularDescricao) {
        this.planoCurricularDescricao = planoCurricularDescricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CriterioAvaliacaoDTO criterioAvaliacaoDTO = (CriterioAvaliacaoDTO) o;
        if (criterioAvaliacaoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), criterioAvaliacaoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CriterioAvaliacaoDTO{" +
            "id=" + getId() +
            ", aprovaCom=" + getAprovaCom() +
            ", reporvaCom=" + getReporvaCom() +
            ", recursoCom=" + getRecursoCom() +
            ", fazExame='" + isFazExame() + "'" +
            ", fazRecurso='" + isFazRecurso() + "'" +
            ", fazExameEspecial='" + isFazExameEspecial() + "'" +
            ", numeroFaltaReprova=" + getNumeroFaltaReprova() +
            ", notaMinimaAprovacao=" + getNotaMinimaAprovacao() +
            ", planoCurricularId=" + getPlanoCurricularId() +
            ", planoCurricularDescricao='" + getPlanoCurricularDescricao() + "'" +
            "}";
    }
}
