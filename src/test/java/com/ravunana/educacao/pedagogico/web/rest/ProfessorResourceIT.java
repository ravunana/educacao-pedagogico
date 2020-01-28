package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.Professor;
import com.ravunana.educacao.pedagogico.domain.Horario;
import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.domain.PlanoAula;
import com.ravunana.educacao.pedagogico.domain.Nota;
import com.ravunana.educacao.pedagogico.domain.TesteConhecimento;
import com.ravunana.educacao.pedagogico.repository.ProfessorRepository;
import com.ravunana.educacao.pedagogico.repository.search.ProfessorSearchRepository;
import com.ravunana.educacao.pedagogico.service.ProfessorService;
import com.ravunana.educacao.pedagogico.service.dto.ProfessorDTO;
import com.ravunana.educacao.pedagogico.service.mapper.ProfessorMapper;
import com.ravunana.educacao.pedagogico.web.rest.errors.ExceptionTranslator;
import com.ravunana.educacao.pedagogico.service.dto.ProfessorCriteria;
import com.ravunana.educacao.pedagogico.service.ProfessorQueryService;

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
 * Integration tests for the {@link ProfessorResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class ProfessorResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SEXO = "AAAAAAAAAA";
    private static final String UPDATED_SEXO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTOGRAFIA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTOGRAFIA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTOGRAFIA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTOGRAFIA_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CONTACTO = "AAAAAAAAAA";
    private static final String UPDATED_CONTACTO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_RESIDENCIA = "AAAAAAAAAA";
    private static final String UPDATED_RESIDENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_AGENTE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_AGENTE = "BBBBBBBBBB";

    private static final String DEFAULT_UTILIZADOR_ID = "AAAAAAAAAA";
    private static final String UPDATED_UTILIZADOR_ID = "BBBBBBBBBB";

    private static final String DEFAULT_GRAU_ACADEMICO = "AAAAAAAAAA";
    private static final String UPDATED_GRAU_ACADEMICO = "BBBBBBBBBB";

    private static final String DEFAULT_CURSO_ACADEMICO = "AAAAAAAAAA";
    private static final String UPDATED_CURSO_ACADEMICO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ProfessorMapper professorMapper;

    @Autowired
    private ProfessorService professorService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.ProfessorSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProfessorSearchRepository mockProfessorSearchRepository;

    @Autowired
    private ProfessorQueryService professorQueryService;

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

    private MockMvc restProfessorMockMvc;

    private Professor professor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfessorResource professorResource = new ProfessorResource(professorService, professorQueryService);
        this.restProfessorMockMvc = MockMvcBuilders.standaloneSetup(professorResource)
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
    public static Professor createEntity(EntityManager em) {
        Professor professor = new Professor()
            .nome(DEFAULT_NOME)
            .sexo(DEFAULT_SEXO)
            .fotografia(DEFAULT_FOTOGRAFIA)
            .fotografiaContentType(DEFAULT_FOTOGRAFIA_CONTENT_TYPE)
            .contacto(DEFAULT_CONTACTO)
            .email(DEFAULT_EMAIL)
            .residencia(DEFAULT_RESIDENCIA)
            .numeroAgente(DEFAULT_NUMERO_AGENTE)
            .utilizadorId(DEFAULT_UTILIZADOR_ID)
            .grauAcademico(DEFAULT_GRAU_ACADEMICO)
            .cursoAcademico(DEFAULT_CURSO_ACADEMICO)
            .observacao(DEFAULT_OBSERVACAO)
            .ativo(DEFAULT_ATIVO);
        return professor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professor createUpdatedEntity(EntityManager em) {
        Professor professor = new Professor()
            .nome(UPDATED_NOME)
            .sexo(UPDATED_SEXO)
            .fotografia(UPDATED_FOTOGRAFIA)
            .fotografiaContentType(UPDATED_FOTOGRAFIA_CONTENT_TYPE)
            .contacto(UPDATED_CONTACTO)
            .email(UPDATED_EMAIL)
            .residencia(UPDATED_RESIDENCIA)
            .numeroAgente(UPDATED_NUMERO_AGENTE)
            .utilizadorId(UPDATED_UTILIZADOR_ID)
            .grauAcademico(UPDATED_GRAU_ACADEMICO)
            .cursoAcademico(UPDATED_CURSO_ACADEMICO)
            .observacao(UPDATED_OBSERVACAO)
            .ativo(UPDATED_ATIVO);
        return professor;
    }

    @BeforeEach
    public void initTest() {
        professor = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfessor() throws Exception {
        int databaseSizeBeforeCreate = professorRepository.findAll().size();

        // Create the Professor
        ProfessorDTO professorDTO = professorMapper.toDto(professor);
        restProfessorMockMvc.perform(post("/api/professors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professorDTO)))
            .andExpect(status().isCreated());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeCreate + 1);
        Professor testProfessor = professorList.get(professorList.size() - 1);
        assertThat(testProfessor.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testProfessor.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testProfessor.getFotografia()).isEqualTo(DEFAULT_FOTOGRAFIA);
        assertThat(testProfessor.getFotografiaContentType()).isEqualTo(DEFAULT_FOTOGRAFIA_CONTENT_TYPE);
        assertThat(testProfessor.getContacto()).isEqualTo(DEFAULT_CONTACTO);
        assertThat(testProfessor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfessor.getResidencia()).isEqualTo(DEFAULT_RESIDENCIA);
        assertThat(testProfessor.getNumeroAgente()).isEqualTo(DEFAULT_NUMERO_AGENTE);
        assertThat(testProfessor.getUtilizadorId()).isEqualTo(DEFAULT_UTILIZADOR_ID);
        assertThat(testProfessor.getGrauAcademico()).isEqualTo(DEFAULT_GRAU_ACADEMICO);
        assertThat(testProfessor.getCursoAcademico()).isEqualTo(DEFAULT_CURSO_ACADEMICO);
        assertThat(testProfessor.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testProfessor.isAtivo()).isEqualTo(DEFAULT_ATIVO);

        // Validate the Professor in Elasticsearch
        verify(mockProfessorSearchRepository, times(1)).save(testProfessor);
    }

    @Test
    @Transactional
    public void createProfessorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = professorRepository.findAll().size();

        // Create the Professor with an existing ID
        professor.setId(1L);
        ProfessorDTO professorDTO = professorMapper.toDto(professor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessorMockMvc.perform(post("/api/professors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeCreate);

        // Validate the Professor in Elasticsearch
        verify(mockProfessorSearchRepository, times(0)).save(professor);
    }


    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = professorRepository.findAll().size();
        // set the field null
        professor.setNome(null);

        // Create the Professor, which fails.
        ProfessorDTO professorDTO = professorMapper.toDto(professor);

        restProfessorMockMvc.perform(post("/api/professors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professorDTO)))
            .andExpect(status().isBadRequest());

        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSexoIsRequired() throws Exception {
        int databaseSizeBeforeTest = professorRepository.findAll().size();
        // set the field null
        professor.setSexo(null);

        // Create the Professor, which fails.
        ProfessorDTO professorDTO = professorMapper.toDto(professor);

        restProfessorMockMvc.perform(post("/api/professors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professorDTO)))
            .andExpect(status().isBadRequest());

        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContactoIsRequired() throws Exception {
        int databaseSizeBeforeTest = professorRepository.findAll().size();
        // set the field null
        professor.setContacto(null);

        // Create the Professor, which fails.
        ProfessorDTO professorDTO = professorMapper.toDto(professor);

        restProfessorMockMvc.perform(post("/api/professors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professorDTO)))
            .andExpect(status().isBadRequest());

        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResidenciaIsRequired() throws Exception {
        int databaseSizeBeforeTest = professorRepository.findAll().size();
        // set the field null
        professor.setResidencia(null);

        // Create the Professor, which fails.
        ProfessorDTO professorDTO = professorMapper.toDto(professor);

        restProfessorMockMvc.perform(post("/api/professors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professorDTO)))
            .andExpect(status().isBadRequest());

        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroAgenteIsRequired() throws Exception {
        int databaseSizeBeforeTest = professorRepository.findAll().size();
        // set the field null
        professor.setNumeroAgente(null);

        // Create the Professor, which fails.
        ProfessorDTO professorDTO = professorMapper.toDto(professor);

        restProfessorMockMvc.perform(post("/api/professors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professorDTO)))
            .andExpect(status().isBadRequest());

        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfessors() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList
        restProfessorMockMvc.perform(get("/api/professors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO)))
            .andExpect(jsonPath("$.[*].fotografiaContentType").value(hasItem(DEFAULT_FOTOGRAFIA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fotografia").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTOGRAFIA))))
            .andExpect(jsonPath("$.[*].contacto").value(hasItem(DEFAULT_CONTACTO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].residencia").value(hasItem(DEFAULT_RESIDENCIA)))
            .andExpect(jsonPath("$.[*].numeroAgente").value(hasItem(DEFAULT_NUMERO_AGENTE)))
            .andExpect(jsonPath("$.[*].utilizadorId").value(hasItem(DEFAULT_UTILIZADOR_ID)))
            .andExpect(jsonPath("$.[*].grauAcademico").value(hasItem(DEFAULT_GRAU_ACADEMICO)))
            .andExpect(jsonPath("$.[*].cursoAcademico").value(hasItem(DEFAULT_CURSO_ACADEMICO)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getProfessor() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get the professor
        restProfessorMockMvc.perform(get("/api/professors/{id}", professor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(professor.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO))
            .andExpect(jsonPath("$.fotografiaContentType").value(DEFAULT_FOTOGRAFIA_CONTENT_TYPE))
            .andExpect(jsonPath("$.fotografia").value(Base64Utils.encodeToString(DEFAULT_FOTOGRAFIA)))
            .andExpect(jsonPath("$.contacto").value(DEFAULT_CONTACTO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.residencia").value(DEFAULT_RESIDENCIA))
            .andExpect(jsonPath("$.numeroAgente").value(DEFAULT_NUMERO_AGENTE))
            .andExpect(jsonPath("$.utilizadorId").value(DEFAULT_UTILIZADOR_ID))
            .andExpect(jsonPath("$.grauAcademico").value(DEFAULT_GRAU_ACADEMICO))
            .andExpect(jsonPath("$.cursoAcademico").value(DEFAULT_CURSO_ACADEMICO))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO.toString()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()));
    }


    @Test
    @Transactional
    public void getProfessorsByIdFiltering() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        Long id = professor.getId();

        defaultProfessorShouldBeFound("id.equals=" + id);
        defaultProfessorShouldNotBeFound("id.notEquals=" + id);

        defaultProfessorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProfessorShouldNotBeFound("id.greaterThan=" + id);

        defaultProfessorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProfessorShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProfessorsByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where nome equals to DEFAULT_NOME
        defaultProfessorShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the professorList where nome equals to UPDATED_NOME
        defaultProfessorShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllProfessorsByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where nome not equals to DEFAULT_NOME
        defaultProfessorShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the professorList where nome not equals to UPDATED_NOME
        defaultProfessorShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllProfessorsByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultProfessorShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the professorList where nome equals to UPDATED_NOME
        defaultProfessorShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllProfessorsByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where nome is not null
        defaultProfessorShouldBeFound("nome.specified=true");

        // Get all the professorList where nome is null
        defaultProfessorShouldNotBeFound("nome.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfessorsByNomeContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where nome contains DEFAULT_NOME
        defaultProfessorShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the professorList where nome contains UPDATED_NOME
        defaultProfessorShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    public void getAllProfessorsByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where nome does not contain DEFAULT_NOME
        defaultProfessorShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the professorList where nome does not contain UPDATED_NOME
        defaultProfessorShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }


    @Test
    @Transactional
    public void getAllProfessorsBySexoIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where sexo equals to DEFAULT_SEXO
        defaultProfessorShouldBeFound("sexo.equals=" + DEFAULT_SEXO);

        // Get all the professorList where sexo equals to UPDATED_SEXO
        defaultProfessorShouldNotBeFound("sexo.equals=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    public void getAllProfessorsBySexoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where sexo not equals to DEFAULT_SEXO
        defaultProfessorShouldNotBeFound("sexo.notEquals=" + DEFAULT_SEXO);

        // Get all the professorList where sexo not equals to UPDATED_SEXO
        defaultProfessorShouldBeFound("sexo.notEquals=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    public void getAllProfessorsBySexoIsInShouldWork() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where sexo in DEFAULT_SEXO or UPDATED_SEXO
        defaultProfessorShouldBeFound("sexo.in=" + DEFAULT_SEXO + "," + UPDATED_SEXO);

        // Get all the professorList where sexo equals to UPDATED_SEXO
        defaultProfessorShouldNotBeFound("sexo.in=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    public void getAllProfessorsBySexoIsNullOrNotNull() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where sexo is not null
        defaultProfessorShouldBeFound("sexo.specified=true");

        // Get all the professorList where sexo is null
        defaultProfessorShouldNotBeFound("sexo.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfessorsBySexoContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where sexo contains DEFAULT_SEXO
        defaultProfessorShouldBeFound("sexo.contains=" + DEFAULT_SEXO);

        // Get all the professorList where sexo contains UPDATED_SEXO
        defaultProfessorShouldNotBeFound("sexo.contains=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    public void getAllProfessorsBySexoNotContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where sexo does not contain DEFAULT_SEXO
        defaultProfessorShouldNotBeFound("sexo.doesNotContain=" + DEFAULT_SEXO);

        // Get all the professorList where sexo does not contain UPDATED_SEXO
        defaultProfessorShouldBeFound("sexo.doesNotContain=" + UPDATED_SEXO);
    }


    @Test
    @Transactional
    public void getAllProfessorsByContactoIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where contacto equals to DEFAULT_CONTACTO
        defaultProfessorShouldBeFound("contacto.equals=" + DEFAULT_CONTACTO);

        // Get all the professorList where contacto equals to UPDATED_CONTACTO
        defaultProfessorShouldNotBeFound("contacto.equals=" + UPDATED_CONTACTO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByContactoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where contacto not equals to DEFAULT_CONTACTO
        defaultProfessorShouldNotBeFound("contacto.notEquals=" + DEFAULT_CONTACTO);

        // Get all the professorList where contacto not equals to UPDATED_CONTACTO
        defaultProfessorShouldBeFound("contacto.notEquals=" + UPDATED_CONTACTO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByContactoIsInShouldWork() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where contacto in DEFAULT_CONTACTO or UPDATED_CONTACTO
        defaultProfessorShouldBeFound("contacto.in=" + DEFAULT_CONTACTO + "," + UPDATED_CONTACTO);

        // Get all the professorList where contacto equals to UPDATED_CONTACTO
        defaultProfessorShouldNotBeFound("contacto.in=" + UPDATED_CONTACTO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByContactoIsNullOrNotNull() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where contacto is not null
        defaultProfessorShouldBeFound("contacto.specified=true");

        // Get all the professorList where contacto is null
        defaultProfessorShouldNotBeFound("contacto.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfessorsByContactoContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where contacto contains DEFAULT_CONTACTO
        defaultProfessorShouldBeFound("contacto.contains=" + DEFAULT_CONTACTO);

        // Get all the professorList where contacto contains UPDATED_CONTACTO
        defaultProfessorShouldNotBeFound("contacto.contains=" + UPDATED_CONTACTO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByContactoNotContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where contacto does not contain DEFAULT_CONTACTO
        defaultProfessorShouldNotBeFound("contacto.doesNotContain=" + DEFAULT_CONTACTO);

        // Get all the professorList where contacto does not contain UPDATED_CONTACTO
        defaultProfessorShouldBeFound("contacto.doesNotContain=" + UPDATED_CONTACTO);
    }


    @Test
    @Transactional
    public void getAllProfessorsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where email equals to DEFAULT_EMAIL
        defaultProfessorShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the professorList where email equals to UPDATED_EMAIL
        defaultProfessorShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllProfessorsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where email not equals to DEFAULT_EMAIL
        defaultProfessorShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the professorList where email not equals to UPDATED_EMAIL
        defaultProfessorShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllProfessorsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultProfessorShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the professorList where email equals to UPDATED_EMAIL
        defaultProfessorShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllProfessorsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where email is not null
        defaultProfessorShouldBeFound("email.specified=true");

        // Get all the professorList where email is null
        defaultProfessorShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfessorsByEmailContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where email contains DEFAULT_EMAIL
        defaultProfessorShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the professorList where email contains UPDATED_EMAIL
        defaultProfessorShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllProfessorsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where email does not contain DEFAULT_EMAIL
        defaultProfessorShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the professorList where email does not contain UPDATED_EMAIL
        defaultProfessorShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllProfessorsByResidenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where residencia equals to DEFAULT_RESIDENCIA
        defaultProfessorShouldBeFound("residencia.equals=" + DEFAULT_RESIDENCIA);

        // Get all the professorList where residencia equals to UPDATED_RESIDENCIA
        defaultProfessorShouldNotBeFound("residencia.equals=" + UPDATED_RESIDENCIA);
    }

    @Test
    @Transactional
    public void getAllProfessorsByResidenciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where residencia not equals to DEFAULT_RESIDENCIA
        defaultProfessorShouldNotBeFound("residencia.notEquals=" + DEFAULT_RESIDENCIA);

        // Get all the professorList where residencia not equals to UPDATED_RESIDENCIA
        defaultProfessorShouldBeFound("residencia.notEquals=" + UPDATED_RESIDENCIA);
    }

    @Test
    @Transactional
    public void getAllProfessorsByResidenciaIsInShouldWork() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where residencia in DEFAULT_RESIDENCIA or UPDATED_RESIDENCIA
        defaultProfessorShouldBeFound("residencia.in=" + DEFAULT_RESIDENCIA + "," + UPDATED_RESIDENCIA);

        // Get all the professorList where residencia equals to UPDATED_RESIDENCIA
        defaultProfessorShouldNotBeFound("residencia.in=" + UPDATED_RESIDENCIA);
    }

    @Test
    @Transactional
    public void getAllProfessorsByResidenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where residencia is not null
        defaultProfessorShouldBeFound("residencia.specified=true");

        // Get all the professorList where residencia is null
        defaultProfessorShouldNotBeFound("residencia.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfessorsByResidenciaContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where residencia contains DEFAULT_RESIDENCIA
        defaultProfessorShouldBeFound("residencia.contains=" + DEFAULT_RESIDENCIA);

        // Get all the professorList where residencia contains UPDATED_RESIDENCIA
        defaultProfessorShouldNotBeFound("residencia.contains=" + UPDATED_RESIDENCIA);
    }

    @Test
    @Transactional
    public void getAllProfessorsByResidenciaNotContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where residencia does not contain DEFAULT_RESIDENCIA
        defaultProfessorShouldNotBeFound("residencia.doesNotContain=" + DEFAULT_RESIDENCIA);

        // Get all the professorList where residencia does not contain UPDATED_RESIDENCIA
        defaultProfessorShouldBeFound("residencia.doesNotContain=" + UPDATED_RESIDENCIA);
    }


    @Test
    @Transactional
    public void getAllProfessorsByNumeroAgenteIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where numeroAgente equals to DEFAULT_NUMERO_AGENTE
        defaultProfessorShouldBeFound("numeroAgente.equals=" + DEFAULT_NUMERO_AGENTE);

        // Get all the professorList where numeroAgente equals to UPDATED_NUMERO_AGENTE
        defaultProfessorShouldNotBeFound("numeroAgente.equals=" + UPDATED_NUMERO_AGENTE);
    }

    @Test
    @Transactional
    public void getAllProfessorsByNumeroAgenteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where numeroAgente not equals to DEFAULT_NUMERO_AGENTE
        defaultProfessorShouldNotBeFound("numeroAgente.notEquals=" + DEFAULT_NUMERO_AGENTE);

        // Get all the professorList where numeroAgente not equals to UPDATED_NUMERO_AGENTE
        defaultProfessorShouldBeFound("numeroAgente.notEquals=" + UPDATED_NUMERO_AGENTE);
    }

    @Test
    @Transactional
    public void getAllProfessorsByNumeroAgenteIsInShouldWork() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where numeroAgente in DEFAULT_NUMERO_AGENTE or UPDATED_NUMERO_AGENTE
        defaultProfessorShouldBeFound("numeroAgente.in=" + DEFAULT_NUMERO_AGENTE + "," + UPDATED_NUMERO_AGENTE);

        // Get all the professorList where numeroAgente equals to UPDATED_NUMERO_AGENTE
        defaultProfessorShouldNotBeFound("numeroAgente.in=" + UPDATED_NUMERO_AGENTE);
    }

    @Test
    @Transactional
    public void getAllProfessorsByNumeroAgenteIsNullOrNotNull() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where numeroAgente is not null
        defaultProfessorShouldBeFound("numeroAgente.specified=true");

        // Get all the professorList where numeroAgente is null
        defaultProfessorShouldNotBeFound("numeroAgente.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfessorsByNumeroAgenteContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where numeroAgente contains DEFAULT_NUMERO_AGENTE
        defaultProfessorShouldBeFound("numeroAgente.contains=" + DEFAULT_NUMERO_AGENTE);

        // Get all the professorList where numeroAgente contains UPDATED_NUMERO_AGENTE
        defaultProfessorShouldNotBeFound("numeroAgente.contains=" + UPDATED_NUMERO_AGENTE);
    }

    @Test
    @Transactional
    public void getAllProfessorsByNumeroAgenteNotContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where numeroAgente does not contain DEFAULT_NUMERO_AGENTE
        defaultProfessorShouldNotBeFound("numeroAgente.doesNotContain=" + DEFAULT_NUMERO_AGENTE);

        // Get all the professorList where numeroAgente does not contain UPDATED_NUMERO_AGENTE
        defaultProfessorShouldBeFound("numeroAgente.doesNotContain=" + UPDATED_NUMERO_AGENTE);
    }


    @Test
    @Transactional
    public void getAllProfessorsByUtilizadorIdIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where utilizadorId equals to DEFAULT_UTILIZADOR_ID
        defaultProfessorShouldBeFound("utilizadorId.equals=" + DEFAULT_UTILIZADOR_ID);

        // Get all the professorList where utilizadorId equals to UPDATED_UTILIZADOR_ID
        defaultProfessorShouldNotBeFound("utilizadorId.equals=" + UPDATED_UTILIZADOR_ID);
    }

    @Test
    @Transactional
    public void getAllProfessorsByUtilizadorIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where utilizadorId not equals to DEFAULT_UTILIZADOR_ID
        defaultProfessorShouldNotBeFound("utilizadorId.notEquals=" + DEFAULT_UTILIZADOR_ID);

        // Get all the professorList where utilizadorId not equals to UPDATED_UTILIZADOR_ID
        defaultProfessorShouldBeFound("utilizadorId.notEquals=" + UPDATED_UTILIZADOR_ID);
    }

    @Test
    @Transactional
    public void getAllProfessorsByUtilizadorIdIsInShouldWork() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where utilizadorId in DEFAULT_UTILIZADOR_ID or UPDATED_UTILIZADOR_ID
        defaultProfessorShouldBeFound("utilizadorId.in=" + DEFAULT_UTILIZADOR_ID + "," + UPDATED_UTILIZADOR_ID);

        // Get all the professorList where utilizadorId equals to UPDATED_UTILIZADOR_ID
        defaultProfessorShouldNotBeFound("utilizadorId.in=" + UPDATED_UTILIZADOR_ID);
    }

    @Test
    @Transactional
    public void getAllProfessorsByUtilizadorIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where utilizadorId is not null
        defaultProfessorShouldBeFound("utilizadorId.specified=true");

        // Get all the professorList where utilizadorId is null
        defaultProfessorShouldNotBeFound("utilizadorId.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfessorsByUtilizadorIdContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where utilizadorId contains DEFAULT_UTILIZADOR_ID
        defaultProfessorShouldBeFound("utilizadorId.contains=" + DEFAULT_UTILIZADOR_ID);

        // Get all the professorList where utilizadorId contains UPDATED_UTILIZADOR_ID
        defaultProfessorShouldNotBeFound("utilizadorId.contains=" + UPDATED_UTILIZADOR_ID);
    }

    @Test
    @Transactional
    public void getAllProfessorsByUtilizadorIdNotContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where utilizadorId does not contain DEFAULT_UTILIZADOR_ID
        defaultProfessorShouldNotBeFound("utilizadorId.doesNotContain=" + DEFAULT_UTILIZADOR_ID);

        // Get all the professorList where utilizadorId does not contain UPDATED_UTILIZADOR_ID
        defaultProfessorShouldBeFound("utilizadorId.doesNotContain=" + UPDATED_UTILIZADOR_ID);
    }


    @Test
    @Transactional
    public void getAllProfessorsByGrauAcademicoIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where grauAcademico equals to DEFAULT_GRAU_ACADEMICO
        defaultProfessorShouldBeFound("grauAcademico.equals=" + DEFAULT_GRAU_ACADEMICO);

        // Get all the professorList where grauAcademico equals to UPDATED_GRAU_ACADEMICO
        defaultProfessorShouldNotBeFound("grauAcademico.equals=" + UPDATED_GRAU_ACADEMICO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByGrauAcademicoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where grauAcademico not equals to DEFAULT_GRAU_ACADEMICO
        defaultProfessorShouldNotBeFound("grauAcademico.notEquals=" + DEFAULT_GRAU_ACADEMICO);

        // Get all the professorList where grauAcademico not equals to UPDATED_GRAU_ACADEMICO
        defaultProfessorShouldBeFound("grauAcademico.notEquals=" + UPDATED_GRAU_ACADEMICO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByGrauAcademicoIsInShouldWork() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where grauAcademico in DEFAULT_GRAU_ACADEMICO or UPDATED_GRAU_ACADEMICO
        defaultProfessorShouldBeFound("grauAcademico.in=" + DEFAULT_GRAU_ACADEMICO + "," + UPDATED_GRAU_ACADEMICO);

        // Get all the professorList where grauAcademico equals to UPDATED_GRAU_ACADEMICO
        defaultProfessorShouldNotBeFound("grauAcademico.in=" + UPDATED_GRAU_ACADEMICO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByGrauAcademicoIsNullOrNotNull() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where grauAcademico is not null
        defaultProfessorShouldBeFound("grauAcademico.specified=true");

        // Get all the professorList where grauAcademico is null
        defaultProfessorShouldNotBeFound("grauAcademico.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfessorsByGrauAcademicoContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where grauAcademico contains DEFAULT_GRAU_ACADEMICO
        defaultProfessorShouldBeFound("grauAcademico.contains=" + DEFAULT_GRAU_ACADEMICO);

        // Get all the professorList where grauAcademico contains UPDATED_GRAU_ACADEMICO
        defaultProfessorShouldNotBeFound("grauAcademico.contains=" + UPDATED_GRAU_ACADEMICO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByGrauAcademicoNotContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where grauAcademico does not contain DEFAULT_GRAU_ACADEMICO
        defaultProfessorShouldNotBeFound("grauAcademico.doesNotContain=" + DEFAULT_GRAU_ACADEMICO);

        // Get all the professorList where grauAcademico does not contain UPDATED_GRAU_ACADEMICO
        defaultProfessorShouldBeFound("grauAcademico.doesNotContain=" + UPDATED_GRAU_ACADEMICO);
    }


    @Test
    @Transactional
    public void getAllProfessorsByCursoAcademicoIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where cursoAcademico equals to DEFAULT_CURSO_ACADEMICO
        defaultProfessorShouldBeFound("cursoAcademico.equals=" + DEFAULT_CURSO_ACADEMICO);

        // Get all the professorList where cursoAcademico equals to UPDATED_CURSO_ACADEMICO
        defaultProfessorShouldNotBeFound("cursoAcademico.equals=" + UPDATED_CURSO_ACADEMICO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByCursoAcademicoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where cursoAcademico not equals to DEFAULT_CURSO_ACADEMICO
        defaultProfessorShouldNotBeFound("cursoAcademico.notEquals=" + DEFAULT_CURSO_ACADEMICO);

        // Get all the professorList where cursoAcademico not equals to UPDATED_CURSO_ACADEMICO
        defaultProfessorShouldBeFound("cursoAcademico.notEquals=" + UPDATED_CURSO_ACADEMICO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByCursoAcademicoIsInShouldWork() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where cursoAcademico in DEFAULT_CURSO_ACADEMICO or UPDATED_CURSO_ACADEMICO
        defaultProfessorShouldBeFound("cursoAcademico.in=" + DEFAULT_CURSO_ACADEMICO + "," + UPDATED_CURSO_ACADEMICO);

        // Get all the professorList where cursoAcademico equals to UPDATED_CURSO_ACADEMICO
        defaultProfessorShouldNotBeFound("cursoAcademico.in=" + UPDATED_CURSO_ACADEMICO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByCursoAcademicoIsNullOrNotNull() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where cursoAcademico is not null
        defaultProfessorShouldBeFound("cursoAcademico.specified=true");

        // Get all the professorList where cursoAcademico is null
        defaultProfessorShouldNotBeFound("cursoAcademico.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfessorsByCursoAcademicoContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where cursoAcademico contains DEFAULT_CURSO_ACADEMICO
        defaultProfessorShouldBeFound("cursoAcademico.contains=" + DEFAULT_CURSO_ACADEMICO);

        // Get all the professorList where cursoAcademico contains UPDATED_CURSO_ACADEMICO
        defaultProfessorShouldNotBeFound("cursoAcademico.contains=" + UPDATED_CURSO_ACADEMICO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByCursoAcademicoNotContainsSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where cursoAcademico does not contain DEFAULT_CURSO_ACADEMICO
        defaultProfessorShouldNotBeFound("cursoAcademico.doesNotContain=" + DEFAULT_CURSO_ACADEMICO);

        // Get all the professorList where cursoAcademico does not contain UPDATED_CURSO_ACADEMICO
        defaultProfessorShouldBeFound("cursoAcademico.doesNotContain=" + UPDATED_CURSO_ACADEMICO);
    }


    @Test
    @Transactional
    public void getAllProfessorsByAtivoIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where ativo equals to DEFAULT_ATIVO
        defaultProfessorShouldBeFound("ativo.equals=" + DEFAULT_ATIVO);

        // Get all the professorList where ativo equals to UPDATED_ATIVO
        defaultProfessorShouldNotBeFound("ativo.equals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByAtivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where ativo not equals to DEFAULT_ATIVO
        defaultProfessorShouldNotBeFound("ativo.notEquals=" + DEFAULT_ATIVO);

        // Get all the professorList where ativo not equals to UPDATED_ATIVO
        defaultProfessorShouldBeFound("ativo.notEquals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByAtivoIsInShouldWork() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where ativo in DEFAULT_ATIVO or UPDATED_ATIVO
        defaultProfessorShouldBeFound("ativo.in=" + DEFAULT_ATIVO + "," + UPDATED_ATIVO);

        // Get all the professorList where ativo equals to UPDATED_ATIVO
        defaultProfessorShouldNotBeFound("ativo.in=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    public void getAllProfessorsByAtivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList where ativo is not null
        defaultProfessorShouldBeFound("ativo.specified=true");

        // Get all the professorList where ativo is null
        defaultProfessorShouldNotBeFound("ativo.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfessorsByHorarioIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);
        Horario horario = HorarioResourceIT.createEntity(em);
        em.persist(horario);
        em.flush();
        professor.addHorario(horario);
        professorRepository.saveAndFlush(professor);
        Long horarioId = horario.getId();

        // Get all the professorList where horario equals to horarioId
        defaultProfessorShouldBeFound("horarioId.equals=" + horarioId);

        // Get all the professorList where horario equals to horarioId + 1
        defaultProfessorShouldNotBeFound("horarioId.equals=" + (horarioId + 1));
    }


    @Test
    @Transactional
    public void getAllProfessorsByTurmaIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);
        Turma turma = TurmaResourceIT.createEntity(em);
        em.persist(turma);
        em.flush();
        professor.addTurma(turma);
        professorRepository.saveAndFlush(professor);
        Long turmaId = turma.getId();

        // Get all the professorList where turma equals to turmaId
        defaultProfessorShouldBeFound("turmaId.equals=" + turmaId);

        // Get all the professorList where turma equals to turmaId + 1
        defaultProfessorShouldNotBeFound("turmaId.equals=" + (turmaId + 1));
    }


    @Test
    @Transactional
    public void getAllProfessorsByPlanoAulaIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);
        PlanoAula planoAula = PlanoAulaResourceIT.createEntity(em);
        em.persist(planoAula);
        em.flush();
        professor.addPlanoAula(planoAula);
        professorRepository.saveAndFlush(professor);
        Long planoAulaId = planoAula.getId();

        // Get all the professorList where planoAula equals to planoAulaId
        defaultProfessorShouldBeFound("planoAulaId.equals=" + planoAulaId);

        // Get all the professorList where planoAula equals to planoAulaId + 1
        defaultProfessorShouldNotBeFound("planoAulaId.equals=" + (planoAulaId + 1));
    }


    @Test
    @Transactional
    public void getAllProfessorsByNotaIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);
        Nota nota = NotaResourceIT.createEntity(em);
        em.persist(nota);
        em.flush();
        professor.addNota(nota);
        professorRepository.saveAndFlush(professor);
        Long notaId = nota.getId();

        // Get all the professorList where nota equals to notaId
        defaultProfessorShouldBeFound("notaId.equals=" + notaId);

        // Get all the professorList where nota equals to notaId + 1
        defaultProfessorShouldNotBeFound("notaId.equals=" + (notaId + 1));
    }


    @Test
    @Transactional
    public void getAllProfessorsByTesteConhecimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);
        TesteConhecimento testeConhecimento = TesteConhecimentoResourceIT.createEntity(em);
        em.persist(testeConhecimento);
        em.flush();
        professor.addTesteConhecimento(testeConhecimento);
        professorRepository.saveAndFlush(professor);
        Long testeConhecimentoId = testeConhecimento.getId();

        // Get all the professorList where testeConhecimento equals to testeConhecimentoId
        defaultProfessorShouldBeFound("testeConhecimentoId.equals=" + testeConhecimentoId);

        // Get all the professorList where testeConhecimento equals to testeConhecimentoId + 1
        defaultProfessorShouldNotBeFound("testeConhecimentoId.equals=" + (testeConhecimentoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProfessorShouldBeFound(String filter) throws Exception {
        restProfessorMockMvc.perform(get("/api/professors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO)))
            .andExpect(jsonPath("$.[*].fotografiaContentType").value(hasItem(DEFAULT_FOTOGRAFIA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fotografia").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTOGRAFIA))))
            .andExpect(jsonPath("$.[*].contacto").value(hasItem(DEFAULT_CONTACTO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].residencia").value(hasItem(DEFAULT_RESIDENCIA)))
            .andExpect(jsonPath("$.[*].numeroAgente").value(hasItem(DEFAULT_NUMERO_AGENTE)))
            .andExpect(jsonPath("$.[*].utilizadorId").value(hasItem(DEFAULT_UTILIZADOR_ID)))
            .andExpect(jsonPath("$.[*].grauAcademico").value(hasItem(DEFAULT_GRAU_ACADEMICO)))
            .andExpect(jsonPath("$.[*].cursoAcademico").value(hasItem(DEFAULT_CURSO_ACADEMICO)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));

        // Check, that the count call also returns 1
        restProfessorMockMvc.perform(get("/api/professors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProfessorShouldNotBeFound(String filter) throws Exception {
        restProfessorMockMvc.perform(get("/api/professors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfessorMockMvc.perform(get("/api/professors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProfessor() throws Exception {
        // Get the professor
        restProfessorMockMvc.perform(get("/api/professors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfessor() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        int databaseSizeBeforeUpdate = professorRepository.findAll().size();

        // Update the professor
        Professor updatedProfessor = professorRepository.findById(professor.getId()).get();
        // Disconnect from session so that the updates on updatedProfessor are not directly saved in db
        em.detach(updatedProfessor);
        updatedProfessor
            .nome(UPDATED_NOME)
            .sexo(UPDATED_SEXO)
            .fotografia(UPDATED_FOTOGRAFIA)
            .fotografiaContentType(UPDATED_FOTOGRAFIA_CONTENT_TYPE)
            .contacto(UPDATED_CONTACTO)
            .email(UPDATED_EMAIL)
            .residencia(UPDATED_RESIDENCIA)
            .numeroAgente(UPDATED_NUMERO_AGENTE)
            .utilizadorId(UPDATED_UTILIZADOR_ID)
            .grauAcademico(UPDATED_GRAU_ACADEMICO)
            .cursoAcademico(UPDATED_CURSO_ACADEMICO)
            .observacao(UPDATED_OBSERVACAO)
            .ativo(UPDATED_ATIVO);
        ProfessorDTO professorDTO = professorMapper.toDto(updatedProfessor);

        restProfessorMockMvc.perform(put("/api/professors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professorDTO)))
            .andExpect(status().isOk());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeUpdate);
        Professor testProfessor = professorList.get(professorList.size() - 1);
        assertThat(testProfessor.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testProfessor.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testProfessor.getFotografia()).isEqualTo(UPDATED_FOTOGRAFIA);
        assertThat(testProfessor.getFotografiaContentType()).isEqualTo(UPDATED_FOTOGRAFIA_CONTENT_TYPE);
        assertThat(testProfessor.getContacto()).isEqualTo(UPDATED_CONTACTO);
        assertThat(testProfessor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfessor.getResidencia()).isEqualTo(UPDATED_RESIDENCIA);
        assertThat(testProfessor.getNumeroAgente()).isEqualTo(UPDATED_NUMERO_AGENTE);
        assertThat(testProfessor.getUtilizadorId()).isEqualTo(UPDATED_UTILIZADOR_ID);
        assertThat(testProfessor.getGrauAcademico()).isEqualTo(UPDATED_GRAU_ACADEMICO);
        assertThat(testProfessor.getCursoAcademico()).isEqualTo(UPDATED_CURSO_ACADEMICO);
        assertThat(testProfessor.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testProfessor.isAtivo()).isEqualTo(UPDATED_ATIVO);

        // Validate the Professor in Elasticsearch
        verify(mockProfessorSearchRepository, times(1)).save(testProfessor);
    }

    @Test
    @Transactional
    public void updateNonExistingProfessor() throws Exception {
        int databaseSizeBeforeUpdate = professorRepository.findAll().size();

        // Create the Professor
        ProfessorDTO professorDTO = professorMapper.toDto(professor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessorMockMvc.perform(put("/api/professors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Professor in Elasticsearch
        verify(mockProfessorSearchRepository, times(0)).save(professor);
    }

    @Test
    @Transactional
    public void deleteProfessor() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        int databaseSizeBeforeDelete = professorRepository.findAll().size();

        // Delete the professor
        restProfessorMockMvc.perform(delete("/api/professors/{id}", professor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Professor in Elasticsearch
        verify(mockProfessorSearchRepository, times(1)).deleteById(professor.getId());
    }

    @Test
    @Transactional
    public void searchProfessor() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);
        when(mockProfessorSearchRepository.search(queryStringQuery("id:" + professor.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(professor), PageRequest.of(0, 1), 1));
        // Search the professor
        restProfessorMockMvc.perform(get("/api/_search/professors?query=id:" + professor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO)))
            .andExpect(jsonPath("$.[*].fotografiaContentType").value(hasItem(DEFAULT_FOTOGRAFIA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fotografia").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTOGRAFIA))))
            .andExpect(jsonPath("$.[*].contacto").value(hasItem(DEFAULT_CONTACTO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].residencia").value(hasItem(DEFAULT_RESIDENCIA)))
            .andExpect(jsonPath("$.[*].numeroAgente").value(hasItem(DEFAULT_NUMERO_AGENTE)))
            .andExpect(jsonPath("$.[*].utilizadorId").value(hasItem(DEFAULT_UTILIZADOR_ID)))
            .andExpect(jsonPath("$.[*].grauAcademico").value(hasItem(DEFAULT_GRAU_ACADEMICO)))
            .andExpect(jsonPath("$.[*].cursoAcademico").value(hasItem(DEFAULT_CURSO_ACADEMICO)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }
}
