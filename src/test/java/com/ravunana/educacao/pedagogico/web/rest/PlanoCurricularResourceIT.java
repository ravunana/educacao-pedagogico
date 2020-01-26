package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import com.ravunana.educacao.pedagogico.domain.Curso;
import com.ravunana.educacao.pedagogico.repository.PlanoCurricularRepository;
import com.ravunana.educacao.pedagogico.repository.search.PlanoCurricularSearchRepository;
import com.ravunana.educacao.pedagogico.service.PlanoCurricularService;
import com.ravunana.educacao.pedagogico.service.dto.PlanoCurricularDTO;
import com.ravunana.educacao.pedagogico.service.mapper.PlanoCurricularMapper;
import com.ravunana.educacao.pedagogico.web.rest.errors.ExceptionTranslator;

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

    private static final String DEFAULT_PERIODO_LECTIVO = "AAAAAAAAAA";
    private static final String UPDATED_PERIODO_LECTIVO = "BBBBBBBBBB";

    private static final String DEFAULT_COMPONENTE = "AAAAAAAAAA";
    private static final String UPDATED_COMPONENTE = "BBBBBBBBBB";

    private static final String DEFAULT_DISCIPLINA = "AAAAAAAAAA";
    private static final String UPDATED_DISCIPLINA = "BBBBBBBBBB";

    private static final Integer DEFAULT_CLASSE = 1;
    private static final Integer UPDATED_CLASSE = 2;

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
        final PlanoCurricularResource planoCurricularResource = new PlanoCurricularResource(planoCurricularService);
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
