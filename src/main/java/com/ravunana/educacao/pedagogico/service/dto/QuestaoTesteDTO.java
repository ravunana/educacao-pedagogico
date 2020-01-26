package com.ravunana.educacao.pedagogico.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.QuestaoTeste} entity.
 */
public class QuestaoTesteDTO implements Serializable {

    private Long id;

    @NotNull
    private String grupo;

    @NotNull
    private Double numero;

    private String argumento;

    @NotNull
    private String questao;


    private Long testeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public Double getNumero() {
        return numero;
    }

    public void setNumero(Double numero) {
        this.numero = numero;
    }

    public String getArgumento() {
        return argumento;
    }

    public void setArgumento(String argumento) {
        this.argumento = argumento;
    }

    public String getQuestao() {
        return questao;
    }

    public void setQuestao(String questao) {
        this.questao = questao;
    }

    public Long getTesteId() {
        return testeId;
    }

    public void setTesteId(Long testeConhecimentoId) {
        this.testeId = testeConhecimentoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestaoTesteDTO questaoTesteDTO = (QuestaoTesteDTO) o;
        if (questaoTesteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), questaoTesteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuestaoTesteDTO{" +
            "id=" + getId() +
            ", grupo='" + getGrupo() + "'" +
            ", numero=" + getNumero() +
            ", argumento='" + getArgumento() + "'" +
            ", questao='" + getQuestao() + "'" +
            ", testeId=" + getTesteId() +
            "}";
    }
}
