package com.ravunana.educacao.pedagogico.service.dto;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.Turma} entity.
 */
public class TurmaDTO implements Serializable {

    private Long id;

    @NotNull
    private String descricao;

    @NotNull
    private Integer anoLectivo;

    @NotNull
    private ZonedDateTime data;

    @NotNull
    private LocalDate abertura;

    @NotNull
    private LocalDate encerramento;

    @NotNull
    @Min(value = 1)
    private Integer lotacao;

    @NotNull
    private Boolean aberta;

    @NotNull
    private String periodoLectivo;

    @NotNull
    private String turno;

    @NotNull
    private Integer sala;

    @NotNull
    private Integer classe;


    private Long cursoId;

    private String cursoNome;

    private Long coordenadorId;

    private String coordenadorNome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getAnoLectivo() {
        return anoLectivo;
    }

    public void setAnoLectivo(Integer anoLectivo) {
        this.anoLectivo = anoLectivo;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public LocalDate getAbertura() {
        return abertura;
    }

    public void setAbertura(LocalDate abertura) {
        this.abertura = abertura;
    }

    public LocalDate getEncerramento() {
        return encerramento;
    }

    public void setEncerramento(LocalDate encerramento) {
        this.encerramento = encerramento;
    }

    public Integer getLotacao() {
        return lotacao;
    }

    public void setLotacao(Integer lotacao) {
        this.lotacao = lotacao;
    }

    public Boolean isAberta() {
        return aberta;
    }

    public void setAberta(Boolean aberta) {
        this.aberta = aberta;
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

    public Integer getSala() {
        return sala;
    }

    public void setSala(Integer sala) {
        this.sala = sala;
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

    public Long getCoordenadorId() {
        return coordenadorId;
    }

    public void setCoordenadorId(Long professorId) {
        this.coordenadorId = professorId;
    }

    public String getCoordenadorNome() {
        return coordenadorNome;
    }

    public void setCoordenadorNome(String professorNome) {
        this.coordenadorNome = professorNome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TurmaDTO turmaDTO = (TurmaDTO) o;
        if (turmaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), turmaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TurmaDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", anoLectivo=" + getAnoLectivo() +
            ", data='" + getData() + "'" +
            ", abertura='" + getAbertura() + "'" +
            ", encerramento='" + getEncerramento() + "'" +
            ", lotacao=" + getLotacao() +
            ", aberta='" + isAberta() + "'" +
            ", periodoLectivo='" + getPeriodoLectivo() + "'" +
            ", turno='" + getTurno() + "'" +
            ", sala=" + getSala() +
            ", classe=" + getClasse() +
            ", cursoId=" + getCursoId() +
            ", cursoNome='" + getCursoNome() + "'" +
            ", coordenadorId=" + getCoordenadorId() +
            ", coordenadorNome='" + getCoordenadorNome() + "'" +
            "}";
    }
}
