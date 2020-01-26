package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.Dosificacao;
import com.ravunana.educacao.pedagogico.domain.Curso;
import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import com.ravunana.educacao.pedagogico.repository.DosificacaoRepository;
import com.ravunana.educacao.pedagogico.repository.search.DosificacaoSearchRepository;
import com.ravunana.educacao.pedagogico.service.DosificacaoService;
import com.ravunana.educacao.pedagogico.service.dto.DosificacaoDTO;
import com.ravunana.educacao.pedagogico.service.mapper.DosificacaoMapper;
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

    private static final LocalDate DEFAULT_DE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ATE = LocalDate.now(ZoneId.systemDefault());

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
        final DosificacaoResource dosificacaoResource = new DosificacaoResource(dosificacaoService);
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
        DosificacaoResource dosificacaoResource = new DosificacaoResource(dosificacaoServiceMock);
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
        DosificacaoResource dosificacaoResource = new DosificacaoResource(dosificacaoServiceMock);
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
