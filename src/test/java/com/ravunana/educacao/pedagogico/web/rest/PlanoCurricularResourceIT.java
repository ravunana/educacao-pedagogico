package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import com.ravunana.educacao.pedagogico.domain.PlanoAula;
import com.ravunana.educacao.pedagogico.domain.Dosificacao;
import com.ravunana.educacao.pedagogico.domain.Nota;
import com.ravunana.educacao.pedagogico.domain.Aula;
import com.ravunana.educacao.pedagogico.domain.Horario;
import com.ravunana.educacao.pedagogico.domain.TesteConhecimento;
import com.ravunana.educacao.pedagogico.domain.Curso;
import com.ravunana.educacao.pedagogico.repository.PlanoCurricularRepository;
import com.ravunana.educacao.pedagogico.repository.search.PlanoCurricularSearchRepository;
import com.ravunana.educacao.pedagogico.service.PlanoCurricularService;
import com.ravunana.educacao.pedagogico.service.dto.PlanoCurricularDTO;
import com.ravunana.educacao.pedagogico.service.mapper.PlanoCurricularMapper;
import com.ravunana.educacao.pedagogico.web.rest.errors.ExceptionTranslator;
import com.ravunana.educacao.pedagogico.service.dto.PlanoCurricularCriteria;
import com.ravunana.educacao.pedagogico.service.PlanoCurricularQueryService;

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
 * Integration tests for the {@link PlanoCurricularResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class PlanoCurricularResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TERMINAL = false;
    private static final Boolean UPDATED_TERMINAL = true;

    private static final Integer DEFAULT_TEMPO_SEMANAL = 1;
    private static final Integer UPDATED_TEMPO_SEMANAL = 2;
    private static final Integer SMALLER_TEMPO_SEMANAL = 1 - 1;

    private static final String DEFAULT_PERIODO_LECTIVO = "AAAAAAAAAA";
    private static final String UPDATED_PERIODO_LECTIVO = "BBBBBBBBBB";

    private static final String DEFAULT_COMPONENTE = "AAAAAAAAAA";
    private static final String UPDATED_COMPONENTE = "BBBBBBBBBB";

    private static final String DEFAULT_DISCIPLINA = "AAAAAAAAAA";
    private static final String UPDATED_DISCIPLINA = "BBBBBBBBBB";

    private static final Integer DEFAULT_CLASSE = 1;
    private static final Integer UPDATED_CLASSE = 2;
    private static final Integer SMALLER_CLASSE = 1 - 1;

    @Autowired
    private PlanoCurricularRepository planoCurricularRepository;

    @Autowired
    private PlanoCurricularMapper planoCurricularMapper;

    @Autowired
    private PlanoCurricularService planoCurricularService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.PlanoCurricularSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlanoCurricularSearchRepository mockPlanoCurricularSearchRepository;

    @Autowired
    private PlanoCurricularQueryService planoCurricularQueryService;

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

    private MockMvc restPlanoCurricularMockMvc;

    private PlanoCurricular planoCurricular;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanoCurricularResource planoCurricularResource = new PlanoCurricularResource(planoCurricularService, planoCurricularQueryService);
        this.restPlanoCurricularMockMvc = MockMvcBuilders.standaloneSetup(planoCurricularResource)
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
    public static PlanoCurricular createEntity(EntityManager em) {
        PlanoCurricular planoCurricular = new PlanoCurricular()
            .descricao(DEFAULT_DESCRICAO)
            .terminal(DEFAULT_TERMINAL)
            .tempoSemanal(DEFAULT_TEMPO_SEMANAL)
            .periodoLectivo(DEFAULT_PERIODO_LECTIVO)
            .componente(DEFAULT_COMPONENTE)
            .disciplina(DEFAULT_DISCIPLINA)
            .classe(DEFAULT_CLASSE);
        // Add required entity
        Curso curso;
        if (TestUtil.findAll(em, Curso.class).isEmpty()) {
            curso = CursoResourceIT.createEntity(em);
            em.persist(curso);
            em.flush();
        } else {
            curso = TestUtil.findAll(em, Curso.class).get(0);
        }
        planoCurricular.setCurso(curso);
        return planoCurricular;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanoCurricular createUpdatedEntity(EntityManager em) {
        PlanoCurricular planoCurricular = new PlanoCurricular()
            .descricao(UPDATED_DESCRICAO)
            .terminal(UPDATED_TERMINAL)
            .tempoSemanal(UPDATED_TEMPO_SEMANAL)
            .periodoLectivo(UPDATED_PERIODO_LECTIVO)
            .componente(UPDATED_COMPONENTE)
            .disciplina(UPDATED_DISCIPLINA)
            .classe(UPDATED_CLASSE);
        // Add required entity
        Curso curso;
        if (TestUtil.findAll(em, Curso.class).isEmpty()) {
            curso = CursoResourceIT.createUpdatedEntity(em);
            em.persist(curso);
            em.flush();
        } else {
            curso = TestUtil.findAll(em, Curso.class).get(0);
        }
        planoCurricular.setCurso(curso);
        return planoCurricular;
    }

    @BeforeEach
    public void initTest() {
        planoCurricular = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanoCurricular() throws Exception {
        int databaseSizeBeforeCreate = planoCurricularRepository.findAll().size();

        // Create the PlanoCurricular
        PlanoCurricularDTO planoCurricularDTO = planoCurricularMapper.toDto(planoCurricular);
        restPlanoCurricularMockMvc.perform(post("/api/plano-curriculars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoCurricularDTO)))
            .andExpect(status().isCreated());

        // Validate the PlanoCurricular in the database
        List<PlanoCurricular> planoCurricularList = planoCurricularRepository.findAll();
        assertThat(planoCurricularList).hasSize(databaseSizeBeforeCreate + 1);
        PlanoCurricular testPlanoCurricular = planoCurricularList.get(planoCurricularList.size() - 1);
        assertThat(testPlanoCurricular.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testPlanoCurricular.isTerminal()).isEqualTo(DEFAULT_TERMINAL);
        assertThat(testPlanoCurricular.getTempoSemanal()).isEqualTo(DEFAULT_TEMPO_SEMANAL);
        assertThat(testPlanoCurricular.getPeriodoLectivo()).isEqualTo(DEFAULT_PERIODO_LECTIVO);
        assertThat(testPlanoCurricular.getComponente()).isEqualTo(DEFAULT_COMPONENTE);
        assertThat(testPlanoCurricular.getDisciplina()).isEqualTo(DEFAULT_DISCIPLINA);
        assertThat(testPlanoCurricular.getClasse()).isEqualTo(DEFAULT_CLASSE);

        // Validate the PlanoCurricular in Elasticsearch
        verify(mockPlanoCurricularSearchRepository, times(1)).save(testPlanoCurricular);
    }

    @Test
    @Transactional
    public void createPlanoCurricularWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planoCurricularRepository.findAll().size();

        // Create the PlanoCurricular with an existing ID
        planoCurricular.setId(1L);
        PlanoCurricularDTO planoCurricularDTO = planoCurricularMapper.toDto(planoCurricular);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanoCurricularMockMvc.perform(post("/api/plano-curriculars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoCurricularDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanoCurricular in the database
        List<PlanoCurricular> planoCurricularList = planoCurricularRepository.findAll();
        assertThat(planoCurricularList).hasSize(databaseSizeBeforeCreate);

        // Validate the PlanoCurricular in Elasticsearch
        verify(mockPlanoCurricularSearchRepository, times(0)).save(planoCurricular);
    }


    @Test
    @Transactional
    public void checkTerminalIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoCurricularRepository.findAll().size();
        // set the field null
        planoCurricular.setTerminal(null);

        // Create the PlanoCurricular, which fails.
        PlanoCurricularDTO planoCurricularDTO = planoCurricularMapper.toDto(planoCurricular);

        restPlanoCurricularMockMvc.perform(post("/api/plano-curriculars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoCurricularDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoCurricular> planoCurricularList = planoCurricularRepository.findAll();
        assertThat(planoCurricularList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTempoSemanalIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoCurricularRepository.findAll().size();
        // set the field null
        planoCurricular.setTempoSemanal(null);

        // Create the PlanoCurricular, which fails.
        PlanoCurricularDTO planoCurricularDTO = planoCurricularMapper.toDto(planoCurricular);

        restPlanoCurricularMockMvc.perform(post("/api/plano-curriculars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoCurricularDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoCurricular> planoCurricularList = planoCurricularRepository.findAll();
        assertThat(planoCurricularList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkComponenteIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoCurricularRepository.findAll().size();
        // set the field null
        planoCurricular.setComponente(null);

        // Create the PlanoCurricular, which fails.
        PlanoCurricularDTO planoCurricularDTO = planoCurricularMapper.toDto(planoCurricular);

        restPlanoCurricularMockMvc.perform(post("/api/plano-curriculars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoCurricularDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoCurricular> planoCurricularList = planoCurricularRepository.findAll();
        assertThat(planoCurricularList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDisciplinaIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoCurricularRepository.findAll().size();
        // set the field null
        planoCurricular.setDisciplina(null);

        // Create the PlanoCurricular, which fails.
        PlanoCurricularDTO planoCurricularDTO = planoCurricularMapper.toDto(planoCurricular);

        restPlanoCurricularMockMvc.perform(post("/api/plano-curriculars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoCurricularDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoCurricular> planoCurricularList = planoCurricularRepository.findAll();
        assertThat(planoCurricularList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClasseIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoCurricularRepository.findAll().size();
        // set the field null
        planoCurricular.setClasse(null);

        // Create the PlanoCurricular, which fails.
        PlanoCurricularDTO planoCurricularDTO = planoCurricularMapper.toDto(planoCurricular);

        restPlanoCurricularMockMvc.perform(post("/api/plano-curriculars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoCurricularDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoCurricular> planoCurricularList = planoCurricularRepository.findAll();
        assertThat(planoCurricularList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlanoCurriculars() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList
        restPlanoCurricularMockMvc.perform(get("/api/plano-curriculars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planoCurricular.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].terminal").value(hasItem(DEFAULT_TERMINAL.booleanValue())))
            .andExpect(jsonPath("$.[*].tempoSemanal").value(hasItem(DEFAULT_TEMPO_SEMANAL)))
            .andExpect(jsonPath("$.[*].periodoLectivo").value(hasItem(DEFAULT_PERIODO_LECTIVO)))
            .andExpect(jsonPath("$.[*].componente").value(hasItem(DEFAULT_COMPONENTE)))
            .andExpect(jsonPath("$.[*].disciplina").value(hasItem(DEFAULT_DISCIPLINA)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)));
    }
    
    @Test
    @Transactional
    public void getPlanoCurricular() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get the planoCurricular
        restPlanoCurricularMockMvc.perform(get("/api/plano-curriculars/{id}", planoCurricular.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planoCurricular.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.terminal").value(DEFAULT_TERMINAL.booleanValue()))
            .andExpect(jsonPath("$.tempoSemanal").value(DEFAULT_TEMPO_SEMANAL))
            .andExpect(jsonPath("$.periodoLectivo").value(DEFAULT_PERIODO_LECTIVO))
            .andExpect(jsonPath("$.componente").value(DEFAULT_COMPONENTE))
            .andExpect(jsonPath("$.disciplina").value(DEFAULT_DISCIPLINA))
            .andExpect(jsonPath("$.classe").value(DEFAULT_CLASSE));
    }


    @Test
    @Transactional
    public void getPlanoCurricularsByIdFiltering() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        Long id = planoCurricular.getId();

        defaultPlanoCurricularShouldBeFound("id.equals=" + id);
        defaultPlanoCurricularShouldNotBeFound("id.notEquals=" + id);

        defaultPlanoCurricularShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlanoCurricularShouldNotBeFound("id.greaterThan=" + id);

        defaultPlanoCurricularShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlanoCurricularShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPlanoCurricularsByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where descricao equals to DEFAULT_DESCRICAO
        defaultPlanoCurricularShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the planoCurricularList where descricao equals to UPDATED_DESCRICAO
        defaultPlanoCurricularShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByDescricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where descricao not equals to DEFAULT_DESCRICAO
        defaultPlanoCurricularShouldNotBeFound("descricao.notEquals=" + DEFAULT_DESCRICAO);

        // Get all the planoCurricularList where descricao not equals to UPDATED_DESCRICAO
        defaultPlanoCurricularShouldBeFound("descricao.notEquals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultPlanoCurricularShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the planoCurricularList where descricao equals to UPDATED_DESCRICAO
        defaultPlanoCurricularShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where descricao is not null
        defaultPlanoCurricularShouldBeFound("descricao.specified=true");

        // Get all the planoCurricularList where descricao is null
        defaultPlanoCurricularShouldNotBeFound("descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlanoCurricularsByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where descricao contains DEFAULT_DESCRICAO
        defaultPlanoCurricularShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the planoCurricularList where descricao contains UPDATED_DESCRICAO
        defaultPlanoCurricularShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where descricao does not contain DEFAULT_DESCRICAO
        defaultPlanoCurricularShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the planoCurricularList where descricao does not contain UPDATED_DESCRICAO
        defaultPlanoCurricularShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }


    @Test
    @Transactional
    public void getAllPlanoCurricularsByTerminalIsEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where terminal equals to DEFAULT_TERMINAL
        defaultPlanoCurricularShouldBeFound("terminal.equals=" + DEFAULT_TERMINAL);

        // Get all the planoCurricularList where terminal equals to UPDATED_TERMINAL
        defaultPlanoCurricularShouldNotBeFound("terminal.equals=" + UPDATED_TERMINAL);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByTerminalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where terminal not equals to DEFAULT_TERMINAL
        defaultPlanoCurricularShouldNotBeFound("terminal.notEquals=" + DEFAULT_TERMINAL);

        // Get all the planoCurricularList where terminal not equals to UPDATED_TERMINAL
        defaultPlanoCurricularShouldBeFound("terminal.notEquals=" + UPDATED_TERMINAL);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByTerminalIsInShouldWork() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where terminal in DEFAULT_TERMINAL or UPDATED_TERMINAL
        defaultPlanoCurricularShouldBeFound("terminal.in=" + DEFAULT_TERMINAL + "," + UPDATED_TERMINAL);

        // Get all the planoCurricularList where terminal equals to UPDATED_TERMINAL
        defaultPlanoCurricularShouldNotBeFound("terminal.in=" + UPDATED_TERMINAL);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByTerminalIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where terminal is not null
        defaultPlanoCurricularShouldBeFound("terminal.specified=true");

        // Get all the planoCurricularList where terminal is null
        defaultPlanoCurricularShouldNotBeFound("terminal.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByTempoSemanalIsEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where tempoSemanal equals to DEFAULT_TEMPO_SEMANAL
        defaultPlanoCurricularShouldBeFound("tempoSemanal.equals=" + DEFAULT_TEMPO_SEMANAL);

        // Get all the planoCurricularList where tempoSemanal equals to UPDATED_TEMPO_SEMANAL
        defaultPlanoCurricularShouldNotBeFound("tempoSemanal.equals=" + UPDATED_TEMPO_SEMANAL);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByTempoSemanalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where tempoSemanal not equals to DEFAULT_TEMPO_SEMANAL
        defaultPlanoCurricularShouldNotBeFound("tempoSemanal.notEquals=" + DEFAULT_TEMPO_SEMANAL);

        // Get all the planoCurricularList where tempoSemanal not equals to UPDATED_TEMPO_SEMANAL
        defaultPlanoCurricularShouldBeFound("tempoSemanal.notEquals=" + UPDATED_TEMPO_SEMANAL);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByTempoSemanalIsInShouldWork() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where tempoSemanal in DEFAULT_TEMPO_SEMANAL or UPDATED_TEMPO_SEMANAL
        defaultPlanoCurricularShouldBeFound("tempoSemanal.in=" + DEFAULT_TEMPO_SEMANAL + "," + UPDATED_TEMPO_SEMANAL);

        // Get all the planoCurricularList where tempoSemanal equals to UPDATED_TEMPO_SEMANAL
        defaultPlanoCurricularShouldNotBeFound("tempoSemanal.in=" + UPDATED_TEMPO_SEMANAL);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByTempoSemanalIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where tempoSemanal is not null
        defaultPlanoCurricularShouldBeFound("tempoSemanal.specified=true");

        // Get all the planoCurricularList where tempoSemanal is null
        defaultPlanoCurricularShouldNotBeFound("tempoSemanal.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByTempoSemanalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where tempoSemanal is greater than or equal to DEFAULT_TEMPO_SEMANAL
        defaultPlanoCurricularShouldBeFound("tempoSemanal.greaterThanOrEqual=" + DEFAULT_TEMPO_SEMANAL);

        // Get all the planoCurricularList where tempoSemanal is greater than or equal to (DEFAULT_TEMPO_SEMANAL + 1)
        defaultPlanoCurricularShouldNotBeFound("tempoSemanal.greaterThanOrEqual=" + (DEFAULT_TEMPO_SEMANAL + 1));
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByTempoSemanalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where tempoSemanal is less than or equal to DEFAULT_TEMPO_SEMANAL
        defaultPlanoCurricularShouldBeFound("tempoSemanal.lessThanOrEqual=" + DEFAULT_TEMPO_SEMANAL);

        // Get all the planoCurricularList where tempoSemanal is less than or equal to SMALLER_TEMPO_SEMANAL
        defaultPlanoCurricularShouldNotBeFound("tempoSemanal.lessThanOrEqual=" + SMALLER_TEMPO_SEMANAL);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByTempoSemanalIsLessThanSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where tempoSemanal is less than DEFAULT_TEMPO_SEMANAL
        defaultPlanoCurricularShouldNotBeFound("tempoSemanal.lessThan=" + DEFAULT_TEMPO_SEMANAL);

        // Get all the planoCurricularList where tempoSemanal is less than (DEFAULT_TEMPO_SEMANAL + 1)
        defaultPlanoCurricularShouldBeFound("tempoSemanal.lessThan=" + (DEFAULT_TEMPO_SEMANAL + 1));
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByTempoSemanalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where tempoSemanal is greater than DEFAULT_TEMPO_SEMANAL
        defaultPlanoCurricularShouldNotBeFound("tempoSemanal.greaterThan=" + DEFAULT_TEMPO_SEMANAL);

        // Get all the planoCurricularList where tempoSemanal is greater than SMALLER_TEMPO_SEMANAL
        defaultPlanoCurricularShouldBeFound("tempoSemanal.greaterThan=" + SMALLER_TEMPO_SEMANAL);
    }


    @Test
    @Transactional
    public void getAllPlanoCurricularsByPeriodoLectivoIsEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where periodoLectivo equals to DEFAULT_PERIODO_LECTIVO
        defaultPlanoCurricularShouldBeFound("periodoLectivo.equals=" + DEFAULT_PERIODO_LECTIVO);

        // Get all the planoCurricularList where periodoLectivo equals to UPDATED_PERIODO_LECTIVO
        defaultPlanoCurricularShouldNotBeFound("periodoLectivo.equals=" + UPDATED_PERIODO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByPeriodoLectivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where periodoLectivo not equals to DEFAULT_PERIODO_LECTIVO
        defaultPlanoCurricularShouldNotBeFound("periodoLectivo.notEquals=" + DEFAULT_PERIODO_LECTIVO);

        // Get all the planoCurricularList where periodoLectivo not equals to UPDATED_PERIODO_LECTIVO
        defaultPlanoCurricularShouldBeFound("periodoLectivo.notEquals=" + UPDATED_PERIODO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByPeriodoLectivoIsInShouldWork() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where periodoLectivo in DEFAULT_PERIODO_LECTIVO or UPDATED_PERIODO_LECTIVO
        defaultPlanoCurricularShouldBeFound("periodoLectivo.in=" + DEFAULT_PERIODO_LECTIVO + "," + UPDATED_PERIODO_LECTIVO);

        // Get all the planoCurricularList where periodoLectivo equals to UPDATED_PERIODO_LECTIVO
        defaultPlanoCurricularShouldNotBeFound("periodoLectivo.in=" + UPDATED_PERIODO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByPeriodoLectivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where periodoLectivo is not null
        defaultPlanoCurricularShouldBeFound("periodoLectivo.specified=true");

        // Get all the planoCurricularList where periodoLectivo is null
        defaultPlanoCurricularShouldNotBeFound("periodoLectivo.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlanoCurricularsByPeriodoLectivoContainsSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where periodoLectivo contains DEFAULT_PERIODO_LECTIVO
        defaultPlanoCurricularShouldBeFound("periodoLectivo.contains=" + DEFAULT_PERIODO_LECTIVO);

        // Get all the planoCurricularList where periodoLectivo contains UPDATED_PERIODO_LECTIVO
        defaultPlanoCurricularShouldNotBeFound("periodoLectivo.contains=" + UPDATED_PERIODO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByPeriodoLectivoNotContainsSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where periodoLectivo does not contain DEFAULT_PERIODO_LECTIVO
        defaultPlanoCurricularShouldNotBeFound("periodoLectivo.doesNotContain=" + DEFAULT_PERIODO_LECTIVO);

        // Get all the planoCurricularList where periodoLectivo does not contain UPDATED_PERIODO_LECTIVO
        defaultPlanoCurricularShouldBeFound("periodoLectivo.doesNotContain=" + UPDATED_PERIODO_LECTIVO);
    }


    @Test
    @Transactional
    public void getAllPlanoCurricularsByComponenteIsEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where componente equals to DEFAULT_COMPONENTE
        defaultPlanoCurricularShouldBeFound("componente.equals=" + DEFAULT_COMPONENTE);

        // Get all the planoCurricularList where componente equals to UPDATED_COMPONENTE
        defaultPlanoCurricularShouldNotBeFound("componente.equals=" + UPDATED_COMPONENTE);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByComponenteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where componente not equals to DEFAULT_COMPONENTE
        defaultPlanoCurricularShouldNotBeFound("componente.notEquals=" + DEFAULT_COMPONENTE);

        // Get all the planoCurricularList where componente not equals to UPDATED_COMPONENTE
        defaultPlanoCurricularShouldBeFound("componente.notEquals=" + UPDATED_COMPONENTE);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByComponenteIsInShouldWork() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where componente in DEFAULT_COMPONENTE or UPDATED_COMPONENTE
        defaultPlanoCurricularShouldBeFound("componente.in=" + DEFAULT_COMPONENTE + "," + UPDATED_COMPONENTE);

        // Get all the planoCurricularList where componente equals to UPDATED_COMPONENTE
        defaultPlanoCurricularShouldNotBeFound("componente.in=" + UPDATED_COMPONENTE);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByComponenteIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where componente is not null
        defaultPlanoCurricularShouldBeFound("componente.specified=true");

        // Get all the planoCurricularList where componente is null
        defaultPlanoCurricularShouldNotBeFound("componente.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlanoCurricularsByComponenteContainsSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where componente contains DEFAULT_COMPONENTE
        defaultPlanoCurricularShouldBeFound("componente.contains=" + DEFAULT_COMPONENTE);

        // Get all the planoCurricularList where componente contains UPDATED_COMPONENTE
        defaultPlanoCurricularShouldNotBeFound("componente.contains=" + UPDATED_COMPONENTE);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByComponenteNotContainsSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where componente does not contain DEFAULT_COMPONENTE
        defaultPlanoCurricularShouldNotBeFound("componente.doesNotContain=" + DEFAULT_COMPONENTE);

        // Get all the planoCurricularList where componente does not contain UPDATED_COMPONENTE
        defaultPlanoCurricularShouldBeFound("componente.doesNotContain=" + UPDATED_COMPONENTE);
    }


    @Test
    @Transactional
    public void getAllPlanoCurricularsByDisciplinaIsEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where disciplina equals to DEFAULT_DISCIPLINA
        defaultPlanoCurricularShouldBeFound("disciplina.equals=" + DEFAULT_DISCIPLINA);

        // Get all the planoCurricularList where disciplina equals to UPDATED_DISCIPLINA
        defaultPlanoCurricularShouldNotBeFound("disciplina.equals=" + UPDATED_DISCIPLINA);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByDisciplinaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where disciplina not equals to DEFAULT_DISCIPLINA
        defaultPlanoCurricularShouldNotBeFound("disciplina.notEquals=" + DEFAULT_DISCIPLINA);

        // Get all the planoCurricularList where disciplina not equals to UPDATED_DISCIPLINA
        defaultPlanoCurricularShouldBeFound("disciplina.notEquals=" + UPDATED_DISCIPLINA);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByDisciplinaIsInShouldWork() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where disciplina in DEFAULT_DISCIPLINA or UPDATED_DISCIPLINA
        defaultPlanoCurricularShouldBeFound("disciplina.in=" + DEFAULT_DISCIPLINA + "," + UPDATED_DISCIPLINA);

        // Get all the planoCurricularList where disciplina equals to UPDATED_DISCIPLINA
        defaultPlanoCurricularShouldNotBeFound("disciplina.in=" + UPDATED_DISCIPLINA);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByDisciplinaIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where disciplina is not null
        defaultPlanoCurricularShouldBeFound("disciplina.specified=true");

        // Get all the planoCurricularList where disciplina is null
        defaultPlanoCurricularShouldNotBeFound("disciplina.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlanoCurricularsByDisciplinaContainsSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where disciplina contains DEFAULT_DISCIPLINA
        defaultPlanoCurricularShouldBeFound("disciplina.contains=" + DEFAULT_DISCIPLINA);

        // Get all the planoCurricularList where disciplina contains UPDATED_DISCIPLINA
        defaultPlanoCurricularShouldNotBeFound("disciplina.contains=" + UPDATED_DISCIPLINA);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByDisciplinaNotContainsSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where disciplina does not contain DEFAULT_DISCIPLINA
        defaultPlanoCurricularShouldNotBeFound("disciplina.doesNotContain=" + DEFAULT_DISCIPLINA);

        // Get all the planoCurricularList where disciplina does not contain UPDATED_DISCIPLINA
        defaultPlanoCurricularShouldBeFound("disciplina.doesNotContain=" + UPDATED_DISCIPLINA);
    }


    @Test
    @Transactional
    public void getAllPlanoCurricularsByClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where classe equals to DEFAULT_CLASSE
        defaultPlanoCurricularShouldBeFound("classe.equals=" + DEFAULT_CLASSE);

        // Get all the planoCurricularList where classe equals to UPDATED_CLASSE
        defaultPlanoCurricularShouldNotBeFound("classe.equals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where classe not equals to DEFAULT_CLASSE
        defaultPlanoCurricularShouldNotBeFound("classe.notEquals=" + DEFAULT_CLASSE);

        // Get all the planoCurricularList where classe not equals to UPDATED_CLASSE
        defaultPlanoCurricularShouldBeFound("classe.notEquals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByClasseIsInShouldWork() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where classe in DEFAULT_CLASSE or UPDATED_CLASSE
        defaultPlanoCurricularShouldBeFound("classe.in=" + DEFAULT_CLASSE + "," + UPDATED_CLASSE);

        // Get all the planoCurricularList where classe equals to UPDATED_CLASSE
        defaultPlanoCurricularShouldNotBeFound("classe.in=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where classe is not null
        defaultPlanoCurricularShouldBeFound("classe.specified=true");

        // Get all the planoCurricularList where classe is null
        defaultPlanoCurricularShouldNotBeFound("classe.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByClasseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where classe is greater than or equal to DEFAULT_CLASSE
        defaultPlanoCurricularShouldBeFound("classe.greaterThanOrEqual=" + DEFAULT_CLASSE);

        // Get all the planoCurricularList where classe is greater than or equal to UPDATED_CLASSE
        defaultPlanoCurricularShouldNotBeFound("classe.greaterThanOrEqual=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByClasseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where classe is less than or equal to DEFAULT_CLASSE
        defaultPlanoCurricularShouldBeFound("classe.lessThanOrEqual=" + DEFAULT_CLASSE);

        // Get all the planoCurricularList where classe is less than or equal to SMALLER_CLASSE
        defaultPlanoCurricularShouldNotBeFound("classe.lessThanOrEqual=" + SMALLER_CLASSE);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByClasseIsLessThanSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where classe is less than DEFAULT_CLASSE
        defaultPlanoCurricularShouldNotBeFound("classe.lessThan=" + DEFAULT_CLASSE);

        // Get all the planoCurricularList where classe is less than UPDATED_CLASSE
        defaultPlanoCurricularShouldBeFound("classe.lessThan=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllPlanoCurricularsByClasseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        // Get all the planoCurricularList where classe is greater than DEFAULT_CLASSE
        defaultPlanoCurricularShouldNotBeFound("classe.greaterThan=" + DEFAULT_CLASSE);

        // Get all the planoCurricularList where classe is greater than SMALLER_CLASSE
        defaultPlanoCurricularShouldBeFound("classe.greaterThan=" + SMALLER_CLASSE);
    }


    @Test
    @Transactional
    public void getAllPlanoCurricularsByPlanoAulaIsEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);
        PlanoAula planoAula = PlanoAulaResourceIT.createEntity(em);
        em.persist(planoAula);
        em.flush();
        planoCurricular.addPlanoAula(planoAula);
        planoCurricularRepository.saveAndFlush(planoCurricular);
        Long planoAulaId = planoAula.getId();

        // Get all the planoCurricularList where planoAula equals to planoAulaId
        defaultPlanoCurricularShouldBeFound("planoAulaId.equals=" + planoAulaId);

        // Get all the planoCurricularList where planoAula equals to planoAulaId + 1
        defaultPlanoCurricularShouldNotBeFound("planoAulaId.equals=" + (planoAulaId + 1));
    }


    @Test
    @Transactional
    public void getAllPlanoCurricularsByDosificacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);
        Dosificacao dosificacao = DosificacaoResourceIT.createEntity(em);
        em.persist(dosificacao);
        em.flush();
        planoCurricular.addDosificacao(dosificacao);
        planoCurricularRepository.saveAndFlush(planoCurricular);
        Long dosificacaoId = dosificacao.getId();

        // Get all the planoCurricularList where dosificacao equals to dosificacaoId
        defaultPlanoCurricularShouldBeFound("dosificacaoId.equals=" + dosificacaoId);

        // Get all the planoCurricularList where dosificacao equals to dosificacaoId + 1
        defaultPlanoCurricularShouldNotBeFound("dosificacaoId.equals=" + (dosificacaoId + 1));
    }


    @Test
    @Transactional
    public void getAllPlanoCurricularsByNotaIsEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);
        Nota nota = NotaResourceIT.createEntity(em);
        em.persist(nota);
        em.flush();
        planoCurricular.addNota(nota);
        planoCurricularRepository.saveAndFlush(planoCurricular);
        Long notaId = nota.getId();

        // Get all the planoCurricularList where nota equals to notaId
        defaultPlanoCurricularShouldBeFound("notaId.equals=" + notaId);

        // Get all the planoCurricularList where nota equals to notaId + 1
        defaultPlanoCurricularShouldNotBeFound("notaId.equals=" + (notaId + 1));
    }


    @Test
    @Transactional
    public void getAllPlanoCurricularsByAulaIsEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);
        Aula aula = AulaResourceIT.createEntity(em);
        em.persist(aula);
        em.flush();
        planoCurricular.addAula(aula);
        planoCurricularRepository.saveAndFlush(planoCurricular);
        Long aulaId = aula.getId();

        // Get all the planoCurricularList where aula equals to aulaId
        defaultPlanoCurricularShouldBeFound("aulaId.equals=" + aulaId);

        // Get all the planoCurricularList where aula equals to aulaId + 1
        defaultPlanoCurricularShouldNotBeFound("aulaId.equals=" + (aulaId + 1));
    }


    @Test
    @Transactional
    public void getAllPlanoCurricularsByHorarioIsEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);
        Horario horario = HorarioResourceIT.createEntity(em);
        em.persist(horario);
        em.flush();
        planoCurricular.addHorario(horario);
        planoCurricularRepository.saveAndFlush(planoCurricular);
        Long horarioId = horario.getId();

        // Get all the planoCurricularList where horario equals to horarioId
        defaultPlanoCurricularShouldBeFound("horarioId.equals=" + horarioId);

        // Get all the planoCurricularList where horario equals to horarioId + 1
        defaultPlanoCurricularShouldNotBeFound("horarioId.equals=" + (horarioId + 1));
    }


    @Test
    @Transactional
    public void getAllPlanoCurricularsByTesteConhecimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);
        TesteConhecimento testeConhecimento = TesteConhecimentoResourceIT.createEntity(em);
        em.persist(testeConhecimento);
        em.flush();
        planoCurricular.addTesteConhecimento(testeConhecimento);
        planoCurricularRepository.saveAndFlush(planoCurricular);
        Long testeConhecimentoId = testeConhecimento.getId();

        // Get all the planoCurricularList where testeConhecimento equals to testeConhecimentoId
        defaultPlanoCurricularShouldBeFound("testeConhecimentoId.equals=" + testeConhecimentoId);

        // Get all the planoCurricularList where testeConhecimento equals to testeConhecimentoId + 1
        defaultPlanoCurricularShouldNotBeFound("testeConhecimentoId.equals=" + (testeConhecimentoId + 1));
    }


    @Test
    @Transactional
    public void getAllPlanoCurricularsByCursoIsEqualToSomething() throws Exception {
        // Get already existing entity
        Curso curso = planoCurricular.getCurso();
        planoCurricularRepository.saveAndFlush(planoCurricular);
        Long cursoId = curso.getId();

        // Get all the planoCurricularList where curso equals to cursoId
        defaultPlanoCurricularShouldBeFound("cursoId.equals=" + cursoId);

        // Get all the planoCurricularList where curso equals to cursoId + 1
        defaultPlanoCurricularShouldNotBeFound("cursoId.equals=" + (cursoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlanoCurricularShouldBeFound(String filter) throws Exception {
        restPlanoCurricularMockMvc.perform(get("/api/plano-curriculars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planoCurricular.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].terminal").value(hasItem(DEFAULT_TERMINAL.booleanValue())))
            .andExpect(jsonPath("$.[*].tempoSemanal").value(hasItem(DEFAULT_TEMPO_SEMANAL)))
            .andExpect(jsonPath("$.[*].periodoLectivo").value(hasItem(DEFAULT_PERIODO_LECTIVO)))
            .andExpect(jsonPath("$.[*].componente").value(hasItem(DEFAULT_COMPONENTE)))
            .andExpect(jsonPath("$.[*].disciplina").value(hasItem(DEFAULT_DISCIPLINA)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)));

        // Check, that the count call also returns 1
        restPlanoCurricularMockMvc.perform(get("/api/plano-curriculars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlanoCurricularShouldNotBeFound(String filter) throws Exception {
        restPlanoCurricularMockMvc.perform(get("/api/plano-curriculars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlanoCurricularMockMvc.perform(get("/api/plano-curriculars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlanoCurricular() throws Exception {
        // Get the planoCurricular
        restPlanoCurricularMockMvc.perform(get("/api/plano-curriculars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanoCurricular() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        int databaseSizeBeforeUpdate = planoCurricularRepository.findAll().size();

        // Update the planoCurricular
        PlanoCurricular updatedPlanoCurricular = planoCurricularRepository.findById(planoCurricular.getId()).get();
        // Disconnect from session so that the updates on updatedPlanoCurricular are not directly saved in db
        em.detach(updatedPlanoCurricular);
        updatedPlanoCurricular
            .descricao(UPDATED_DESCRICAO)
            .terminal(UPDATED_TERMINAL)
            .tempoSemanal(UPDATED_TEMPO_SEMANAL)
            .periodoLectivo(UPDATED_PERIODO_LECTIVO)
            .componente(UPDATED_COMPONENTE)
            .disciplina(UPDATED_DISCIPLINA)
            .classe(UPDATED_CLASSE);
        PlanoCurricularDTO planoCurricularDTO = planoCurricularMapper.toDto(updatedPlanoCurricular);

        restPlanoCurricularMockMvc.perform(put("/api/plano-curriculars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoCurricularDTO)))
            .andExpect(status().isOk());

        // Validate the PlanoCurricular in the database
        List<PlanoCurricular> planoCurricularList = planoCurricularRepository.findAll();
        assertThat(planoCurricularList).hasSize(databaseSizeBeforeUpdate);
        PlanoCurricular testPlanoCurricular = planoCurricularList.get(planoCurricularList.size() - 1);
        assertThat(testPlanoCurricular.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testPlanoCurricular.isTerminal()).isEqualTo(UPDATED_TERMINAL);
        assertThat(testPlanoCurricular.getTempoSemanal()).isEqualTo(UPDATED_TEMPO_SEMANAL);
        assertThat(testPlanoCurricular.getPeriodoLectivo()).isEqualTo(UPDATED_PERIODO_LECTIVO);
        assertThat(testPlanoCurricular.getComponente()).isEqualTo(UPDATED_COMPONENTE);
        assertThat(testPlanoCurricular.getDisciplina()).isEqualTo(UPDATED_DISCIPLINA);
        assertThat(testPlanoCurricular.getClasse()).isEqualTo(UPDATED_CLASSE);

        // Validate the PlanoCurricular in Elasticsearch
        verify(mockPlanoCurricularSearchRepository, times(1)).save(testPlanoCurricular);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanoCurricular() throws Exception {
        int databaseSizeBeforeUpdate = planoCurricularRepository.findAll().size();

        // Create the PlanoCurricular
        PlanoCurricularDTO planoCurricularDTO = planoCurricularMapper.toDto(planoCurricular);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanoCurricularMockMvc.perform(put("/api/plano-curriculars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoCurricularDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanoCurricular in the database
        List<PlanoCurricular> planoCurricularList = planoCurricularRepository.findAll();
        assertThat(planoCurricularList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanoCurricular in Elasticsearch
        verify(mockPlanoCurricularSearchRepository, times(0)).save(planoCurricular);
    }

    @Test
    @Transactional
    public void deletePlanoCurricular() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);

        int databaseSizeBeforeDelete = planoCurricularRepository.findAll().size();

        // Delete the planoCurricular
        restPlanoCurricularMockMvc.perform(delete("/api/plano-curriculars/{id}", planoCurricular.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlanoCurricular> planoCurricularList = planoCurricularRepository.findAll();
        assertThat(planoCurricularList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PlanoCurricular in Elasticsearch
        verify(mockPlanoCurricularSearchRepository, times(1)).deleteById(planoCurricular.getId());
    }

    @Test
    @Transactional
    public void searchPlanoCurricular() throws Exception {
        // Initialize the database
        planoCurricularRepository.saveAndFlush(planoCurricular);
        when(mockPlanoCurricularSearchRepository.search(queryStringQuery("id:" + planoCurricular.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(planoCurricular), PageRequest.of(0, 1), 1));
        // Search the planoCurricular
        restPlanoCurricularMockMvc.perform(get("/api/_search/plano-curriculars?query=id:" + planoCurricular.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planoCurricular.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].terminal").value(hasItem(DEFAULT_TERMINAL.booleanValue())))
            .andExpect(jsonPath("$.[*].tempoSemanal").value(hasItem(DEFAULT_TEMPO_SEMANAL)))
            .andExpect(jsonPath("$.[*].periodoLectivo").value(hasItem(DEFAULT_PERIODO_LECTIVO)))
            .andExpect(jsonPath("$.[*].componente").value(hasItem(DEFAULT_COMPONENTE)))
            .andExpect(jsonPath("$.[*].disciplina").value(hasItem(DEFAULT_DISCIPLINA)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)));
    }
}
