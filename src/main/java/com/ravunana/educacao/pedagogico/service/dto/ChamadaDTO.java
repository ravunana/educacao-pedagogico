package com.ravunana.educacao.pedagogico.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.Chamada} entity.
 */
public class ChamadaDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime data;

    @NotNull
    private Boolean presente;

    private String numeroProcesso;


    private Long aulaId;

    private String aulaData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public Boolean isPresente() {
        return presente;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public Long getAulaId() {
        return aulaId;
    }

    public void setAulaId(Long aulaId) {
        this.aulaId = aulaId;
    }

    public String getAulaData() {
        return aulaData;
    }

    public void setAulaData(String aulaData) {
        this.aulaData = aulaData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChamadaDTO chamadaDTO = (ChamadaDTO) o;
        if (chamadaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chamadaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChamadaDTO{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", presente='" + isPresente() + "'" +
            ", numeroProcesso='" + getNumeroProcesso() + "'" +
            ", aulaId=" + getAulaId() +
            ", aulaData='" + getAulaData() + "'" +
            "}";
    }
}
