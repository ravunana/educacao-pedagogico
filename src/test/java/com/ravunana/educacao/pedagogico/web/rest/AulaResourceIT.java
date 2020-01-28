package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.Aula;
import com.ravunana.educacao.pedagogico.domain.Chamada;
import com.ravunana.educacao.pedagogico.domain.PlanoAula;
import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import com.ravunana.educacao.pedagogico.repository.AulaRepository;
import com.ravunana.educacao.pedagogico.repository.search.AulaSearchRepository;
import com.ravunana.educacao.pedagogico.service.AulaService;
import com.ravunana.educacao.pedagogico.service.dto.AulaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.AulaMapper;
import com.ravunana.educacao.pedagogico.web.rest.errors.ExceptionTranslator;
import com.ravunana.educacao.pedagogico.service.dto.AulaCriteria;
import com.ravunana.educacao.pedagogico.service.AulaQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import java.util.ArrayList;
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
 * Integration tests for the {@link AulaResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class AulaResourceIT {

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_SUMARIO = "AAAAAAAAAA";
    private static final String UPDATED_SUMARIO = "BBBBBBBBBB";

    private static final Integer DEFAULT_LICAO = 1;
    private static final Integer UPDATED_LICAO = 2;
    private static final Integer SMALLER_LICAO = 1 - 1;

    private static final Boolean DEFAULT_DADA = false;
    private static final Boolean UPDATED_DADA = true;

    @Autowired
    private AulaRepository aulaRepository;

    @Mock
    private AulaRepository aulaRepositoryMock;

    @Autowired
    private AulaMapper aulaMapper;

    @Mock
    private AulaService aulaServiceMock;

    @Autowired
    private AulaService aulaService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.AulaSearchRepositoryMockConfiguration
     */
    @Autowired
    private AulaSearchRepository mockAulaSearchRepository;

    @Autowired
    private AulaQueryService aulaQueryService;

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

    private MockMvc restAulaMockMvc;

    private Aula aula;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AulaResource aulaResource = new AulaResource(aulaService, aulaQueryService);
        this.restAulaMockMvc = MockMvcBuilders.standaloneSetup(aulaResource)
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
    public static Aula createEntity(EntityManager em) {
        Aula aula = new Aula()
            .data(DEFAULT_DATA)
            .sumario(DEFAULT_SUMARIO)
            .licao(DEFAULT_LICAO)
            .dada(DEFAULT_DADA);
        // Add required entity
        Turma turma;
        if (TestUtil.findAll(em, Turma.class).isEmpty()) {
            turma = TurmaResourceIT.createEntity(em);
            em.persist(turma);
            em.flush();
        } else {
            turma = TestUtil.findAll(em, Turma.class).get(0);
        }
        aula.setTurma(turma);
        // Add required entity
        PlanoCurricular planoCurricular;
        if (TestUtil.findAll(em, PlanoCurricular.class).isEmpty()) {
            planoCurricular = PlanoCurricularResourceIT.createEntity(em);
            em.persist(planoCurricular);
            em.flush();
        } else {
            planoCurricular = TestUtil.findAll(em, PlanoCurricular.class).get(0);
        }
        aula.setCurriulo(planoCurricular);
        return aula;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aula createUpdatedEntity(EntityManager em) {
        Aula aula = new Aula()
            .data(UPDATED_DATA)
            .sumario(UPDATED_SUMARIO)
            .licao(UPDATED_LICAO)
            .dada(UPDATED_DADA);
        // Add required entity
        Turma turma;
        if (TestUtil.findAll(em, Turma.class).isEmpty()) {
            turma = TurmaResourceIT.createUpdatedEntity(em);
            em.persist(turma);
            em.flush();
        } else {
            turma = TestUtil.findAll(em, Turma.class).get(0);
        }
        aula.setTurma(turma);
        // Add required entity
        PlanoCurricular planoCurricular;
        if (TestUtil.findAll(em, PlanoCurricular.class).isEmpty()) {
            planoCurricular = PlanoCurricularResourceIT.createUpdatedEntity(em);
            em.persist(planoCurricular);
            em.flush();
        } else {
            planoCurricular = TestUtil.findAll(em, PlanoCurricular.class).get(0);
        }
        aula.setCurriulo(planoCurricular);
        return aula;
    }

    @BeforeEach
    public void initTest() {
        aula = createEntity(em);
    }

    @Test
    @Transactional
    public void createAula() throws Exception {
        int databaseSizeBeforeCreate = aulaRepository.findAll().size();

        // Create the Aula
        AulaDTO aulaDTO = aulaMapper.toDto(aula);
        restAulaMockMvc.perform(post("/api/aulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aulaDTO)))
            .andExpect(status().isCreated());

        // Validate the Aula in the database
        List<Aula> aulaList = aulaRepository.findAll();
        assertThat(aulaList).hasSize(databaseSizeBeforeCreate + 1);
        Aula testAula = aulaList.get(aulaList.size() - 1);
        assertThat(testAula.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testAula.getSumario()).isEqualTo(DEFAULT_SUMARIO);
        assertThat(testAula.getLicao()).isEqualTo(DEFAULT_LICAO);
        assertThat(testAula.isDada()).isEqualTo(DEFAULT_DADA);

        // Validate the Aula in Elasticsearch
        verify(mockAulaSearchRepository, times(1)).save(testAula);
    }

    @Test
    @Transactional
    public void createAulaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aulaRepository.findAll().size();

        // Create the Aula with an existing ID
        aula.setId(1L);
        AulaDTO aulaDTO = aulaMapper.toDto(aula);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAulaMockMvc.perform(post("/api/aulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aulaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Aula in the database
        List<Aula> aulaList = aulaRepository.findAll();
        assertThat(aulaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Aula in Elasticsearch
        verify(mockAulaSearchRepository, times(0)).save(aula);
    }


    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = aulaRepository.findAll().size();
        // set the field null
        aula.setData(null);

        // Create the Aula, which fails.
        AulaDTO aulaDTO = aulaMapper.toDto(aula);

        restAulaMockMvc.perform(post("/api/aulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aulaDTO)))
            .andExpect(status().isBadRequest());

        List<Aula> aulaList = aulaRepository.findAll();
        assertThat(aulaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSumarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = aulaRepository.findAll().size();
        // set the field null
        aula.setSumario(null);

        // Create the Aula, which fails.
        AulaDTO aulaDTO = aulaMapper.toDto(aula);

        restAulaMockMvc.perform(post("/api/aulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aulaDTO)))
            .andExpect(status().isBadRequest());

        List<Aula> aulaList = aulaRepository.findAll();
        assertThat(aulaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLicaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = aulaRepository.findAll().size();
        // set the field null
        aula.setLicao(null);

        // Create the Aula, which fails.
        AulaDTO aulaDTO = aulaMapper.toDto(aula);

        restAulaMockMvc.perform(post("/api/aulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aulaDTO)))
            .andExpect(status().isBadRequest());

        List<Aula> aulaList = aulaRepository.findAll();
        assertThat(aulaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDadaIsRequired() throws Exception {
        int databaseSizeBeforeTest = aulaRepository.findAll().size();
        // set the field null
        aula.setDada(null);

        // Create the Aula, which fails.
        AulaDTO aulaDTO = aulaMapper.toDto(aula);

        restAulaMockMvc.perform(post("/api/aulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aulaDTO)))
            .andExpect(status().isBadRequest());

        List<Aula> aulaList = aulaRepository.findAll();
        assertThat(aulaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAulas() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList
        restAulaMockMvc.perform(get("/api/aulas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aula.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].sumario").value(hasItem(DEFAULT_SUMARIO)))
            .andExpect(jsonPath("$.[*].licao").value(hasItem(DEFAULT_LICAO)))
            .andExpect(jsonPath("$.[*].dada").value(hasItem(DEFAULT_DADA.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllAulasWithEagerRelationshipsIsEnabled() throws Exception {
        AulaResource aulaResource = new AulaResource(aulaServiceMock, aulaQueryService);
        when(aulaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restAulaMockMvc = MockMvcBuilders.standaloneSetup(aulaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restAulaMockMvc.perform(get("/api/aulas?eagerload=true"))
        .andExpect(status().isOk());

        verify(aulaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllAulasWithEagerRelationshipsIsNotEnabled() throws Exception {
        AulaResource aulaResource = new AulaResource(aulaServiceMock, aulaQueryService);
            when(aulaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restAulaMockMvc = MockMvcBuilders.standaloneSetup(aulaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restAulaMockMvc.perform(get("/api/aulas?eagerload=true"))
        .andExpect(status().isOk());

            verify(aulaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getAula() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get the aula
        restAulaMockMvc.perform(get("/api/aulas/{id}", aula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(aula.getId().intValue()))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)))
            .andExpect(jsonPath("$.sumario").value(DEFAULT_SUMARIO))
            .andExpect(jsonPath("$.licao").value(DEFAULT_LICAO))
            .andExpect(jsonPath("$.dada").value(DEFAULT_DADA.booleanValue()));
    }


    @Test
    @Transactional
    public void getAulasByIdFiltering() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        Long id = aula.getId();

        defaultAulaShouldBeFound("id.equals=" + id);
        defaultAulaShouldNotBeFound("id.notEquals=" + id);

        defaultAulaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAulaShouldNotBeFound("id.greaterThan=" + id);

        defaultAulaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAulaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAulasByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where data equals to DEFAULT_DATA
        defaultAulaShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the aulaList where data equals to UPDATED_DATA
        defaultAulaShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllAulasByDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where data not equals to DEFAULT_DATA
        defaultAulaShouldNotBeFound("data.notEquals=" + DEFAULT_DATA);

        // Get all the aulaList where data not equals to UPDATED_DATA
        defaultAulaShouldBeFound("data.notEquals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllAulasByDataIsInShouldWork() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where data in DEFAULT_DATA or UPDATED_DATA
        defaultAulaShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the aulaList where data equals to UPDATED_DATA
        defaultAulaShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllAulasByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where data is not null
        defaultAulaShouldBeFound("data.specified=true");

        // Get all the aulaList where data is null
        defaultAulaShouldNotBeFound("data.specified=false");
    }

    @Test
    @Transactional
    public void getAllAulasByDataIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where data is greater than or equal to DEFAULT_DATA
        defaultAulaShouldBeFound("data.greaterThanOrEqual=" + DEFAULT_DATA);

        // Get all the aulaList where data is greater than or equal to UPDATED_DATA
        defaultAulaShouldNotBeFound("data.greaterThanOrEqual=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllAulasByDataIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where data is less than or equal to DEFAULT_DATA
        defaultAulaShouldBeFound("data.lessThanOrEqual=" + DEFAULT_DATA);

        // Get all the aulaList where data is less than or equal to SMALLER_DATA
        defaultAulaShouldNotBeFound("data.lessThanOrEqual=" + SMALLER_DATA);
    }

    @Test
    @Transactional
    public void getAllAulasByDataIsLessThanSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where data is less than DEFAULT_DATA
        defaultAulaShouldNotBeFound("data.lessThan=" + DEFAULT_DATA);

        // Get all the aulaList where data is less than UPDATED_DATA
        defaultAulaShouldBeFound("data.lessThan=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllAulasByDataIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where data is greater than DEFAULT_DATA
        defaultAulaShouldNotBeFound("data.greaterThan=" + DEFAULT_DATA);

        // Get all the aulaList where data is greater than SMALLER_DATA
        defaultAulaShouldBeFound("data.greaterThan=" + SMALLER_DATA);
    }


    @Test
    @Transactional
    public void getAllAulasBySumarioIsEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where sumario equals to DEFAULT_SUMARIO
        defaultAulaShouldBeFound("sumario.equals=" + DEFAULT_SUMARIO);

        // Get all the aulaList where sumario equals to UPDATED_SUMARIO
        defaultAulaShouldNotBeFound("sumario.equals=" + UPDATED_SUMARIO);
    }

    @Test
    @Transactional
    public void getAllAulasBySumarioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where sumario not equals to DEFAULT_SUMARIO
        defaultAulaShouldNotBeFound("sumario.notEquals=" + DEFAULT_SUMARIO);

        // Get all the aulaList where sumario not equals to UPDATED_SUMARIO
        defaultAulaShouldBeFound("sumario.notEquals=" + UPDATED_SUMARIO);
    }

    @Test
    @Transactional
    public void getAllAulasBySumarioIsInShouldWork() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where sumario in DEFAULT_SUMARIO or UPDATED_SUMARIO
        defaultAulaShouldBeFound("sumario.in=" + DEFAULT_SUMARIO + "," + UPDATED_SUMARIO);

        // Get all the aulaList where sumario equals to UPDATED_SUMARIO
        defaultAulaShouldNotBeFound("sumario.in=" + UPDATED_SUMARIO);
    }

    @Test
    @Transactional
    public void getAllAulasBySumarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where sumario is not null
        defaultAulaShouldBeFound("sumario.specified=true");

        // Get all the aulaList where sumario is null
        defaultAulaShouldNotBeFound("sumario.specified=false");
    }
                @Test
    @Transactional
    public void getAllAulasBySumarioContainsSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where sumario contains DEFAULT_SUMARIO
        defaultAulaShouldBeFound("sumario.contains=" + DEFAULT_SUMARIO);

        // Get all the aulaList where sumario contains UPDATED_SUMARIO
        defaultAulaShouldNotBeFound("sumario.contains=" + UPDATED_SUMARIO);
    }

    @Test
    @Transactional
    public void getAllAulasBySumarioNotContainsSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where sumario does not contain DEFAULT_SUMARIO
        defaultAulaShouldNotBeFound("sumario.doesNotContain=" + DEFAULT_SUMARIO);

        // Get all the aulaList where sumario does not contain UPDATED_SUMARIO
        defaultAulaShouldBeFound("sumario.doesNotContain=" + UPDATED_SUMARIO);
    }


    @Test
    @Transactional
    public void getAllAulasByLicaoIsEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where licao equals to DEFAULT_LICAO
        defaultAulaShouldBeFound("licao.equals=" + DEFAULT_LICAO);

        // Get all the aulaList where licao equals to UPDATED_LICAO
        defaultAulaShouldNotBeFound("licao.equals=" + UPDATED_LICAO);
    }

    @Test
    @Transactional
    public void getAllAulasByLicaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where licao not equals to DEFAULT_LICAO
        defaultAulaShouldNotBeFound("licao.notEquals=" + DEFAULT_LICAO);

        // Get all the aulaList where licao not equals to UPDATED_LICAO
        defaultAulaShouldBeFound("licao.notEquals=" + UPDATED_LICAO);
    }

    @Test
    @Transactional
    public void getAllAulasByLicaoIsInShouldWork() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where licao in DEFAULT_LICAO or UPDATED_LICAO
        defaultAulaShouldBeFound("licao.in=" + DEFAULT_LICAO + "," + UPDATED_LICAO);

        // Get all the aulaList where licao equals to UPDATED_LICAO
        defaultAulaShouldNotBeFound("licao.in=" + UPDATED_LICAO);
    }

    @Test
    @Transactional
    public void getAllAulasByLicaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where licao is not null
        defaultAulaShouldBeFound("licao.specified=true");

        // Get all the aulaList where licao is null
        defaultAulaShouldNotBeFound("licao.specified=false");
    }

    @Test
    @Transactional
    public void getAllAulasByLicaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where licao is greater than or equal to DEFAULT_LICAO
        defaultAulaShouldBeFound("licao.greaterThanOrEqual=" + DEFAULT_LICAO);

        // Get all the aulaList where licao is greater than or equal to UPDATED_LICAO
        defaultAulaShouldNotBeFound("licao.greaterThanOrEqual=" + UPDATED_LICAO);
    }

    @Test
    @Transactional
    public void getAllAulasByLicaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where licao is less than or equal to DEFAULT_LICAO
        defaultAulaShouldBeFound("licao.lessThanOrEqual=" + DEFAULT_LICAO);

        // Get all the aulaList where licao is less than or equal to SMALLER_LICAO
        defaultAulaShouldNotBeFound("licao.lessThanOrEqual=" + SMALLER_LICAO);
    }

    @Test
    @Transactional
    public void getAllAulasByLicaoIsLessThanSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where licao is less than DEFAULT_LICAO
        defaultAulaShouldNotBeFound("licao.lessThan=" + DEFAULT_LICAO);

        // Get all the aulaList where licao is less than UPDATED_LICAO
        defaultAulaShouldBeFound("licao.lessThan=" + UPDATED_LICAO);
    }

    @Test
    @Transactional
    public void getAllAulasByLicaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where licao is greater than DEFAULT_LICAO
        defaultAulaShouldNotBeFound("licao.greaterThan=" + DEFAULT_LICAO);

        // Get all the aulaList where licao is greater than SMALLER_LICAO
        defaultAulaShouldBeFound("licao.greaterThan=" + SMALLER_LICAO);
    }


    @Test
    @Transactional
    public void getAllAulasByDadaIsEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where dada equals to DEFAULT_DADA
        defaultAulaShouldBeFound("dada.equals=" + DEFAULT_DADA);

        // Get all the aulaList where dada equals to UPDATED_DADA
        defaultAulaShouldNotBeFound("dada.equals=" + UPDATED_DADA);
    }

    @Test
    @Transactional
    public void getAllAulasByDadaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where dada not equals to DEFAULT_DADA
        defaultAulaShouldNotBeFound("dada.notEquals=" + DEFAULT_DADA);

        // Get all the aulaList where dada not equals to UPDATED_DADA
        defaultAulaShouldBeFound("dada.notEquals=" + UPDATED_DADA);
    }

    @Test
    @Transactional
    public void getAllAulasByDadaIsInShouldWork() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where dada in DEFAULT_DADA or UPDATED_DADA
        defaultAulaShouldBeFound("dada.in=" + DEFAULT_DADA + "," + UPDATED_DADA);

        // Get all the aulaList where dada equals to UPDATED_DADA
        defaultAulaShouldNotBeFound("dada.in=" + UPDATED_DADA);
    }

    @Test
    @Transactional
    public void getAllAulasByDadaIsNullOrNotNull() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        // Get all the aulaList where dada is not null
        defaultAulaShouldBeFound("dada.specified=true");

        // Get all the aulaList where dada is null
        defaultAulaShouldNotBeFound("dada.specified=false");
    }

    @Test
    @Transactional
    public void getAllAulasByChamadaIsEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);
        Chamada chamada = ChamadaResourceIT.createEntity(em);
        em.persist(chamada);
        em.flush();
        aula.addChamada(chamada);
        aulaRepository.saveAndFlush(aula);
        Long chamadaId = chamada.getId();

        // Get all the aulaList where chamada equals to chamadaId
        defaultAulaShouldBeFound("chamadaId.equals=" + chamadaId);

        // Get all the aulaList where chamada equals to chamadaId + 1
        defaultAulaShouldNotBeFound("chamadaId.equals=" + (chamadaId + 1));
    }


    @Test
    @Transactional
    public void getAllAulasByPlanoAulaIsEqualToSomething() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);
        PlanoAula planoAula = PlanoAulaResourceIT.createEntity(em);
        em.persist(planoAula);
        em.flush();
        aula.addPlanoAula(planoAula);
        aulaRepository.saveAndFlush(aula);
        Long planoAulaId = planoAula.getId();

        // Get all the aulaList where planoAula equals to planoAulaId
        defaultAulaShouldBeFound("planoAulaId.equals=" + planoAulaId);

        // Get all the aulaList where planoAula equals to planoAulaId + 1
        defaultAulaShouldNotBeFound("planoAulaId.equals=" + (planoAulaId + 1));
    }


    @Test
    @Transactional
    public void getAllAulasByTurmaIsEqualToSomething() throws Exception {
        // Get already existing entity
        Turma turma = aula.getTurma();
        aulaRepository.saveAndFlush(aula);
        Long turmaId = turma.getId();

        // Get all the aulaList where turma equals to turmaId
        defaultAulaShouldBeFound("turmaId.equals=" + turmaId);

        // Get all the aulaList where turma equals to turmaId + 1
        defaultAulaShouldNotBeFound("turmaId.equals=" + (turmaId + 1));
    }


    @Test
    @Transactional
    public void getAllAulasByCurriuloIsEqualToSomething() throws Exception {
        // Get already existing entity
        PlanoCurricular curriulo = aula.getCurriulo();
        aulaRepository.saveAndFlush(aula);
        Long curriuloId = curriulo.getId();

        // Get all the aulaList where curriulo equals to curriuloId
        defaultAulaShouldBeFound("curriuloId.equals=" + curriuloId);

        // Get all the aulaList where curriulo equals to curriuloId + 1
        defaultAulaShouldNotBeFound("curriuloId.equals=" + (curriuloId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAulaShouldBeFound(String filter) throws Exception {
        restAulaMockMvc.perform(get("/api/aulas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aula.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].sumario").value(hasItem(DEFAULT_SUMARIO)))
            .andExpect(jsonPath("$.[*].licao").value(hasItem(DEFAULT_LICAO)))
            .andExpect(jsonPath("$.[*].dada").value(hasItem(DEFAULT_DADA.booleanValue())));

        // Check, that the count call also returns 1
        restAulaMockMvc.perform(get("/api/aulas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAulaShouldNotBeFound(String filter) throws Exception {
        restAulaMockMvc.perform(get("/api/aulas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAulaMockMvc.perform(get("/api/aulas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAula() throws Exception {
        // Get the aula
        restAulaMockMvc.perform(get("/api/aulas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAula() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        int databaseSizeBeforeUpdate = aulaRepository.findAll().size();

        // Update the aula
        Aula updatedAula = aulaRepository.findById(aula.getId()).get();
        // Disconnect from session so that the updates on updatedAula are not directly saved in db
        em.detach(updatedAula);
        updatedAula
            .data(UPDATED_DATA)
            .sumario(UPDATED_SUMARIO)
            .licao(UPDATED_LICAO)
            .dada(UPDATED_DADA);
        AulaDTO aulaDTO = aulaMapper.toDto(updatedAula);

        restAulaMockMvc.perform(put("/api/aulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aulaDTO)))
            .andExpect(status().isOk());

        // Validate the Aula in the database
        List<Aula> aulaList = aulaRepository.findAll();
        assertThat(aulaList).hasSize(databaseSizeBeforeUpdate);
        Aula testAula = aulaList.get(aulaList.size() - 1);
        assertThat(testAula.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testAula.getSumario()).isEqualTo(UPDATED_SUMARIO);
        assertThat(testAula.getLicao()).isEqualTo(UPDATED_LICAO);
        assertThat(testAula.isDada()).isEqualTo(UPDATED_DADA);

        // Validate the Aula in Elasticsearch
        verify(mockAulaSearchRepository, times(1)).save(testAula);
    }

    @Test
    @Transactional
    public void updateNonExistingAula() throws Exception {
        int databaseSizeBeforeUpdate = aulaRepository.findAll().size();

        // Create the Aula
        AulaDTO aulaDTO = aulaMapper.toDto(aula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAulaMockMvc.perform(put("/api/aulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aulaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Aula in the database
        List<Aula> aulaList = aulaRepository.findAll();
        assertThat(aulaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Aula in Elasticsearch
        verify(mockAulaSearchRepository, times(0)).save(aula);
    }

    @Test
    @Transactional
    public void deleteAula() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);

        int databaseSizeBeforeDelete = aulaRepository.findAll().size();

        // Delete the aula
        restAulaMockMvc.perform(delete("/api/aulas/{id}", aula.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Aula> aulaList = aulaRepository.findAll();
        assertThat(aulaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Aula in Elasticsearch
        verify(mockAulaSearchRepository, times(1)).deleteById(aula.getId());
    }

    @Test
    @Transactional
    public void searchAula() throws Exception {
        // Initialize the database
        aulaRepository.saveAndFlush(aula);
        when(mockAulaSearchRepository.search(queryStringQuery("id:" + aula.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(aula), PageRequest.of(0, 1), 1));
        // Search the aula
        restAulaMockMvc.perform(get("/api/_search/aulas?query=id:" + aula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aula.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].sumario").value(hasItem(DEFAULT_SUMARIO)))
            .andExpect(jsonPath("$.[*].licao").value(hasItem(DEFAULT_LICAO)))
            .andExpect(jsonPath("$.[*].dada").value(hasItem(DEFAULT_DADA.booleanValue())));
    }
}
