package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.CriterioAvaliacao;
import com.ravunana.educacao.pedagogico.repository.CriterioAvaliacaoRepository;
import com.ravunana.educacao.pedagogico.repository.search.CriterioAvaliacaoSearchRepository;
import com.ravunana.educacao.pedagogico.service.CriterioAvaliacaoService;
import com.ravunana.educacao.pedagogico.service.dto.CriterioAvaliacaoDTO;
import com.ravunana.educacao.pedagogico.service.mapper.CriterioAvaliacaoMapper;
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
 * Integration tests for the {@link CriterioAvaliacaoResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class CriterioAvaliacaoResourceIT {

    private static final Integer DEFAULT_APROVA_COM = 0;
    private static final Integer UPDATED_APROVA_COM = 1;

    private static final Integer DEFAULT_REPORVA_COM = 0;
    private static final Integer UPDATED_REPORVA_COM = 1;

    private static final Integer DEFAULT_RECURSO_COM = 0;
    private static final Integer UPDATED_RECURSO_COM = 1;

    private static final Boolean DEFAULT_FAZ_EXAME = false;
    private static final Boolean UPDATED_FAZ_EXAME = true;

    private static final Boolean DEFAULT_FAZ_RECURSO = false;
    private static final Boolean UPDATED_FAZ_RECURSO = true;

    private static final Boolean DEFAULT_FAZ_EXAME_ESPECIAL = false;
    private static final Boolean UPDATED_FAZ_EXAME_ESPECIAL = true;

    private static final Integer DEFAULT_NUMERO_FALTA_REPROVA = 0;
    private static final Integer UPDATED_NUMERO_FALTA_REPROVA = 1;

    private static final Double DEFAULT_NOTA_MINIMA_APROVACAO = 0D;
    private static final Double UPDATED_NOTA_MINIMA_APROVACAO = 1D;

    @Autowired
    private CriterioAvaliacaoRepository criterioAvaliacaoRepository;

    @Autowired
    private CriterioAvaliacaoMapper criterioAvaliacaoMapper;

    @Autowired
    private CriterioAvaliacaoService criterioAvaliacaoService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.CriterioAvaliacaoSearchRepositoryMockConfiguration
     */
    @Autowired
    private CriterioAvaliacaoSearchRepository mockCriterioAvaliacaoSearchRepository;

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

    private MockMvc restCriterioAvaliacaoMockMvc;

    private CriterioAvaliacao criterioAvaliacao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CriterioAvaliacaoResource criterioAvaliacaoResource = new CriterioAvaliacaoResource(criterioAvaliacaoService);
        this.restCriterioAvaliacaoMockMvc = MockMvcBuilders.standaloneSetup(criterioAvaliacaoResource)
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
    public static CriterioAvaliacao createEntity(EntityManager em) {
        CriterioAvaliacao criterioAvaliacao = new CriterioAvaliacao()
            .aprovaCom(DEFAULT_APROVA_COM)
            .reporvaCom(DEFAULT_REPORVA_COM)
            .recursoCom(DEFAULT_RECURSO_COM)
            .fazExame(DEFAULT_FAZ_EXAME)
            .fazRecurso(DEFAULT_FAZ_RECURSO)
            .fazExameEspecial(DEFAULT_FAZ_EXAME_ESPECIAL)
            .numeroFaltaReprova(DEFAULT_NUMERO_FALTA_REPROVA)
            .notaMinimaAprovacao(DEFAULT_NOTA_MINIMA_APROVACAO);
        return criterioAvaliacao;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CriterioAvaliacao createUpdatedEntity(EntityManager em) {
        CriterioAvaliacao criterioAvaliacao = new CriterioAvaliacao()
            .aprovaCom(UPDATED_APROVA_COM)
            .reporvaCom(UPDATED_REPORVA_COM)
            .recursoCom(UPDATED_RECURSO_COM)
            .fazExame(UPDATED_FAZ_EXAME)
            .fazRecurso(UPDATED_FAZ_RECURSO)
            .fazExameEspecial(UPDATED_FAZ_EXAME_ESPECIAL)
            .numeroFaltaReprova(UPDATED_NUMERO_FALTA_REPROVA)
            .notaMinimaAprovacao(UPDATED_NOTA_MINIMA_APROVACAO);
        return criterioAvaliacao;
    }

    @BeforeEach
    public void initTest() {
        criterioAvaliacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createCriterioAvaliacao() throws Exception {
        int databaseSizeBeforeCreate = criterioAvaliacaoRepository.findAll().size();

        // Create the CriterioAvaliacao
        CriterioAvaliacaoDTO criterioAvaliacaoDTO = criterioAvaliacaoMapper.toDto(criterioAvaliacao);
        restCriterioAvaliacaoMockMvc.perform(post("/api/criterio-avaliacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criterioAvaliacaoDTO)))
            .andExpect(status().isCreated());

        // Validate the CriterioAvaliacao in the database
        List<CriterioAvaliacao> criterioAvaliacaoList = criterioAvaliacaoRepository.findAll();
        assertThat(criterioAvaliacaoList).hasSize(databaseSizeBeforeCreate + 1);
        CriterioAvaliacao testCriterioAvaliacao = criterioAvaliacaoList.get(criterioAvaliacaoList.size() - 1);
        assertThat(testCriterioAvaliacao.getAprovaCom()).isEqualTo(DEFAULT_APROVA_COM);
        assertThat(testCriterioAvaliacao.getReporvaCom()).isEqualTo(DEFAULT_REPORVA_COM);
        assertThat(testCriterioAvaliacao.getRecursoCom()).isEqualTo(DEFAULT_RECURSO_COM);
        assertThat(testCriterioAvaliacao.isFazExame()).isEqualTo(DEFAULT_FAZ_EXAME);
        assertThat(testCriterioAvaliacao.isFazRecurso()).isEqualTo(DEFAULT_FAZ_RECURSO);
        assertThat(testCriterioAvaliacao.isFazExameEspecial()).isEqualTo(DEFAULT_FAZ_EXAME_ESPECIAL);
        assertThat(testCriterioAvaliacao.getNumeroFaltaReprova()).isEqualTo(DEFAULT_NUMERO_FALTA_REPROVA);
        assertThat(testCriterioAvaliacao.getNotaMinimaAprovacao()).isEqualTo(DEFAULT_NOTA_MINIMA_APROVACAO);

        // Validate the CriterioAvaliacao in Elasticsearch
        verify(mockCriterioAvaliacaoSearchRepository, times(1)).save(testCriterioAvaliacao);
    }

    @Test
    @Transactional
    public void createCriterioAvaliacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = criterioAvaliacaoRepository.findAll().size();

        // Create the CriterioAvaliacao with an existing ID
        criterioAvaliacao.setId(1L);
        CriterioAvaliacaoDTO criterioAvaliacaoDTO = criterioAvaliacaoMapper.toDto(criterioAvaliacao);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCriterioAvaliacaoMockMvc.perform(post("/api/criterio-avaliacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criterioAvaliacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CriterioAvaliacao in the database
        List<CriterioAvaliacao> criterioAvaliacaoList = criterioAvaliacaoRepository.findAll();
        assertThat(criterioAvaliacaoList).hasSize(databaseSizeBeforeCreate);

        // Validate the CriterioAvaliacao in Elasticsearch
        verify(mockCriterioAvaliacaoSearchRepository, times(0)).save(criterioAvaliacao);
    }


    @Test
    @Transactional
    public void checkAprovaComIsRequired() throws Exception {
        int databaseSizeBeforeTest = criterioAvaliacaoRepository.findAll().size();
        // set the field null
        criterioAvaliacao.setAprovaCom(null);

        // Create the CriterioAvaliacao, which fails.
        CriterioAvaliacaoDTO criterioAvaliacaoDTO = criterioAvaliacaoMapper.toDto(criterioAvaliacao);

        restCriterioAvaliacaoMockMvc.perform(post("/api/criterio-avaliacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criterioAvaliacaoDTO)))
            .andExpect(status().isBadRequest());

        List<CriterioAvaliacao> criterioAvaliacaoList = criterioAvaliacaoRepository.findAll();
        assertThat(criterioAvaliacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReporvaComIsRequired() throws Exception {
        int databaseSizeBeforeTest = criterioAvaliacaoRepository.findAll().size();
        // set the field null
        criterioAvaliacao.setReporvaCom(null);

        // Create the CriterioAvaliacao, which fails.
        CriterioAvaliacaoDTO criterioAvaliacaoDTO = criterioAvaliacaoMapper.toDto(criterioAvaliacao);

        restCriterioAvaliacaoMockMvc.perform(post("/api/criterio-avaliacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criterioAvaliacaoDTO)))
            .andExpect(status().isBadRequest());

        List<CriterioAvaliacao> criterioAvaliacaoList = criterioAvaliacaoRepository.findAll();
        assertThat(criterioAvaliacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRecursoComIsRequired() throws Exception {
        int databaseSizeBeforeTest = criterioAvaliacaoRepository.findAll().size();
        // set the field null
        criterioAvaliacao.setRecursoCom(null);

        // Create the CriterioAvaliacao, which fails.
        CriterioAvaliacaoDTO criterioAvaliacaoDTO = criterioAvaliacaoMapper.toDto(criterioAvaliacao);

        restCriterioAvaliacaoMockMvc.perform(post("/api/criterio-avaliacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criterioAvaliacaoDTO)))
            .andExpect(status().isBadRequest());

        List<CriterioAvaliacao> criterioAvaliacaoList = criterioAvaliacaoRepository.findAll();
        assertThat(criterioAvaliacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFazExameIsRequired() throws Exception {
        int databaseSizeBeforeTest = criterioAvaliacaoRepository.findAll().size();
        // set the field null
        criterioAvaliacao.setFazExame(null);

        // Create the CriterioAvaliacao, which fails.
        CriterioAvaliacaoDTO criterioAvaliacaoDTO = criterioAvaliacaoMapper.toDto(criterioAvaliacao);

        restCriterioAvaliacaoMockMvc.perform(post("/api/criterio-avaliacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criterioAvaliacaoDTO)))
            .andExpect(status().isBadRequest());

        List<CriterioAvaliacao> criterioAvaliacaoList = criterioAvaliacaoRepository.findAll();
        assertThat(criterioAvaliacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFazRecursoIsRequired() throws Exception {
        int databaseSizeBeforeTest = criterioAvaliacaoRepository.findAll().size();
        // set the field null
        criterioAvaliacao.setFazRecurso(null);

        // Create the CriterioAvaliacao, which fails.
        CriterioAvaliacaoDTO criterioAvaliacaoDTO = criterioAvaliacaoMapper.toDto(criterioAvaliacao);

        restCriterioAvaliacaoMockMvc.perform(post("/api/criterio-avaliacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criterioAvaliacaoDTO)))
            .andExpect(status().isBadRequest());

        List<CriterioAvaliacao> criterioAvaliacaoList = criterioAvaliacaoRepository.findAll();
        assertThat(criterioAvaliacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFazExameEspecialIsRequired() throws Exception {
        int databaseSizeBeforeTest = criterioAvaliacaoRepository.findAll().size();
        // set the field null
        criterioAvaliacao.setFazExameEspecial(null);

        // Create the CriterioAvaliacao, which fails.
        CriterioAvaliacaoDTO criterioAvaliacaoDTO = criterioAvaliacaoMapper.toDto(criterioAvaliacao);

        restCriterioAvaliacaoMockMvc.perform(post("/api/criterio-avaliacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criterioAvaliacaoDTO)))
            .andExpect(status().isBadRequest());

        List<CriterioAvaliacao> criterioAvaliacaoList = criterioAvaliacaoRepository.findAll();
        assertThat(criterioAvaliacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroFaltaReprovaIsRequired() throws Exception {
        int databaseSizeBeforeTest = criterioAvaliacaoRepository.findAll().size();
        // set the field null
        criterioAvaliacao.setNumeroFaltaReprova(null);

        // Create the CriterioAvaliacao, which fails.
        CriterioAvaliacaoDTO criterioAvaliacaoDTO = criterioAvaliacaoMapper.toDto(criterioAvaliacao);

        restCriterioAvaliacaoMockMvc.perform(post("/api/criterio-avaliacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criterioAvaliacaoDTO)))
            .andExpect(status().isBadRequest());

        List<CriterioAvaliacao> criterioAvaliacaoList = criterioAvaliacaoRepository.findAll();
        assertThat(criterioAvaliacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNotaMinimaAprovacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = criterioAvaliacaoRepository.findAll().size();
        // set the field null
        criterioAvaliacao.setNotaMinimaAprovacao(null);

        // Create the CriterioAvaliacao, which fails.
        CriterioAvaliacaoDTO criterioAvaliacaoDTO = criterioAvaliacaoMapper.toDto(criterioAvaliacao);

        restCriterioAvaliacaoMockMvc.perform(post("/api/criterio-avaliacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criterioAvaliacaoDTO)))
            .andExpect(status().isBadRequest());

        List<CriterioAvaliacao> criterioAvaliacaoList = criterioAvaliacaoRepository.findAll();
        assertThat(criterioAvaliacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCriterioAvaliacaos() throws Exception {
        // Initialize the database
        criterioAvaliacaoRepository.saveAndFlush(criterioAvaliacao);

        // Get all the criterioAvaliacaoList
        restCriterioAvaliacaoMockMvc.perform(get("/api/criterio-avaliacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(criterioAvaliacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].aprovaCom").value(hasItem(DEFAULT_APROVA_COM)))
            .andExpect(jsonPath("$.[*].reporvaCom").value(hasItem(DEFAULT_REPORVA_COM)))
            .andExpect(jsonPath("$.[*].recursoCom").value(hasItem(DEFAULT_RECURSO_COM)))
            .andExpect(jsonPath("$.[*].fazExame").value(hasItem(DEFAULT_FAZ_EXAME.booleanValue())))
            .andExpect(jsonPath("$.[*].fazRecurso").value(hasItem(DEFAULT_FAZ_RECURSO.booleanValue())))
            .andExpect(jsonPath("$.[*].fazExameEspecial").value(hasItem(DEFAULT_FAZ_EXAME_ESPECIAL.booleanValue())))
            .andExpect(jsonPath("$.[*].numeroFaltaReprova").value(hasItem(DEFAULT_NUMERO_FALTA_REPROVA)))
            .andExpect(jsonPath("$.[*].notaMinimaAprovacao").value(hasItem(DEFAULT_NOTA_MINIMA_APROVACAO.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getCriterioAvaliacao() throws Exception {
        // Initialize the database
        criterioAvaliacaoRepository.saveAndFlush(criterioAvaliacao);

        // Get the criterioAvaliacao
        restCriterioAvaliacaoMockMvc.perform(get("/api/criterio-avaliacaos/{id}", criterioAvaliacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(criterioAvaliacao.getId().intValue()))
            .andExpect(jsonPath("$.aprovaCom").value(DEFAULT_APROVA_COM))
            .andExpect(jsonPath("$.reporvaCom").value(DEFAULT_REPORVA_COM))
            .andExpect(jsonPath("$.recursoCom").value(DEFAULT_RECURSO_COM))
            .andExpect(jsonPath("$.fazExame").value(DEFAULT_FAZ_EXAME.booleanValue()))
            .andExpect(jsonPath("$.fazRecurso").value(DEFAULT_FAZ_RECURSO.booleanValue()))
            .andExpect(jsonPath("$.fazExameEspecial").value(DEFAULT_FAZ_EXAME_ESPECIAL.booleanValue()))
            .andExpect(jsonPath("$.numeroFaltaReprova").value(DEFAULT_NUMERO_FALTA_REPROVA))
            .andExpect(jsonPath("$.notaMinimaAprovacao").value(DEFAULT_NOTA_MINIMA_APROVACAO.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCriterioAvaliacao() throws Exception {
        // Get the criterioAvaliacao
        restCriterioAvaliacaoMockMvc.perform(get("/api/criterio-avaliacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCriterioAvaliacao() throws Exception {
        // Initialize the database
        criterioAvaliacaoRepository.saveAndFlush(criterioAvaliacao);

        int databaseSizeBeforeUpdate = criterioAvaliacaoRepository.findAll().size();

        // Update the criterioAvaliacao
        CriterioAvaliacao updatedCriterioAvaliacao = criterioAvaliacaoRepository.findById(criterioAvaliacao.getId()).get();
        // Disconnect from session so that the updates on updatedCriterioAvaliacao are not directly saved in db
        em.detach(updatedCriterioAvaliacao);
        updatedCriterioAvaliacao
            .aprovaCom(UPDATED_APROVA_COM)
            .reporvaCom(UPDATED_REPORVA_COM)
            .recursoCom(UPDATED_RECURSO_COM)
            .fazExame(UPDATED_FAZ_EXAME)
            .fazRecurso(UPDATED_FAZ_RECURSO)
            .fazExameEspecial(UPDATED_FAZ_EXAME_ESPECIAL)
            .numeroFaltaReprova(UPDATED_NUMERO_FALTA_REPROVA)
            .notaMinimaAprovacao(UPDATED_NOTA_MINIMA_APROVACAO);
        CriterioAvaliacaoDTO criterioAvaliacaoDTO = criterioAvaliacaoMapper.toDto(updatedCriterioAvaliacao);

        restCriterioAvaliacaoMockMvc.perform(put("/api/criterio-avaliacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criterioAvaliacaoDTO)))
            .andExpect(status().isOk());

        // Validate the CriterioAvaliacao in the database
        List<CriterioAvaliacao> criterioAvaliacaoList = criterioAvaliacaoRepository.findAll();
        assertThat(criterioAvaliacaoList).hasSize(databaseSizeBeforeUpdate);
        CriterioAvaliacao testCriterioAvaliacao = criterioAvaliacaoList.get(criterioAvaliacaoList.size() - 1);
        assertThat(testCriterioAvaliacao.getAprovaCom()).isEqualTo(UPDATED_APROVA_COM);
        assertThat(testCriterioAvaliacao.getReporvaCom()).isEqualTo(UPDATED_REPORVA_COM);
        assertThat(testCriterioAvaliacao.getRecursoCom()).isEqualTo(UPDATED_RECURSO_COM);
        assertThat(testCriterioAvaliacao.isFazExame()).isEqualTo(UPDATED_FAZ_EXAME);
        assertThat(testCriterioAvaliacao.isFazRecurso()).isEqualTo(UPDATED_FAZ_RECURSO);
        assertThat(testCriterioAvaliacao.isFazExameEspecial()).isEqualTo(UPDATED_FAZ_EXAME_ESPECIAL);
        assertThat(testCriterioAvaliacao.getNumeroFaltaReprova()).isEqualTo(UPDATED_NUMERO_FALTA_REPROVA);
        assertThat(testCriterioAvaliacao.getNotaMinimaAprovacao()).isEqualTo(UPDATED_NOTA_MINIMA_APROVACAO);

        // Validate the CriterioAvaliacao in Elasticsearch
        verify(mockCriterioAvaliacaoSearchRepository, times(1)).save(testCriterioAvaliacao);
    }

    @Test
    @Transactional
    public void updateNonExistingCriterioAvaliacao() throws Exception {
        int databaseSizeBeforeUpdate = criterioAvaliacaoRepository.findAll().size();

        // Create the CriterioAvaliacao
        CriterioAvaliacaoDTO criterioAvaliacaoDTO = criterioAvaliacaoMapper.toDto(criterioAvaliacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCriterioAvaliacaoMockMvc.perform(put("/api/criterio-avaliacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(criterioAvaliacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CriterioAvaliacao in the database
        List<CriterioAvaliacao> criterioAvaliacaoList = criterioAvaliacaoRepository.findAll();
        assertThat(criterioAvaliacaoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CriterioAvaliacao in Elasticsearch
        verify(mockCriterioAvaliacaoSearchRepository, times(0)).save(criterioAvaliacao);
    }

    @Test
    @Transactional
    public void deleteCriterioAvaliacao() throws Exception {
        // Initialize the database
        criterioAvaliacaoRepository.saveAndFlush(criterioAvaliacao);

        int databaseSizeBeforeDelete = criterioAvaliacaoRepository.findAll().size();

        // Delete the criterioAvaliacao
        restCriterioAvaliacaoMockMvc.perform(delete("/api/criterio-avaliacaos/{id}", criterioAvaliacao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CriterioAvaliacao> criterioAvaliacaoList = criterioAvaliacaoRepository.findAll();
        assertThat(criterioAvaliacaoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CriterioAvaliacao in Elasticsearch
        verify(mockCriterioAvaliacaoSearchRepository, times(1)).deleteById(criterioAvaliacao.getId());
    }

    @Test
    @Transactional
    public void searchCriterioAvaliacao() throws Exception {
        // Initialize the database
        criterioAvaliacaoRepository.saveAndFlush(criterioAvaliacao);
        when(mockCriterioAvaliacaoSearchRepository.search(queryStringQuery("id:" + criterioAvaliacao.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(criterioAvaliacao), PageRequest.of(0, 1), 1));
        // Search the criterioAvaliacao
        restCriterioAvaliacaoMockMvc.perform(get("/api/_search/criterio-avaliacaos?query=id:" + criterioAvaliacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(criterioAvaliacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].aprovaCom").value(hasItem(DEFAULT_APROVA_COM)))
            .andExpect(jsonPath("$.[*].reporvaCom").value(hasItem(DEFAULT_REPORVA_COM)))
            .andExpect(jsonPath("$.[*].recursoCom").value(hasItem(DEFAULT_RECURSO_COM)))
            .andExpect(jsonPath("$.[*].fazExame").value(hasItem(DEFAULT_FAZ_EXAME.booleanValue())))
            .andExpect(jsonPath("$.[*].fazRecurso").value(hasItem(DEFAULT_FAZ_RECURSO.booleanValue())))
            .andExpect(jsonPath("$.[*].fazExameEspecial").value(hasItem(DEFAULT_FAZ_EXAME_ESPECIAL.booleanValue())))
            .andExpect(jsonPath("$.[*].numeroFaltaReprova").value(hasItem(DEFAULT_NUMERO_FALTA_REPROVA)))
            .andExpect(jsonPath("$.[*].notaMinimaAprovacao").value(hasItem(DEFAULT_NOTA_MINIMA_APROVACAO.doubleValue())));
    }
}
