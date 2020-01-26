package com.ravunana.educacao.pedagogico.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A QuestaoTeste.
 */
@Entity
@Table(name = "questao_teste")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "questaoteste")
public class QuestaoTeste implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "grupo", nullable = false)
    private String grupo;

    @NotNull
    @Column(name = "numero", nullable = false)
    private Double numero;

    @Column(name = "argumento")
    private String argumento;

    @NotNull
    @Column(name = "questao", nullable = false)
    private String questao;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("questaoTestes")
    private TesteConhecimento teste;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public QuestaoTeste grupo(String grupo) {
        this.grupo = grupo;
        return this;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public Double getNumero() {
        return numero;
    }

    public QuestaoTeste numero(Double numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Double numero) {
        this.numero = numero;
    }

    public String getArgumento() {
        return argumento;
    }

    public QuestaoTeste argumento(String argumento) {
        this.argumento = argumento;
        return this;
    }

    public void setArgumento(String argumento) {
        this.argumento = argumento;
    }

    public String getQuestao() {
        return questao;
    }

    public QuestaoTeste questao(String questao) {
        this.questao = questao;
        return this;
    }

    public void setQuestao(String questao) {
        this.questao = questao;
    }

    public TesteConhecimento getTeste() {
        return teste;
    }

    public QuestaoTeste teste(TesteConhecimento testeConhecimento) {
        this.teste = testeConhecimento;
        return this;
    }

    public void setTeste(TesteConhecimento testeConhecimento) {
        this.teste = testeConhecimento;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestaoTeste)) {
            return false;
        }
        return id != null && id.equals(((QuestaoTeste) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "QuestaoTeste{" +
            "id=" + getId() +
            ", grupo='" + getGrupo() + "'" +
            ", numero=" + getNumero() +
            ", argumento='" + getArgumento() + "'" +
            ", questao='" + getQuestao() + "'" +
            "}";
    }
}
