package com.ravunana.educacao.pedagogico.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Chamada.
 */
@Entity
@Table(name = "cor_chamada")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "chamada")
public class Chamada implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "data", nullable = false)
    private ZonedDateTime data;

    @NotNull
    @Column(name = "presente", nullable = false)
    private Boolean presente;

    @Column(name = "numero_processo")
    private String numeroProcesso;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("chamadas")
    private Aula aula;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public Chamada data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public Boolean isPresente() {
        return presente;
    }

    public Chamada presente(Boolean presente) {
        this.presente = presente;
        return this;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public Chamada numeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
        return this;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public Aula getAula() {
        return aula;
    }

    public Chamada aula(Aula aula) {
        this.aula = aula;
        return this;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Chamada)) {
            return false;
        }
        return id != null && id.equals(((Chamada) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Chamada{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", presente='" + isPresente() + "'" +
            ", numeroProcesso='" + getNumeroProcesso() + "'" +
            "}";
    }
}
