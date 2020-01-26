package com.ravunana.educacao.pedagogico.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.PlanoActividade} entity.
 */
public class PlanoActividadeDTO implements Serializable {

    private Long id;

    @Min(value = 1)
    private Integer numeroActividade;

    @NotNull
    private String atividade;

    
    @Lob
    private String objectivos;

    @NotNull
    private LocalDate de;

    @NotNull
    private LocalDate ate;

    @NotNull
    private String responsavel;

    private String local;

    @Lob
    private String observacao;

    private String participantes;

    private String coResponsavel;

    @NotNull
    private Integer anoLectivo;

    private String statusActividade;

    private String periodoLectivo;

    private String turno;

    private Integer classe;


    private Long cursoId;

    private String cursoNome;

    private Long turmaId;

    private String turmaDescricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroActividade() {
        return numeroActividade;
    }

    public void setNumeroActividade(Integer numeroActividade) {
        this.numeroActividade = numeroActividade;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public String getObjectivos() {
        return objectivos;
    }

    public void setObjectivos(String objectivos) {
        this.objectivos = objectivos;
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

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getParticipantes() {
        return participantes;
    }

    public void setParticipantes(String participantes) {
        this.participantes = participantes;
    }

    public String getCoResponsavel() {
        return coResponsavel;
    }

    public void setCoResponsavel(String coResponsavel) {
        this.coResponsavel = coResponsavel;
    }

    public Integer getAnoLectivo() {
        return anoLectivo;
    }

    public void setAnoLectivo(Integer anoLectivo) {
        this.anoLectivo = anoLectivo;
    }

    public String getStatusActividade() {
        return statusActividade;
    }

    public void setStatusActividade(String statusActividade) {
        this.statusActividade = statusActividade;
    }

    public String getPeriodoLectivo() {
        return periodoLectivo;
    }

    public void setPeriodoLectivo(String periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Integer getClasse() {
        return classe;
    }

    public void setClasse(Integer classe) {
        this.classe = classe;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public String getCursoNome() {
        return cursoNome;
    }

    public void setCursoNome(String cursoNome) {
        this.cursoNome = cursoNome;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlanoActividadeDTO planoActividadeDTO = (PlanoActividadeDTO) o;
        if (planoActividadeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), planoActividadeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlanoActividadeDTO{" +
            "id=" + getId() +
            ", numeroActividade=" + getNumeroActividade() +
            ", atividade='" + getAtividade() + "'" +
            ", objectivos='" + getObjectivos() + "'" +
            ", de='" + getDe() + "'" +
            ", ate='" + getAte() + "'" +
            ", responsavel='" + getResponsavel() + "'" +
            ", local='" + getLocal() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", participantes='" + getParticipantes() + "'" +
            ", coResponsavel='" + getCoResponsavel() + "'" +
            ", anoLectivo=" + getAnoLectivo() +
            ", statusActividade='" + getStatusActividade() + "'" +
            ", periodoLectivo='" + getPeriodoLectivo() + "'" +
            ", turno='" + getTurno() + "'" +
            ", classe=" + getClasse() +
            ", cursoId=" + getCursoId() +
            ", cursoNome='" + getCursoNome() + "'" +
            ", turmaId=" + getTurmaId() +
            ", turmaDescricao='" + getTurmaDescricao() + "'" +
            "}";
    }
}
