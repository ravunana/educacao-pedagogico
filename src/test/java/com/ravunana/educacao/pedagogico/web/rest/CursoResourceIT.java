package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.Curso;
import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.domain.PlanoActividade;
import com.ravunana.educacao.pedagogico.domain.Dosificacao;
import com.ravunana.educacao.pedagogico.repository.CursoRepository;
import com.ravunana.educacao.pedagogico.repository.search.CursoSearchRepository;
import com.ravunana.educacao.pedagogico.service.CursoService;
import com.ravunana.educacao.pedagogico.service.dto.CursoDTO;
import com.ravunana.educacao.pedagogico.service.mapper.CursoMapper;
import com.ravunana.educacao.pedagogico.web.rest.errors.ExceptionTranslator;
import com.ravunana.educacao.pedagogico.service.dto.CursoCriteria;
import com.ravunana.educacao.pedagogico.service.CursoQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.ravunana.educacao.pedagogico.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CursoResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class CursoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLA = "AAAAAAAAAA";
    private static final String UPDATED_SIGLA = "BBBBBBBBBB";

    private static final String DEFAULT_COMPETENCIAS = "AAAAAAAAAA";
    private static final String UPDATED_COMPETENCIAS = "BBBBBBBBBB";

    private static final String DEFAULT_AREA_FORMACAO = "AAAAAAAAAA";
    private static final String UPDATED_AREA_FORMACAO = "BBBBBBBBBB";

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CursoMapper cursoMapper;

    @Autowired
    private CursoService cursoService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.CursoSearchRepositoryMockConfiguration
     */
    @Autowired
    private CursoSearchRepository mockCursoSearchRepository;

    @Autowired
    private CursoQueryService cursoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCursoMockMvc;

    private Curso curso;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CursoResource cursoResource = new CursoResource(cursoService, cursoQueryService);
        this.restCursoMockMvc = MockMvcBuilders.standaloneSetup(cursoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Curso createEntity(EntityManager em) {
        Curso curso = new Curso()
            .nome(DEFAULT_NOME)
            .sigla(DEFAULT_SIGLA)
            .competencias(DEFAULT_COMPETENCIAS)
            .areaFormacao(DEFAULT_AREA_FORMACAO);
        return curso;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Curso createUpdatedEntity(EntityManager em) {
        Curso curso = new Curso()
            .nome(UPDATED_NOME)
            .sigla(UPDATED_SIGLA)
            .competencias(UPDATED_COMPETENCIAS)
            .areaFormacao(UPDATED_AREA_FORMACAO);
        return curso;
    }

    @BeforeEach
    public void initTest() {
        curso = createEntity(em);
    }

    @Test
    @Transactional
    public void createCurso() throws Exception {
        int databaseSizeBeforeCreate = cursoRepository.findAll().size();

        // Create the Curso
        CursoDTO cursoDTO = cursoMapper.toDto(curso);
        restCursoMockMvc.perform(post("/api/cursos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cursoDTO)))
            .andExpect(status().isCreated());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeCreate + 1);
        Curso testCurso = cursoList.get(cursoList.size() - 1);
        assertThat(testCurso.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCurso.getSigla()).isEqualTo(DEFAULT_SIGLA);
        assertThat(testCurso.getCompetencias()).isEqualTo(DEFAULT_COMPETENCIAS);
        assertThat(testCurso.getAreaFormacao()).isEqualTo(DEFAULT_AREA_FORMACAO);

        // Validate the Curso in Elasticsearch
        verify(mockCursoSearchRepository, times(1)).save(testCurso);
    }

    @Test
    @Transactional
    public void createCursoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cursoRepository.findAll().size();

        // Create the Curso with an existing ID
        curso.setId(1L);
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCursoMockMvc.perform(post("/api/cursos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cursoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Curso in Elasticsearch
        verify(mockCursoSearchRepository, times(0)).save(curso);
    }


    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cursoRepository.findAll().size();
        // set the field null
        curso.setNome(null);

        // Create the Curso, which fails.
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        restCursoMockMvc.perform(post("/api/cursos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cursoDTO)))
            .andExpect(status().isBadRequest());

        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSiglaIsRequired() throws Exception {
        int databaseSizeBeforeTest = cursoRepository.findAll().size();
        // set the field null
        curso.setSigla(null);

        // Create the Curso, which fails.
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        restCursoMockMvc.perform(post("/api/cursos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cursoDTO)))
            .andExpect(status().isBadRequest());

        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAreaFormacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cursoRepository.findAll().size();
        // set the field null
        curso.setAreaFormacao(null);

        // Create the Curso, which fails.
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        restCursoMockMvc.perform(post("/api/cursos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cursoDTO)))
            .andExpect(status().isBadRequest());

        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCursos() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList
        restCursoMockMvc.perform(get("/api/cursos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(curso.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)))
            .andExpect(jsonPath("$.[*].competencias").value(hasItem(DEFAULT_COMPETENCIAS.toString())))
            .andExpect(jsonPath("$.[*].areaFormacao").value(hasItem(DEFAULT_AREA_FORMACAO)));
    }
    
    @Test
    @Transactional
    public void getCurso() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get the curso
        restCursoMockMvc.perform(get("/api/cursos/{id}", curso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(curso.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.sigla").value(DEFAULT_SIGLA))
            .andExpect(jsonPath("$.competencias").value(DEFAULT_COMPETENCIAS.toString()))
            .andExpect(jsonPath("$.areaFormacao").value(DEFAULT_AREA_FORMACAO));
    }


    @Test
    @Transactional
    public void getCursosByIdFiltering() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        Long id = curso.getId();

        defaultCursoShouldBeFound("id.equals=" + id);
        defaultCursoShouldNotBeFound("id.notEquals=" + id);

        defaultCursoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCursoShouldNotBeFound("id.greaterThan=" + id);

        defaultCursoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCursoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCursosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where nome equals to DEFAULT_NOME
        defaultCursoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the cursoList where nome equals to UPDATED_NOME
        defaultCursoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllCursosByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where nome not equals to DEFAULT_NOME
        defaultCursoShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the cursoList where nome not equals to UPDATED_NOME
        defaultCursoShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllCursosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultCursoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the cursoList where nome equals to UPDATED_NOME
        defaultCursoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllCursosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where nome is not null
        defaultCursoShouldBeFound("nome.specified=true");

        // Get all the cursoList where nome is null
        defaultCursoShouldNotBeFound("nome.specified=false");
    }
                @Test
    @Transactional
    public void getAllCursosByNomeContainsSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where nome contains DEFAULT_NOME
        defaultCursoShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the cursoList where nome contains UPDATED_NOME
        defaultCursoShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllCursosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where nome does not contain DEFAULT_NOME
        defaultCursoShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the cursoList where nome does not contain UPDATED_NOME
        defaultCursoShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }


    @Test
    @Transactional
    public void getAllCursosBySiglaIsEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where sigla equals to DEFAULT_SIGLA
        defaultCursoShouldBeFound("sigla.equals=" + DEFAULT_SIGLA);

        // Get all the cursoList where sigla equals to UPDATED_SIGLA
        defaultCursoShouldNotBeFound("sigla.equals=" + UPDATED_SIGLA);
    }

    @Test
    @Transactional
    public void getAllCursosBySiglaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where sigla not equals to DEFAULT_SIGLA
        defaultCursoShouldNotBeFound("sigla.notEquals=" + DEFAULT_SIGLA);

        // Get all the cursoList where sigla not equals to UPDATED_SIGLA
        defaultCursoShouldBeFound("sigla.notEquals=" + UPDATED_SIGLA);
    }

    @Test
    @Transactional
    public void getAllCursosBySiglaIsInShouldWork() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where sigla in DEFAULT_SIGLA or UPDATED_SIGLA
        defaultCursoShouldBeFound("sigla.in=" + DEFAULT_SIGLA + "," + UPDATED_SIGLA);

        // Get all the cursoList where sigla equals to UPDATED_SIGLA
        defaultCursoShouldNotBeFound("sigla.in=" + UPDATED_SIGLA);
    }

    @Test
    @Transactional
    public void getAllCursosBySiglaIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where sigla is not null
        defaultCursoShouldBeFound("sigla.specified=true");

        // Get all the cursoList where sigla is null
        defaultCursoShouldNotBeFound("sigla.specified=false");
    }
                @Test
    @Transactional
    public void getAllCursosBySiglaContainsSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where sigla contains DEFAULT_SIGLA
        defaultCursoShouldBeFound("sigla.contains=" + DEFAULT_SIGLA);

        // Get all the cursoList where sigla contains UPDATED_SIGLA
        defaultCursoShouldNotBeFound("sigla.contains=" + UPDATED_SIGLA);
    }

    @Test
    @Transactional
    public void getAllCursosBySiglaNotContainsSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where sigla does not contain DEFAULT_SIGLA
        defaultCursoShouldNotBeFound("sigla.doesNotContain=" + DEFAULT_SIGLA);

        // Get all the cursoList where sigla does not contain UPDATED_SIGLA
        defaultCursoShouldBeFound("sigla.doesNotContain=" + UPDATED_SIGLA);
    }


    @Test
    @Transactional
    public void getAllCursosByAreaFormacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where areaFormacao equals to DEFAULT_AREA_FORMACAO
        defaultCursoShouldBeFound("areaFormacao.equals=" + DEFAULT_AREA_FORMACAO);

        // Get all the cursoList where areaFormacao equals to UPDATED_AREA_FORMACAO
        defaultCursoShouldNotBeFound("areaFormacao.equals=" + UPDATED_AREA_FORMACAO);
    }

    @Test
    @Transactional
    public void getAllCursosByAreaFormacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where areaFormacao not equals to DEFAULT_AREA_FORMACAO
        defaultCursoShouldNotBeFound("areaFormacao.notEquals=" + DEFAULT_AREA_FORMACAO);

        // Get all the cursoList where areaFormacao not equals to UPDATED_AREA_FORMACAO
        defaultCursoShouldBeFound("areaFormacao.notEquals=" + UPDATED_AREA_FORMACAO);
    }

    @Test
    @Transactional
    public void getAllCursosByAreaFormacaoIsInShouldWork() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where areaFormacao in DEFAULT_AREA_FORMACAO or UPDATED_AREA_FORMACAO
        defaultCursoShouldBeFound("areaFormacao.in=" + DEFAULT_AREA_FORMACAO + "," + UPDATED_AREA_FORMACAO);

        // Get all the cursoList where areaFormacao equals to UPDATED_AREA_FORMACAO
        defaultCursoShouldNotBeFound("areaFormacao.in=" + UPDATED_AREA_FORMACAO);
    }

    @Test
    @Transactional
    public void getAllCursosByAreaFormacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where areaFormacao is not null
        defaultCursoShouldBeFound("areaFormacao.specified=true");

        // Get all the cursoList where areaFormacao is null
        defaultCursoShouldNotBeFound("areaFormacao.specified=false");
    }
                @Test
    @Transactional
    public void getAllCursosByAreaFormacaoContainsSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where areaFormacao contains DEFAULT_AREA_FORMACAO
        defaultCursoShouldBeFound("areaFormacao.contains=" + DEFAULT_AREA_FORMACAO);

        // Get all the cursoList where areaFormacao contains UPDATED_AREA_FORMACAO
        defaultCursoShouldNotBeFound("areaFormacao.contains=" + UPDATED_AREA_FORMACAO);
    }

    @Test
    @Transactional
    public void getAllCursosByAreaFormacaoNotContainsSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        // Get all the cursoList where areaFormacao does not contain DEFAULT_AREA_FORMACAO
        defaultCursoShouldNotBeFound("areaFormacao.doesNotContain=" + DEFAULT_AREA_FORMACAO);

        // Get all the cursoList where areaFormacao does not contain UPDATED_AREA_FORMACAO
        defaultCursoShouldBeFound("areaFormacao.doesNotContain=" + UPDATED_AREA_FORMACAO);
    }


    @Test
    @Transactional
    public void getAllCursosByPlanoCurricularIsEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);
        PlanoCurricular planoCurricular = PlanoCurricularResourceIT.createEntity(em);
        em.persist(planoCurricular);
        em.flush();
        curso.addPlanoCurricular(planoCurricular);
        cursoRepository.saveAndFlush(curso);
        Long planoCurricularId = planoCurricular.getId();

        // Get all the cursoList where planoCurricular equals to planoCurricularId
        defaultCursoShouldBeFound("planoCurricularId.equals=" + planoCurricularId);

        // Get all the cursoList where planoCurricular equals to planoCurricularId + 1
        defaultCursoShouldNotBeFound("planoCurricularId.equals=" + (planoCurricularId + 1));
    }


    @Test
    @Transactional
    public void getAllCursosByTurmaIsEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);
        Turma turma = TurmaResourceIT.createEntity(em);
        em.persist(turma);
        em.flush();
        curso.addTurma(turma);
        cursoRepository.saveAndFlush(curso);
        Long turmaId = turma.getId();

        // Get all the cursoList where turma equals to turmaId
        defaultCursoShouldBeFound("turmaId.equals=" + turmaId);

        // Get all the cursoList where turma equals to turmaId + 1
        defaultCursoShouldNotBeFound("turmaId.equals=" + (turmaId + 1));
    }


    @Test
    @Transactional
    public void getAllCursosByPlanoActividadeIsEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);
        PlanoActividade planoActividade = PlanoActividadeResourceIT.createEntity(em);
        em.persist(planoActividade);
        em.flush();
        curso.addPlanoActividade(planoActividade);
        cursoRepository.saveAndFlush(curso);
        Long planoActividadeId = planoActividade.getId();

        // Get all the cursoList where planoActividade equals to planoActividadeId
        defaultCursoShouldBeFound("planoActividadeId.equals=" + planoActividadeId);

        // Get all the cursoList where planoActividade equals to planoActividadeId + 1
        defaultCursoShouldNotBeFound("planoActividadeId.equals=" + (planoActividadeId + 1));
    }


    @Test
    @Transactional
    public void getAllCursosByDosificacaoCursoIsEqualToSomething() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);
        Dosificacao dosificacaoCurso = DosificacaoResourceIT.createEntity(em);
        em.persist(dosificacaoCurso);
        em.flush();
        curso.addDosificacaoCurso(dosificacaoCurso);
        cursoRepository.saveAndFlush(curso);
        Long dosificacaoCursoId = dosificacaoCurso.getId();

        // Get all the cursoList where dosificacaoCurso equals to dosificacaoCursoId
        defaultCursoShouldBeFound("dosificacaoCursoId.equals=" + dosificacaoCursoId);

        // Get all the cursoList where dosificacaoCurso equals to dosificacaoCursoId + 1
        defaultCursoShouldNotBeFound("dosificacaoCursoId.equals=" + (dosificacaoCursoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCursoShouldBeFound(String filter) throws Exception {
        restCursoMockMvc.perform(get("/api/cursos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(curso.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)))
            .andExpect(jsonPath("$.[*].competencias").value(hasItem(DEFAULT_COMPETENCIAS.toString())))
            .andExpect(jsonPath("$.[*].areaFormacao").value(hasItem(DEFAULT_AREA_FORMACAO)));

        // Check, that the count call also returns 1
        restCursoMockMvc.perform(get("/api/cursos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCursoShouldNotBeFound(String filter) throws Exception {
        restCursoMockMvc.perform(get("/api/cursos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCursoMockMvc.perform(get("/api/cursos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCurso() throws Exception {
        // Get the curso
        restCursoMockMvc.perform(get("/api/cursos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurso() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        int databaseSizeBeforeUpdate = cursoRepository.findAll().size();

        // Update the curso
        Curso updatedCurso = cursoRepository.findById(curso.getId()).get();
        // Disconnect from session so that the updates on updatedCurso are not directly saved in db
        em.detach(updatedCurso);
        updatedCurso
            .nome(UPDATED_NOME)
            .sigla(UPDATED_SIGLA)
            .competencias(UPDATED_COMPETENCIAS)
            .areaFormacao(UPDATED_AREA_FORMACAO);
        CursoDTO cursoDTO = cursoMapper.toDto(updatedCurso);

        restCursoMockMvc.perform(put("/api/cursos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cursoDTO)))
            .andExpect(status().isOk());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeUpdate);
        Curso testCurso = cursoList.get(cursoList.size() - 1);
        assertThat(testCurso.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCurso.getSigla()).isEqualTo(UPDATED_SIGLA);
        assertThat(testCurso.getCompetencias()).isEqualTo(UPDATED_COMPETENCIAS);
        assertThat(testCurso.getAreaFormacao()).isEqualTo(UPDATED_AREA_FORMACAO);

        // Validate the Curso in Elasticsearch
        verify(mockCursoSearchRepository, times(1)).save(testCurso);
    }

    @Test
    @Transactional
    public void updateNonExistingCurso() throws Exception {
        int databaseSizeBeforeUpdate = cursoRepository.findAll().size();

        // Create the Curso
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCursoMockMvc.perform(put("/api/cursos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cursoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Curso in Elasticsearch
        verify(mockCursoSearchRepository, times(0)).save(curso);
    }

    @Test
    @Transactional
    public void deleteCurso() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);

        int databaseSizeBeforeDelete = cursoRepository.findAll().size();

        // Delete the curso
        restCursoMockMvc.perform(delete("/api/cursos/{id}", curso.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Curso> cursoList = cursoRepository.findAll();
        assertThat(cursoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Curso in Elasticsearch
        verify(mockCursoSearchRepository, times(1)).deleteById(curso.getId());
    }

    @Test
    @Transactional
    public void searchCurso() throws Exception {
        // Initialize the database
        cursoRepository.saveAndFlush(curso);
        when(mockCursoSearchRepository.search(queryStringQuery("id:" + curso.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(curso), PageRequest.of(0, 1), 1));
        // Search the curso
        restCursoMockMvc.perform(get("/api/_search/cursos?query=id:" + curso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(curso.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sigla").value(hasItem(DEFAULT_SIGLA)))
            .andExpect(jsonPath("$.[*].competencias").value(hasItem(DEFAULT_COMPETENCIAS.toString())))
            .andExpect(jsonPath("$.[*].areaFormacao").value(hasItem(DEFAULT_AREA_FORMACAO)));
    }
}
