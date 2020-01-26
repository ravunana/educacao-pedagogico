package com.ravunana.educacao.pedagogico.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Professor.
 */
@Entity
@Table(name = "professor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "professor")
public class Professor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "sexo", nullable = false)
    private String sexo;

    @Column(name = "fotografia")
    private String fotografia;

    @NotNull
    @Column(name = "contacto", nullable = false, unique = true)
    private String contacto;

    
    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Column(name = "residencia", nullable = false)
    private String residencia;

    @NotNull
    @Column(name = "numero_agente", nullable = false, unique = true)
    private String numeroAgente;

    @Column(name = "utilizador_id")
    private String utilizadorId;

    @Column(name = "ativo")
    private Boolean ativo;

    @OneToMany(mappedBy = "professor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Horario> horarios = new HashSet<>();

    @OneToMany(mappedBy = "coordenador")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Turma> turmas = new HashSet<>();

    @OneToMany(mappedBy = "professor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PlanoAula> planoAulas = new HashSet<>();

    @OneToMany(mappedBy = "professor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Nota> notas = new HashSet<>();

    @OneToMany(mappedBy = "professor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TesteConhecimento> testeConhecimentos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Professor nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public Professor sexo(String sexo) {
        this.sexo = sexo;
        return this;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFotografia() {
        return fotografia;
    }

    public Professor fotografia(String fotografia) {
        this.fotografia = fotografia;
        return this;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public String getContacto() {
        return contacto;
    }

    public Professor contacto(String contacto) {
        this.contacto = contacto;
        return this;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getEmail() {
        return email;
    }

    public Professor email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResidencia() {
        return residencia;
    }

    public Professor residencia(String residencia) {
        this.residencia = residencia;
        return this;
    }

    public void setResidencia(String residencia) {
        this.residencia = residencia;
    }

    public String getNumeroAgente() {
        return numeroAgente;
    }

    public Professor numeroAgente(String numeroAgente) {
        this.numeroAgente = numeroAgente;
        return this;
    }

    public void setNumeroAgente(String numeroAgente) {
        this.numeroAgente = numeroAgente;
    }

    public String getUtilizadorId() {
        return utilizadorId;
    }

    public Professor utilizadorId(String utilizadorId) {
        this.utilizadorId = utilizadorId;
        return this;
    }

    public void setUtilizadorId(String utilizadorId) {
        this.utilizadorId = utilizadorId;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public Professor ativo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Set<Horario> getHorarios() {
        return horarios;
    }

    public Professor horarios(Set<Horario> horarios) {
        this.horarios = horarios;
        return this;
    }

    public Professor addHorario(Horario horario) {
        this.horarios.add(horario);
        horario.setProfessor(this);
        return this;
    }

    public Professor removeHorario(Horario horario) {
        this.horarios.remove(horario);
        horario.setProfessor(null);
        return this;
    }

    public void setHorarios(Set<Horario> horarios) {
        this.horarios = horarios;
    }

    public Set<Turma> getTurmas() {
        return turmas;
    }

    public Professor turmas(Set<Turma> turmas) {
        this.turmas = turmas;
        return this;
    }

    public Professor addTurma(Turma turma) {
        this.turmas.add(turma);
        turma.setCoordenador(this);
        return this;
    }

    public Professor removeTurma(Turma turma) {
        this.turmas.remove(turma);
        turma.setCoordenador(null);
        return this;
    }

    public void setTurmas(Set<Turma> turmas) {
        this.turmas = turmas;
    }

    public Set<PlanoAula> getPlanoAulas() {
        return planoAulas;
    }

    public Professor planoAulas(Set<PlanoAula> planoAulas) {
        this.planoAulas = planoAulas;
        return this;
    }

    public Professor addPlanoAula(PlanoAula planoAula) {
        this.planoAulas.add(planoAula);
        planoAula.setProfessor(this);
        return this;
    }

    public Professor removePlanoAula(PlanoAula planoAula) {
        this.planoAulas.remove(planoAula);
        planoAula.setProfessor(null);
        return this;
    }

    public void setPlanoAulas(Set<PlanoAula> planoAulas) {
        this.planoAulas = planoAulas;
    }

    public Set<Nota> getNotas() {
        return notas;
    }

    public Professor notas(Set<Nota> notas) {
        this.notas = notas;
        return this;
    }

    public Professor addNota(Nota nota) {
        this.notas.add(nota);
        nota.setProfessor(this);
        return this;
    }

    public Professor removeNota(Nota nota) {
        this.notas.remove(nota);
        nota.setProfessor(null);
        return this;
    }

    public void setNotas(Set<Nota> notas) {
        this.notas = notas;
    }

    public Set<TesteConhecimento> getTesteConhecimentos() {
        return testeConhecimentos;
    }

    public Professor testeConhecimentos(Set<TesteConhecimento> testeConhecimentos) {
        this.testeConhecimentos = testeConhecimentos;
        return this;
    }

    public Professor addTesteConhecimento(TesteConhecimento testeConhecimento) {
        this.testeConhecimentos.add(testeConhecimento);
        testeConhecimento.setProfessor(this);
        return this;
    }

    public Professor removeTesteConhecimento(TesteConhecimento testeConhecimento) {
        this.testeConhecimentos.remove(testeConhecimento);
        testeConhecimento.setProfessor(null);
        return this;
    }

    public void setTesteConhecimentos(Set<TesteConhecimento> testeConhecimentos) {
        this.testeConhecimentos = testeConhecimentos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Professor)) {
            return false;
        }
        return id != null && id.equals(((Professor) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Professor{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", fotografia='" + getFotografia() + "'" +
            ", contacto='" + getContacto() + "'" +
            ", email='" + getEmail() + "'" +
            ", residencia='" + getResidencia() + "'" +
            ", numeroAgente='" + getNumeroAgente() + "'" +
            ", utilizadorId='" + getUtilizadorId() + "'" +
            ", ativo='" + isAtivo() + "'" +
            "}";
    }
}
