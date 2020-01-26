package com.ravunana.educacao.pedagogico.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.ravunana.educacao.pedagogico.domain.PlanoAula} entity.
 */
public class PlanoAulaDTO implements Serializable {

    private Long id;

    
    @Lob
    private String objectivoGeral;

    
    @Lob
    private String objectivoEspecifico;

    
    @Lob
    private String conteudo;

    
    @Lob
    private String estrategia;

    
    @Lob
    private String actividades;

    @NotNull
    private ZonedDateTime tempo;

    
    @Lob
    private String recursosEnsino;

    
    @Lob
    private String avaliacao;

    
    @Lob
    private String observacao;

    
    @Lob
    private String bibliografia;

    
    @Lob
    private String perfilEntrada;

    
    @Lob
    private String perfilSaida;

    @Lob
    private byte[] anexo1;

    private String anexo1ContentType;

    private Set<TurmaDTO> turmas = new HashSet<>();

    private Long dosificacaoId;

    private Long professorId;

    private String professorNome;

    private Long curriculoId;

    private String curriculoDescricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectivoGeral() {
        return objectivoGeral;
    }

    public void setObjectivoGeral(String objectivoGeral) {
        this.objectivoGeral = objectivoGeral;
    }

    public String getObjectivoEspecifico() {
        return objectivoEspecifico;
    }

    public void setObjectivoEspecifico(String objectivoEspecifico) {
        this.objectivoEspecifico = objectivoEspecifico;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(String estrategia) {
        this.estrategia = estrategia;
    }

    public String getActividades() {
        return actividades;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public ZonedDateTime getTempo() {
        return tempo;
    }

    public void setTempo(ZonedDateTime tempo) {
        this.tempo = tempo;
    }

    public String getRecursosEnsino() {
        return recursosEnsino;
    }

    public void setRecursosEnsino(String recursosEnsino) {
        this.recursosEnsino = recursosEnsino;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getBibliografia() {
        return bibliografia;
    }

    public void setBibliografia(String bibliografia) {
        this.bibliografia = bibliografia;
    }

    public String getPerfilEntrada() {
        return perfilEntrada;
    }

    public void setPerfilEntrada(String perfilEntrada) {
        this.perfilEntrada = perfilEntrada;
    }

    public String getPerfilSaida() {
        return perfilSaida;
    }

    public void setPerfilSaida(String perfilSaida) {
        this.perfilSaida = perfilSaida;
    }

    public byte[] getAnexo1() {
        return anexo1;
    }

    public void setAnexo1(byte[] anexo1) {
        this.anexo1 = anexo1;
    }

    public String getAnexo1ContentType() {
        return anexo1ContentType;
    }

    public void setAnexo1ContentType(String anexo1ContentType) {
        this.anexo1ContentType = anexo1ContentType;
    }

    public Set<TurmaDTO> getTurmas() {
        return turmas;
    }

    public void setTurmas(Set<TurmaDTO> turmas) {
        this.turmas = turmas;
    }

    public Long getDosificacaoId() {
        return dosificacaoId;
    }

    public void setDosificacaoId(Long dosificacaoId) {
        this.dosificacaoId = dosificacaoId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlanoAulaDTO planoAulaDTO = (PlanoAulaDTO) o;
        if (planoAulaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), planoAulaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlanoAulaDTO{" +
            "id=" + getId() +
            ", objectivoGeral='" + getObjectivoGeral() + "'" +
            ", objectivoEspecifico='" + getObjectivoEspecifico() + "'" +
            ", conteudo='" + getConteudo() + "'" +
            ", estrategia='" + getEstrategia() + "'" +
            ", actividades='" + getActividades() + "'" +
            ", tempo='" + getTempo() + "'" +
            ", recursosEnsino='" + getRecursosEnsino() + "'" +
            ", avaliacao='" + getAvaliacao() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", bibliografia='" + getBibliografia() + "'" +
            ", perfilEntrada='" + getPerfilEntrada() + "'" +
            ", perfilSaida='" + getPerfilSaida() + "'" +
            ", anexo1='" + getAnexo1() + "'" +
            ", dosificacaoId=" + getDosificacaoId() +
            ", professorId=" + getProfessorId() +
            ", professorNome='" + getProfessorNome() + "'" +
            ", curriculoId=" + getCurriculoId() +
            ", curriculoDescricao='" + getCurriculoDescricao() + "'" +
            "}";
    }
}
