package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.Chamada;
import com.ravunana.educacao.pedagogico.domain.Aula;
import com.ravunana.educacao.pedagogico.repository.ChamadaRepository;
import com.ravunana.educacao.pedagogico.repository.search.ChamadaSearchRepository;
import com.ravunana.educacao.pedagogico.service.ChamadaService;
import com.ravunana.educacao.pedagogico.service.dto.ChamadaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.ChamadaMapper;
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
 * Integration tests for the {@link ChamadaResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class ChamadaResourceIT {

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_PRESENTE = false;
    private static final Boolean UPDATED_PRESENTE = true;

    private static final String DEFAULT_NUMERO_PROCESSO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_PROCESSO = "BBBBBBBBBB";

    @Autowired
    private ChamadaRepository chamadaRepository;

    @Autowired
    private ChamadaMapper chamadaMapper;

    @Autowired
    private ChamadaService chamadaService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.ChamadaSearchRepositoryMockConfiguration
     */
    @Autowired
    private ChamadaSearchRepository mockChamadaSearchRepository;

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

    private MockMvc restChamadaMockMvc;

    private Chamada chamada;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChamadaResource chamadaResource = new ChamadaResource(chamadaService);
        this.restChamadaMockMvc = MockMvcBuilders.standaloneSetup(chamadaResource)
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
    public static Chamada createEntity(EntityManager em) {
        Chamada chamada = new Chamada()
            .data(DEFAULT_DATA)
            .presente(DEFAULT_PRESENTE)
            .numeroProcesso(DEFAULT_NUMERO_PROCESSO);
        // Add required entity
        Aula aula;
        if (TestUtil.findAll(em, Aula.class).isEmpty()) {
            aula = AulaResourceIT.createEntity(em);
            em.persist(aula);
            em.flush();
        } else {
            aula = TestUtil.findAll(em, Aula.class).get(0);
        }
        chamada.setAula(aula);
        return chamada;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chamada createUpdatedEntity(EntityManager em) {
        Chamada chamada = new Chamada()
            .data(UPDATED_DATA)
            .presente(UPDATED_PRESENTE)
            .numeroProcesso(UPDATED_NUMERO_PROCESSO);
        // Add required entity
        Aula aula;
        if (TestUtil.findAll(em, Aula.class).isEmpty()) {
            aula = AulaResourceIT.createUpdatedEntity(em);
            em.persist(aula);
            em.flush();
        } else {
            aula = TestUtil.findAll(em, Aula.class).get(0);
        }
        chamada.setAula(aula);
        return chamada;
    }

    @BeforeEach
    public void initTest() {
        chamada = createEntity(em);
    }

    @Test
    @Transactional
    public void createChamada() throws Exception {
        int databaseSizeBeforeCreate = chamadaRepository.findAll().size();

        // Create the Chamada
        ChamadaDTO chamadaDTO = chamadaMapper.toDto(chamada);
        restChamadaMockMvc.perform(post("/api/chamadas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chamadaDTO)))
            .andExpect(status().isCreated());

        // Validate the Chamada in the database
        List<Chamada> chamadaList = chamadaRepository.findAll();
        assertThat(chamadaList).hasSize(databaseSizeBeforeCreate + 1);
        Chamada testChamada = chamadaList.get(chamadaList.size() - 1);
        assertThat(testChamada.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testChamada.isPresente()).isEqualTo(DEFAULT_PRESENTE);
        assertThat(testChamada.getNumeroProcesso()).isEqualTo(DEFAULT_NUMERO_PROCESSO);

        // Validate the Chamada in Elasticsearch
        verify(mockChamadaSearchRepository, times(1)).save(testChamada);
    }

    @Test
    @Transactional
    public void createChamadaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chamadaRepository.findAll().size();

        // Create the Chamada with an existing ID
        chamada.setId(1L);
        ChamadaDTO chamadaDTO = chamadaMapper.toDto(chamada);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChamadaMockMvc.perform(post("/api/chamadas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chamadaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Chamada in the database
        List<Chamada> chamadaList = chamadaRepository.findAll();
        assertThat(chamadaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Chamada in Elasticsearch
        verify(mockChamadaSearchRepository, times(0)).save(chamada);
    }


    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = chamadaRepository.findAll().size();
        // set the field null
        chamada.setData(null);

        // Create the Chamada, which fails.
        ChamadaDTO chamadaDTO = chamadaMapper.toDto(chamada);

        restChamadaMockMvc.perform(post("/api/chamadas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chamadaDTO)))
            .andExpect(status().isBadRequest());

        List<Chamada> chamadaList = chamadaRepository.findAll();
        assertThat(chamadaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPresenteIsRequired() throws Exception {
        int databaseSizeBeforeTest = chamadaRepository.findAll().size();
        // set the field null
        chamada.setPresente(null);

        // Create the Chamada, which fails.
        ChamadaDTO chamadaDTO = chamadaMapper.toDto(chamada);

        restChamadaMockMvc.perform(post("/api/chamadas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chamadaDTO)))
            .andExpect(status().isBadRequest());

        List<Chamada> chamadaList = chamadaRepository.findAll();
        assertThat(chamadaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChamadas() throws Exception {
        // Initialize the database
        chamadaRepository.saveAndFlush(chamada);

        // Get all the chamadaList
        restChamadaMockMvc.perform(get("/api/chamadas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chamada.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].presente").value(hasItem(DEFAULT_PRESENTE.booleanValue())))
            .andExpect(jsonPath("$.[*].numeroProcesso").value(hasItem(DEFAULT_NUMERO_PROCESSO)));
    }
    
    @Test
    @Transactional
    public void getChamada() throws Exception {
        // Initialize the database
        chamadaRepository.saveAndFlush(chamada);

        // Get the chamada
        restChamadaMockMvc.perform(get("/api/chamadas/{id}", chamada.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chamada.getId().intValue()))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)))
            .andExpect(jsonPath("$.presente").value(DEFAULT_PRESENTE.booleanValue()))
            .andExpect(jsonPath("$.numeroProcesso").value(DEFAULT_NUMERO_PROCESSO));
    }

    @Test
    @Transactional
    public void getNonExistingChamada() throws Exception {
        // Get the chamada
        restChamadaMockMvc.perform(get("/api/chamadas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChamada() throws Exception {
        // Initialize the database
        chamadaRepository.saveAndFlush(chamada);

        int databaseSizeBeforeUpdate = chamadaRepository.findAll().size();

        // Update the chamada
        Chamada updatedChamada = chamadaRepository.findById(chamada.getId()).get();
        // Disconnect from session so that the updates on updatedChamada are not directly saved in db
        em.detach(updatedChamada);
        updatedChamada
            .data(UPDATED_DATA)
            .presente(UPDATED_PRESENTE)
            .numeroProcesso(UPDATED_NUMERO_PROCESSO);
        ChamadaDTO chamadaDTO = chamadaMapper.toDto(updatedChamada);

        restChamadaMockMvc.perform(put("/api/chamadas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chamadaDTO)))
            .andExpect(status().isOk());

        // Validate the Chamada in the database
        List<Chamada> chamadaList = chamadaRepository.findAll();
        assertThat(chamadaList).hasSize(databaseSizeBeforeUpdate);
        Chamada testChamada = chamadaList.get(chamadaList.size() - 1);
        assertThat(testChamada.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testChamada.isPresente()).isEqualTo(UPDATED_PRESENTE);
        assertThat(testChamada.getNumeroProcesso()).isEqualTo(UPDATED_NUMERO_PROCESSO);

        // Validate the Chamada in Elasticsearch
        verify(mockChamadaSearchRepository, times(1)).save(testChamada);
    }

    @Test
    @Transactional
    public void updateNonExistingChamada() throws Exception {
        int databaseSizeBeforeUpdate = chamadaRepository.findAll().size();

        // Create the Chamada
        ChamadaDTO chamadaDTO = chamadaMapper.toDto(chamada);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChamadaMockMvc.perform(put("/api/chamadas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chamadaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Chamada in the database
        List<Chamada> chamadaList = chamadaRepository.findAll();
        assertThat(chamadaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Chamada in Elasticsearch
        verify(mockChamadaSearchRepository, times(0)).save(chamada);
    }

    @Test
    @Transactional
    public void deleteChamada() throws Exception {
        // Initialize the database
        chamadaRepository.saveAndFlush(chamada);

        int databaseSizeBeforeDelete = chamadaRepository.findAll().size();

        // Delete the chamada
        restChamadaMockMvc.perform(delete("/api/chamadas/{id}", chamada.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Chamada> chamadaList = chamadaRepository.findAll();
        assertThat(chamadaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Chamada in Elasticsearch
        verify(mockChamadaSearchRepository, times(1)).deleteById(chamada.getId());
    }

    @Test
    @Transactional
    public void searchChamada() throws Exception {
        // Initialize the database
        chamadaRepository.saveAndFlush(chamada);
        when(mockChamadaSearchRepository.search(queryStringQuery("id:" + chamada.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(chamada), PageRequest.of(0, 1), 1));
        // Search the chamada
        restChamadaMockMvc.perform(get("/api/_search/chamadas?query=id:" + chamada.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chamada.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].presente").value(hasItem(DEFAULT_PRESENTE.booleanValue())))
            .andExpect(jsonPath("$.[*].numeroProcesso").value(hasItem(DEFAULT_NUMERO_PROCESSO)));
    }
}
