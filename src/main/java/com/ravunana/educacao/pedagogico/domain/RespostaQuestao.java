package com.ravunana.educacao.pedagogico.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A RespostaQuestao.
 */
@Entity
@Table(name = "resposta_questao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "respostaquestao")
public class RespostaQuestao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "resposta")
    private String resposta;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private QuestaoTeste questao;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResposta() {
        return resposta;
    }

    public RespostaQuestao resposta(String resposta) {
        this.resposta = resposta;
        return this;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public QuestaoTeste getQuestao() {
        return questao;
    }

    public RespostaQuestao questao(QuestaoTeste questaoTeste) {
        this.questao = questaoTeste;
        return this;
    }

    public void setQuestao(QuestaoTeste questaoTeste) {
        this.questao = questaoTeste;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RespostaQuestao)) {
            return false;
        }
        return id != null && id.equals(((RespostaQuestao) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RespostaQuestao{" +
            "id=" + getId() +
            ", resposta='" + getResposta() + "'" +
            "}";
    }
}
