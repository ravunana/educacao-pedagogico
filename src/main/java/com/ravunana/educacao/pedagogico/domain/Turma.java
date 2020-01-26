package com.ravunana.educacao.pedagogico.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Turma.
 */
@Entity
@Table(name = "turma")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "turma")
public class Turma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "descricao", nullable = false, unique = true)
    private String descricao;

    @NotNull
    @Column(name = "ano_lectivo", nullable = false)
    private Integer anoLectivo;

    @NotNull
    @Column(name = "data", nullable = false)
    private ZonedDateTime data;

    @NotNull
    @Column(name = "abertura", nullable = false)
    private LocalDate abertura;

    @NotNull
    @Column(name = "encerramento", nullable = false)
    private LocalDate encerramento;

    @NotNull
    @Min(value = 1)
    @Column(name = "lotacao", nullable = false)
    private Integer lotacao;

    @NotNull
    @Column(name = "aberta", nullable = false)
    private Boolean aberta;

    @NotNull
    @Column(name = "periodo_lectivo", nullable = false)
    private String periodoLectivo;

    @NotNull
    @Column(name = "turno", nullable = false)
    private String turno;

    @NotNull
    @Column(name = "sala", nullable = false)
    private Integer sala;

    @NotNull
    @Column(name = "classe", nullable = false)
    private Integer classe;

    @OneToMany(mappedBy = "turma")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Horario> horarios = new HashSet<>();

    @OneToMany(mappedBy = "turma")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PlanoActividade> planoActividades = new HashSet<>();

    @OneToMany(mappedBy = "turma")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Nota> notas = new HashSet<>();

    @OneToMany(mappedBy = "turma")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Aula> aulas = new HashSet<>();

    @OneToMany(mappedBy = "turma")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TesteConhecimento> testeConhecimentos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("turmas")
    private Curso curso;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("turmas")
    private Professor coordenador;

    @ManyToMany(mappedBy = "turmas")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<PlanoAula> planoAulaTurmas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Turma descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getAnoLectivo() {
        return anoLectivo;
    }

    public Turma anoLectivo(Integer anoLectivo) {
        this.anoLectivo = anoLectivo;
        return this;
    }

    public void setAnoLectivo(Integer anoLectivo) {
        this.anoLectivo = anoLectivo;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public Turma data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public LocalDate getAbertura() {
        return abertura;
    }

    public Turma abertura(LocalDate abertura) {
        this.abertura = abertura;
        return this;
    }

    public void setAbertura(LocalDate abertura) {
        this.abertura = abertura;
    }

    public LocalDate getEncerramento() {
        return encerramento;
    }

    public Turma encerramento(LocalDate encerramento) {
        this.encerramento = encerramento;
        return this;
    }

    public void setEncerramento(LocalDate encerramento) {
        this.encerramento = encerramento;
    }

    public Integer getLotacao() {
        return lotacao;
    }

    public Turma lotacao(Integer lotacao) {
        this.lotacao = lotacao;
        return this;
    }

    public void setLotacao(Integer lotacao) {
        this.lotacao = lotacao;
    }

    public Boolean isAberta() {
        return aberta;
    }

    public Turma aberta(Boolean aberta) {
        this.aberta = aberta;
        return this;
    }

    public void setAberta(Boolean aberta) {
        this.aberta = aberta;
    }

    public String getPeriodoLectivo() {
        return periodoLectivo;
    }

    public Turma periodoLectivo(String periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
        return this;
    }

    public void setPeriodoLectivo(String periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
    }

    public String getTurno() {
        return turno;
    }

    public Turma turno(String turno) {
        this.turno = turno;
        return this;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Integer getSala() {
        return sala;
    }

    public Turma sala(Integer sala) {
        this.sala = sala;
        return this;
    }

    public void setSala(Integer sala) {
        this.sala = sala;
    }

    public Integer getClasse() {
        return classe;
    }

    public Turma classe(Integer classe) {
        this.classe = classe;
        return this;
    }

    public void setClasse(Integer classe) {
        this.classe = classe;
    }

    public Set<Horario> getHorarios() {
        return horarios;
    }

    public Turma horarios(Set<Horario> horarios) {
        this.horarios = horarios;
        return this;
    }

    public Turma addHorario(Horario horario) {
        this.horarios.add(horario);
        horario.setTurma(this);
        return this;
    }

    public Turma removeHorario(Horario horario) {
        this.horarios.remove(horario);
        horario.setTurma(null);
        return this;
    }

    public void setHorarios(Set<Horario> horarios) {
        this.horarios = horarios;
    }

    public Set<PlanoActividade> getPlanoActividades() {
        return planoActividades;
    }

    public Turma planoActividades(Set<PlanoActividade> planoActividades) {
        this.planoActividades = planoActividades;
        return this;
    }

    public Turma addPlanoActividade(PlanoActividade planoActividade) {
        this.planoActividades.add(planoActividade);
        planoActividade.setTurma(this);
        return this;
    }

    public Turma removePlanoActividade(PlanoActividade planoActividade) {
        this.planoActividades.remove(planoActividade);
        planoActividade.setTurma(null);
        return this;
    }

    public void setPlanoActividades(Set<PlanoActividade> planoActividades) {
        this.planoActividades = planoActividades;
    }

    public Set<Nota> getNotas() {
        return notas;
    }

    public Turma notas(Set<Nota> notas) {
        this.notas = notas;
        return this;
    }

    public Turma addNota(Nota nota) {
        this.notas.add(nota);
        nota.setTurma(this);
        return this;
    }

    public Turma removeNota(Nota nota) {
        this.notas.remove(nota);
        nota.setTurma(null);
        return this;
    }

    public void setNotas(Set<Nota> notas) {
        this.notas = notas;
    }

    public Set<Aula> getAulas() {
        return aulas;
    }

    public Turma aulas(Set<Aula> aulas) {
        this.aulas = aulas;
        return this;
    }

    public Turma addAula(Aula aula) {
        this.aulas.add(aula);
        aula.setTurma(this);
        return this;
    }

    public Turma removeAula(Aula aula) {
        this.aulas.remove(aula);
        aula.setTurma(null);
        return this;
    }

    public void setAulas(Set<Aula> aulas) {
        this.aulas = aulas;
    }

    public Set<TesteConhecimento> getTesteConhecimentos() {
        return testeConhecimentos;
    }

    public Turma testeConhecimentos(Set<TesteConhecimento> testeConhecimentos) {
        this.testeConhecimentos = testeConhecimentos;
        return this;
    }

    public Turma addTesteConhecimento(TesteConhecimento testeConhecimento) {
        this.testeConhecimentos.add(testeConhecimento);
        testeConhecimento.setTurma(this);
        return this;
    }

    public Turma removeTesteConhecimento(TesteConhecimento testeConhecimento) {
        this.testeConhecimentos.remove(testeConhecimento);
        testeConhecimento.setTurma(null);
        return this;
    }

    public void setTesteConhecimentos(Set<TesteConhecimento> testeConhecimentos) {
        this.testeConhecimentos = testeConhecimentos;
    }

    public Curso getCurso() {
        return curso;
    }

    public Turma curso(Curso curso) {
        this.curso = curso;
        return this;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Professor getCoordenador() {
        return coordenador;
    }

    public Turma coordenador(Professor professor) {
        this.coordenador = professor;
        return this;
    }

    public void setCoordenador(Professor professor) {
        this.coordenador = professor;
    }

    public Set<PlanoAula> getPlanoAulaTurmas() {
        return planoAulaTurmas;
    }

    public Turma planoAulaTurmas(Set<PlanoAula> planoAulas) {
        this.planoAulaTurmas = planoAulas;
        return this;
    }

    public Turma addPlanoAulaTurma(PlanoAula planoAula) {
        this.planoAulaTurmas.add(planoAula);
        planoAula.getTurmas().add(this);
        return this;
    }

    public Turma removePlanoAulaTurma(PlanoAula planoAula) {
        this.planoAulaTurmas.remove(planoAula);
        planoAula.getTurmas().remove(this);
        return this;
    }

    public void setPlanoAulaTurmas(Set<PlanoAula> planoAulas) {
        this.planoAulaTurmas = planoAulas;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Turma)) {
            return false;
        }
        return id != null && id.equals(((Turma) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Turma{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", anoLectivo=" + getAnoLectivo() +
            ", data='" + getData() + "'" +
            ", abertura='" + getAbertura() + "'" +
            ", encerramento='" + getEncerramento() + "'" +
            ", lotacao=" + getLotacao() +
            ", aberta='" + isAberta() + "'" +
            ", periodoLectivo='" + getPeriodoLectivo() + "'" +
            ", turno='" + getTurno() + "'" +
            ", sala=" + getSala() +
            ", classe=" + getClasse() +
            "}";
    }
}
