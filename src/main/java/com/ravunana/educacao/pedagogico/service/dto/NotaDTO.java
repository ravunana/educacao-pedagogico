package com.ravunana.educacao.pedagogico.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.Nota} entity.
 */
public class NotaDTO implements Serializable {

    private Long id;

    @NotNull
    private String numeroProcesso;

    @NotNull
    private String nomeAluno;

    private String disciplina;

    private String peridoLectivo;

    private Integer anoLectivo;

    private Integer faltaJustificada;

    private Integer faltaInjustificada;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    private Double avaliacaoContinuca;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    private Double primeiraProva;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    private Double segundaProva;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    private Double exame;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    private Double recurso;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    private Double exameEspecial;

    
    @Lob
    private byte[] prova;

    private String provaContentType;
    private String situacao;


    private Long turmaId;

    private String turmaDescricao;

    private Long curriculoId;

    private String curriculoDescricao;

    private Long professorId;

    private String professorNome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getPeridoLectivo() {
        return peridoLectivo;
    }

    public void setPeridoLectivo(String peridoLectivo) {
        this.peridoLectivo = peridoLectivo;
    }

    public Integer getAnoLectivo() {
        return anoLectivo;
    }

    public void setAnoLectivo(Integer anoLectivo) {
        this.anoLectivo = anoLectivo;
    }

    public Integer getFaltaJustificada() {
        return faltaJustificada;
    }

    public void setFaltaJustificada(Integer faltaJustificada) {
        this.faltaJustificada = faltaJustificada;
    }

    public Integer getFaltaInjustificada() {
        return faltaInjustificada;
    }

    public void setFaltaInjustificada(Integer faltaInjustificada) {
        this.faltaInjustificada = faltaInjustificada;
    }

    public Double getAvaliacaoContinuca() {
        return avaliacaoContinuca;
    }

    public void setAvaliacaoContinuca(Double avaliacaoContinuca) {
        this.avaliacaoContinuca = avaliacaoContinuca;
    }

    public Double getPrimeiraProva() {
        return primeiraProva;
    }

    public void setPrimeiraProva(Double primeiraProva) {
        this.primeiraProva = primeiraProva;
    }

    public Double getSegundaProva() {
        return segundaProva;
    }

    public void setSegundaProva(Double segundaProva) {
        this.segundaProva = segundaProva;
    }

    public Double getExame() {
        return exame;
    }

    public void setExame(Double exame) {
        this.exame = exame;
    }

    public Double getRecurso() {
        return recurso;
    }

    public void setRecurso(Double recurso) {
        this.recurso = recurso;
    }

    public Double getExameEspecial() {
        return exameEspecial;
    }

    public void setExameEspecial(Double exameEspecial) {
        this.exameEspecial = exameEspecial;
    }

    public byte[] getProva() {
        return prova;
    }

    public void setProva(byte[] prova) {
        this.prova = prova;
    }

    public String getProvaContentType() {
        return provaContentType;
    }

    public void setProvaContentType(String provaContentType) {
        this.provaContentType = provaContentType;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Long getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }

    public String getTurmaDescricao() {
        return turmaDescricao;
    }

    public void setTurmaDescricao(String turmaDescricao) {
        this.turmaDescricao = turmaDescricao;
    }

    public Long getCurriculoId() {
        return curriculoId;
    }

    public void setCurriculoId(Long planoCurricularId) {
        this.curriculoId = planoCurricularId;
    }

    public String getCurriculoDescricao() {
        return curriculoDescricao;
    }

    public void setCurriculoDescricao(String planoCurricularDescricao) {
        this.curriculoDescricao = planoCurricularDescricao;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }

    public String getProfessorNome() {
        return professorNome;
    }

    public void setProfessorNome(String professorNome) {
        this.professorNome = professorNome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotaDTO notaDTO = (NotaDTO) o;
        if (notaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotaDTO{" +
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
            ", situacao='" + getSituacao() + "'" +
            ", turmaId=" + getTurmaId() +
            ", turmaDescricao='" + getTurmaDescricao() + "'" +
            ", curriculoId=" + getCurriculoId() +
            ", curriculoDescricao='" + getCurriculoDescricao() + "'" +
            ", professorId=" + getProfessorId() +
            ", professorNome='" + getProfessorNome() + "'" +
            "}";
    }
}
