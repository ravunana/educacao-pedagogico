package com.ravunana.educacao.pedagogico.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * A ProvaAptidaoProfissional.
 */
@Entity
@Table(name = "prova_aptidao_profissional")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "provaaptidaoprofissional")
public class ProvaAptidaoProfissional implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "numero_processo", nullable = false)
    private String numeroProcesso;

    @NotNull
    @Column(name = "nome_aluno", nullable = false)
    private String nomeAluno;

    @Column(name = "livro_registro")
    private Integer livroRegistro;

    @Column(name = "folha_registro")
    private Integer folhaRegistro;

    @Column(name = "tema_projecto_tecnologio")
    private String temaProjectoTecnologio;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    @Column(name = "nota_projecto_tecnologio")
    private Double notaProjectoTecnologio;

    @Column(name = "data_defesa")
    private ZonedDateTime dataDefesa;

    @Column(name = "local_estagio")
    private String localEstagio;

    @Column(name = "aproveitamento_estagio")
    private String aproveitamentoEstagio;

    @Column(name = "inicio_estagio")
    private LocalDate inicioEstagio;

    @Column(name = "final_estagio")
    private LocalDate finalEstagio;

    @Column(name = "data")
    private ZonedDateTime data;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public ProvaAptidaoProfissional numeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
        return this;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public ProvaAptidaoProfissional nomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
        return this;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public Integer getLivroRegistro() {
        return livroRegistro;
    }

    public ProvaAptidaoProfissional livroRegistro(Integer livroRegistro) {
        this.livroRegistro = livroRegistro;
        return this;
    }

    public void setLivroRegistro(Integer livroRegistro) {
        this.livroRegistro = livroRegistro;
    }

    public Integer getFolhaRegistro() {
        return folhaRegistro;
    }

    public ProvaAptidaoProfissional folhaRegistro(Integer folhaRegistro) {
        this.folhaRegistro = folhaRegistro;
        return this;
    }

    public void setFolhaRegistro(Integer folhaRegistro) {
        this.folhaRegistro = folhaRegistro;
    }

    public String getTemaProjectoTecnologio() {
        return temaProjectoTecnologio;
    }

    public ProvaAptidaoProfissional temaProjectoTecnologio(String temaProjectoTecnologio) {
        this.temaProjectoTecnologio = temaProjectoTecnologio;
        return this;
    }

    public void setTemaProjectoTecnologio(String temaProjectoTecnologio) {
        this.temaProjectoTecnologio = temaProjectoTecnologio;
    }

    public Double getNotaProjectoTecnologio() {
        return notaProjectoTecnologio;
    }

    public ProvaAptidaoProfissional notaProjectoTecnologio(Double notaProjectoTecnologio) {
        this.notaProjectoTecnologio = notaProjectoTecnologio;
        return this;
    }

    public void setNotaProjectoTecnologio(Double notaProjectoTecnologio) {
        this.notaProjectoTecnologio = notaProjectoTecnologio;
    }

    public ZonedDateTime getDataDefesa() {
        return dataDefesa;
    }

    public ProvaAptidaoProfissional dataDefesa(ZonedDateTime dataDefesa) {
        this.dataDefesa = dataDefesa;
        return this;
    }

    public void setDataDefesa(ZonedDateTime dataDefesa) {
        this.dataDefesa = dataDefesa;
    }

    public String getLocalEstagio() {
        return localEstagio;
    }

    public ProvaAptidaoProfissional localEstagio(String localEstagio) {
        this.localEstagio = localEstagio;
        return this;
    }

    public void setLocalEstagio(String localEstagio) {
        this.localEstagio = localEstagio;
    }

    public String getAproveitamentoEstagio() {
        return aproveitamentoEstagio;
    }

    public ProvaAptidaoProfissional aproveitamentoEstagio(String aproveitamentoEstagio) {
        this.aproveitamentoEstagio = aproveitamentoEstagio;
        return this;
    }

    public void setAproveitamentoEstagio(String aproveitamentoEstagio) {
        this.aproveitamentoEstagio = aproveitamentoEstagio;
    }

    public LocalDate getInicioEstagio() {
        return inicioEstagio;
    }

    public ProvaAptidaoProfissional inicioEstagio(LocalDate inicioEstagio) {
        this.inicioEstagio = inicioEstagio;
        return this;
    }

    public void setInicioEstagio(LocalDate inicioEstagio) {
        this.inicioEstagio = inicioEstagio;
    }

    public LocalDate getFinalEstagio() {
        return finalEstagio;
    }

    public ProvaAptidaoProfissional finalEstagio(LocalDate finalEstagio) {
        this.finalEstagio = finalEstagio;
        return this;
    }

    public void setFinalEstagio(LocalDate finalEstagio) {
        this.finalEstagio = finalEstagio;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public ProvaAptidaoProfissional data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProvaAptidaoProfissional)) {
            return false;
        }
        return id != null && id.equals(((ProvaAptidaoProfissional) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProvaAptidaoProfissional{" +
            "id=" + getId() +
            ", numeroProcesso='" + getNumeroProcesso() + "'" +
            ", nomeAluno='" + getNomeAluno() + "'" +
            ", livroRegistro=" + getLivroRegistro() +
            ", folhaRegistro=" + getFolhaRegistro() +
            ", temaProjectoTecnologio='" + getTemaProjectoTecnologio() + "'" +
            ", notaProjectoTecnologio=" + getNotaProjectoTecnologio() +
            ", dataDefesa='" + getDataDefesa() + "'" +
            ", localEstagio='" + getLocalEstagio() + "'" +
            ", aproveitamentoEstagio='" + getAproveitamentoEstagio() + "'" +
            ", inicioEstagio='" + getInicioEstagio() + "'" +
            ", finalEstagio='" + getFinalEstagio() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
}
