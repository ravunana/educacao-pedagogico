package com.ravunana.educacao.pedagogico.service.dto;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.ProvaAptidaoProfissional} entity.
 */
public class ProvaAptidaoProfissionalDTO implements Serializable {

    private Long id;

    @NotNull
    private String numeroProcesso;

    @NotNull
    private String nomeAluno;

    private Integer livroRegistro;

    private Integer folhaRegistro;

    private String temaProjectoTecnologio;

    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    private Double notaProjectoTecnologio;

    private ZonedDateTime dataDefesa;

    private String localEstagio;

    private String aproveitamentoEstagio;

    private LocalDate inicioEstagio;

    private LocalDate finalEstagio;

    private ZonedDateTime data;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public Integer getLivroRegistro() {
        return livroRegistro;
    }

    public void setLivroRegistro(Integer livroRegistro) {
        this.livroRegistro = livroRegistro;
    }

    public Integer getFolhaRegistro() {
        return folhaRegistro;
    }

    public void setFolhaRegistro(Integer folhaRegistro) {
        this.folhaRegistro = folhaRegistro;
    }

    public String getTemaProjectoTecnologio() {
        return temaProjectoTecnologio;
    }

    public void setTemaProjectoTecnologio(String temaProjectoTecnologio) {
        this.temaProjectoTecnologio = temaProjectoTecnologio;
    }

    public Double getNotaProjectoTecnologio() {
        return notaProjectoTecnologio;
    }

    public void setNotaProjectoTecnologio(Double notaProjectoTecnologio) {
        this.notaProjectoTecnologio = notaProjectoTecnologio;
    }

    public ZonedDateTime getDataDefesa() {
        return dataDefesa;
    }

    public void setDataDefesa(ZonedDateTime dataDefesa) {
        this.dataDefesa = dataDefesa;
    }

    public String getLocalEstagio() {
        return localEstagio;
    }

    public void setLocalEstagio(String localEstagio) {
        this.localEstagio = localEstagio;
    }

    public String getAproveitamentoEstagio() {
        return aproveitamentoEstagio;
    }

    public void setAproveitamentoEstagio(String aproveitamentoEstagio) {
        this.aproveitamentoEstagio = aproveitamentoEstagio;
    }

    public LocalDate getInicioEstagio() {
        return inicioEstagio;
    }

    public void setInicioEstagio(LocalDate inicioEstagio) {
        this.inicioEstagio = inicioEstagio;
    }

    public LocalDate getFinalEstagio() {
        return finalEstagio;
    }

    public void setFinalEstagio(LocalDate finalEstagio) {
        this.finalEstagio = finalEstagio;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProvaAptidaoProfissionalDTO provaAptidaoProfissionalDTO = (ProvaAptidaoProfissionalDTO) o;
        if (provaAptidaoProfissionalDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), provaAptidaoProfissionalDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProvaAptidaoProfissionalDTO{" +
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
