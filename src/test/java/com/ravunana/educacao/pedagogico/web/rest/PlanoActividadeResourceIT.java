package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.PlanoActividade;
import com.ravunana.educacao.pedagogico.domain.Curso;
import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.repository.PlanoActividadeRepository;
import com.ravunana.educacao.pedagogico.repository.search.PlanoActividadeSearchRepository;
import com.ravunana.educacao.pedagogico.service.PlanoActividadeService;
import com.ravunana.educacao.pedagogico.service.dto.PlanoActividadeDTO;
import com.ravunana.educacao.pedagogico.service.mapper.PlanoActividadeMapper;
import com.ravunana.educacao.pedagogico.web.rest.errors.ExceptionTranslator;
import com.ravunana.educacao.pedagogico.service.dto.PlanoActividadeCriteria;
import com.ravunana.educacao.pedagogico.service.PlanoActividadeQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PlanoActividadeResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class PlanoActividadeResourceIT {

    private static final Integer DEFAULT_NUMERO_ACTIVIDADE = 1;
    private static final Integer UPDATED_NUMERO_ACTIVIDADE = 2;
    private static final Integer SMALLER_NUMERO_ACTIVIDADE = 1 - 1;

    private static final String DEFAULT_ATIVIDADE = "AAAAAAAAAA";
    private static final String UPDATED_ATIVIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECTIVOS = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIVOS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_ATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_RESPONSAVEL = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSAVEL = "BBBBBBBBBB";

    private static final String DEFAULT_LOCAL = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final String DEFAULT_PARTICIPANTES = "AAAAAAAAAA";
    private static final String UPDATED_PARTICIPANTES = "BBBBBBBBBB";

    private static final String DEFAULT_CO_RESPONSAVEL = "AAAAAAAAAA";
    private static final String UPDATED_CO_RESPONSAVEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANO_LECTIVO = 1;
    private static final Integer UPDATED_ANO_LECTIVO = 2;
    private static final Integer SMALLER_ANO_LECTIVO = 1 - 1;

    private static final String DEFAULT_STATUS_ACTIVIDADE = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_ACTIVIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_PERIODO_LECTIVO = "AAAAAAAAAA";
    private static final String UPDATED_PERIODO_LECTIVO = "BBBBBBBBBB";

    private static final String DEFAULT_TURNO = "AAAAAAAAAA";
    private static final String UPDATED_TURNO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CLASSE = 1;
    private static final Integer UPDATED_CLASSE = 2;
    private static final Integer SMALLER_CLASSE = 1 - 1;

    @Autowired
    private PlanoActividadeRepository planoActividadeRepository;

    @Autowired
    private PlanoActividadeMapper planoActividadeMapper;

    @Autowired
    private PlanoActividadeService planoActividadeService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.PlanoActividadeSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlanoActividadeSearchRepository mockPlanoActividadeSearchRepository;

    @Autowired
    private PlanoActividadeQueryService planoActividadeQueryService;

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

    private MockMvc restPlanoActividadeMockMvc;

    private PlanoActividade planoActividade;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanoActividadeResource planoActividadeResource = new PlanoActividadeResource(planoActividadeService, planoActividadeQueryService);
        this.restPlanoActividadeMockMvc = MockMvcBuilders.standaloneSetup(planoActividadeResource)
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
    public static PlanoActividade createEntity(EntityManager em) {
        PlanoActividade planoActividade = new PlanoActividade()
            .numeroActividade(DEFAULT_NUMERO_ACTIVIDADE)
            .atividade(DEFAULT_ATIVIDADE)
            .objectivos(DEFAULT_OBJECTIVOS)
            .de(DEFAULT_DE)
            .ate(DEFAULT_ATE)
            .responsavel(DEFAULT_RESPONSAVEL)
            .local(DEFAULT_LOCAL)
            .observacao(DEFAULT_OBSERVACAO)
            .participantes(DEFAULT_PARTICIPANTES)
            .coResponsavel(DEFAULT_CO_RESPONSAVEL)
            .anoLectivo(DEFAULT_ANO_LECTIVO)
            .statusActividade(DEFAULT_STATUS_ACTIVIDADE)
            .periodoLectivo(DEFAULT_PERIODO_LECTIVO)
            .turno(DEFAULT_TURNO)
            .classe(DEFAULT_CLASSE);
        return planoActividade;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanoActividade createUpdatedEntity(EntityManager em) {
        PlanoActividade planoActividade = new PlanoActividade()
            .numeroActividade(UPDATED_NUMERO_ACTIVIDADE)
            .atividade(UPDATED_ATIVIDADE)
            .objectivos(UPDATED_OBJECTIVOS)
            .de(UPDATED_DE)
            .ate(UPDATED_ATE)
            .responsavel(UPDATED_RESPONSAVEL)
            .local(UPDATED_LOCAL)
            .observacao(UPDATED_OBSERVACAO)
            .participantes(UPDATED_PARTICIPANTES)
            .coResponsavel(UPDATED_CO_RESPONSAVEL)
            .anoLectivo(UPDATED_ANO_LECTIVO)
            .statusActividade(UPDATED_STATUS_ACTIVIDADE)
            .periodoLectivo(UPDATED_PERIODO_LECTIVO)
            .turno(UPDATED_TURNO)
            .classe(UPDATED_CLASSE);
        return planoActividade;
    }

    @BeforeEach
    public void initTest() {
        planoActividade = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanoActividade() throws Exception {
        int databaseSizeBeforeCreate = planoActividadeRepository.findAll().size();

        // Create the PlanoActividade
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);
        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isCreated());

        // Validate the PlanoActividade in the database
        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeCreate + 1);
        PlanoActividade testPlanoActividade = planoActividadeList.get(planoActividadeList.size() - 1);
        assertThat(testPlanoActividade.getNumeroActividade()).isEqualTo(DEFAULT_NUMERO_ACTIVIDADE);
        assertThat(testPlanoActividade.getAtividade()).isEqualTo(DEFAULT_ATIVIDADE);
        assertThat(testPlanoActividade.getObjectivos()).isEqualTo(DEFAULT_OBJECTIVOS);
        assertThat(testPlanoActividade.getDe()).isEqualTo(DEFAULT_DE);
        assertThat(testPlanoActividade.getAte()).isEqualTo(DEFAULT_ATE);
        assertThat(testPlanoActividade.getResponsavel()).isEqualTo(DEFAULT_RESPONSAVEL);
        assertThat(testPlanoActividade.getLocal()).isEqualTo(DEFAULT_LOCAL);
        assertThat(testPlanoActividade.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testPlanoActividade.getParticipantes()).isEqualTo(DEFAULT_PARTICIPANTES);
        assertThat(testPlanoActividade.getCoResponsavel()).isEqualTo(DEFAULT_CO_RESPONSAVEL);
        assertThat(testPlanoActividade.getAnoLectivo()).isEqualTo(DEFAULT_ANO_LECTIVO);
        assertThat(testPlanoActividade.getStatusActividade()).isEqualTo(DEFAULT_STATUS_ACTIVIDADE);
        assertThat(testPlanoActividade.getPeriodoLectivo()).isEqualTo(DEFAULT_PERIODO_LECTIVO);
        assertThat(testPlanoActividade.getTurno()).isEqualTo(DEFAULT_TURNO);
        assertThat(testPlanoActividade.getClasse()).isEqualTo(DEFAULT_CLASSE);

        // Validate the PlanoActividade in Elasticsearch
        verify(mockPlanoActividadeSearchRepository, times(1)).save(testPlanoActividade);
    }

    @Test
    @Transactional
    public void createPlanoActividadeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planoActividadeRepository.findAll().size();

        // Create the PlanoActividade with an existing ID
        planoActividade.setId(1L);
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanoActividade in the database
        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeCreate);

        // Validate the PlanoActividade in Elasticsearch
        verify(mockPlanoActividadeSearchRepository, times(0)).save(planoActividade);
    }


    @Test
    @Transactional
    public void checkAtividadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoActividadeRepository.findAll().size();
        // set the field null
        planoActividade.setAtividade(null);

        // Create the PlanoActividade, which fails.
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoActividadeRepository.findAll().size();
        // set the field null
        planoActividade.setDe(null);

        // Create the PlanoActividade, which fails.
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAteIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoActividadeRepository.findAll().size();
        // set the field null
        planoActividade.setAte(null);

        // Create the PlanoActividade, which fails.
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResponsavelIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoActividadeRepository.findAll().size();
        // set the field null
        planoActividade.setResponsavel(null);

        // Create the PlanoActividade, which fails.
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnoLectivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoActividadeRepository.findAll().size();
        // set the field null
        planoActividade.setAnoLectivo(null);

        // Create the PlanoActividade, which fails.
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlanoActividades() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList
        restPlanoActividadeMockMvc.perform(get("/api/plano-actividades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planoActividade.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroActividade").value(hasItem(DEFAULT_NUMERO_ACTIVIDADE)))
            .andExpect(jsonPath("$.[*].atividade").value(hasItem(DEFAULT_ATIVIDADE)))
            .andExpect(jsonPath("$.[*].objectivos").value(hasItem(DEFAULT_OBJECTIVOS.toString())))
            .andExpect(jsonPath("$.[*].de").value(hasItem(DEFAULT_DE.toString())))
            .andExpect(jsonPath("$.[*].ate").value(hasItem(DEFAULT_ATE.toString())))
            .andExpect(jsonPath("$.[*].responsavel").value(hasItem(DEFAULT_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].participantes").value(hasItem(DEFAULT_PARTICIPANTES)))
            .andExpect(jsonPath("$.[*].coResponsavel").value(hasItem(DEFAULT_CO_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].statusActividade").value(hasItem(DEFAULT_STATUS_ACTIVIDADE)))
            .andExpect(jsonPath("$.[*].periodoLectivo").value(hasItem(DEFAULT_PERIODO_LECTIVO)))
            .andExpect(jsonPath("$.[*].turno").value(hasItem(DEFAULT_TURNO)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)));
    }
    
    @Test
    @Transactional
    public void getPlanoActividade() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get the planoActividade
        restPlanoActividadeMockMvc.perform(get("/api/plano-actividades/{id}", planoActividade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planoActividade.getId().intValue()))
            .andExpect(jsonPath("$.numeroActividade").value(DEFAULT_NUMERO_ACTIVIDADE))
            .andExpect(jsonPath("$.atividade").value(DEFAULT_ATIVIDADE))
            .andExpect(jsonPath("$.objectivos").value(DEFAULT_OBJECTIVOS.toString()))
            .andExpect(jsonPath("$.de").value(DEFAULT_DE.toString()))
            .andExpect(jsonPath("$.ate").value(DEFAULT_ATE.toString()))
            .andExpect(jsonPath("$.responsavel").value(DEFAULT_RESPONSAVEL))
            .andExpect(jsonPath("$.local").value(DEFAULT_LOCAL))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO.toString()))
            .andExpect(jsonPath("$.participantes").value(DEFAULT_PARTICIPANTES))
            .andExpect(jsonPath("$.coResponsavel").value(DEFAULT_CO_RESPONSAVEL))
            .andExpect(jsonPath("$.anoLectivo").value(DEFAULT_ANO_LECTIVO))
            .andExpect(jsonPath("$.statusActividade").value(DEFAULT_STATUS_ACTIVIDADE))
            .andExpect(jsonPath("$.periodoLectivo").value(DEFAULT_PERIODO_LECTIVO))
            .andExpect(jsonPath("$.turno").value(DEFAULT_TURNO))
            .andExpect(jsonPath("$.classe").value(DEFAULT_CLASSE));
    }


    @Test
    @Transactional
    public void getPlanoActividadesByIdFiltering() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        Long id = planoActividade.getId();

        defaultPlanoActividadeShouldBeFound("id.equals=" + id);
        defaultPlanoActividadeShouldNotBeFound("id.notEquals=" + id);

        defaultPlanoActividadeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlanoActividadeShouldNotBeFound("id.greaterThan=" + id);

        defaultPlanoActividadeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlanoActividadeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByNumeroActividadeIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where numeroActividade equals to DEFAULT_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldBeFound("numeroActividade.equals=" + DEFAULT_NUMERO_ACTIVIDADE);

        // Get all the planoActividadeList where numeroActividade equals to UPDATED_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldNotBeFound("numeroActividade.equals=" + UPDATED_NUMERO_ACTIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByNumeroActividadeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where numeroActividade not equals to DEFAULT_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldNotBeFound("numeroActividade.notEquals=" + DEFAULT_NUMERO_ACTIVIDADE);

        // Get all the planoActividadeList where numeroActividade not equals to UPDATED_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldBeFound("numeroActividade.notEquals=" + UPDATED_NUMERO_ACTIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByNumeroActividadeIsInShouldWork() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where numeroActividade in DEFAULT_NUMERO_ACTIVIDADE or UPDATED_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldBeFound("numeroActividade.in=" + DEFAULT_NUMERO_ACTIVIDADE + "," + UPDATED_NUMERO_ACTIVIDADE);

        // Get all the planoActividadeList where numeroActividade equals to UPDATED_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldNotBeFound("numeroActividade.in=" + UPDATED_NUMERO_ACTIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByNumeroActividadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where numeroActividade is not null
        defaultPlanoActividadeShouldBeFound("numeroActividade.specified=true");

        // Get all the planoActividadeList where numeroActividade is null
        defaultPlanoActividadeShouldNotBeFound("numeroActividade.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByNumeroActividadeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where numeroActividade is greater than or equal to DEFAULT_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldBeFound("numeroActividade.greaterThanOrEqual=" + DEFAULT_NUMERO_ACTIVIDADE);

        // Get all the planoActividadeList where numeroActividade is greater than or equal to UPDATED_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldNotBeFound("numeroActividade.greaterThanOrEqual=" + UPDATED_NUMERO_ACTIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByNumeroActividadeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where numeroActividade is less than or equal to DEFAULT_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldBeFound("numeroActividade.lessThanOrEqual=" + DEFAULT_NUMERO_ACTIVIDADE);

        // Get all the planoActividadeList where numeroActividade is less than or equal to SMALLER_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldNotBeFound("numeroActividade.lessThanOrEqual=" + SMALLER_NUMERO_ACTIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByNumeroActividadeIsLessThanSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where numeroActividade is less than DEFAULT_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldNotBeFound("numeroActividade.lessThan=" + DEFAULT_NUMERO_ACTIVIDADE);

        // Get all the planoActividadeList where numeroActividade is less than UPDATED_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldBeFound("numeroActividade.lessThan=" + UPDATED_NUMERO_ACTIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByNumeroActividadeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where numeroActividade is greater than DEFAULT_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldNotBeFound("numeroActividade.greaterThan=" + DEFAULT_NUMERO_ACTIVIDADE);

        // Get all the planoActividadeList where numeroActividade is greater than SMALLER_NUMERO_ACTIVIDADE
        defaultPlanoActividadeShouldBeFound("numeroActividade.greaterThan=" + SMALLER_NUMERO_ACTIVIDADE);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByAtividadeIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where atividade equals to DEFAULT_ATIVIDADE
        defaultPlanoActividadeShouldBeFound("atividade.equals=" + DEFAULT_ATIVIDADE);

        // Get all the planoActividadeList where atividade equals to UPDATED_ATIVIDADE
        defaultPlanoActividadeShouldNotBeFound("atividade.equals=" + UPDATED_ATIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAtividadeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where atividade not equals to DEFAULT_ATIVIDADE
        defaultPlanoActividadeShouldNotBeFound("atividade.notEquals=" + DEFAULT_ATIVIDADE);

        // Get all the planoActividadeList where atividade not equals to UPDATED_ATIVIDADE
        defaultPlanoActividadeShouldBeFound("atividade.notEquals=" + UPDATED_ATIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAtividadeIsInShouldWork() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where atividade in DEFAULT_ATIVIDADE or UPDATED_ATIVIDADE
        defaultPlanoActividadeShouldBeFound("atividade.in=" + DEFAULT_ATIVIDADE + "," + UPDATED_ATIVIDADE);

        // Get all the planoActividadeList where atividade equals to UPDATED_ATIVIDADE
        defaultPlanoActividadeShouldNotBeFound("atividade.in=" + UPDATED_ATIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAtividadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where atividade is not null
        defaultPlanoActividadeShouldBeFound("atividade.specified=true");

        // Get all the planoActividadeList where atividade is null
        defaultPlanoActividadeShouldNotBeFound("atividade.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlanoActividadesByAtividadeContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where atividade contains DEFAULT_ATIVIDADE
        defaultPlanoActividadeShouldBeFound("atividade.contains=" + DEFAULT_ATIVIDADE);

        // Get all the planoActividadeList where atividade contains UPDATED_ATIVIDADE
        defaultPlanoActividadeShouldNotBeFound("atividade.contains=" + UPDATED_ATIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAtividadeNotContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where atividade does not contain DEFAULT_ATIVIDADE
        defaultPlanoActividadeShouldNotBeFound("atividade.doesNotContain=" + DEFAULT_ATIVIDADE);

        // Get all the planoActividadeList where atividade does not contain UPDATED_ATIVIDADE
        defaultPlanoActividadeShouldBeFound("atividade.doesNotContain=" + UPDATED_ATIVIDADE);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByDeIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where de equals to DEFAULT_DE
        defaultPlanoActividadeShouldBeFound("de.equals=" + DEFAULT_DE);

        // Get all the planoActividadeList where de equals to UPDATED_DE
        defaultPlanoActividadeShouldNotBeFound("de.equals=" + UPDATED_DE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByDeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where de not equals to DEFAULT_DE
        defaultPlanoActividadeShouldNotBeFound("de.notEquals=" + DEFAULT_DE);

        // Get all the planoActividadeList where de not equals to UPDATED_DE
        defaultPlanoActividadeShouldBeFound("de.notEquals=" + UPDATED_DE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByDeIsInShouldWork() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where de in DEFAULT_DE or UPDATED_DE
        defaultPlanoActividadeShouldBeFound("de.in=" + DEFAULT_DE + "," + UPDATED_DE);

        // Get all the planoActividadeList where de equals to UPDATED_DE
        defaultPlanoActividadeShouldNotBeFound("de.in=" + UPDATED_DE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByDeIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where de is not null
        defaultPlanoActividadeShouldBeFound("de.specified=true");

        // Get all the planoActividadeList where de is null
        defaultPlanoActividadeShouldNotBeFound("de.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByDeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where de is greater than or equal to DEFAULT_DE
        defaultPlanoActividadeShouldBeFound("de.greaterThanOrEqual=" + DEFAULT_DE);

        // Get all the planoActividadeList where de is greater than or equal to UPDATED_DE
        defaultPlanoActividadeShouldNotBeFound("de.greaterThanOrEqual=" + UPDATED_DE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByDeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where de is less than or equal to DEFAULT_DE
        defaultPlanoActividadeShouldBeFound("de.lessThanOrEqual=" + DEFAULT_DE);

        // Get all the planoActividadeList where de is less than or equal to SMALLER_DE
        defaultPlanoActividadeShouldNotBeFound("de.lessThanOrEqual=" + SMALLER_DE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByDeIsLessThanSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where de is less than DEFAULT_DE
        defaultPlanoActividadeShouldNotBeFound("de.lessThan=" + DEFAULT_DE);

        // Get all the planoActividadeList where de is less than UPDATED_DE
        defaultPlanoActividadeShouldBeFound("de.lessThan=" + UPDATED_DE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByDeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where de is greater than DEFAULT_DE
        defaultPlanoActividadeShouldNotBeFound("de.greaterThan=" + DEFAULT_DE);

        // Get all the planoActividadeList where de is greater than SMALLER_DE
        defaultPlanoActividadeShouldBeFound("de.greaterThan=" + SMALLER_DE);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByAteIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where ate equals to DEFAULT_ATE
        defaultPlanoActividadeShouldBeFound("ate.equals=" + DEFAULT_ATE);

        // Get all the planoActividadeList where ate equals to UPDATED_ATE
        defaultPlanoActividadeShouldNotBeFound("ate.equals=" + UPDATED_ATE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where ate not equals to DEFAULT_ATE
        defaultPlanoActividadeShouldNotBeFound("ate.notEquals=" + DEFAULT_ATE);

        // Get all the planoActividadeList where ate not equals to UPDATED_ATE
        defaultPlanoActividadeShouldBeFound("ate.notEquals=" + UPDATED_ATE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAteIsInShouldWork() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where ate in DEFAULT_ATE or UPDATED_ATE
        defaultPlanoActividadeShouldBeFound("ate.in=" + DEFAULT_ATE + "," + UPDATED_ATE);

        // Get all the planoActividadeList where ate equals to UPDATED_ATE
        defaultPlanoActividadeShouldNotBeFound("ate.in=" + UPDATED_ATE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAteIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where ate is not null
        defaultPlanoActividadeShouldBeFound("ate.specified=true");

        // Get all the planoActividadeList where ate is null
        defaultPlanoActividadeShouldNotBeFound("ate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where ate is greater than or equal to DEFAULT_ATE
        defaultPlanoActividadeShouldBeFound("ate.greaterThanOrEqual=" + DEFAULT_ATE);

        // Get all the planoActividadeList where ate is greater than or equal to UPDATED_ATE
        defaultPlanoActividadeShouldNotBeFound("ate.greaterThanOrEqual=" + UPDATED_ATE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where ate is less than or equal to DEFAULT_ATE
        defaultPlanoActividadeShouldBeFound("ate.lessThanOrEqual=" + DEFAULT_ATE);

        // Get all the planoActividadeList where ate is less than or equal to SMALLER_ATE
        defaultPlanoActividadeShouldNotBeFound("ate.lessThanOrEqual=" + SMALLER_ATE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAteIsLessThanSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where ate is less than DEFAULT_ATE
        defaultPlanoActividadeShouldNotBeFound("ate.lessThan=" + DEFAULT_ATE);

        // Get all the planoActividadeList where ate is less than UPDATED_ATE
        defaultPlanoActividadeShouldBeFound("ate.lessThan=" + UPDATED_ATE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where ate is greater than DEFAULT_ATE
        defaultPlanoActividadeShouldNotBeFound("ate.greaterThan=" + DEFAULT_ATE);

        // Get all the planoActividadeList where ate is greater than SMALLER_ATE
        defaultPlanoActividadeShouldBeFound("ate.greaterThan=" + SMALLER_ATE);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByResponsavelIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where responsavel equals to DEFAULT_RESPONSAVEL
        defaultPlanoActividadeShouldBeFound("responsavel.equals=" + DEFAULT_RESPONSAVEL);

        // Get all the planoActividadeList where responsavel equals to UPDATED_RESPONSAVEL
        defaultPlanoActividadeShouldNotBeFound("responsavel.equals=" + UPDATED_RESPONSAVEL);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByResponsavelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where responsavel not equals to DEFAULT_RESPONSAVEL
        defaultPlanoActividadeShouldNotBeFound("responsavel.notEquals=" + DEFAULT_RESPONSAVEL);

        // Get all the planoActividadeList where responsavel not equals to UPDATED_RESPONSAVEL
        defaultPlanoActividadeShouldBeFound("responsavel.notEquals=" + UPDATED_RESPONSAVEL);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByResponsavelIsInShouldWork() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where responsavel in DEFAULT_RESPONSAVEL or UPDATED_RESPONSAVEL
        defaultPlanoActividadeShouldBeFound("responsavel.in=" + DEFAULT_RESPONSAVEL + "," + UPDATED_RESPONSAVEL);

        // Get all the planoActividadeList where responsavel equals to UPDATED_RESPONSAVEL
        defaultPlanoActividadeShouldNotBeFound("responsavel.in=" + UPDATED_RESPONSAVEL);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByResponsavelIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where responsavel is not null
        defaultPlanoActividadeShouldBeFound("responsavel.specified=true");

        // Get all the planoActividadeList where responsavel is null
        defaultPlanoActividadeShouldNotBeFound("responsavel.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlanoActividadesByResponsavelContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where responsavel contains DEFAULT_RESPONSAVEL
        defaultPlanoActividadeShouldBeFound("responsavel.contains=" + DEFAULT_RESPONSAVEL);

        // Get all the planoActividadeList where responsavel contains UPDATED_RESPONSAVEL
        defaultPlanoActividadeShouldNotBeFound("responsavel.contains=" + UPDATED_RESPONSAVEL);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByResponsavelNotContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where responsavel does not contain DEFAULT_RESPONSAVEL
        defaultPlanoActividadeShouldNotBeFound("responsavel.doesNotContain=" + DEFAULT_RESPONSAVEL);

        // Get all the planoActividadeList where responsavel does not contain UPDATED_RESPONSAVEL
        defaultPlanoActividadeShouldBeFound("responsavel.doesNotContain=" + UPDATED_RESPONSAVEL);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByLocalIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where local equals to DEFAULT_LOCAL
        defaultPlanoActividadeShouldBeFound("local.equals=" + DEFAULT_LOCAL);

        // Get all the planoActividadeList where local equals to UPDATED_LOCAL
        defaultPlanoActividadeShouldNotBeFound("local.equals=" + UPDATED_LOCAL);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByLocalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where local not equals to DEFAULT_LOCAL
        defaultPlanoActividadeShouldNotBeFound("local.notEquals=" + DEFAULT_LOCAL);

        // Get all the planoActividadeList where local not equals to UPDATED_LOCAL
        defaultPlanoActividadeShouldBeFound("local.notEquals=" + UPDATED_LOCAL);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByLocalIsInShouldWork() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where local in DEFAULT_LOCAL or UPDATED_LOCAL
        defaultPlanoActividadeShouldBeFound("local.in=" + DEFAULT_LOCAL + "," + UPDATED_LOCAL);

        // Get all the planoActividadeList where local equals to UPDATED_LOCAL
        defaultPlanoActividadeShouldNotBeFound("local.in=" + UPDATED_LOCAL);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByLocalIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where local is not null
        defaultPlanoActividadeShouldBeFound("local.specified=true");

        // Get all the planoActividadeList where local is null
        defaultPlanoActividadeShouldNotBeFound("local.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlanoActividadesByLocalContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where local contains DEFAULT_LOCAL
        defaultPlanoActividadeShouldBeFound("local.contains=" + DEFAULT_LOCAL);

        // Get all the planoActividadeList where local contains UPDATED_LOCAL
        defaultPlanoActividadeShouldNotBeFound("local.contains=" + UPDATED_LOCAL);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByLocalNotContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where local does not contain DEFAULT_LOCAL
        defaultPlanoActividadeShouldNotBeFound("local.doesNotContain=" + DEFAULT_LOCAL);

        // Get all the planoActividadeList where local does not contain UPDATED_LOCAL
        defaultPlanoActividadeShouldBeFound("local.doesNotContain=" + UPDATED_LOCAL);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByParticipantesIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where participantes equals to DEFAULT_PARTICIPANTES
        defaultPlanoActividadeShouldBeFound("participantes.equals=" + DEFAULT_PARTICIPANTES);

        // Get all the planoActividadeList where participantes equals to UPDATED_PARTICIPANTES
        defaultPlanoActividadeShouldNotBeFound("participantes.equals=" + UPDATED_PARTICIPANTES);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByParticipantesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where participantes not equals to DEFAULT_PARTICIPANTES
        defaultPlanoActividadeShouldNotBeFound("participantes.notEquals=" + DEFAULT_PARTICIPANTES);

        // Get all the planoActividadeList where participantes not equals to UPDATED_PARTICIPANTES
        defaultPlanoActividadeShouldBeFound("participantes.notEquals=" + UPDATED_PARTICIPANTES);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByParticipantesIsInShouldWork() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where participantes in DEFAULT_PARTICIPANTES or UPDATED_PARTICIPANTES
        defaultPlanoActividadeShouldBeFound("participantes.in=" + DEFAULT_PARTICIPANTES + "," + UPDATED_PARTICIPANTES);

        // Get all the planoActividadeList where participantes equals to UPDATED_PARTICIPANTES
        defaultPlanoActividadeShouldNotBeFound("participantes.in=" + UPDATED_PARTICIPANTES);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByParticipantesIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where participantes is not null
        defaultPlanoActividadeShouldBeFound("participantes.specified=true");

        // Get all the planoActividadeList where participantes is null
        defaultPlanoActividadeShouldNotBeFound("participantes.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlanoActividadesByParticipantesContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where participantes contains DEFAULT_PARTICIPANTES
        defaultPlanoActividadeShouldBeFound("participantes.contains=" + DEFAULT_PARTICIPANTES);

        // Get all the planoActividadeList where participantes contains UPDATED_PARTICIPANTES
        defaultPlanoActividadeShouldNotBeFound("participantes.contains=" + UPDATED_PARTICIPANTES);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByParticipantesNotContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where participantes does not contain DEFAULT_PARTICIPANTES
        defaultPlanoActividadeShouldNotBeFound("participantes.doesNotContain=" + DEFAULT_PARTICIPANTES);

        // Get all the planoActividadeList where participantes does not contain UPDATED_PARTICIPANTES
        defaultPlanoActividadeShouldBeFound("participantes.doesNotContain=" + UPDATED_PARTICIPANTES);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByCoResponsavelIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where coResponsavel equals to DEFAULT_CO_RESPONSAVEL
        defaultPlanoActividadeShouldBeFound("coResponsavel.equals=" + DEFAULT_CO_RESPONSAVEL);

        // Get all the planoActividadeList where coResponsavel equals to UPDATED_CO_RESPONSAVEL
        defaultPlanoActividadeShouldNotBeFound("coResponsavel.equals=" + UPDATED_CO_RESPONSAVEL);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByCoResponsavelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where coResponsavel not equals to DEFAULT_CO_RESPONSAVEL
        defaultPlanoActividadeShouldNotBeFound("coResponsavel.notEquals=" + DEFAULT_CO_RESPONSAVEL);

        // Get all the planoActividadeList where coResponsavel not equals to UPDATED_CO_RESPONSAVEL
        defaultPlanoActividadeShouldBeFound("coResponsavel.notEquals=" + UPDATED_CO_RESPONSAVEL);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByCoResponsavelIsInShouldWork() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where coResponsavel in DEFAULT_CO_RESPONSAVEL or UPDATED_CO_RESPONSAVEL
        defaultPlanoActividadeShouldBeFound("coResponsavel.in=" + DEFAULT_CO_RESPONSAVEL + "," + UPDATED_CO_RESPONSAVEL);

        // Get all the planoActividadeList where coResponsavel equals to UPDATED_CO_RESPONSAVEL
        defaultPlanoActividadeShouldNotBeFound("coResponsavel.in=" + UPDATED_CO_RESPONSAVEL);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByCoResponsavelIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where coResponsavel is not null
        defaultPlanoActividadeShouldBeFound("coResponsavel.specified=true");

        // Get all the planoActividadeList where coResponsavel is null
        defaultPlanoActividadeShouldNotBeFound("coResponsavel.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlanoActividadesByCoResponsavelContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where coResponsavel contains DEFAULT_CO_RESPONSAVEL
        defaultPlanoActividadeShouldBeFound("coResponsavel.contains=" + DEFAULT_CO_RESPONSAVEL);

        // Get all the planoActividadeList where coResponsavel contains UPDATED_CO_RESPONSAVEL
        defaultPlanoActividadeShouldNotBeFound("coResponsavel.contains=" + UPDATED_CO_RESPONSAVEL);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByCoResponsavelNotContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where coResponsavel does not contain DEFAULT_CO_RESPONSAVEL
        defaultPlanoActividadeShouldNotBeFound("coResponsavel.doesNotContain=" + DEFAULT_CO_RESPONSAVEL);

        // Get all the planoActividadeList where coResponsavel does not contain UPDATED_CO_RESPONSAVEL
        defaultPlanoActividadeShouldBeFound("coResponsavel.doesNotContain=" + UPDATED_CO_RESPONSAVEL);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByAnoLectivoIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where anoLectivo equals to DEFAULT_ANO_LECTIVO
        defaultPlanoActividadeShouldBeFound("anoLectivo.equals=" + DEFAULT_ANO_LECTIVO);

        // Get all the planoActividadeList where anoLectivo equals to UPDATED_ANO_LECTIVO
        defaultPlanoActividadeShouldNotBeFound("anoLectivo.equals=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAnoLectivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where anoLectivo not equals to DEFAULT_ANO_LECTIVO
        defaultPlanoActividadeShouldNotBeFound("anoLectivo.notEquals=" + DEFAULT_ANO_LECTIVO);

        // Get all the planoActividadeList where anoLectivo not equals to UPDATED_ANO_LECTIVO
        defaultPlanoActividadeShouldBeFound("anoLectivo.notEquals=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAnoLectivoIsInShouldWork() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where anoLectivo in DEFAULT_ANO_LECTIVO or UPDATED_ANO_LECTIVO
        defaultPlanoActividadeShouldBeFound("anoLectivo.in=" + DEFAULT_ANO_LECTIVO + "," + UPDATED_ANO_LECTIVO);

        // Get all the planoActividadeList where anoLectivo equals to UPDATED_ANO_LECTIVO
        defaultPlanoActividadeShouldNotBeFound("anoLectivo.in=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAnoLectivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where anoLectivo is not null
        defaultPlanoActividadeShouldBeFound("anoLectivo.specified=true");

        // Get all the planoActividadeList where anoLectivo is null
        defaultPlanoActividadeShouldNotBeFound("anoLectivo.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAnoLectivoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where anoLectivo is greater than or equal to DEFAULT_ANO_LECTIVO
        defaultPlanoActividadeShouldBeFound("anoLectivo.greaterThanOrEqual=" + DEFAULT_ANO_LECTIVO);

        // Get all the planoActividadeList where anoLectivo is greater than or equal to UPDATED_ANO_LECTIVO
        defaultPlanoActividadeShouldNotBeFound("anoLectivo.greaterThanOrEqual=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAnoLectivoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where anoLectivo is less than or equal to DEFAULT_ANO_LECTIVO
        defaultPlanoActividadeShouldBeFound("anoLectivo.lessThanOrEqual=" + DEFAULT_ANO_LECTIVO);

        // Get all the planoActividadeList where anoLectivo is less than or equal to SMALLER_ANO_LECTIVO
        defaultPlanoActividadeShouldNotBeFound("anoLectivo.lessThanOrEqual=" + SMALLER_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAnoLectivoIsLessThanSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where anoLectivo is less than DEFAULT_ANO_LECTIVO
        defaultPlanoActividadeShouldNotBeFound("anoLectivo.lessThan=" + DEFAULT_ANO_LECTIVO);

        // Get all the planoActividadeList where anoLectivo is less than UPDATED_ANO_LECTIVO
        defaultPlanoActividadeShouldBeFound("anoLectivo.lessThan=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByAnoLectivoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where anoLectivo is greater than DEFAULT_ANO_LECTIVO
        defaultPlanoActividadeShouldNotBeFound("anoLectivo.greaterThan=" + DEFAULT_ANO_LECTIVO);

        // Get all the planoActividadeList where anoLectivo is greater than SMALLER_ANO_LECTIVO
        defaultPlanoActividadeShouldBeFound("anoLectivo.greaterThan=" + SMALLER_ANO_LECTIVO);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByStatusActividadeIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where statusActividade equals to DEFAULT_STATUS_ACTIVIDADE
        defaultPlanoActividadeShouldBeFound("statusActividade.equals=" + DEFAULT_STATUS_ACTIVIDADE);

        // Get all the planoActividadeList where statusActividade equals to UPDATED_STATUS_ACTIVIDADE
        defaultPlanoActividadeShouldNotBeFound("statusActividade.equals=" + UPDATED_STATUS_ACTIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByStatusActividadeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where statusActividade not equals to DEFAULT_STATUS_ACTIVIDADE
        defaultPlanoActividadeShouldNotBeFound("statusActividade.notEquals=" + DEFAULT_STATUS_ACTIVIDADE);

        // Get all the planoActividadeList where statusActividade not equals to UPDATED_STATUS_ACTIVIDADE
        defaultPlanoActividadeShouldBeFound("statusActividade.notEquals=" + UPDATED_STATUS_ACTIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByStatusActividadeIsInShouldWork() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where statusActividade in DEFAULT_STATUS_ACTIVIDADE or UPDATED_STATUS_ACTIVIDADE
        defaultPlanoActividadeShouldBeFound("statusActividade.in=" + DEFAULT_STATUS_ACTIVIDADE + "," + UPDATED_STATUS_ACTIVIDADE);

        // Get all the planoActividadeList where statusActividade equals to UPDATED_STATUS_ACTIVIDADE
        defaultPlanoActividadeShouldNotBeFound("statusActividade.in=" + UPDATED_STATUS_ACTIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByStatusActividadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where statusActividade is not null
        defaultPlanoActividadeShouldBeFound("statusActividade.specified=true");

        // Get all the planoActividadeList where statusActividade is null
        defaultPlanoActividadeShouldNotBeFound("statusActividade.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlanoActividadesByStatusActividadeContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where statusActividade contains DEFAULT_STATUS_ACTIVIDADE
        defaultPlanoActividadeShouldBeFound("statusActividade.contains=" + DEFAULT_STATUS_ACTIVIDADE);

        // Get all the planoActividadeList where statusActividade contains UPDATED_STATUS_ACTIVIDADE
        defaultPlanoActividadeShouldNotBeFound("statusActividade.contains=" + UPDATED_STATUS_ACTIVIDADE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByStatusActividadeNotContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where statusActividade does not contain DEFAULT_STATUS_ACTIVIDADE
        defaultPlanoActividadeShouldNotBeFound("statusActividade.doesNotContain=" + DEFAULT_STATUS_ACTIVIDADE);

        // Get all the planoActividadeList where statusActividade does not contain UPDATED_STATUS_ACTIVIDADE
        defaultPlanoActividadeShouldBeFound("statusActividade.doesNotContain=" + UPDATED_STATUS_ACTIVIDADE);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByPeriodoLectivoIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where periodoLectivo equals to DEFAULT_PERIODO_LECTIVO
        defaultPlanoActividadeShouldBeFound("periodoLectivo.equals=" + DEFAULT_PERIODO_LECTIVO);

        // Get all the planoActividadeList where periodoLectivo equals to UPDATED_PERIODO_LECTIVO
        defaultPlanoActividadeShouldNotBeFound("periodoLectivo.equals=" + UPDATED_PERIODO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByPeriodoLectivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where periodoLectivo not equals to DEFAULT_PERIODO_LECTIVO
        defaultPlanoActividadeShouldNotBeFound("periodoLectivo.notEquals=" + DEFAULT_PERIODO_LECTIVO);

        // Get all the planoActividadeList where periodoLectivo not equals to UPDATED_PERIODO_LECTIVO
        defaultPlanoActividadeShouldBeFound("periodoLectivo.notEquals=" + UPDATED_PERIODO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByPeriodoLectivoIsInShouldWork() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where periodoLectivo in DEFAULT_PERIODO_LECTIVO or UPDATED_PERIODO_LECTIVO
        defaultPlanoActividadeShouldBeFound("periodoLectivo.in=" + DEFAULT_PERIODO_LECTIVO + "," + UPDATED_PERIODO_LECTIVO);

        // Get all the planoActividadeList where periodoLectivo equals to UPDATED_PERIODO_LECTIVO
        defaultPlanoActividadeShouldNotBeFound("periodoLectivo.in=" + UPDATED_PERIODO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByPeriodoLectivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where periodoLectivo is not null
        defaultPlanoActividadeShouldBeFound("periodoLectivo.specified=true");

        // Get all the planoActividadeList where periodoLectivo is null
        defaultPlanoActividadeShouldNotBeFound("periodoLectivo.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlanoActividadesByPeriodoLectivoContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where periodoLectivo contains DEFAULT_PERIODO_LECTIVO
        defaultPlanoActividadeShouldBeFound("periodoLectivo.contains=" + DEFAULT_PERIODO_LECTIVO);

        // Get all the planoActividadeList where periodoLectivo contains UPDATED_PERIODO_LECTIVO
        defaultPlanoActividadeShouldNotBeFound("periodoLectivo.contains=" + UPDATED_PERIODO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByPeriodoLectivoNotContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where periodoLectivo does not contain DEFAULT_PERIODO_LECTIVO
        defaultPlanoActividadeShouldNotBeFound("periodoLectivo.doesNotContain=" + DEFAULT_PERIODO_LECTIVO);

        // Get all the planoActividadeList where periodoLectivo does not contain UPDATED_PERIODO_LECTIVO
        defaultPlanoActividadeShouldBeFound("periodoLectivo.doesNotContain=" + UPDATED_PERIODO_LECTIVO);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByTurnoIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where turno equals to DEFAULT_TURNO
        defaultPlanoActividadeShouldBeFound("turno.equals=" + DEFAULT_TURNO);

        // Get all the planoActividadeList where turno equals to UPDATED_TURNO
        defaultPlanoActividadeShouldNotBeFound("turno.equals=" + UPDATED_TURNO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByTurnoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where turno not equals to DEFAULT_TURNO
        defaultPlanoActividadeShouldNotBeFound("turno.notEquals=" + DEFAULT_TURNO);

        // Get all the planoActividadeList where turno not equals to UPDATED_TURNO
        defaultPlanoActividadeShouldBeFound("turno.notEquals=" + UPDATED_TURNO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByTurnoIsInShouldWork() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where turno in DEFAULT_TURNO or UPDATED_TURNO
        defaultPlanoActividadeShouldBeFound("turno.in=" + DEFAULT_TURNO + "," + UPDATED_TURNO);

        // Get all the planoActividadeList where turno equals to UPDATED_TURNO
        defaultPlanoActividadeShouldNotBeFound("turno.in=" + UPDATED_TURNO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByTurnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where turno is not null
        defaultPlanoActividadeShouldBeFound("turno.specified=true");

        // Get all the planoActividadeList where turno is null
        defaultPlanoActividadeShouldNotBeFound("turno.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlanoActividadesByTurnoContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where turno contains DEFAULT_TURNO
        defaultPlanoActividadeShouldBeFound("turno.contains=" + DEFAULT_TURNO);

        // Get all the planoActividadeList where turno contains UPDATED_TURNO
        defaultPlanoActividadeShouldNotBeFound("turno.contains=" + UPDATED_TURNO);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByTurnoNotContainsSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where turno does not contain DEFAULT_TURNO
        defaultPlanoActividadeShouldNotBeFound("turno.doesNotContain=" + DEFAULT_TURNO);

        // Get all the planoActividadeList where turno does not contain UPDATED_TURNO
        defaultPlanoActividadeShouldBeFound("turno.doesNotContain=" + UPDATED_TURNO);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where classe equals to DEFAULT_CLASSE
        defaultPlanoActividadeShouldBeFound("classe.equals=" + DEFAULT_CLASSE);

        // Get all the planoActividadeList where classe equals to UPDATED_CLASSE
        defaultPlanoActividadeShouldNotBeFound("classe.equals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where classe not equals to DEFAULT_CLASSE
        defaultPlanoActividadeShouldNotBeFound("classe.notEquals=" + DEFAULT_CLASSE);

        // Get all the planoActividadeList where classe not equals to UPDATED_CLASSE
        defaultPlanoActividadeShouldBeFound("classe.notEquals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByClasseIsInShouldWork() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where classe in DEFAULT_CLASSE or UPDATED_CLASSE
        defaultPlanoActividadeShouldBeFound("classe.in=" + DEFAULT_CLASSE + "," + UPDATED_CLASSE);

        // Get all the planoActividadeList where classe equals to UPDATED_CLASSE
        defaultPlanoActividadeShouldNotBeFound("classe.in=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where classe is not null
        defaultPlanoActividadeShouldBeFound("classe.specified=true");

        // Get all the planoActividadeList where classe is null
        defaultPlanoActividadeShouldNotBeFound("classe.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByClasseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where classe is greater than or equal to DEFAULT_CLASSE
        defaultPlanoActividadeShouldBeFound("classe.greaterThanOrEqual=" + DEFAULT_CLASSE);

        // Get all the planoActividadeList where classe is greater than or equal to UPDATED_CLASSE
        defaultPlanoActividadeShouldNotBeFound("classe.greaterThanOrEqual=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByClasseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where classe is less than or equal to DEFAULT_CLASSE
        defaultPlanoActividadeShouldBeFound("classe.lessThanOrEqual=" + DEFAULT_CLASSE);

        // Get all the planoActividadeList where classe is less than or equal to SMALLER_CLASSE
        defaultPlanoActividadeShouldNotBeFound("classe.lessThanOrEqual=" + SMALLER_CLASSE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByClasseIsLessThanSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where classe is less than DEFAULT_CLASSE
        defaultPlanoActividadeShouldNotBeFound("classe.lessThan=" + DEFAULT_CLASSE);

        // Get all the planoActividadeList where classe is less than UPDATED_CLASSE
        defaultPlanoActividadeShouldBeFound("classe.lessThan=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllPlanoActividadesByClasseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList where classe is greater than DEFAULT_CLASSE
        defaultPlanoActividadeShouldNotBeFound("classe.greaterThan=" + DEFAULT_CLASSE);

        // Get all the planoActividadeList where classe is greater than SMALLER_CLASSE
        defaultPlanoActividadeShouldBeFound("classe.greaterThan=" + SMALLER_CLASSE);
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByCursoIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);
        Curso curso = CursoResourceIT.createEntity(em);
        em.persist(curso);
        em.flush();
        planoActividade.setCurso(curso);
        planoActividadeRepository.saveAndFlush(planoActividade);
        Long cursoId = curso.getId();

        // Get all the planoActividadeList where curso equals to cursoId
        defaultPlanoActividadeShouldBeFound("cursoId.equals=" + cursoId);

        // Get all the planoActividadeList where curso equals to cursoId + 1
        defaultPlanoActividadeShouldNotBeFound("cursoId.equals=" + (cursoId + 1));
    }


    @Test
    @Transactional
    public void getAllPlanoActividadesByTurmaIsEqualToSomething() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);
        Turma turma = TurmaResourceIT.createEntity(em);
        em.persist(turma);
        em.flush();
        planoActividade.setTurma(turma);
        planoActividadeRepository.saveAndFlush(planoActividade);
        Long turmaId = turma.getId();

        // Get all the planoActividadeList where turma equals to turmaId
        defaultPlanoActividadeShouldBeFound("turmaId.equals=" + turmaId);

        // Get all the planoActividadeList where turma equals to turmaId + 1
        defaultPlanoActividadeShouldNotBeFound("turmaId.equals=" + (turmaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlanoActividadeShouldBeFound(String filter) throws Exception {
        restPlanoActividadeMockMvc.perform(get("/api/plano-actividades?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planoActividade.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroActividade").value(hasItem(DEFAULT_NUMERO_ACTIVIDADE)))
            .andExpect(jsonPath("$.[*].atividade").value(hasItem(DEFAULT_ATIVIDADE)))
            .andExpect(jsonPath("$.[*].objectivos").value(hasItem(DEFAULT_OBJECTIVOS.toString())))
            .andExpect(jsonPath("$.[*].de").value(hasItem(DEFAULT_DE.toString())))
            .andExpect(jsonPath("$.[*].ate").value(hasItem(DEFAULT_ATE.toString())))
            .andExpect(jsonPath("$.[*].responsavel").value(hasItem(DEFAULT_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].participantes").value(hasItem(DEFAULT_PARTICIPANTES)))
            .andExpect(jsonPath("$.[*].coResponsavel").value(hasItem(DEFAULT_CO_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].statusActividade").value(hasItem(DEFAULT_STATUS_ACTIVIDADE)))
            .andExpect(jsonPath("$.[*].periodoLectivo").value(hasItem(DEFAULT_PERIODO_LECTIVO)))
            .andExpect(jsonPath("$.[*].turno").value(hasItem(DEFAULT_TURNO)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)));

        // Check, that the count call also returns 1
        restPlanoActividadeMockMvc.perform(get("/api/plano-actividades/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlanoActividadeShouldNotBeFound(String filter) throws Exception {
        restPlanoActividadeMockMvc.perform(get("/api/plano-actividades?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlanoActividadeMockMvc.perform(get("/api/plano-actividades/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlanoActividade() throws Exception {
        // Get the planoActividade
        restPlanoActividadeMockMvc.perform(get("/api/plano-actividades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanoActividade() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        int databaseSizeBeforeUpdate = planoActividadeRepository.findAll().size();

        // Update the planoActividade
        PlanoActividade updatedPlanoActividade = planoActividadeRepository.findById(planoActividade.getId()).get();
        // Disconnect from session so that the updates on updatedPlanoActividade are not directly saved in db
        em.detach(updatedPlanoActividade);
        updatedPlanoActividade
            .numeroActividade(UPDATED_NUMERO_ACTIVIDADE)
            .atividade(UPDATED_ATIVIDADE)
            .objectivos(UPDATED_OBJECTIVOS)
            .de(UPDATED_DE)
            .ate(UPDATED_ATE)
            .responsavel(UPDATED_RESPONSAVEL)
            .local(UPDATED_LOCAL)
            .observacao(UPDATED_OBSERVACAO)
            .participantes(UPDATED_PARTICIPANTES)
            .coResponsavel(UPDATED_CO_RESPONSAVEL)
            .anoLectivo(UPDATED_ANO_LECTIVO)
            .statusActividade(UPDATED_STATUS_ACTIVIDADE)
            .periodoLectivo(UPDATED_PERIODO_LECTIVO)
            .turno(UPDATED_TURNO)
            .classe(UPDATED_CLASSE);
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(updatedPlanoActividade);

        restPlanoActividadeMockMvc.perform(put("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isOk());

        // Validate the PlanoActividade in the database
        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeUpdate);
        PlanoActividade testPlanoActividade = planoActividadeList.get(planoActividadeList.size() - 1);
        assertThat(testPlanoActividade.getNumeroActividade()).isEqualTo(UPDATED_NUMERO_ACTIVIDADE);
        assertThat(testPlanoActividade.getAtividade()).isEqualTo(UPDATED_ATIVIDADE);
        assertThat(testPlanoActividade.getObjectivos()).isEqualTo(UPDATED_OBJECTIVOS);
        assertThat(testPlanoActividade.getDe()).isEqualTo(UPDATED_DE);
        assertThat(testPlanoActividade.getAte()).isEqualTo(UPDATED_ATE);
        assertThat(testPlanoActividade.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
        assertThat(testPlanoActividade.getLocal()).isEqualTo(UPDATED_LOCAL);
        assertThat(testPlanoActividade.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testPlanoActividade.getParticipantes()).isEqualTo(UPDATED_PARTICIPANTES);
        assertThat(testPlanoActividade.getCoResponsavel()).isEqualTo(UPDATED_CO_RESPONSAVEL);
        assertThat(testPlanoActividade.getAnoLectivo()).isEqualTo(UPDATED_ANO_LECTIVO);
        assertThat(testPlanoActividade.getStatusActividade()).isEqualTo(UPDATED_STATUS_ACTIVIDADE);
        assertThat(testPlanoActividade.getPeriodoLectivo()).isEqualTo(UPDATED_PERIODO_LECTIVO);
        assertThat(testPlanoActividade.getTurno()).isEqualTo(UPDATED_TURNO);
        assertThat(testPlanoActividade.getClasse()).isEqualTo(UPDATED_CLASSE);

        // Validate the PlanoActividade in Elasticsearch
        verify(mockPlanoActividadeSearchRepository, times(1)).save(testPlanoActividade);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanoActividade() throws Exception {
        int databaseSizeBeforeUpdate = planoActividadeRepository.findAll().size();

        // Create the PlanoActividade
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanoActividadeMockMvc.perform(put("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanoActividade in the database
        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanoActividade in Elasticsearch
        verify(mockPlanoActividadeSearchRepository, times(0)).save(planoActividade);
    }

    @Test
    @Transactional
    public void deletePlanoActividade() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        int databaseSizeBeforeDelete = planoActividadeRepository.findAll().size();

        // Delete the planoActividade
        restPlanoActividadeMockMvc.perform(delete("/api/plano-actividades/{id}", planoActividade.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PlanoActividade in Elasticsearch
        verify(mockPlanoActividadeSearchRepository, times(1)).deleteById(planoActividade.getId());
    }

    @Test
    @Transactional
    public void searchPlanoActividade() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);
        when(mockPlanoActividadeSearchRepository.search(queryStringQuery("id:" + planoActividade.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(planoActividade), PageRequest.of(0, 1), 1));
        // Search the planoActividade
        restPlanoActividadeMockMvc.perform(get("/api/_search/plano-actividades?query=id:" + planoActividade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planoActividade.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroActividade").value(hasItem(DEFAULT_NUMERO_ACTIVIDADE)))
            .andExpect(jsonPath("$.[*].atividade").value(hasItem(DEFAULT_ATIVIDADE)))
            .andExpect(jsonPath("$.[*].objectivos").value(hasItem(DEFAULT_OBJECTIVOS.toString())))
            .andExpect(jsonPath("$.[*].de").value(hasItem(DEFAULT_DE.toString())))
            .andExpect(jsonPath("$.[*].ate").value(hasItem(DEFAULT_ATE.toString())))
            .andExpect(jsonPath("$.[*].responsavel").value(hasItem(DEFAULT_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].participantes").value(hasItem(DEFAULT_PARTICIPANTES)))
            .andExpect(jsonPath("$.[*].coResponsavel").value(hasItem(DEFAULT_CO_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].statusActividade").value(hasItem(DEFAULT_STATUS_ACTIVIDADE)))
            .andExpect(jsonPath("$.[*].periodoLectivo").value(hasItem(DEFAULT_PERIODO_LECTIVO)))
            .andExpect(jsonPath("$.[*].turno").value(hasItem(DEFAULT_TURNO)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)));
    }
}
