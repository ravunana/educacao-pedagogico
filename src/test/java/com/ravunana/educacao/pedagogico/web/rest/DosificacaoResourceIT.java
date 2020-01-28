package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.Dosificacao;
import com.ravunana.educacao.pedagogico.domain.PlanoAula;
import com.ravunana.educacao.pedagogico.domain.Curso;
import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import com.ravunana.educacao.pedagogico.repository.DosificacaoRepository;
import com.ravunana.educacao.pedagogico.repository.search.DosificacaoSearchRepository;
import com.ravunana.educacao.pedagogico.service.DosificacaoService;
import com.ravunana.educacao.pedagogico.service.dto.DosificacaoDTO;
import com.ravunana.educacao.pedagogico.service.mapper.DosificacaoMapper;
import com.ravunana.educacao.pedagogico.web.rest.errors.ExceptionTranslator;
import com.ravunana.educacao.pedagogico.service.dto.DosificacaoCriteria;
import com.ravunana.educacao.pedagogico.service.DosificacaoQueryService;

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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
 * Integration tests for the {@link DosificacaoResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class DosificacaoResourceIT {

    private static final String DEFAULT_PERIDO_LECTIVE = "AAAAAAAAAA";
    private static final String UPDATED_PERIDO_LECTIVE = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECTIVO_GERAL = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIVO_GERAL = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEMANA_LECTIVA = 1;
    private static final Integer UPDATED_SEMANA_LECTIVA = 2;
    private static final Integer SMALLER_SEMANA_LECTIVA = 1 - 1;

    private static final LocalDate DEFAULT_DE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_ATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_UNIDADE_TEMATICA = "AAAAAAAAAA";
    private static final String UPDATED_UNIDADE_TEMATICA = "BBBBBBBBBB";

    private static final String DEFAULT_CONTEUDO = "AAAAAAAAAA";
    private static final String UPDATED_CONTEUDO = "BBBBBBBBBB";

    private static final String DEFAULT_PROCEDIMENTO_ENSINO = "AAAAAAAAAA";
    private static final String UPDATED_PROCEDIMENTO_ENSINO = "BBBBBBBBBB";

    private static final String DEFAULT_RECURSOS_DIDATICOS = "AAAAAAAAAA";
    private static final String UPDATED_RECURSOS_DIDATICOS = "BBBBBBBBBB";

    private static final Integer DEFAULT_TEMPO_AULA = 1;
    private static final Integer UPDATED_TEMPO_AULA = 2;
    private static final Integer SMALLER_TEMPO_AULA = 1 - 1;

    private static final String DEFAULT_FORMA_AVALIACAO = "AAAAAAAAAA";
    private static final String UPDATED_FORMA_AVALIACAO = "BBBBBBBBBB";

    @Autowired
    private DosificacaoRepository dosificacaoRepository;

    @Mock
    private DosificacaoRepository dosificacaoRepositoryMock;

    @Autowired
    private DosificacaoMapper dosificacaoMapper;

    @Mock
    private DosificacaoService dosificacaoServiceMock;

    @Autowired
    private DosificacaoService dosificacaoService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.DosificacaoSearchRepositoryMockConfiguration
     */
    @Autowired
    private DosificacaoSearchRepository mockDosificacaoSearchRepository;

    @Autowired
    private DosificacaoQueryService dosificacaoQueryService;

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

    private MockMvc restDosificacaoMockMvc;

    private Dosificacao dosificacao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DosificacaoResource dosificacaoResource = new DosificacaoResource(dosificacaoService, dosificacaoQueryService);
        this.restDosificacaoMockMvc = MockMvcBuilders.standaloneSetup(dosificacaoResource)
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
    public static Dosificacao createEntity(EntityManager em) {
        Dosificacao dosificacao = new Dosificacao()
            .peridoLective(DEFAULT_PERIDO_LECTIVE)
            .objectivoGeral(DEFAULT_OBJECTIVO_GERAL)
            .semanaLectiva(DEFAULT_SEMANA_LECTIVA)
            .de(DEFAULT_DE)
            .ate(DEFAULT_ATE)
            .unidadeTematica(DEFAULT_UNIDADE_TEMATICA)
            .conteudo(DEFAULT_CONTEUDO)
            .procedimentoEnsino(DEFAULT_PROCEDIMENTO_ENSINO)
            .recursosDidaticos(DEFAULT_RECURSOS_DIDATICOS)
            .tempoAula(DEFAULT_TEMPO_AULA)
            .formaAvaliacao(DEFAULT_FORMA_AVALIACAO);
        // Add required entity
        Curso curso;
        if (TestUtil.findAll(em, Curso.class).isEmpty()) {
            curso = CursoResourceIT.createEntity(em);
            em.persist(curso);
            em.flush();
        } else {
            curso = TestUtil.findAll(em, Curso.class).get(0);
        }
        dosificacao.getCursos().add(curso);
        // Add required entity
        PlanoCurricular planoCurricular;
        if (TestUtil.findAll(em, PlanoCurricular.class).isEmpty()) {
            planoCurricular = PlanoCurricularResourceIT.createEntity(em);
            em.persist(planoCurricular);
            em.flush();
        } else {
            planoCurricular = TestUtil.findAll(em, PlanoCurricular.class).get(0);
        }
        dosificacao.setCurriulo(planoCurricular);
        return dosificacao;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dosificacao createUpdatedEntity(EntityManager em) {
        Dosificacao dosificacao = new Dosificacao()
            .peridoLective(UPDATED_PERIDO_LECTIVE)
            .objectivoGeral(UPDATED_OBJECTIVO_GERAL)
            .semanaLectiva(UPDATED_SEMANA_LECTIVA)
            .de(UPDATED_DE)
            .ate(UPDATED_ATE)
            .unidadeTematica(UPDATED_UNIDADE_TEMATICA)
            .conteudo(UPDATED_CONTEUDO)
            .procedimentoEnsino(UPDATED_PROCEDIMENTO_ENSINO)
            .recursosDidaticos(UPDATED_RECURSOS_DIDATICOS)
            .tempoAula(UPDATED_TEMPO_AULA)
            .formaAvaliacao(UPDATED_FORMA_AVALIACAO);
        // Add required entity
        Curso curso;
        if (TestUtil.findAll(em, Curso.class).isEmpty()) {
            curso = CursoResourceIT.createUpdatedEntity(em);
            em.persist(curso);
            em.flush();
        } else {
            curso = TestUtil.findAll(em, Curso.class).get(0);
        }
        dosificacao.getCursos().add(curso);
        // Add required entity
        PlanoCurricular planoCurricular;
        if (TestUtil.findAll(em, PlanoCurricular.class).isEmpty()) {
            planoCurricular = PlanoCurricularResourceIT.createUpdatedEntity(em);
            em.persist(planoCurricular);
            em.flush();
        } else {
            planoCurricular = TestUtil.findAll(em, PlanoCurricular.class).get(0);
        }
        dosificacao.setCurriulo(planoCurricular);
        return dosificacao;
    }

    @BeforeEach
    public void initTest() {
        dosificacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createDosificacao() throws Exception {
        int databaseSizeBeforeCreate = dosificacaoRepository.findAll().size();

        // Create the Dosificacao
        DosificacaoDTO dosificacaoDTO = dosificacaoMapper.toDto(dosificacao);
        restDosificacaoMockMvc.perform(post("/api/dosificacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dosificacaoDTO)))
            .andExpect(status().isCreated());

        // Validate the Dosificacao in the database
        List<Dosificacao> dosificacaoList = dosificacaoRepository.findAll();
        assertThat(dosificacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Dosificacao testDosificacao = dosificacaoList.get(dosificacaoList.size() - 1);
        assertThat(testDosificacao.getPeridoLective()).isEqualTo(DEFAULT_PERIDO_LECTIVE);
        assertThat(testDosificacao.getObjectivoGeral()).isEqualTo(DEFAULT_OBJECTIVO_GERAL);
        assertThat(testDosificacao.getSemanaLectiva()).isEqualTo(DEFAULT_SEMANA_LECTIVA);
        assertThat(testDosificacao.getDe()).isEqualTo(DEFAULT_DE);
        assertThat(testDosificacao.getAte()).isEqualTo(DEFAULT_ATE);
        assertThat(testDosificacao.getUnidadeTematica()).isEqualTo(DEFAULT_UNIDADE_TEMATICA);
        assertThat(testDosificacao.getConteudo()).isEqualTo(DEFAULT_CONTEUDO);
        assertThat(testDosificacao.getProcedimentoEnsino()).isEqualTo(DEFAULT_PROCEDIMENTO_ENSINO);
        assertThat(testDosificacao.getRecursosDidaticos()).isEqualTo(DEFAULT_RECURSOS_DIDATICOS);
        assertThat(testDosificacao.getTempoAula()).isEqualTo(DEFAULT_TEMPO_AULA);
        assertThat(testDosificacao.getFormaAvaliacao()).isEqualTo(DEFAULT_FORMA_AVALIACAO);

        // Validate the Dosificacao in Elasticsearch
        verify(mockDosificacaoSearchRepository, times(1)).save(testDosificacao);
    }

    @Test
    @Transactional
    public void createDosificacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dosificacaoRepository.findAll().size();

        // Create the Dosificacao with an existing ID
        dosificacao.setId(1L);
        DosificacaoDTO dosificacaoDTO = dosificacaoMapper.toDto(dosificacao);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDosificacaoMockMvc.perform(post("/api/dosificacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dosificacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dosificacao in the database
        List<Dosificacao> dosificacaoList = dosificacaoRepository.findAll();
        assertThat(dosificacaoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Dosificacao in Elasticsearch
        verify(mockDosificacaoSearchRepository, times(0)).save(dosificacao);
    }


    @Test
    @Transactional
    public void checkPeridoLectiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = dosificacaoRepository.findAll().size();
        // set the field null
        dosificacao.setPeridoLective(null);

        // Create the Dosificacao, which fails.
        DosificacaoDTO dosificacaoDTO = dosificacaoMapper.toDto(dosificacao);

        restDosificacaoMockMvc.perform(post("/api/dosificacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dosificacaoDTO)))
            .andExpect(status().isBadRequest());

        List<Dosificacao> dosificacaoList = dosificacaoRepository.findAll();
        assertThat(dosificacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSemanaLectivaIsRequired() throws Exception {
        int databaseSizeBeforeTest = dosificacaoRepository.findAll().size();
        // set the field null
        dosificacao.setSemanaLectiva(null);

        // Create the Dosificacao, which fails.
        DosificacaoDTO dosificacaoDTO = dosificacaoMapper.toDto(dosificacao);

        restDosificacaoMockMvc.perform(post("/api/dosificacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dosificacaoDTO)))
            .andExpect(status().isBadRequest());

        List<Dosificacao> dosificacaoList = dosificacaoRepository.findAll();
        assertThat(dosificacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dosificacaoRepository.findAll().size();
        // set the field null
        dosificacao.setDe(null);

        // Create the Dosificacao, which fails.
        DosificacaoDTO dosificacaoDTO = dosificacaoMapper.toDto(dosificacao);

        restDosificacaoMockMvc.perform(post("/api/dosificacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dosificacaoDTO)))
            .andExpect(status().isBadRequest());

        List<Dosificacao> dosificacaoList = dosificacaoRepository.findAll();
        assertThat(dosificacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAteIsRequired() throws Exception {
        int databaseSizeBeforeTest = dosificacaoRepository.findAll().size();
        // set the field null
        dosificacao.setAte(null);

        // Create the Dosificacao, which fails.
        DosificacaoDTO dosificacaoDTO = dosificacaoMapper.toDto(dosificacao);

        restDosificacaoMockMvc.perform(post("/api/dosificacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dosificacaoDTO)))
            .andExpect(status().isBadRequest());

        List<Dosificacao> dosificacaoList = dosificacaoRepository.findAll();
        assertThat(dosificacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnidadeTematicaIsRequired() throws Exception {
        int databaseSizeBeforeTest = dosificacaoRepository.findAll().size();
        // set the field null
        dosificacao.setUnidadeTematica(null);

        // Create the Dosificacao, which fails.
        DosificacaoDTO dosificacaoDTO = dosificacaoMapper.toDto(dosificacao);

        restDosificacaoMockMvc.perform(post("/api/dosificacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dosificacaoDTO)))
            .andExpect(status().isBadRequest());

        List<Dosificacao> dosificacaoList = dosificacaoRepository.findAll();
        assertThat(dosificacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTempoAulaIsRequired() throws Exception {
        int databaseSizeBeforeTest = dosificacaoRepository.findAll().size();
        // set the field null
        dosificacao.setTempoAula(null);

        // Create the Dosificacao, which fails.
        DosificacaoDTO dosificacaoDTO = dosificacaoMapper.toDto(dosificacao);

        restDosificacaoMockMvc.perform(post("/api/dosificacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dosificacaoDTO)))
            .andExpect(status().isBadRequest());

        List<Dosificacao> dosificacaoList = dosificacaoRepository.findAll();
        assertThat(dosificacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFormaAvaliacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = dosificacaoRepository.findAll().size();
        // set the field null
        dosificacao.setFormaAvaliacao(null);

        // Create the Dosificacao, which fails.
        DosificacaoDTO dosificacaoDTO = dosificacaoMapper.toDto(dosificacao);

        restDosificacaoMockMvc.perform(post("/api/dosificacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dosificacaoDTO)))
            .andExpect(status().isBadRequest());

        List<Dosificacao> dosificacaoList = dosificacaoRepository.findAll();
        assertThat(dosificacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDosificacaos() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList
        restDosificacaoMockMvc.perform(get("/api/dosificacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dosificacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].peridoLective").value(hasItem(DEFAULT_PERIDO_LECTIVE)))
            .andExpect(jsonPath("$.[*].objectivoGeral").value(hasItem(DEFAULT_OBJECTIVO_GERAL.toString())))
            .andExpect(jsonPath("$.[*].semanaLectiva").value(hasItem(DEFAULT_SEMANA_LECTIVA)))
            .andExpect(jsonPath("$.[*].de").value(hasItem(DEFAULT_DE.toString())))
            .andExpect(jsonPath("$.[*].ate").value(hasItem(DEFAULT_ATE.toString())))
            .andExpect(jsonPath("$.[*].unidadeTematica").value(hasItem(DEFAULT_UNIDADE_TEMATICA)))
            .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO.toString())))
            .andExpect(jsonPath("$.[*].procedimentoEnsino").value(hasItem(DEFAULT_PROCEDIMENTO_ENSINO.toString())))
            .andExpect(jsonPath("$.[*].recursosDidaticos").value(hasItem(DEFAULT_RECURSOS_DIDATICOS.toString())))
            .andExpect(jsonPath("$.[*].tempoAula").value(hasItem(DEFAULT_TEMPO_AULA)))
            .andExpect(jsonPath("$.[*].formaAvaliacao").value(hasItem(DEFAULT_FORMA_AVALIACAO)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllDosificacaosWithEagerRelationshipsIsEnabled() throws Exception {
        DosificacaoResource dosificacaoResource = new DosificacaoResource(dosificacaoServiceMock, dosificacaoQueryService);
        when(dosificacaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restDosificacaoMockMvc = MockMvcBuilders.standaloneSetup(dosificacaoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDosificacaoMockMvc.perform(get("/api/dosificacaos?eagerload=true"))
        .andExpect(status().isOk());

        verify(dosificacaoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDosificacaosWithEagerRelationshipsIsNotEnabled() throws Exception {
        DosificacaoResource dosificacaoResource = new DosificacaoResource(dosificacaoServiceMock, dosificacaoQueryService);
            when(dosificacaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restDosificacaoMockMvc = MockMvcBuilders.standaloneSetup(dosificacaoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDosificacaoMockMvc.perform(get("/api/dosificacaos?eagerload=true"))
        .andExpect(status().isOk());

            verify(dosificacaoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getDosificacao() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get the dosificacao
        restDosificacaoMockMvc.perform(get("/api/dosificacaos/{id}", dosificacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dosificacao.getId().intValue()))
            .andExpect(jsonPath("$.peridoLective").value(DEFAULT_PERIDO_LECTIVE))
            .andExpect(jsonPath("$.objectivoGeral").value(DEFAULT_OBJECTIVO_GERAL.toString()))
            .andExpect(jsonPath("$.semanaLectiva").value(DEFAULT_SEMANA_LECTIVA))
            .andExpect(jsonPath("$.de").value(DEFAULT_DE.toString()))
            .andExpect(jsonPath("$.ate").value(DEFAULT_ATE.toString()))
            .andExpect(jsonPath("$.unidadeTematica").value(DEFAULT_UNIDADE_TEMATICA))
            .andExpect(jsonPath("$.conteudo").value(DEFAULT_CONTEUDO.toString()))
            .andExpect(jsonPath("$.procedimentoEnsino").value(DEFAULT_PROCEDIMENTO_ENSINO.toString()))
            .andExpect(jsonPath("$.recursosDidaticos").value(DEFAULT_RECURSOS_DIDATICOS.toString()))
            .andExpect(jsonPath("$.tempoAula").value(DEFAULT_TEMPO_AULA))
            .andExpect(jsonPath("$.formaAvaliacao").value(DEFAULT_FORMA_AVALIACAO));
    }


    @Test
    @Transactional
    public void getDosificacaosByIdFiltering() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        Long id = dosificacao.getId();

        defaultDosificacaoShouldBeFound("id.equals=" + id);
        defaultDosificacaoShouldNotBeFound("id.notEquals=" + id);

        defaultDosificacaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDosificacaoShouldNotBeFound("id.greaterThan=" + id);

        defaultDosificacaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDosificacaoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDosificacaosByPeridoLectiveIsEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where peridoLective equals to DEFAULT_PERIDO_LECTIVE
        defaultDosificacaoShouldBeFound("peridoLective.equals=" + DEFAULT_PERIDO_LECTIVE);

        // Get all the dosificacaoList where peridoLective equals to UPDATED_PERIDO_LECTIVE
        defaultDosificacaoShouldNotBeFound("peridoLective.equals=" + UPDATED_PERIDO_LECTIVE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByPeridoLectiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where peridoLective not equals to DEFAULT_PERIDO_LECTIVE
        defaultDosificacaoShouldNotBeFound("peridoLective.notEquals=" + DEFAULT_PERIDO_LECTIVE);

        // Get all the dosificacaoList where peridoLective not equals to UPDATED_PERIDO_LECTIVE
        defaultDosificacaoShouldBeFound("peridoLective.notEquals=" + UPDATED_PERIDO_LECTIVE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByPeridoLectiveIsInShouldWork() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where peridoLective in DEFAULT_PERIDO_LECTIVE or UPDATED_PERIDO_LECTIVE
        defaultDosificacaoShouldBeFound("peridoLective.in=" + DEFAULT_PERIDO_LECTIVE + "," + UPDATED_PERIDO_LECTIVE);

        // Get all the dosificacaoList where peridoLective equals to UPDATED_PERIDO_LECTIVE
        defaultDosificacaoShouldNotBeFound("peridoLective.in=" + UPDATED_PERIDO_LECTIVE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByPeridoLectiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where peridoLective is not null
        defaultDosificacaoShouldBeFound("peridoLective.specified=true");

        // Get all the dosificacaoList where peridoLective is null
        defaultDosificacaoShouldNotBeFound("peridoLective.specified=false");
    }
                @Test
    @Transactional
    public void getAllDosificacaosByPeridoLectiveContainsSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where peridoLective contains DEFAULT_PERIDO_LECTIVE
        defaultDosificacaoShouldBeFound("peridoLective.contains=" + DEFAULT_PERIDO_LECTIVE);

        // Get all the dosificacaoList where peridoLective contains UPDATED_PERIDO_LECTIVE
        defaultDosificacaoShouldNotBeFound("peridoLective.contains=" + UPDATED_PERIDO_LECTIVE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByPeridoLectiveNotContainsSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where peridoLective does not contain DEFAULT_PERIDO_LECTIVE
        defaultDosificacaoShouldNotBeFound("peridoLective.doesNotContain=" + DEFAULT_PERIDO_LECTIVE);

        // Get all the dosificacaoList where peridoLective does not contain UPDATED_PERIDO_LECTIVE
        defaultDosificacaoShouldBeFound("peridoLective.doesNotContain=" + UPDATED_PERIDO_LECTIVE);
    }


    @Test
    @Transactional
    public void getAllDosificacaosBySemanaLectivaIsEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where semanaLectiva equals to DEFAULT_SEMANA_LECTIVA
        defaultDosificacaoShouldBeFound("semanaLectiva.equals=" + DEFAULT_SEMANA_LECTIVA);

        // Get all the dosificacaoList where semanaLectiva equals to UPDATED_SEMANA_LECTIVA
        defaultDosificacaoShouldNotBeFound("semanaLectiva.equals=" + UPDATED_SEMANA_LECTIVA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosBySemanaLectivaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where semanaLectiva not equals to DEFAULT_SEMANA_LECTIVA
        defaultDosificacaoShouldNotBeFound("semanaLectiva.notEquals=" + DEFAULT_SEMANA_LECTIVA);

        // Get all the dosificacaoList where semanaLectiva not equals to UPDATED_SEMANA_LECTIVA
        defaultDosificacaoShouldBeFound("semanaLectiva.notEquals=" + UPDATED_SEMANA_LECTIVA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosBySemanaLectivaIsInShouldWork() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where semanaLectiva in DEFAULT_SEMANA_LECTIVA or UPDATED_SEMANA_LECTIVA
        defaultDosificacaoShouldBeFound("semanaLectiva.in=" + DEFAULT_SEMANA_LECTIVA + "," + UPDATED_SEMANA_LECTIVA);

        // Get all the dosificacaoList where semanaLectiva equals to UPDATED_SEMANA_LECTIVA
        defaultDosificacaoShouldNotBeFound("semanaLectiva.in=" + UPDATED_SEMANA_LECTIVA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosBySemanaLectivaIsNullOrNotNull() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where semanaLectiva is not null
        defaultDosificacaoShouldBeFound("semanaLectiva.specified=true");

        // Get all the dosificacaoList where semanaLectiva is null
        defaultDosificacaoShouldNotBeFound("semanaLectiva.specified=false");
    }

    @Test
    @Transactional
    public void getAllDosificacaosBySemanaLectivaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where semanaLectiva is greater than or equal to DEFAULT_SEMANA_LECTIVA
        defaultDosificacaoShouldBeFound("semanaLectiva.greaterThanOrEqual=" + DEFAULT_SEMANA_LECTIVA);

        // Get all the dosificacaoList where semanaLectiva is greater than or equal to UPDATED_SEMANA_LECTIVA
        defaultDosificacaoShouldNotBeFound("semanaLectiva.greaterThanOrEqual=" + UPDATED_SEMANA_LECTIVA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosBySemanaLectivaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where semanaLectiva is less than or equal to DEFAULT_SEMANA_LECTIVA
        defaultDosificacaoShouldBeFound("semanaLectiva.lessThanOrEqual=" + DEFAULT_SEMANA_LECTIVA);

        // Get all the dosificacaoList where semanaLectiva is less than or equal to SMALLER_SEMANA_LECTIVA
        defaultDosificacaoShouldNotBeFound("semanaLectiva.lessThanOrEqual=" + SMALLER_SEMANA_LECTIVA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosBySemanaLectivaIsLessThanSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where semanaLectiva is less than DEFAULT_SEMANA_LECTIVA
        defaultDosificacaoShouldNotBeFound("semanaLectiva.lessThan=" + DEFAULT_SEMANA_LECTIVA);

        // Get all the dosificacaoList where semanaLectiva is less than UPDATED_SEMANA_LECTIVA
        defaultDosificacaoShouldBeFound("semanaLectiva.lessThan=" + UPDATED_SEMANA_LECTIVA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosBySemanaLectivaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where semanaLectiva is greater than DEFAULT_SEMANA_LECTIVA
        defaultDosificacaoShouldNotBeFound("semanaLectiva.greaterThan=" + DEFAULT_SEMANA_LECTIVA);

        // Get all the dosificacaoList where semanaLectiva is greater than SMALLER_SEMANA_LECTIVA
        defaultDosificacaoShouldBeFound("semanaLectiva.greaterThan=" + SMALLER_SEMANA_LECTIVA);
    }


    @Test
    @Transactional
    public void getAllDosificacaosByDeIsEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where de equals to DEFAULT_DE
        defaultDosificacaoShouldBeFound("de.equals=" + DEFAULT_DE);

        // Get all the dosificacaoList where de equals to UPDATED_DE
        defaultDosificacaoShouldNotBeFound("de.equals=" + UPDATED_DE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByDeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where de not equals to DEFAULT_DE
        defaultDosificacaoShouldNotBeFound("de.notEquals=" + DEFAULT_DE);

        // Get all the dosificacaoList where de not equals to UPDATED_DE
        defaultDosificacaoShouldBeFound("de.notEquals=" + UPDATED_DE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByDeIsInShouldWork() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where de in DEFAULT_DE or UPDATED_DE
        defaultDosificacaoShouldBeFound("de.in=" + DEFAULT_DE + "," + UPDATED_DE);

        // Get all the dosificacaoList where de equals to UPDATED_DE
        defaultDosificacaoShouldNotBeFound("de.in=" + UPDATED_DE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByDeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where de is not null
        defaultDosificacaoShouldBeFound("de.specified=true");

        // Get all the dosificacaoList where de is null
        defaultDosificacaoShouldNotBeFound("de.specified=false");
    }

    @Test
    @Transactional
    public void getAllDosificacaosByDeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where de is greater than or equal to DEFAULT_DE
        defaultDosificacaoShouldBeFound("de.greaterThanOrEqual=" + DEFAULT_DE);

        // Get all the dosificacaoList where de is greater than or equal to UPDATED_DE
        defaultDosificacaoShouldNotBeFound("de.greaterThanOrEqual=" + UPDATED_DE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByDeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where de is less than or equal to DEFAULT_DE
        defaultDosificacaoShouldBeFound("de.lessThanOrEqual=" + DEFAULT_DE);

        // Get all the dosificacaoList where de is less than or equal to SMALLER_DE
        defaultDosificacaoShouldNotBeFound("de.lessThanOrEqual=" + SMALLER_DE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByDeIsLessThanSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where de is less than DEFAULT_DE
        defaultDosificacaoShouldNotBeFound("de.lessThan=" + DEFAULT_DE);

        // Get all the dosificacaoList where de is less than UPDATED_DE
        defaultDosificacaoShouldBeFound("de.lessThan=" + UPDATED_DE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByDeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where de is greater than DEFAULT_DE
        defaultDosificacaoShouldNotBeFound("de.greaterThan=" + DEFAULT_DE);

        // Get all the dosificacaoList where de is greater than SMALLER_DE
        defaultDosificacaoShouldBeFound("de.greaterThan=" + SMALLER_DE);
    }


    @Test
    @Transactional
    public void getAllDosificacaosByAteIsEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where ate equals to DEFAULT_ATE
        defaultDosificacaoShouldBeFound("ate.equals=" + DEFAULT_ATE);

        // Get all the dosificacaoList where ate equals to UPDATED_ATE
        defaultDosificacaoShouldNotBeFound("ate.equals=" + UPDATED_ATE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByAteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where ate not equals to DEFAULT_ATE
        defaultDosificacaoShouldNotBeFound("ate.notEquals=" + DEFAULT_ATE);

        // Get all the dosificacaoList where ate not equals to UPDATED_ATE
        defaultDosificacaoShouldBeFound("ate.notEquals=" + UPDATED_ATE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByAteIsInShouldWork() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where ate in DEFAULT_ATE or UPDATED_ATE
        defaultDosificacaoShouldBeFound("ate.in=" + DEFAULT_ATE + "," + UPDATED_ATE);

        // Get all the dosificacaoList where ate equals to UPDATED_ATE
        defaultDosificacaoShouldNotBeFound("ate.in=" + UPDATED_ATE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByAteIsNullOrNotNull() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where ate is not null
        defaultDosificacaoShouldBeFound("ate.specified=true");

        // Get all the dosificacaoList where ate is null
        defaultDosificacaoShouldNotBeFound("ate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDosificacaosByAteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where ate is greater than or equal to DEFAULT_ATE
        defaultDosificacaoShouldBeFound("ate.greaterThanOrEqual=" + DEFAULT_ATE);

        // Get all the dosificacaoList where ate is greater than or equal to UPDATED_ATE
        defaultDosificacaoShouldNotBeFound("ate.greaterThanOrEqual=" + UPDATED_ATE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByAteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where ate is less than or equal to DEFAULT_ATE
        defaultDosificacaoShouldBeFound("ate.lessThanOrEqual=" + DEFAULT_ATE);

        // Get all the dosificacaoList where ate is less than or equal to SMALLER_ATE
        defaultDosificacaoShouldNotBeFound("ate.lessThanOrEqual=" + SMALLER_ATE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByAteIsLessThanSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where ate is less than DEFAULT_ATE
        defaultDosificacaoShouldNotBeFound("ate.lessThan=" + DEFAULT_ATE);

        // Get all the dosificacaoList where ate is less than UPDATED_ATE
        defaultDosificacaoShouldBeFound("ate.lessThan=" + UPDATED_ATE);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByAteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where ate is greater than DEFAULT_ATE
        defaultDosificacaoShouldNotBeFound("ate.greaterThan=" + DEFAULT_ATE);

        // Get all the dosificacaoList where ate is greater than SMALLER_ATE
        defaultDosificacaoShouldBeFound("ate.greaterThan=" + SMALLER_ATE);
    }


    @Test
    @Transactional
    public void getAllDosificacaosByUnidadeTematicaIsEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where unidadeTematica equals to DEFAULT_UNIDADE_TEMATICA
        defaultDosificacaoShouldBeFound("unidadeTematica.equals=" + DEFAULT_UNIDADE_TEMATICA);

        // Get all the dosificacaoList where unidadeTematica equals to UPDATED_UNIDADE_TEMATICA
        defaultDosificacaoShouldNotBeFound("unidadeTematica.equals=" + UPDATED_UNIDADE_TEMATICA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByUnidadeTematicaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where unidadeTematica not equals to DEFAULT_UNIDADE_TEMATICA
        defaultDosificacaoShouldNotBeFound("unidadeTematica.notEquals=" + DEFAULT_UNIDADE_TEMATICA);

        // Get all the dosificacaoList where unidadeTematica not equals to UPDATED_UNIDADE_TEMATICA
        defaultDosificacaoShouldBeFound("unidadeTematica.notEquals=" + UPDATED_UNIDADE_TEMATICA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByUnidadeTematicaIsInShouldWork() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where unidadeTematica in DEFAULT_UNIDADE_TEMATICA or UPDATED_UNIDADE_TEMATICA
        defaultDosificacaoShouldBeFound("unidadeTematica.in=" + DEFAULT_UNIDADE_TEMATICA + "," + UPDATED_UNIDADE_TEMATICA);

        // Get all the dosificacaoList where unidadeTematica equals to UPDATED_UNIDADE_TEMATICA
        defaultDosificacaoShouldNotBeFound("unidadeTematica.in=" + UPDATED_UNIDADE_TEMATICA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByUnidadeTematicaIsNullOrNotNull() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where unidadeTematica is not null
        defaultDosificacaoShouldBeFound("unidadeTematica.specified=true");

        // Get all the dosificacaoList where unidadeTematica is null
        defaultDosificacaoShouldNotBeFound("unidadeTematica.specified=false");
    }
                @Test
    @Transactional
    public void getAllDosificacaosByUnidadeTematicaContainsSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where unidadeTematica contains DEFAULT_UNIDADE_TEMATICA
        defaultDosificacaoShouldBeFound("unidadeTematica.contains=" + DEFAULT_UNIDADE_TEMATICA);

        // Get all the dosificacaoList where unidadeTematica contains UPDATED_UNIDADE_TEMATICA
        defaultDosificacaoShouldNotBeFound("unidadeTematica.contains=" + UPDATED_UNIDADE_TEMATICA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByUnidadeTematicaNotContainsSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where unidadeTematica does not contain DEFAULT_UNIDADE_TEMATICA
        defaultDosificacaoShouldNotBeFound("unidadeTematica.doesNotContain=" + DEFAULT_UNIDADE_TEMATICA);

        // Get all the dosificacaoList where unidadeTematica does not contain UPDATED_UNIDADE_TEMATICA
        defaultDosificacaoShouldBeFound("unidadeTematica.doesNotContain=" + UPDATED_UNIDADE_TEMATICA);
    }


    @Test
    @Transactional
    public void getAllDosificacaosByTempoAulaIsEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where tempoAula equals to DEFAULT_TEMPO_AULA
        defaultDosificacaoShouldBeFound("tempoAula.equals=" + DEFAULT_TEMPO_AULA);

        // Get all the dosificacaoList where tempoAula equals to UPDATED_TEMPO_AULA
        defaultDosificacaoShouldNotBeFound("tempoAula.equals=" + UPDATED_TEMPO_AULA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByTempoAulaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where tempoAula not equals to DEFAULT_TEMPO_AULA
        defaultDosificacaoShouldNotBeFound("tempoAula.notEquals=" + DEFAULT_TEMPO_AULA);

        // Get all the dosificacaoList where tempoAula not equals to UPDATED_TEMPO_AULA
        defaultDosificacaoShouldBeFound("tempoAula.notEquals=" + UPDATED_TEMPO_AULA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByTempoAulaIsInShouldWork() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where tempoAula in DEFAULT_TEMPO_AULA or UPDATED_TEMPO_AULA
        defaultDosificacaoShouldBeFound("tempoAula.in=" + DEFAULT_TEMPO_AULA + "," + UPDATED_TEMPO_AULA);

        // Get all the dosificacaoList where tempoAula equals to UPDATED_TEMPO_AULA
        defaultDosificacaoShouldNotBeFound("tempoAula.in=" + UPDATED_TEMPO_AULA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByTempoAulaIsNullOrNotNull() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where tempoAula is not null
        defaultDosificacaoShouldBeFound("tempoAula.specified=true");

        // Get all the dosificacaoList where tempoAula is null
        defaultDosificacaoShouldNotBeFound("tempoAula.specified=false");
    }

    @Test
    @Transactional
    public void getAllDosificacaosByTempoAulaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where tempoAula is greater than or equal to DEFAULT_TEMPO_AULA
        defaultDosificacaoShouldBeFound("tempoAula.greaterThanOrEqual=" + DEFAULT_TEMPO_AULA);

        // Get all the dosificacaoList where tempoAula is greater than or equal to UPDATED_TEMPO_AULA
        defaultDosificacaoShouldNotBeFound("tempoAula.greaterThanOrEqual=" + UPDATED_TEMPO_AULA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByTempoAulaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where tempoAula is less than or equal to DEFAULT_TEMPO_AULA
        defaultDosificacaoShouldBeFound("tempoAula.lessThanOrEqual=" + DEFAULT_TEMPO_AULA);

        // Get all the dosificacaoList where tempoAula is less than or equal to SMALLER_TEMPO_AULA
        defaultDosificacaoShouldNotBeFound("tempoAula.lessThanOrEqual=" + SMALLER_TEMPO_AULA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByTempoAulaIsLessThanSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where tempoAula is less than DEFAULT_TEMPO_AULA
        defaultDosificacaoShouldNotBeFound("tempoAula.lessThan=" + DEFAULT_TEMPO_AULA);

        // Get all the dosificacaoList where tempoAula is less than UPDATED_TEMPO_AULA
        defaultDosificacaoShouldBeFound("tempoAula.lessThan=" + UPDATED_TEMPO_AULA);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByTempoAulaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where tempoAula is greater than DEFAULT_TEMPO_AULA
        defaultDosificacaoShouldNotBeFound("tempoAula.greaterThan=" + DEFAULT_TEMPO_AULA);

        // Get all the dosificacaoList where tempoAula is greater than SMALLER_TEMPO_AULA
        defaultDosificacaoShouldBeFound("tempoAula.greaterThan=" + SMALLER_TEMPO_AULA);
    }


    @Test
    @Transactional
    public void getAllDosificacaosByFormaAvaliacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where formaAvaliacao equals to DEFAULT_FORMA_AVALIACAO
        defaultDosificacaoShouldBeFound("formaAvaliacao.equals=" + DEFAULT_FORMA_AVALIACAO);

        // Get all the dosificacaoList where formaAvaliacao equals to UPDATED_FORMA_AVALIACAO
        defaultDosificacaoShouldNotBeFound("formaAvaliacao.equals=" + UPDATED_FORMA_AVALIACAO);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByFormaAvaliacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where formaAvaliacao not equals to DEFAULT_FORMA_AVALIACAO
        defaultDosificacaoShouldNotBeFound("formaAvaliacao.notEquals=" + DEFAULT_FORMA_AVALIACAO);

        // Get all the dosificacaoList where formaAvaliacao not equals to UPDATED_FORMA_AVALIACAO
        defaultDosificacaoShouldBeFound("formaAvaliacao.notEquals=" + UPDATED_FORMA_AVALIACAO);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByFormaAvaliacaoIsInShouldWork() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where formaAvaliacao in DEFAULT_FORMA_AVALIACAO or UPDATED_FORMA_AVALIACAO
        defaultDosificacaoShouldBeFound("formaAvaliacao.in=" + DEFAULT_FORMA_AVALIACAO + "," + UPDATED_FORMA_AVALIACAO);

        // Get all the dosificacaoList where formaAvaliacao equals to UPDATED_FORMA_AVALIACAO
        defaultDosificacaoShouldNotBeFound("formaAvaliacao.in=" + UPDATED_FORMA_AVALIACAO);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByFormaAvaliacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where formaAvaliacao is not null
        defaultDosificacaoShouldBeFound("formaAvaliacao.specified=true");

        // Get all the dosificacaoList where formaAvaliacao is null
        defaultDosificacaoShouldNotBeFound("formaAvaliacao.specified=false");
    }
                @Test
    @Transactional
    public void getAllDosificacaosByFormaAvaliacaoContainsSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where formaAvaliacao contains DEFAULT_FORMA_AVALIACAO
        defaultDosificacaoShouldBeFound("formaAvaliacao.contains=" + DEFAULT_FORMA_AVALIACAO);

        // Get all the dosificacaoList where formaAvaliacao contains UPDATED_FORMA_AVALIACAO
        defaultDosificacaoShouldNotBeFound("formaAvaliacao.contains=" + UPDATED_FORMA_AVALIACAO);
    }

    @Test
    @Transactional
    public void getAllDosificacaosByFormaAvaliacaoNotContainsSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        // Get all the dosificacaoList where formaAvaliacao does not contain DEFAULT_FORMA_AVALIACAO
        defaultDosificacaoShouldNotBeFound("formaAvaliacao.doesNotContain=" + DEFAULT_FORMA_AVALIACAO);

        // Get all the dosificacaoList where formaAvaliacao does not contain UPDATED_FORMA_AVALIACAO
        defaultDosificacaoShouldBeFound("formaAvaliacao.doesNotContain=" + UPDATED_FORMA_AVALIACAO);
    }


    @Test
    @Transactional
    public void getAllDosificacaosByPlanoAulaIsEqualToSomething() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);
        PlanoAula planoAula = PlanoAulaResourceIT.createEntity(em);
        em.persist(planoAula);
        em.flush();
        dosificacao.addPlanoAula(planoAula);
        dosificacaoRepository.saveAndFlush(dosificacao);
        Long planoAulaId = planoAula.getId();

        // Get all the dosificacaoList where planoAula equals to planoAulaId
        defaultDosificacaoShouldBeFound("planoAulaId.equals=" + planoAulaId);

        // Get all the dosificacaoList where planoAula equals to planoAulaId + 1
        defaultDosificacaoShouldNotBeFound("planoAulaId.equals=" + (planoAulaId + 1));
    }


    @Test
    @Transactional
    public void getAllDosificacaosByCursoIsEqualToSomething() throws Exception {
        // Get already existing entity
        Curso curso = dosificacao.getCurso();
        dosificacaoRepository.saveAndFlush(dosificacao);
        Long cursoId = curso.getId();

        // Get all the dosificacaoList where curso equals to cursoId
        defaultDosificacaoShouldBeFound("cursoId.equals=" + cursoId);

        // Get all the dosificacaoList where curso equals to cursoId + 1
        defaultDosificacaoShouldNotBeFound("cursoId.equals=" + (cursoId + 1));
    }


    @Test
    @Transactional
    public void getAllDosificacaosByCurriuloIsEqualToSomething() throws Exception {
        // Get already existing entity
        PlanoCurricular curriulo = dosificacao.getCurriulo();
        dosificacaoRepository.saveAndFlush(dosificacao);
        Long curriuloId = curriulo.getId();

        // Get all the dosificacaoList where curriulo equals to curriuloId
        defaultDosificacaoShouldBeFound("curriuloId.equals=" + curriuloId);

        // Get all the dosificacaoList where curriulo equals to curriuloId + 1
        defaultDosificacaoShouldNotBeFound("curriuloId.equals=" + (curriuloId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDosificacaoShouldBeFound(String filter) throws Exception {
        restDosificacaoMockMvc.perform(get("/api/dosificacaos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dosificacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].peridoLective").value(hasItem(DEFAULT_PERIDO_LECTIVE)))
            .andExpect(jsonPath("$.[*].objectivoGeral").value(hasItem(DEFAULT_OBJECTIVO_GERAL.toString())))
            .andExpect(jsonPath("$.[*].semanaLectiva").value(hasItem(DEFAULT_SEMANA_LECTIVA)))
            .andExpect(jsonPath("$.[*].de").value(hasItem(DEFAULT_DE.toString())))
            .andExpect(jsonPath("$.[*].ate").value(hasItem(DEFAULT_ATE.toString())))
            .andExpect(jsonPath("$.[*].unidadeTematica").value(hasItem(DEFAULT_UNIDADE_TEMATICA)))
            .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO.toString())))
            .andExpect(jsonPath("$.[*].procedimentoEnsino").value(hasItem(DEFAULT_PROCEDIMENTO_ENSINO.toString())))
            .andExpect(jsonPath("$.[*].recursosDidaticos").value(hasItem(DEFAULT_RECURSOS_DIDATICOS.toString())))
            .andExpect(jsonPath("$.[*].tempoAula").value(hasItem(DEFAULT_TEMPO_AULA)))
            .andExpect(jsonPath("$.[*].formaAvaliacao").value(hasItem(DEFAULT_FORMA_AVALIACAO)));

        // Check, that the count call also returns 1
        restDosificacaoMockMvc.perform(get("/api/dosificacaos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDosificacaoShouldNotBeFound(String filter) throws Exception {
        restDosificacaoMockMvc.perform(get("/api/dosificacaos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDosificacaoMockMvc.perform(get("/api/dosificacaos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDosificacao() throws Exception {
        // Get the dosificacao
        restDosificacaoMockMvc.perform(get("/api/dosificacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDosificacao() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        int databaseSizeBeforeUpdate = dosificacaoRepository.findAll().size();

        // Update the dosificacao
        Dosificacao updatedDosificacao = dosificacaoRepository.findById(dosificacao.getId()).get();
        // Disconnect from session so that the updates on updatedDosificacao are not directly saved in db
        em.detach(updatedDosificacao);
        updatedDosificacao
            .peridoLective(UPDATED_PERIDO_LECTIVE)
            .objectivoGeral(UPDATED_OBJECTIVO_GERAL)
            .semanaLectiva(UPDATED_SEMANA_LECTIVA)
            .de(UPDATED_DE)
            .ate(UPDATED_ATE)
            .unidadeTematica(UPDATED_UNIDADE_TEMATICA)
            .conteudo(UPDATED_CONTEUDO)
            .procedimentoEnsino(UPDATED_PROCEDIMENTO_ENSINO)
            .recursosDidaticos(UPDATED_RECURSOS_DIDATICOS)
            .tempoAula(UPDATED_TEMPO_AULA)
            .formaAvaliacao(UPDATED_FORMA_AVALIACAO);
        DosificacaoDTO dosificacaoDTO = dosificacaoMapper.toDto(updatedDosificacao);

        restDosificacaoMockMvc.perform(put("/api/dosificacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dosificacaoDTO)))
            .andExpect(status().isOk());

        // Validate the Dosificacao in the database
        List<Dosificacao> dosificacaoList = dosificacaoRepository.findAll();
        assertThat(dosificacaoList).hasSize(databaseSizeBeforeUpdate);
        Dosificacao testDosificacao = dosificacaoList.get(dosificacaoList.size() - 1);
        assertThat(testDosificacao.getPeridoLective()).isEqualTo(UPDATED_PERIDO_LECTIVE);
        assertThat(testDosificacao.getObjectivoGeral()).isEqualTo(UPDATED_OBJECTIVO_GERAL);
        assertThat(testDosificacao.getSemanaLectiva()).isEqualTo(UPDATED_SEMANA_LECTIVA);
        assertThat(testDosificacao.getDe()).isEqualTo(UPDATED_DE);
        assertThat(testDosificacao.getAte()).isEqualTo(UPDATED_ATE);
        assertThat(testDosificacao.getUnidadeTematica()).isEqualTo(UPDATED_UNIDADE_TEMATICA);
        assertThat(testDosificacao.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
        assertThat(testDosificacao.getProcedimentoEnsino()).isEqualTo(UPDATED_PROCEDIMENTO_ENSINO);
        assertThat(testDosificacao.getRecursosDidaticos()).isEqualTo(UPDATED_RECURSOS_DIDATICOS);
        assertThat(testDosificacao.getTempoAula()).isEqualTo(UPDATED_TEMPO_AULA);
        assertThat(testDosificacao.getFormaAvaliacao()).isEqualTo(UPDATED_FORMA_AVALIACAO);

        // Validate the Dosificacao in Elasticsearch
        verify(mockDosificacaoSearchRepository, times(1)).save(testDosificacao);
    }

    @Test
    @Transactional
    public void updateNonExistingDosificacao() throws Exception {
        int databaseSizeBeforeUpdate = dosificacaoRepository.findAll().size();

        // Create the Dosificacao
        DosificacaoDTO dosificacaoDTO = dosificacaoMapper.toDto(dosificacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDosificacaoMockMvc.perform(put("/api/dosificacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dosificacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dosificacao in the database
        List<Dosificacao> dosificacaoList = dosificacaoRepository.findAll();
        assertThat(dosificacaoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dosificacao in Elasticsearch
        verify(mockDosificacaoSearchRepository, times(0)).save(dosificacao);
    }

    @Test
    @Transactional
    public void deleteDosificacao() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);

        int databaseSizeBeforeDelete = dosificacaoRepository.findAll().size();

        // Delete the dosificacao
        restDosificacaoMockMvc.perform(delete("/api/dosificacaos/{id}", dosificacao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dosificacao> dosificacaoList = dosificacaoRepository.findAll();
        assertThat(dosificacaoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Dosificacao in Elasticsearch
        verify(mockDosificacaoSearchRepository, times(1)).deleteById(dosificacao.getId());
    }

    @Test
    @Transactional
    public void searchDosificacao() throws Exception {
        // Initialize the database
        dosificacaoRepository.saveAndFlush(dosificacao);
        when(mockDosificacaoSearchRepository.search(queryStringQuery("id:" + dosificacao.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dosificacao), PageRequest.of(0, 1), 1));
        // Search the dosificacao
        restDosificacaoMockMvc.perform(get("/api/_search/dosificacaos?query=id:" + dosificacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dosificacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].peridoLective").value(hasItem(DEFAULT_PERIDO_LECTIVE)))
            .andExpect(jsonPath("$.[*].objectivoGeral").value(hasItem(DEFAULT_OBJECTIVO_GERAL.toString())))
            .andExpect(jsonPath("$.[*].semanaLectiva").value(hasItem(DEFAULT_SEMANA_LECTIVA)))
            .andExpect(jsonPath("$.[*].de").value(hasItem(DEFAULT_DE.toString())))
            .andExpect(jsonPath("$.[*].ate").value(hasItem(DEFAULT_ATE.toString())))
            .andExpect(jsonPath("$.[*].unidadeTematica").value(hasItem(DEFAULT_UNIDADE_TEMATICA)))
            .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO.toString())))
            .andExpect(jsonPath("$.[*].procedimentoEnsino").value(hasItem(DEFAULT_PROCEDIMENTO_ENSINO.toString())))
            .andExpect(jsonPath("$.[*].recursosDidaticos").value(hasItem(DEFAULT_RECURSOS_DIDATICOS.toString())))
            .andExpect(jsonPath("$.[*].tempoAula").value(hasItem(DEFAULT_TEMPO_AULA)))
            .andExpect(jsonPath("$.[*].formaAvaliacao").value(hasItem(DEFAULT_FORMA_AVALIACAO)));
    }
}
