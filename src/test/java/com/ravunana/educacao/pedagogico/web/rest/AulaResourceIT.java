package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.Aula;
import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import com.ravunana.educacao.pedagogico.repository.AulaRepository;
import com.ravunana.educacao.pedagogico.repository.search.AulaSearchRepository;
import com.ravunana.educacao.pedagogico.service.AulaService;
import com.ravunana.educacao.pedagogico.service.dto.AulaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.AulaMapper;
import com.ravunana.educacao.pedagogico.web.rest.errors.ExceptionTranslator;

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

    private static final String DEFAULT_SUMARIO = "AAAAAAAAAA";
    private static final String UPDATED_SUMARIO = "BBBBBBBBBB";

    private static final Integer DEFAULT_LICAO = 1;
    private static final Integer UPDATED_LICAO = 2;

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
        final AulaResource aulaResource = new AulaResource(aulaService);
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
        AulaResource aulaResource = new AulaResource(aulaServiceMock);
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
        AulaResource aulaResource = new AulaResource(aulaServiceMock);
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
