package com.ravunana.educacao.pedagogico.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.Dosificacao} entity.
 */
public class DosificacaoDTO implements Serializable {

    private Long id;

    @NotNull
    private String peridoLective;

    
    @Lob
    private String objectivoGeral;

    @NotNull
    private Integer semanaLectiva;

    @NotNull
    private LocalDate de;

    @NotNull
    private LocalDate ate;

    @NotNull
    private String unidadeTematica;

    
    @Lob
    private String conteudo;

    
    @Lob
    private String procedimentoEnsino;

    
    @Lob
    private String recursosDidaticos;

    @NotNull
    private Integer tempoAula;

    @NotNull
    private String formaAvaliacao;


    private Set<CursoDTO> cursos = new HashSet<>();

    private Long curriuloId;

    private String curriuloDescricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeridoLective() {
        return peridoLective;
    }

    public void setPeridoLective(String peridoLective) {
        this.peridoLective = peridoLective;
    }

    public String getObjectivoGeral() {
        return objectivoGeral;
    }

    public void setObjectivoGeral(String objectivoGeral) {
        this.objectivoGeral = objectivoGeral;
    }

    public Integer getSemanaLectiva() {
        return semanaLectiva;
    }

    public void setSemanaLectiva(Integer semanaLectiva) {
        this.semanaLectiva = semanaLectiva;
    }

    public LocalDate getDe() {
        return de;
    }

    public void setDe(LocalDate de) {
        this.de = de;
    }

    public LocalDate getAte() {
        return ate;
    }

    public void setAte(LocalDate ate) {
        this.ate = ate;
    }

    public String getUnidadeTematica() {
        return unidadeTematica;
    }

    public void setUnidadeTematica(String unidadeTematica) {
        this.unidadeTematica = unidadeTematica;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getProcedimentoEnsino() {
        return procedimentoEnsino;
    }

    public void setProcedimentoEnsino(String procedimentoEnsino) {
        this.procedimentoEnsino = procedimentoEnsino;
    }

    public String getRecursosDidaticos() {
        return recursosDidaticos;
    }

    public void setRecursosDidaticos(String recursosDidaticos) {
        this.recursosDidaticos = recursosDidaticos;
    }

    public Integer getTempoAula() {
        return tempoAula;
    }

    public void setTempoAula(Integer tempoAula) {
        this.tempoAula = tempoAula;
    }

    public String getFormaAvaliacao() {
        return formaAvaliacao;
    }

    public void setFormaAvaliacao(String formaAvaliacao) {
        this.formaAvaliacao = formaAvaliacao;
    }

    public Set<CursoDTO> getCursos() {
        return cursos;
    }

    public void setCursos(Set<CursoDTO> cursos) {
        this.cursos = cursos;
    }

    public Long getCurriuloId() {
        return curriuloId;
    }

    public void setCurriuloId(Long planoCurricularId) {
        this.curriuloId = planoCurricularId;
    }

    public String getCurriuloDescricao() {
        return curriuloDescricao;
    }

    public void setCurriuloDescricao(String planoCurricularDescricao) {
        this.curriuloDescricao = planoCurricularDescricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DosificacaoDTO dosificacaoDTO = (DosificacaoDTO) o;
        if (dosificacaoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dosificacaoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DosificacaoDTO{" +
            "id=" + getId() +
            ", peridoLective='" + getPeridoLective() + "'" +
            ", objectivoGeral='" + getObjectivoGeral() + "'" +
            ", semanaLectiva=" + getSemanaLectiva() +
            ", de='" + getDe() + "'" +
            ", ate='" + getAte() + "'" +
            ", unidadeTematica='" + getUnidadeTematica() + "'" +
            ", conteudo='" + getConteudo() + "'" +
            ", procedimentoEnsino='" + getProcedimentoEnsino() + "'" +
            ", recursosDidaticos='" + getRecursosDidaticos() + "'" +
            ", tempoAula=" + getTempoAula() +
            ", formaAvaliacao='" + getFormaAvaliacao() + "'" +
            ", curriuloId=" + getCurriuloId() +
            ", curriuloDescricao='" + getCurriuloDescricao() + "'" +
            "}";
    }
}
