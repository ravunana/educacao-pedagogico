package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.TesteConhecimento;
import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.domain.Professor;
import com.ravunana.educacao.pedagogico.repository.TesteConhecimentoRepository;
import com.ravunana.educacao.pedagogico.repository.search.TesteConhecimentoSearchRepository;
import com.ravunana.educacao.pedagogico.service.TesteConhecimentoService;
import com.ravunana.educacao.pedagogico.service.dto.TesteConhecimentoDTO;
import com.ravunana.educacao.pedagogico.service.mapper.TesteConhecimentoMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.ravunana.educacao.pedagogico.web.rest.TestUtil.sameInstant;
import static com.ravunana.educacao.pedagogico.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TesteConhecimentoResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class TesteConhecimentoResourceIT {

    private static final String DEFAULT_CATEGORIA = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIA = "BBBBBBBBBB";

    private static final String DEFAULT_PERIODO_LECTIVO = "AAAAAAAAAA";
    private static final String UPDATED_PERIODO_LECTIVO = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURACAO = 0;
    private static final Integer UPDATED_DURACAO = 1;

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private TesteConhecimentoRepository testeConhecimentoRepository;

    @Autowired
    private TesteConhecimentoMapper testeConhecimentoMapper;

    @Autowired
    private TesteConhecimentoService testeConhecimentoService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.TesteConhecimentoSearchRepositoryMockConfiguration
     */
    @Autowired
    private TesteConhecimentoSearchRepository mockTesteConhecimentoSearchRepository;

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

    private MockMvc restTesteConhecimentoMockMvc;

    private TesteConhecimento testeConhecimento;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TesteConhecimentoResource testeConhecimentoResource = new TesteConhecimentoResource(testeConhecimentoService);
        this.restTesteConhecimentoMockMvc = MockMvcBuilders.standaloneSetup(testeConhecimentoResource)
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
    public static TesteConhecimento createEntity(EntityManager em) {
        TesteConhecimento testeConhecimento = new TesteConhecimento()
            .categoria(DEFAULT_CATEGORIA)
            .periodoLectivo(DEFAULT_PERIODO_LECTIVO)
            .duracao(DEFAULT_DURACAO)
            .data(DEFAULT_DATA);
        // Add required entity
        PlanoCurricular planoCurricular;
        if (TestUtil.findAll(em, PlanoCurricular.class).isEmpty()) {
            planoCurricular = PlanoCurricularResourceIT.createEntity(em);
            em.persist(planoCurricular);
            em.flush();
        } else {
            planoCurricular = TestUtil.findAll(em, PlanoCurricular.class).get(0);
        }
        testeConhecimento.setCurriculo(planoCurricular);
        // Add required entity
        Turma turma;
        if (TestUtil.findAll(em, Turma.class).isEmpty()) {
            turma = TurmaResourceIT.createEntity(em);
            em.persist(turma);
            em.flush();
        } else {
            turma = TestUtil.findAll(em, Turma.class).get(0);
        }
        testeConhecimento.setTurma(turma);
        // Add required entity
        Professor professor;
        if (TestUtil.findAll(em, Professor.class).isEmpty()) {
            professor = ProfessorResourceIT.createEntity(em);
            em.persist(professor);
            em.flush();
        } else {
            professor = TestUtil.findAll(em, Professor.class).get(0);
        }
        testeConhecimento.setProfessor(professor);
        return testeConhecimento;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TesteConhecimento createUpdatedEntity(EntityManager em) {
        TesteConhecimento testeConhecimento = new TesteConhecimento()
            .categoria(UPDATED_CATEGORIA)
            .periodoLectivo(UPDATED_PERIODO_LECTIVO)
            .duracao(UPDATED_DURACAO)
            .data(UPDATED_DATA);
        // Add required entity
        PlanoCurricular planoCurricular;
        if (TestUtil.findAll(em, PlanoCurricular.class).isEmpty()) {
            planoCurricular = PlanoCurricularResourceIT.createUpdatedEntity(em);
            em.persist(planoCurricular);
            em.flush();
        } else {
            planoCurricular = TestUtil.findAll(em, PlanoCurricular.class).get(0);
        }
        testeConhecimento.setCurriculo(planoCurricular);
        // Add required entity
        Turma turma;
        if (TestUtil.findAll(em, Turma.class).isEmpty()) {
            turma = TurmaResourceIT.createUpdatedEntity(em);
            em.persist(turma);
            em.flush();
        } else {
            turma = TestUtil.findAll(em, Turma.class).get(0);
        }
        testeConhecimento.setTurma(turma);
        // Add required entity
        Professor professor;
        if (TestUtil.findAll(em, Professor.class).isEmpty()) {
            professor = ProfessorResourceIT.createUpdatedEntity(em);
            em.persist(professor);
            em.flush();
        } else {
            professor = TestUtil.findAll(em, Professor.class).get(0);
        }
        testeConhecimento.setProfessor(professor);
        return testeConhecimento;
    }

    @BeforeEach
    public void initTest() {
        testeConhecimento = createEntity(em);
    }

    @Test
    @Transactional
    public void createTesteConhecimento() throws Exception {
        int databaseSizeBeforeCreate = testeConhecimentoRepository.findAll().size();

        // Create the TesteConhecimento
        TesteConhecimentoDTO testeConhecimentoDTO = testeConhecimentoMapper.toDto(testeConhecimento);
        restTesteConhecimentoMockMvc.perform(post("/api/teste-conhecimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testeConhecimentoDTO)))
            .andExpect(status().isCreated());

        // Validate the TesteConhecimento in the database
        List<TesteConhecimento> testeConhecimentoList = testeConhecimentoRepository.findAll();
        assertThat(testeConhecimentoList).hasSize(databaseSizeBeforeCreate + 1);
        TesteConhecimento testTesteConhecimento = testeConhecimentoList.get(testeConhecimentoList.size() - 1);
        assertThat(testTesteConhecimento.getCategoria()).isEqualTo(DEFAULT_CATEGORIA);
        assertThat(testTesteConhecimento.getPeriodoLectivo()).isEqualTo(DEFAULT_PERIODO_LECTIVO);
        assertThat(testTesteConhecimento.getDuracao()).isEqualTo(DEFAULT_DURACAO);
        assertThat(testTesteConhecimento.getData()).isEqualTo(DEFAULT_DATA);

        // Validate the TesteConhecimento in Elasticsearch
        verify(mockTesteConhecimentoSearchRepository, times(1)).save(testTesteConhecimento);
    }

    @Test
    @Transactional
    public void createTesteConhecimentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = testeConhecimentoRepository.findAll().size();

        // Create the TesteConhecimento with an existing ID
        testeConhecimento.setId(1L);
        TesteConhecimentoDTO testeConhecimentoDTO = testeConhecimentoMapper.toDto(testeConhecimento);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTesteConhecimentoMockMvc.perform(post("/api/teste-conhecimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testeConhecimentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TesteConhecimento in the database
        List<TesteConhecimento> testeConhecimentoList = testeConhecimentoRepository.findAll();
        assertThat(testeConhecimentoList).hasSize(databaseSizeBeforeCreate);

        // Validate the TesteConhecimento in Elasticsearch
        verify(mockTesteConhecimentoSearchRepository, times(0)).save(testeConhecimento);
    }


    @Test
    @Transactional
    public void checkCategoriaIsRequired() throws Exception {
        int databaseSizeBeforeTest = testeConhecimentoRepository.findAll().size();
        // set the field null
        testeConhecimento.setCategoria(null);

        // Create the TesteConhecimento, which fails.
        TesteConhecimentoDTO testeConhecimentoDTO = testeConhecimentoMapper.toDto(testeConhecimento);

        restTesteConhecimentoMockMvc.perform(post("/api/teste-conhecimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testeConhecimentoDTO)))
            .andExpect(status().isBadRequest());

        List<TesteConhecimento> testeConhecimentoList = testeConhecimentoRepository.findAll();
        assertThat(testeConhecimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPeriodoLectivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = testeConhecimentoRepository.findAll().size();
        // set the field null
        testeConhecimento.setPeriodoLectivo(null);

        // Create the TesteConhecimento, which fails.
        TesteConhecimentoDTO testeConhecimentoDTO = testeConhecimentoMapper.toDto(testeConhecimento);

        restTesteConhecimentoMockMvc.perform(post("/api/teste-conhecimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testeConhecimentoDTO)))
            .andExpect(status().isBadRequest());

        List<TesteConhecimento> testeConhecimentoList = testeConhecimentoRepository.findAll();
        assertThat(testeConhecimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDuracaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = testeConhecimentoRepository.findAll().size();
        // set the field null
        testeConhecimento.setDuracao(null);

        // Create the TesteConhecimento, which fails.
        TesteConhecimentoDTO testeConhecimentoDTO = testeConhecimentoMapper.toDto(testeConhecimento);

        restTesteConhecimentoMockMvc.perform(post("/api/teste-conhecimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testeConhecimentoDTO)))
            .andExpect(status().isBadRequest());

        List<TesteConhecimento> testeConhecimentoList = testeConhecimentoRepository.findAll();
        assertThat(testeConhecimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTesteConhecimentos() throws Exception {
        // Initialize the database
        testeConhecimentoRepository.saveAndFlush(testeConhecimento);

        // Get all the testeConhecimentoList
        restTesteConhecimentoMockMvc.perform(get("/api/teste-conhecimentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testeConhecimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA)))
            .andExpect(jsonPath("$.[*].periodoLectivo").value(hasItem(DEFAULT_PERIODO_LECTIVO)))
            .andExpect(jsonPath("$.[*].duracao").value(hasItem(DEFAULT_DURACAO)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))));
    }
    
    @Test
    @Transactional
    public void getTesteConhecimento() throws Exception {
        // Initialize the database
        testeConhecimentoRepository.saveAndFlush(testeConhecimento);

        // Get the testeConhecimento
        restTesteConhecimentoMockMvc.perform(get("/api/teste-conhecimentos/{id}", testeConhecimento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(testeConhecimento.getId().intValue()))
            .andExpect(jsonPath("$.categoria").value(DEFAULT_CATEGORIA))
            .andExpect(jsonPath("$.periodoLectivo").value(DEFAULT_PERIODO_LECTIVO))
            .andExpect(jsonPath("$.duracao").value(DEFAULT_DURACAO))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)));
    }

    @Test
    @Transactional
    public void getNonExistingTesteConhecimento() throws Exception {
        // Get the testeConhecimento
        restTesteConhecimentoMockMvc.perform(get("/api/teste-conhecimentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTesteConhecimento() throws Exception {
        // Initialize the database
        testeConhecimentoRepository.saveAndFlush(testeConhecimento);

        int databaseSizeBeforeUpdate = testeConhecimentoRepository.findAll().size();

        // Update the testeConhecimento
        TesteConhecimento updatedTesteConhecimento = testeConhecimentoRepository.findById(testeConhecimento.getId()).get();
        // Disconnect from session so that the updates on updatedTesteConhecimento are not directly saved in db
        em.detach(updatedTesteConhecimento);
        updatedTesteConhecimento
            .categoria(UPDATED_CATEGORIA)
            .periodoLectivo(UPDATED_PERIODO_LECTIVO)
            .duracao(UPDATED_DURACAO)
            .data(UPDATED_DATA);
        TesteConhecimentoDTO testeConhecimentoDTO = testeConhecimentoMapper.toDto(updatedTesteConhecimento);

        restTesteConhecimentoMockMvc.perform(put("/api/teste-conhecimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testeConhecimentoDTO)))
            .andExpect(status().isOk());

        // Validate the TesteConhecimento in the database
        List<TesteConhecimento> testeConhecimentoList = testeConhecimentoRepository.findAll();
        assertThat(testeConhecimentoList).hasSize(databaseSizeBeforeUpdate);
        TesteConhecimento testTesteConhecimento = testeConhecimentoList.get(testeConhecimentoList.size() - 1);
        assertThat(testTesteConhecimento.getCategoria()).isEqualTo(UPDATED_CATEGORIA);
        assertThat(testTesteConhecimento.getPeriodoLectivo()).isEqualTo(UPDATED_PERIODO_LECTIVO);
        assertThat(testTesteConhecimento.getDuracao()).isEqualTo(UPDATED_DURACAO);
        assertThat(testTesteConhecimento.getData()).isEqualTo(UPDATED_DATA);

        // Validate the TesteConhecimento in Elasticsearch
        verify(mockTesteConhecimentoSearchRepository, times(1)).save(testTesteConhecimento);
    }

    @Test
    @Transactional
    public void updateNonExistingTesteConhecimento() throws Exception {
        int databaseSizeBeforeUpdate = testeConhecimentoRepository.findAll().size();

        // Create the TesteConhecimento
        TesteConhecimentoDTO testeConhecimentoDTO = testeConhecimentoMapper.toDto(testeConhecimento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTesteConhecimentoMockMvc.perform(put("/api/teste-conhecimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testeConhecimentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TesteConhecimento in the database
        List<TesteConhecimento> testeConhecimentoList = testeConhecimentoRepository.findAll();
        assertThat(testeConhecimentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TesteConhecimento in Elasticsearch
        verify(mockTesteConhecimentoSearchRepository, times(0)).save(testeConhecimento);
    }

    @Test
    @Transactional
    public void deleteTesteConhecimento() throws Exception {
        // Initialize the database
        testeConhecimentoRepository.saveAndFlush(testeConhecimento);

        int databaseSizeBeforeDelete = testeConhecimentoRepository.findAll().size();

        // Delete the testeConhecimento
        restTesteConhecimentoMockMvc.perform(delete("/api/teste-conhecimentos/{id}", testeConhecimento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TesteConhecimento> testeConhecimentoList = testeConhecimentoRepository.findAll();
        assertThat(testeConhecimentoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TesteConhecimento in Elasticsearch
        verify(mockTesteConhecimentoSearchRepository, times(1)).deleteById(testeConhecimento.getId());
    }

    @Test
    @Transactional
    public void searchTesteConhecimento() throws Exception {
        // Initialize the database
        testeConhecimentoRepository.saveAndFlush(testeConhecimento);
        when(mockTesteConhecimentoSearchRepository.search(queryStringQuery("id:" + testeConhecimento.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(testeConhecimento), PageRequest.of(0, 1), 1));
        // Search the testeConhecimento
        restTesteConhecimentoMockMvc.perform(get("/api/_search/teste-conhecimentos?query=id:" + testeConhecimento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testeConhecimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA)))
            .andExpect(jsonPath("$.[*].periodoLectivo").value(hasItem(DEFAULT_PERIODO_LECTIVO)))
            .andExpect(jsonPath("$.[*].duracao").value(hasItem(DEFAULT_DURACAO)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))));
    }
}
