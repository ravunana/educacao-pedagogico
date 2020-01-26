package com.ravunana.educacao.pedagogico.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.Curso} entity.
 */
public class CursoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 10)
    private String nome;

    @NotNull
    @Size(min = 3, max = 12)
    private String sigla;

    @Lob
    private String competencias;

    @NotNull
    private String areaFormacao;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getCompetencias() {
        return competencias;
    }

    public void setCompetencias(String competencias) {
        this.competencias = competencias;
    }

    public String getAreaFormacao() {
        return areaFormacao;
    }

    public void setAreaFormacao(String areaFormacao) {
        this.areaFormacao = areaFormacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CursoDTO cursoDTO = (CursoDTO) o;
        if (cursoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cursoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CursoDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", sigla='" + getSigla() + "'" +
            ", competencias='" + getCompetencias() + "'" +
            ", areaFormacao='" + getAreaFormacao() + "'" +
            "}";
    }
}
