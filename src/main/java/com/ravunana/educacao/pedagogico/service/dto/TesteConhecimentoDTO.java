package com.ravunana.educacao.pedagogico.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.TesteConhecimento} entity.
 */
public class TesteConhecimentoDTO implements Serializable {

    private Long id;

    @NotNull
    private String categoria;

    @NotNull
    private String periodoLectivo;

    @NotNull
    @Min(value = 0)
    private Integer duracao;

    private ZonedDateTime data;


    private Long curriculoId;

    private String curriculoDescricao;

    private Long turmaId;

    private String turmaDescricao;

    private Long professorId;

    private String professorNome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPeriodoLectivo() {
        return periodoLectivo;
    }

    public void setPeriodoLectivo(String periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
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

        TesteConhecimentoDTO testeConhecimentoDTO = (TesteConhecimentoDTO) o;
        if (testeConhecimentoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), testeConhecimentoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TesteConhecimentoDTO{" +
            "id=" + getId() +
            ", categoria='" + getCategoria() + "'" +
            ", periodoLectivo='" + getPeriodoLectivo() + "'" +
            ", duracao=" + getDuracao() +
            ", data='" + getData() + "'" +
            ", curriculoId=" + getCurriculoId() +
            ", curriculoDescricao='" + getCurriculoDescricao() + "'" +
            ", turmaId=" + getTurmaId() +
            ", turmaDescricao='" + getTurmaDescricao() + "'" +
            ", professorId=" + getProfessorId() +
            ", professorNome='" + getProfessorNome() + "'" +
            "}";
    }
}
