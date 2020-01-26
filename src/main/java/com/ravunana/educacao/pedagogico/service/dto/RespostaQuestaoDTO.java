package com.ravunana.educacao.pedagogico.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.RespostaQuestao} entity.
 */
public class RespostaQuestaoDTO implements Serializable {

    private Long id;

    private String resposta;


    private Long questaoId;

    private String questaoQuestao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public Long getQuestaoId() {
        return questaoId;
    }

    public void setQuestaoId(Long questaoTesteId) {
        this.questaoId = questaoTesteId;
    }

    public String getQuestaoQuestao() {
        return questaoQuestao;
    }

    public void setQuestaoQuestao(String questaoTesteQuestao) {
        this.questaoQuestao = questaoTesteQuestao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RespostaQuestaoDTO respostaQuestaoDTO = (RespostaQuestaoDTO) o;
        if (respostaQuestaoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), respostaQuestaoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RespostaQuestaoDTO{" +
            "id=" + getId() +
            ", resposta='" + getResposta() + "'" +
            ", questaoId=" + getQuestaoId() +
            ", questaoQuestao='" + getQuestaoQuestao() + "'" +
            "}";
    }
}
