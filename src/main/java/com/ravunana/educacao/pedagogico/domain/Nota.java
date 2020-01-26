package com.ravunana.educacao.pedagogico.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Nota.
 */
@Entity
@Table(name = "cor_nota")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "nota")
public class Nota implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "numero_processo", nullable = false)
    private String numeroProcesso;

    @NotNull
    @Column(name = "nome_aluno", nullable = false)
    private String nomeAluno;

    @Column(name = "disciplina")
    private String disciplina;

    @Column(name = "perido_lectivo")
    private String peridoLectivo;

    @Column(name = "ano_lectivo")
    private Integer anoLectivo;

    @Column(name = "falta_justificada")
    private Integer faltaJustificada;

    @Column(name = "falta_injustificada")
    private Integer faltaInjustificada;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    @Column(name = "avaliacao_continuca")
    private Double avaliacaoContinuca;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    @Column(name = "primeira_prova")
    private Double primeiraProva;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    @Column(name = "segunda_prova")
    private Double segundaProva;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    @Column(name = "exame")
    private Double exame;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    @Column(name = "recurso")
    private Double recurso;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    @Column(name = "exame_especial")
    private Double exameEspecial;

    
    @Lob
    @Column(name = "prova", nullable = false)
    private byte[] prova;

    @Column(name = "prova_content_type", nullable = false)
    private String provaContentType;

    @Column(name = "situacao")
    private String situacao;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("notas")
    private Turma turma;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("notas")
    private PlanoCurricular curriculo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("notas")
    private Professor professor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public Nota numeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
        return this;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public Nota nomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
        return this;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public Nota disciplina(String disciplina) {
        this.disciplina = disciplina;
        return this;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getPeridoLectivo() {
        return peridoLectivo;
    }

    public Nota peridoLectivo(String peridoLectivo) {
        this.peridoLectivo = peridoLectivo;
        return this;
    }

    public void setPeridoLectivo(String peridoLectivo) {
        this.peridoLectivo = peridoLectivo;
    }

    public Integer getAnoLectivo() {
        return anoLectivo;
    }

    public Nota anoLectivo(Integer anoLectivo) {
        this.anoLectivo = anoLectivo;
        return this;
    }

    public void setAnoLectivo(Integer anoLectivo) {
        this.anoLectivo = anoLectivo;
    }

    public Integer getFaltaJustificada() {
        return faltaJustificada;
    }

    public Nota faltaJustificada(Integer faltaJustificada) {
        this.faltaJustificada = faltaJustificada;
        return this;
    }

    public void setFaltaJustificada(Integer faltaJustificada) {
        this.faltaJustificada = faltaJustificada;
    }

    public Integer getFaltaInjustificada() {
        return faltaInjustificada;
    }

    public Nota faltaInjustificada(Integer faltaInjustificada) {
        this.faltaInjustificada = faltaInjustificada;
        return this;
    }

    public void setFaltaInjustificada(Integer faltaInjustificada) {
        this.faltaInjustificada = faltaInjustificada;
    }

    public Double getAvaliacaoContinuca() {
        return avaliacaoContinuca;
    }

    public Nota avaliacaoContinuca(Double avaliacaoContinuca) {
        this.avaliacaoContinuca = avaliacaoContinuca;
        return this;
    }

    public void setAvaliacaoContinuca(Double avaliacaoContinuca) {
        this.avaliacaoContinuca = avaliacaoContinuca;
    }

    public Double getPrimeiraProva() {
        return primeiraProva;
    }

    public Nota primeiraProva(Double primeiraProva) {
        this.primeiraProva = primeiraProva;
        return this;
    }

    public void setPrimeiraProva(Double primeiraProva) {
        this.primeiraProva = primeiraProva;
    }

    public Double getSegundaProva() {
        return segundaProva;
    }

    public Nota segundaProva(Double segundaProva) {
        this.segundaProva = segundaProva;
        return this;
    }

    public void setSegundaProva(Double segundaProva) {
        this.segundaProva = segundaProva;
    }

    public Double getExame() {
        return exame;
    }

    public Nota exame(Double exame) {
        this.exame = exame;
        return this;
    }

    public void setExame(Double exame) {
        this.exame = exame;
    }

    public Double getRecurso() {
        return recurso;
    }

    public Nota recurso(Double recurso) {
        this.recurso = recurso;
        return this;
    }

    public void setRecurso(Double recurso) {
        this.recurso = recurso;
    }

    public Double getExameEspecial() {
        return exameEspecial;
    }

    public Nota exameEspecial(Double exameEspecial) {
        this.exameEspecial = exameEspecial;
        return this;
    }

    public void setExameEspecial(Double exameEspecial) {
        this.exameEspecial = exameEspecial;
    }

    public byte[] getProva() {
        return prova;
    }

    public Nota prova(byte[] prova) {
        this.prova = prova;
        return this;
    }

    public void setProva(byte[] prova) {
        this.prova = prova;
    }

    public String getProvaContentType() {
        return provaContentType;
    }

    public Nota provaContentType(String provaContentType) {
        this.provaContentType = provaContentType;
        return this;
    }

    public void setProvaContentType(String provaContentType) {
        this.provaContentType = provaContentType;
    }

    public String getSituacao() {
        return situacao;
    }

    public Nota situacao(String situacao) {
        this.situacao = situacao;
        return this;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Turma getTurma() {
        return turma;
    }

    public Nota turma(Turma turma) {
        this.turma = turma;
        return this;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public PlanoCurricular getCurriculo() {
        return curriculo;
    }

    public Nota curriculo(PlanoCurricular planoCurricular) {
        this.curriculo = planoCurricular;
        return this;
    }

    public void setCurriculo(PlanoCurricular planoCurricular) {
        this.curriculo = planoCurricular;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Nota professor(Professor professor) {
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
        if (!(o instanceof Nota)) {
            return false;
        }
        return id != null && id.equals(((Nota) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Nota{" +
            "id=" + getId() +
            ", numeroProcesso='" + getNumeroProcesso() + "'" +
            ", nomeAluno='" + getNomeAluno() + "'" +
            ", disciplina='" + getDisciplina() + "'" +
            ", peridoLectivo='" + getPeridoLectivo() + "'" +
            ", anoLectivo=" + getAnoLectivo() +
            ", faltaJustificada=" + getFaltaJustificada() +
            ", faltaInjustificada=" + getFaltaInjustificada() +
            ", avaliacaoContinuca=" + getAvaliacaoContinuca() +
            ", primeiraProva=" + getPrimeiraProva() +
            ", segundaProva=" + getSegundaProva() +
            ", exame=" + getExame() +
            ", recurso=" + getRecurso() +
            ", exameEspecial=" + getExameEspecial() +
            ", prova='" + getProva() + "'" +
            ", provaContentType='" + getProvaContentType() + "'" +
            ", situacao='" + getSituacao() + "'" +
            "}";
    }
}
