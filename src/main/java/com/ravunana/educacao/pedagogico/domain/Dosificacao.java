package com.ravunana.educacao.pedagogico.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Dosificacao.
 */
@Entity
@Table(name = "cor_dosificacao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dosificacao")
public class Dosificacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "perido_lective", nullable = false)
    private String peridoLective;

    
    @Lob
    @Column(name = "objectivo_geral", nullable = false)
    private String objectivoGeral;

    @NotNull
    @Column(name = "semana_lectiva", nullable = false)
    private Integer semanaLectiva;

    @NotNull
    @Column(name = "de", nullable = false)
    private LocalDate de;

    @NotNull
    @Column(name = "ate", nullable = false)
    private LocalDate ate;

    @NotNull
    @Column(name = "unidade_tematica", nullable = false)
    private String unidadeTematica;

    
    @Lob
    @Column(name = "conteudo", nullable = false)
    private String conteudo;

    
    @Lob
    @Column(name = "procedimento_ensino", nullable = false)
    private String procedimentoEnsino;

    
    @Lob
    @Column(name = "recursos_didaticos", nullable = false)
    private String recursosDidaticos;

    @NotNull
    @Column(name = "tempo_aula", nullable = false)
    private Integer tempoAula;

    @NotNull
    @Column(name = "forma_avaliacao", nullable = false)
    private String formaAvaliacao;

    @OneToMany(mappedBy = "dosificacao")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PlanoAula> planoAulas = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "cor_dosificacao_curso",
               joinColumns = @JoinColumn(name = "dosificacao_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "curso_id", referencedColumnName = "id"))
    private Set<Curso> cursos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("dosificacaos")
    private PlanoCurricular curriulo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeridoLective() {
        return peridoLective;
    }

    public Dosificacao peridoLective(String peridoLective) {
        this.peridoLective = peridoLective;
        return this;
    }

    public void setPeridoLective(String peridoLective) {
        this.peridoLective = peridoLective;
    }

    public String getObjectivoGeral() {
        return objectivoGeral;
    }

    public Dosificacao objectivoGeral(String objectivoGeral) {
        this.objectivoGeral = objectivoGeral;
        return this;
    }

    public void setObjectivoGeral(String objectivoGeral) {
        this.objectivoGeral = objectivoGeral;
    }

    public Integer getSemanaLectiva() {
        return semanaLectiva;
    }

    public Dosificacao semanaLectiva(Integer semanaLectiva) {
        this.semanaLectiva = semanaLectiva;
        return this;
    }

    public void setSemanaLectiva(Integer semanaLectiva) {
        this.semanaLectiva = semanaLectiva;
    }

    public LocalDate getDe() {
        return de;
    }

    public Dosificacao de(LocalDate de) {
        this.de = de;
        return this;
    }

    public void setDe(LocalDate de) {
        this.de = de;
    }

    public LocalDate getAte() {
        return ate;
    }

    public Dosificacao ate(LocalDate ate) {
        this.ate = ate;
        return this;
    }

    public void setAte(LocalDate ate) {
        this.ate = ate;
    }

    public String getUnidadeTematica() {
        return unidadeTematica;
    }

    public Dosificacao unidadeTematica(String unidadeTematica) {
        this.unidadeTematica = unidadeTematica;
        return this;
    }

    public void setUnidadeTematica(String unidadeTematica) {
        this.unidadeTematica = unidadeTematica;
    }

    public String getConteudo() {
        return conteudo;
    }

    public Dosificacao conteudo(String conteudo) {
        this.conteudo = conteudo;
        return this;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getProcedimentoEnsino() {
        return procedimentoEnsino;
    }

    public Dosificacao procedimentoEnsino(String procedimentoEnsino) {
        this.procedimentoEnsino = procedimentoEnsino;
        return this;
    }

    public void setProcedimentoEnsino(String procedimentoEnsino) {
        this.procedimentoEnsino = procedimentoEnsino;
    }

    public String getRecursosDidaticos() {
        return recursosDidaticos;
    }

    public Dosificacao recursosDidaticos(String recursosDidaticos) {
        this.recursosDidaticos = recursosDidaticos;
        return this;
    }

    public void setRecursosDidaticos(String recursosDidaticos) {
        this.recursosDidaticos = recursosDidaticos;
    }

    public Integer getTempoAula() {
        return tempoAula;
    }

    public Dosificacao tempoAula(Integer tempoAula) {
        this.tempoAula = tempoAula;
        return this;
    }

    public void setTempoAula(Integer tempoAula) {
        this.tempoAula = tempoAula;
    }

    public String getFormaAvaliacao() {
        return formaAvaliacao;
    }

    public Dosificacao formaAvaliacao(String formaAvaliacao) {
        this.formaAvaliacao = formaAvaliacao;
        return this;
    }

    public void setFormaAvaliacao(String formaAvaliacao) {
        this.formaAvaliacao = formaAvaliacao;
    }

    public Set<PlanoAula> getPlanoAulas() {
        return planoAulas;
    }

    public Dosificacao planoAulas(Set<PlanoAula> planoAulas) {
        this.planoAulas = planoAulas;
        return this;
    }

    public Dosificacao addPlanoAula(PlanoAula planoAula) {
        this.planoAulas.add(planoAula);
        planoAula.setDosificacao(this);
        return this;
    }

    public Dosificacao removePlanoAula(PlanoAula planoAula) {
        this.planoAulas.remove(planoAula);
        planoAula.setDosificacao(null);
        return this;
    }

    public void setPlanoAulas(Set<PlanoAula> planoAulas) {
        this.planoAulas = planoAulas;
    }

    public Set<Curso> getCursos() {
        return cursos;
    }

    public Dosificacao cursos(Set<Curso> cursos) {
        this.cursos = cursos;
        return this;
    }

    public Dosificacao addCurso(Curso curso) {
        this.cursos.add(curso);
        curso.getDosificacaoCursos().add(this);
        return this;
    }

    public Dosificacao removeCurso(Curso curso) {
        this.cursos.remove(curso);
        curso.getDosificacaoCursos().remove(this);
        return this;
    }

    public void setCursos(Set<Curso> cursos) {
        this.cursos = cursos;
    }

    public PlanoCurricular getCurriulo() {
        return curriulo;
    }

    public Dosificacao curriulo(PlanoCurricular planoCurricular) {
        this.curriulo = planoCurricular;
        return this;
    }

    public void setCurriulo(PlanoCurricular planoCurricular) {
        this.curriulo = planoCurricular;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dosificacao)) {
            return false;
        }
        return id != null && id.equals(((Dosificacao) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Dosificacao{" +
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
            "}";
    }
}
