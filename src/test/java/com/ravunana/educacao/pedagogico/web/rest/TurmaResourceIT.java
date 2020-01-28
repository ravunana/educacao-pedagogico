package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.domain.Horario;
import com.ravunana.educacao.pedagogico.domain.PlanoActividade;
import com.ravunana.educacao.pedagogico.domain.Nota;
import com.ravunana.educacao.pedagogico.domain.Aula;
import com.ravunana.educacao.pedagogico.domain.TesteConhecimento;
import com.ravunana.educacao.pedagogico.domain.Curso;
import com.ravunana.educacao.pedagogico.domain.Professor;
import com.ravunana.educacao.pedagogico.domain.PlanoAula;
import com.ravunana.educacao.pedagogico.repository.TurmaRepository;
import com.ravunana.educacao.pedagogico.repository.search.TurmaSearchRepository;
import com.ravunana.educacao.pedagogico.service.TurmaService;
import com.ravunana.educacao.pedagogico.service.dto.TurmaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.TurmaMapper;
import com.ravunana.educacao.pedagogico.web.rest.errors.ExceptionTranslator;
import com.ravunana.educacao.pedagogico.service.dto.TurmaCriteria;
import com.ravunana.educacao.pedagogico.service.TurmaQueryService;

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
import java.time.LocalDate;
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
 * Integration tests for the {@link TurmaResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class TurmaResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANO_LECTIVO = 1;
    private static final Integer UPDATED_ANO_LECTIVO = 2;
    private static final Integer SMALLER_ANO_LECTIVO = 1 - 1;

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final LocalDate DEFAULT_ABERTURA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ABERTURA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ABERTURA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_ENCERRAMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENCERRAMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ENCERRAMENTO = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_LOTACAO = 1;
    private static final Integer UPDATED_LOTACAO = 2;
    private static final Integer SMALLER_LOTACAO = 1 - 1;

    private static final Boolean DEFAULT_ABERTA = false;
    private static final Boolean UPDATED_ABERTA = true;

    private static final String DEFAULT_PERIODO_LECTIVO = "AAAAAAAAAA";
    private static final String UPDATED_PERIODO_LECTIVO = "BBBBBBBBBB";

    private static final String DEFAULT_TURNO = "AAAAAAAAAA";
    private static final String UPDATED_TURNO = "BBBBBBBBBB";

    private static final Integer DEFAULT_SALA = 1;
    private static final Integer UPDATED_SALA = 2;
    private static final Integer SMALLER_SALA = 1 - 1;

    private static final Integer DEFAULT_CLASSE = 1;
    private static final Integer UPDATED_CLASSE = 2;
    private static final Integer SMALLER_CLASSE = 1 - 1;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private TurmaMapper turmaMapper;

    @Autowired
    private TurmaService turmaService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.TurmaSearchRepositoryMockConfiguration
     */
    @Autowired
    private TurmaSearchRepository mockTurmaSearchRepository;

    @Autowired
    private TurmaQueryService turmaQueryService;

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

    private MockMvc restTurmaMockMvc;

    private Turma turma;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TurmaResource turmaResource = new TurmaResource(turmaService, turmaQueryService);
        this.restTurmaMockMvc = MockMvcBuilders.standaloneSetup(turmaResource)
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
    public static Turma createEntity(EntityManager em) {
        Turma turma = new Turma()
            .descricao(DEFAULT_DESCRICAO)
            .anoLectivo(DEFAULT_ANO_LECTIVO)
            .data(DEFAULT_DATA)
            .abertura(DEFAULT_ABERTURA)
            .encerramento(DEFAULT_ENCERRAMENTO)
            .lotacao(DEFAULT_LOTACAO)
            .aberta(DEFAULT_ABERTA)
            .periodoLectivo(DEFAULT_PERIODO_LECTIVO)
            .turno(DEFAULT_TURNO)
            .sala(DEFAULT_SALA)
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
        turma.setCurso(curso);
        // Add required entity
        Professor professor;
        if (TestUtil.findAll(em, Professor.class).isEmpty()) {
            professor = ProfessorResourceIT.createEntity(em);
            em.persist(professor);
            em.flush();
        } else {
            professor = TestUtil.findAll(em, Professor.class).get(0);
        }
        turma.setCoordenador(professor);
        return turma;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Turma createUpdatedEntity(EntityManager em) {
        Turma turma = new Turma()
            .descricao(UPDATED_DESCRICAO)
            .anoLectivo(UPDATED_ANO_LECTIVO)
            .data(UPDATED_DATA)
            .abertura(UPDATED_ABERTURA)
            .encerramento(UPDATED_ENCERRAMENTO)
            .lotacao(UPDATED_LOTACAO)
            .aberta(UPDATED_ABERTA)
            .periodoLectivo(UPDATED_PERIODO_LECTIVO)
            .turno(UPDATED_TURNO)
            .sala(UPDATED_SALA)
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
        turma.setCurso(curso);
        // Add required entity
        Professor professor;
        if (TestUtil.findAll(em, Professor.class).isEmpty()) {
            professor = ProfessorResourceIT.createUpdatedEntity(em);
            em.persist(professor);
            em.flush();
        } else {
            professor = TestUtil.findAll(em, Professor.class).get(0);
        }
        turma.setCoordenador(professor);
        return turma;
    }

    @BeforeEach
    public void initTest() {
        turma = createEntity(em);
    }

    @Test
    @Transactional
    public void createTurma() throws Exception {
        int databaseSizeBeforeCreate = turmaRepository.findAll().size();

        // Create the Turma
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);
        restTurmaMockMvc.perform(post("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isCreated());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeCreate + 1);
        Turma testTurma = turmaList.get(turmaList.size() - 1);
        assertThat(testTurma.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testTurma.getAnoLectivo()).isEqualTo(DEFAULT_ANO_LECTIVO);
        assertThat(testTurma.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testTurma.getAbertura()).isEqualTo(DEFAULT_ABERTURA);
        assertThat(testTurma.getEncerramento()).isEqualTo(DEFAULT_ENCERRAMENTO);
        assertThat(testTurma.getLotacao()).isEqualTo(DEFAULT_LOTACAO);
        assertThat(testTurma.isAberta()).isEqualTo(DEFAULT_ABERTA);
        assertThat(testTurma.getPeriodoLectivo()).isEqualTo(DEFAULT_PERIODO_LECTIVO);
        assertThat(testTurma.getTurno()).isEqualTo(DEFAULT_TURNO);
        assertThat(testTurma.getSala()).isEqualTo(DEFAULT_SALA);
        assertThat(testTurma.getClasse()).isEqualTo(DEFAULT_CLASSE);

        // Validate the Turma in Elasticsearch
        verify(mockTurmaSearchRepository, times(1)).save(testTurma);
    }

    @Test
    @Transactional
    public void createTurmaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = turmaRepository.findAll().size();

        // Create the Turma with an existing ID
        turma.setId(1L);
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTurmaMockMvc.perform(post("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Turma in Elasticsearch
        verify(mockTurmaSearchRepository, times(0)).save(turma);
    }


    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = turmaRepository.findAll().size();
        // set the field null
        turma.setDescricao(null);

        // Create the Turma, which fails.
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        restTurmaMockMvc.perform(post("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnoLectivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = turmaRepository.findAll().size();
        // set the field null
        turma.setAnoLectivo(null);

        // Create the Turma, which fails.
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        restTurmaMockMvc.perform(post("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = turmaRepository.findAll().size();
        // set the field null
        turma.setData(null);

        // Create the Turma, which fails.
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        restTurmaMockMvc.perform(post("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAberturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = turmaRepository.findAll().size();
        // set the field null
        turma.setAbertura(null);

        // Create the Turma, which fails.
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        restTurmaMockMvc.perform(post("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEncerramentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = turmaRepository.findAll().size();
        // set the field null
        turma.setEncerramento(null);

        // Create the Turma, which fails.
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        restTurmaMockMvc.perform(post("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLotacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = turmaRepository.findAll().size();
        // set the field null
        turma.setLotacao(null);

        // Create the Turma, which fails.
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        restTurmaMockMvc.perform(post("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAbertaIsRequired() throws Exception {
        int databaseSizeBeforeTest = turmaRepository.findAll().size();
        // set the field null
        turma.setAberta(null);

        // Create the Turma, which fails.
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        restTurmaMockMvc.perform(post("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPeriodoLectivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = turmaRepository.findAll().size();
        // set the field null
        turma.setPeriodoLectivo(null);

        // Create the Turma, which fails.
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        restTurmaMockMvc.perform(post("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTurnoIsRequired() throws Exception {
        int databaseSizeBeforeTest = turmaRepository.findAll().size();
        // set the field null
        turma.setTurno(null);

        // Create the Turma, which fails.
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        restTurmaMockMvc.perform(post("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSalaIsRequired() throws Exception {
        int databaseSizeBeforeTest = turmaRepository.findAll().size();
        // set the field null
        turma.setSala(null);

        // Create the Turma, which fails.
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        restTurmaMockMvc.perform(post("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClasseIsRequired() throws Exception {
        int databaseSizeBeforeTest = turmaRepository.findAll().size();
        // set the field null
        turma.setClasse(null);

        // Create the Turma, which fails.
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        restTurmaMockMvc.perform(post("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTurmas() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList
        restTurmaMockMvc.perform(get("/api/turmas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turma.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].abertura").value(hasItem(DEFAULT_ABERTURA.toString())))
            .andExpect(jsonPath("$.[*].encerramento").value(hasItem(DEFAULT_ENCERRAMENTO.toString())))
            .andExpect(jsonPath("$.[*].lotacao").value(hasItem(DEFAULT_LOTACAO)))
            .andExpect(jsonPath("$.[*].aberta").value(hasItem(DEFAULT_ABERTA.booleanValue())))
            .andExpect(jsonPath("$.[*].periodoLectivo").value(hasItem(DEFAULT_PERIODO_LECTIVO)))
            .andExpect(jsonPath("$.[*].turno").value(hasItem(DEFAULT_TURNO)))
            .andExpect(jsonPath("$.[*].sala").value(hasItem(DEFAULT_SALA)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)));
    }
    
    @Test
    @Transactional
    public void getTurma() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get the turma
        restTurmaMockMvc.perform(get("/api/turmas/{id}", turma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(turma.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.anoLectivo").value(DEFAULT_ANO_LECTIVO))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)))
            .andExpect(jsonPath("$.abertura").value(DEFAULT_ABERTURA.toString()))
            .andExpect(jsonPath("$.encerramento").value(DEFAULT_ENCERRAMENTO.toString()))
            .andExpect(jsonPath("$.lotacao").value(DEFAULT_LOTACAO))
            .andExpect(jsonPath("$.aberta").value(DEFAULT_ABERTA.booleanValue()))
            .andExpect(jsonPath("$.periodoLectivo").value(DEFAULT_PERIODO_LECTIVO))
            .andExpect(jsonPath("$.turno").value(DEFAULT_TURNO))
            .andExpect(jsonPath("$.sala").value(DEFAULT_SALA))
            .andExpect(jsonPath("$.classe").value(DEFAULT_CLASSE));
    }


    @Test
    @Transactional
    public void getTurmasByIdFiltering() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        Long id = turma.getId();

        defaultTurmaShouldBeFound("id.equals=" + id);
        defaultTurmaShouldNotBeFound("id.notEquals=" + id);

        defaultTurmaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTurmaShouldNotBeFound("id.greaterThan=" + id);

        defaultTurmaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTurmaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTurmasByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where descricao equals to DEFAULT_DESCRICAO
        defaultTurmaShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the turmaList where descricao equals to UPDATED_DESCRICAO
        defaultTurmaShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllTurmasByDescricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where descricao not equals to DEFAULT_DESCRICAO
        defaultTurmaShouldNotBeFound("descricao.notEquals=" + DEFAULT_DESCRICAO);

        // Get all the turmaList where descricao not equals to UPDATED_DESCRICAO
        defaultTurmaShouldBeFound("descricao.notEquals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllTurmasByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultTurmaShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the turmaList where descricao equals to UPDATED_DESCRICAO
        defaultTurmaShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllTurmasByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where descricao is not null
        defaultTurmaShouldBeFound("descricao.specified=true");

        // Get all the turmaList where descricao is null
        defaultTurmaShouldNotBeFound("descricao.specified=false");
    }
                @Test
    @Transactional
    public void getAllTurmasByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where descricao contains DEFAULT_DESCRICAO
        defaultTurmaShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the turmaList where descricao contains UPDATED_DESCRICAO
        defaultTurmaShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void getAllTurmasByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where descricao does not contain DEFAULT_DESCRICAO
        defaultTurmaShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the turmaList where descricao does not contain UPDATED_DESCRICAO
        defaultTurmaShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }


    @Test
    @Transactional
    public void getAllTurmasByAnoLectivoIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where anoLectivo equals to DEFAULT_ANO_LECTIVO
        defaultTurmaShouldBeFound("anoLectivo.equals=" + DEFAULT_ANO_LECTIVO);

        // Get all the turmaList where anoLectivo equals to UPDATED_ANO_LECTIVO
        defaultTurmaShouldNotBeFound("anoLectivo.equals=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllTurmasByAnoLectivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where anoLectivo not equals to DEFAULT_ANO_LECTIVO
        defaultTurmaShouldNotBeFound("anoLectivo.notEquals=" + DEFAULT_ANO_LECTIVO);

        // Get all the turmaList where anoLectivo not equals to UPDATED_ANO_LECTIVO
        defaultTurmaShouldBeFound("anoLectivo.notEquals=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllTurmasByAnoLectivoIsInShouldWork() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where anoLectivo in DEFAULT_ANO_LECTIVO or UPDATED_ANO_LECTIVO
        defaultTurmaShouldBeFound("anoLectivo.in=" + DEFAULT_ANO_LECTIVO + "," + UPDATED_ANO_LECTIVO);

        // Get all the turmaList where anoLectivo equals to UPDATED_ANO_LECTIVO
        defaultTurmaShouldNotBeFound("anoLectivo.in=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllTurmasByAnoLectivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where anoLectivo is not null
        defaultTurmaShouldBeFound("anoLectivo.specified=true");

        // Get all the turmaList where anoLectivo is null
        defaultTurmaShouldNotBeFound("anoLectivo.specified=false");
    }

    @Test
    @Transactional
    public void getAllTurmasByAnoLectivoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where anoLectivo is greater than or equal to DEFAULT_ANO_LECTIVO
        defaultTurmaShouldBeFound("anoLectivo.greaterThanOrEqual=" + DEFAULT_ANO_LECTIVO);

        // Get all the turmaList where anoLectivo is greater than or equal to UPDATED_ANO_LECTIVO
        defaultTurmaShouldNotBeFound("anoLectivo.greaterThanOrEqual=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllTurmasByAnoLectivoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where anoLectivo is less than or equal to DEFAULT_ANO_LECTIVO
        defaultTurmaShouldBeFound("anoLectivo.lessThanOrEqual=" + DEFAULT_ANO_LECTIVO);

        // Get all the turmaList where anoLectivo is less than or equal to SMALLER_ANO_LECTIVO
        defaultTurmaShouldNotBeFound("anoLectivo.lessThanOrEqual=" + SMALLER_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllTurmasByAnoLectivoIsLessThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where anoLectivo is less than DEFAULT_ANO_LECTIVO
        defaultTurmaShouldNotBeFound("anoLectivo.lessThan=" + DEFAULT_ANO_LECTIVO);

        // Get all the turmaList where anoLectivo is less than UPDATED_ANO_LECTIVO
        defaultTurmaShouldBeFound("anoLectivo.lessThan=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllTurmasByAnoLectivoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where anoLectivo is greater than DEFAULT_ANO_LECTIVO
        defaultTurmaShouldNotBeFound("anoLectivo.greaterThan=" + DEFAULT_ANO_LECTIVO);

        // Get all the turmaList where anoLectivo is greater than SMALLER_ANO_LECTIVO
        defaultTurmaShouldBeFound("anoLectivo.greaterThan=" + SMALLER_ANO_LECTIVO);
    }


    @Test
    @Transactional
    public void getAllTurmasByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where data equals to DEFAULT_DATA
        defaultTurmaShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the turmaList where data equals to UPDATED_DATA
        defaultTurmaShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllTurmasByDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where data not equals to DEFAULT_DATA
        defaultTurmaShouldNotBeFound("data.notEquals=" + DEFAULT_DATA);

        // Get all the turmaList where data not equals to UPDATED_DATA
        defaultTurmaShouldBeFound("data.notEquals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllTurmasByDataIsInShouldWork() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where data in DEFAULT_DATA or UPDATED_DATA
        defaultTurmaShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the turmaList where data equals to UPDATED_DATA
        defaultTurmaShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllTurmasByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where data is not null
        defaultTurmaShouldBeFound("data.specified=true");

        // Get all the turmaList where data is null
        defaultTurmaShouldNotBeFound("data.specified=false");
    }

    @Test
    @Transactional
    public void getAllTurmasByDataIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where data is greater than or equal to DEFAULT_DATA
        defaultTurmaShouldBeFound("data.greaterThanOrEqual=" + DEFAULT_DATA);

        // Get all the turmaList where data is greater than or equal to UPDATED_DATA
        defaultTurmaShouldNotBeFound("data.greaterThanOrEqual=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllTurmasByDataIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where data is less than or equal to DEFAULT_DATA
        defaultTurmaShouldBeFound("data.lessThanOrEqual=" + DEFAULT_DATA);

        // Get all the turmaList where data is less than or equal to SMALLER_DATA
        defaultTurmaShouldNotBeFound("data.lessThanOrEqual=" + SMALLER_DATA);
    }

    @Test
    @Transactional
    public void getAllTurmasByDataIsLessThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where data is less than DEFAULT_DATA
        defaultTurmaShouldNotBeFound("data.lessThan=" + DEFAULT_DATA);

        // Get all the turmaList where data is less than UPDATED_DATA
        defaultTurmaShouldBeFound("data.lessThan=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllTurmasByDataIsGreaterThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where data is greater than DEFAULT_DATA
        defaultTurmaShouldNotBeFound("data.greaterThan=" + DEFAULT_DATA);

        // Get all the turmaList where data is greater than SMALLER_DATA
        defaultTurmaShouldBeFound("data.greaterThan=" + SMALLER_DATA);
    }


    @Test
    @Transactional
    public void getAllTurmasByAberturaIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where abertura equals to DEFAULT_ABERTURA
        defaultTurmaShouldBeFound("abertura.equals=" + DEFAULT_ABERTURA);

        // Get all the turmaList where abertura equals to UPDATED_ABERTURA
        defaultTurmaShouldNotBeFound("abertura.equals=" + UPDATED_ABERTURA);
    }

    @Test
    @Transactional
    public void getAllTurmasByAberturaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where abertura not equals to DEFAULT_ABERTURA
        defaultTurmaShouldNotBeFound("abertura.notEquals=" + DEFAULT_ABERTURA);

        // Get all the turmaList where abertura not equals to UPDATED_ABERTURA
        defaultTurmaShouldBeFound("abertura.notEquals=" + UPDATED_ABERTURA);
    }

    @Test
    @Transactional
    public void getAllTurmasByAberturaIsInShouldWork() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where abertura in DEFAULT_ABERTURA or UPDATED_ABERTURA
        defaultTurmaShouldBeFound("abertura.in=" + DEFAULT_ABERTURA + "," + UPDATED_ABERTURA);

        // Get all the turmaList where abertura equals to UPDATED_ABERTURA
        defaultTurmaShouldNotBeFound("abertura.in=" + UPDATED_ABERTURA);
    }

    @Test
    @Transactional
    public void getAllTurmasByAberturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where abertura is not null
        defaultTurmaShouldBeFound("abertura.specified=true");

        // Get all the turmaList where abertura is null
        defaultTurmaShouldNotBeFound("abertura.specified=false");
    }

    @Test
    @Transactional
    public void getAllTurmasByAberturaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where abertura is greater than or equal to DEFAULT_ABERTURA
        defaultTurmaShouldBeFound("abertura.greaterThanOrEqual=" + DEFAULT_ABERTURA);

        // Get all the turmaList where abertura is greater than or equal to UPDATED_ABERTURA
        defaultTurmaShouldNotBeFound("abertura.greaterThanOrEqual=" + UPDATED_ABERTURA);
    }

    @Test
    @Transactional
    public void getAllTurmasByAberturaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where abertura is less than or equal to DEFAULT_ABERTURA
        defaultTurmaShouldBeFound("abertura.lessThanOrEqual=" + DEFAULT_ABERTURA);

        // Get all the turmaList where abertura is less than or equal to SMALLER_ABERTURA
        defaultTurmaShouldNotBeFound("abertura.lessThanOrEqual=" + SMALLER_ABERTURA);
    }

    @Test
    @Transactional
    public void getAllTurmasByAberturaIsLessThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where abertura is less than DEFAULT_ABERTURA
        defaultTurmaShouldNotBeFound("abertura.lessThan=" + DEFAULT_ABERTURA);

        // Get all the turmaList where abertura is less than UPDATED_ABERTURA
        defaultTurmaShouldBeFound("abertura.lessThan=" + UPDATED_ABERTURA);
    }

    @Test
    @Transactional
    public void getAllTurmasByAberturaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where abertura is greater than DEFAULT_ABERTURA
        defaultTurmaShouldNotBeFound("abertura.greaterThan=" + DEFAULT_ABERTURA);

        // Get all the turmaList where abertura is greater than SMALLER_ABERTURA
        defaultTurmaShouldBeFound("abertura.greaterThan=" + SMALLER_ABERTURA);
    }


    @Test
    @Transactional
    public void getAllTurmasByEncerramentoIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where encerramento equals to DEFAULT_ENCERRAMENTO
        defaultTurmaShouldBeFound("encerramento.equals=" + DEFAULT_ENCERRAMENTO);

        // Get all the turmaList where encerramento equals to UPDATED_ENCERRAMENTO
        defaultTurmaShouldNotBeFound("encerramento.equals=" + UPDATED_ENCERRAMENTO);
    }

    @Test
    @Transactional
    public void getAllTurmasByEncerramentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where encerramento not equals to DEFAULT_ENCERRAMENTO
        defaultTurmaShouldNotBeFound("encerramento.notEquals=" + DEFAULT_ENCERRAMENTO);

        // Get all the turmaList where encerramento not equals to UPDATED_ENCERRAMENTO
        defaultTurmaShouldBeFound("encerramento.notEquals=" + UPDATED_ENCERRAMENTO);
    }

    @Test
    @Transactional
    public void getAllTurmasByEncerramentoIsInShouldWork() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where encerramento in DEFAULT_ENCERRAMENTO or UPDATED_ENCERRAMENTO
        defaultTurmaShouldBeFound("encerramento.in=" + DEFAULT_ENCERRAMENTO + "," + UPDATED_ENCERRAMENTO);

        // Get all the turmaList where encerramento equals to UPDATED_ENCERRAMENTO
        defaultTurmaShouldNotBeFound("encerramento.in=" + UPDATED_ENCERRAMENTO);
    }

    @Test
    @Transactional
    public void getAllTurmasByEncerramentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where encerramento is not null
        defaultTurmaShouldBeFound("encerramento.specified=true");

        // Get all the turmaList where encerramento is null
        defaultTurmaShouldNotBeFound("encerramento.specified=false");
    }

    @Test
    @Transactional
    public void getAllTurmasByEncerramentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where encerramento is greater than or equal to DEFAULT_ENCERRAMENTO
        defaultTurmaShouldBeFound("encerramento.greaterThanOrEqual=" + DEFAULT_ENCERRAMENTO);

        // Get all the turmaList where encerramento is greater than or equal to UPDATED_ENCERRAMENTO
        defaultTurmaShouldNotBeFound("encerramento.greaterThanOrEqual=" + UPDATED_ENCERRAMENTO);
    }

    @Test
    @Transactional
    public void getAllTurmasByEncerramentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where encerramento is less than or equal to DEFAULT_ENCERRAMENTO
        defaultTurmaShouldBeFound("encerramento.lessThanOrEqual=" + DEFAULT_ENCERRAMENTO);

        // Get all the turmaList where encerramento is less than or equal to SMALLER_ENCERRAMENTO
        defaultTurmaShouldNotBeFound("encerramento.lessThanOrEqual=" + SMALLER_ENCERRAMENTO);
    }

    @Test
    @Transactional
    public void getAllTurmasByEncerramentoIsLessThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where encerramento is less than DEFAULT_ENCERRAMENTO
        defaultTurmaShouldNotBeFound("encerramento.lessThan=" + DEFAULT_ENCERRAMENTO);

        // Get all the turmaList where encerramento is less than UPDATED_ENCERRAMENTO
        defaultTurmaShouldBeFound("encerramento.lessThan=" + UPDATED_ENCERRAMENTO);
    }

    @Test
    @Transactional
    public void getAllTurmasByEncerramentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where encerramento is greater than DEFAULT_ENCERRAMENTO
        defaultTurmaShouldNotBeFound("encerramento.greaterThan=" + DEFAULT_ENCERRAMENTO);

        // Get all the turmaList where encerramento is greater than SMALLER_ENCERRAMENTO
        defaultTurmaShouldBeFound("encerramento.greaterThan=" + SMALLER_ENCERRAMENTO);
    }


    @Test
    @Transactional
    public void getAllTurmasByLotacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where lotacao equals to DEFAULT_LOTACAO
        defaultTurmaShouldBeFound("lotacao.equals=" + DEFAULT_LOTACAO);

        // Get all the turmaList where lotacao equals to UPDATED_LOTACAO
        defaultTurmaShouldNotBeFound("lotacao.equals=" + UPDATED_LOTACAO);
    }

    @Test
    @Transactional
    public void getAllTurmasByLotacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where lotacao not equals to DEFAULT_LOTACAO
        defaultTurmaShouldNotBeFound("lotacao.notEquals=" + DEFAULT_LOTACAO);

        // Get all the turmaList where lotacao not equals to UPDATED_LOTACAO
        defaultTurmaShouldBeFound("lotacao.notEquals=" + UPDATED_LOTACAO);
    }

    @Test
    @Transactional
    public void getAllTurmasByLotacaoIsInShouldWork() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where lotacao in DEFAULT_LOTACAO or UPDATED_LOTACAO
        defaultTurmaShouldBeFound("lotacao.in=" + DEFAULT_LOTACAO + "," + UPDATED_LOTACAO);

        // Get all the turmaList where lotacao equals to UPDATED_LOTACAO
        defaultTurmaShouldNotBeFound("lotacao.in=" + UPDATED_LOTACAO);
    }

    @Test
    @Transactional
    public void getAllTurmasByLotacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where lotacao is not null
        defaultTurmaShouldBeFound("lotacao.specified=true");

        // Get all the turmaList where lotacao is null
        defaultTurmaShouldNotBeFound("lotacao.specified=false");
    }

    @Test
    @Transactional
    public void getAllTurmasByLotacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where lotacao is greater than or equal to DEFAULT_LOTACAO
        defaultTurmaShouldBeFound("lotacao.greaterThanOrEqual=" + DEFAULT_LOTACAO);

        // Get all the turmaList where lotacao is greater than or equal to UPDATED_LOTACAO
        defaultTurmaShouldNotBeFound("lotacao.greaterThanOrEqual=" + UPDATED_LOTACAO);
    }

    @Test
    @Transactional
    public void getAllTurmasByLotacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where lotacao is less than or equal to DEFAULT_LOTACAO
        defaultTurmaShouldBeFound("lotacao.lessThanOrEqual=" + DEFAULT_LOTACAO);

        // Get all the turmaList where lotacao is less than or equal to SMALLER_LOTACAO
        defaultTurmaShouldNotBeFound("lotacao.lessThanOrEqual=" + SMALLER_LOTACAO);
    }

    @Test
    @Transactional
    public void getAllTurmasByLotacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where lotacao is less than DEFAULT_LOTACAO
        defaultTurmaShouldNotBeFound("lotacao.lessThan=" + DEFAULT_LOTACAO);

        // Get all the turmaList where lotacao is less than UPDATED_LOTACAO
        defaultTurmaShouldBeFound("lotacao.lessThan=" + UPDATED_LOTACAO);
    }

    @Test
    @Transactional
    public void getAllTurmasByLotacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where lotacao is greater than DEFAULT_LOTACAO
        defaultTurmaShouldNotBeFound("lotacao.greaterThan=" + DEFAULT_LOTACAO);

        // Get all the turmaList where lotacao is greater than SMALLER_LOTACAO
        defaultTurmaShouldBeFound("lotacao.greaterThan=" + SMALLER_LOTACAO);
    }


    @Test
    @Transactional
    public void getAllTurmasByAbertaIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where aberta equals to DEFAULT_ABERTA
        defaultTurmaShouldBeFound("aberta.equals=" + DEFAULT_ABERTA);

        // Get all the turmaList where aberta equals to UPDATED_ABERTA
        defaultTurmaShouldNotBeFound("aberta.equals=" + UPDATED_ABERTA);
    }

    @Test
    @Transactional
    public void getAllTurmasByAbertaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where aberta not equals to DEFAULT_ABERTA
        defaultTurmaShouldNotBeFound("aberta.notEquals=" + DEFAULT_ABERTA);

        // Get all the turmaList where aberta not equals to UPDATED_ABERTA
        defaultTurmaShouldBeFound("aberta.notEquals=" + UPDATED_ABERTA);
    }

    @Test
    @Transactional
    public void getAllTurmasByAbertaIsInShouldWork() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where aberta in DEFAULT_ABERTA or UPDATED_ABERTA
        defaultTurmaShouldBeFound("aberta.in=" + DEFAULT_ABERTA + "," + UPDATED_ABERTA);

        // Get all the turmaList where aberta equals to UPDATED_ABERTA
        defaultTurmaShouldNotBeFound("aberta.in=" + UPDATED_ABERTA);
    }

    @Test
    @Transactional
    public void getAllTurmasByAbertaIsNullOrNotNull() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where aberta is not null
        defaultTurmaShouldBeFound("aberta.specified=true");

        // Get all the turmaList where aberta is null
        defaultTurmaShouldNotBeFound("aberta.specified=false");
    }

    @Test
    @Transactional
    public void getAllTurmasByPeriodoLectivoIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where periodoLectivo equals to DEFAULT_PERIODO_LECTIVO
        defaultTurmaShouldBeFound("periodoLectivo.equals=" + DEFAULT_PERIODO_LECTIVO);

        // Get all the turmaList where periodoLectivo equals to UPDATED_PERIODO_LECTIVO
        defaultTurmaShouldNotBeFound("periodoLectivo.equals=" + UPDATED_PERIODO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllTurmasByPeriodoLectivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where periodoLectivo not equals to DEFAULT_PERIODO_LECTIVO
        defaultTurmaShouldNotBeFound("periodoLectivo.notEquals=" + DEFAULT_PERIODO_LECTIVO);

        // Get all the turmaList where periodoLectivo not equals to UPDATED_PERIODO_LECTIVO
        defaultTurmaShouldBeFound("periodoLectivo.notEquals=" + UPDATED_PERIODO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllTurmasByPeriodoLectivoIsInShouldWork() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where periodoLectivo in DEFAULT_PERIODO_LECTIVO or UPDATED_PERIODO_LECTIVO
        defaultTurmaShouldBeFound("periodoLectivo.in=" + DEFAULT_PERIODO_LECTIVO + "," + UPDATED_PERIODO_LECTIVO);

        // Get all the turmaList where periodoLectivo equals to UPDATED_PERIODO_LECTIVO
        defaultTurmaShouldNotBeFound("periodoLectivo.in=" + UPDATED_PERIODO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllTurmasByPeriodoLectivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where periodoLectivo is not null
        defaultTurmaShouldBeFound("periodoLectivo.specified=true");

        // Get all the turmaList where periodoLectivo is null
        defaultTurmaShouldNotBeFound("periodoLectivo.specified=false");
    }
                @Test
    @Transactional
    public void getAllTurmasByPeriodoLectivoContainsSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where periodoLectivo contains DEFAULT_PERIODO_LECTIVO
        defaultTurmaShouldBeFound("periodoLectivo.contains=" + DEFAULT_PERIODO_LECTIVO);

        // Get all the turmaList where periodoLectivo contains UPDATED_PERIODO_LECTIVO
        defaultTurmaShouldNotBeFound("periodoLectivo.contains=" + UPDATED_PERIODO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllTurmasByPeriodoLectivoNotContainsSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where periodoLectivo does not contain DEFAULT_PERIODO_LECTIVO
        defaultTurmaShouldNotBeFound("periodoLectivo.doesNotContain=" + DEFAULT_PERIODO_LECTIVO);

        // Get all the turmaList where periodoLectivo does not contain UPDATED_PERIODO_LECTIVO
        defaultTurmaShouldBeFound("periodoLectivo.doesNotContain=" + UPDATED_PERIODO_LECTIVO);
    }


    @Test
    @Transactional
    public void getAllTurmasByTurnoIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where turno equals to DEFAULT_TURNO
        defaultTurmaShouldBeFound("turno.equals=" + DEFAULT_TURNO);

        // Get all the turmaList where turno equals to UPDATED_TURNO
        defaultTurmaShouldNotBeFound("turno.equals=" + UPDATED_TURNO);
    }

    @Test
    @Transactional
    public void getAllTurmasByTurnoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where turno not equals to DEFAULT_TURNO
        defaultTurmaShouldNotBeFound("turno.notEquals=" + DEFAULT_TURNO);

        // Get all the turmaList where turno not equals to UPDATED_TURNO
        defaultTurmaShouldBeFound("turno.notEquals=" + UPDATED_TURNO);
    }

    @Test
    @Transactional
    public void getAllTurmasByTurnoIsInShouldWork() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where turno in DEFAULT_TURNO or UPDATED_TURNO
        defaultTurmaShouldBeFound("turno.in=" + DEFAULT_TURNO + "," + UPDATED_TURNO);

        // Get all the turmaList where turno equals to UPDATED_TURNO
        defaultTurmaShouldNotBeFound("turno.in=" + UPDATED_TURNO);
    }

    @Test
    @Transactional
    public void getAllTurmasByTurnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where turno is not null
        defaultTurmaShouldBeFound("turno.specified=true");

        // Get all the turmaList where turno is null
        defaultTurmaShouldNotBeFound("turno.specified=false");
    }
                @Test
    @Transactional
    public void getAllTurmasByTurnoContainsSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where turno contains DEFAULT_TURNO
        defaultTurmaShouldBeFound("turno.contains=" + DEFAULT_TURNO);

        // Get all the turmaList where turno contains UPDATED_TURNO
        defaultTurmaShouldNotBeFound("turno.contains=" + UPDATED_TURNO);
    }

    @Test
    @Transactional
    public void getAllTurmasByTurnoNotContainsSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where turno does not contain DEFAULT_TURNO
        defaultTurmaShouldNotBeFound("turno.doesNotContain=" + DEFAULT_TURNO);

        // Get all the turmaList where turno does not contain UPDATED_TURNO
        defaultTurmaShouldBeFound("turno.doesNotContain=" + UPDATED_TURNO);
    }


    @Test
    @Transactional
    public void getAllTurmasBySalaIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where sala equals to DEFAULT_SALA
        defaultTurmaShouldBeFound("sala.equals=" + DEFAULT_SALA);

        // Get all the turmaList where sala equals to UPDATED_SALA
        defaultTurmaShouldNotBeFound("sala.equals=" + UPDATED_SALA);
    }

    @Test
    @Transactional
    public void getAllTurmasBySalaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where sala not equals to DEFAULT_SALA
        defaultTurmaShouldNotBeFound("sala.notEquals=" + DEFAULT_SALA);

        // Get all the turmaList where sala not equals to UPDATED_SALA
        defaultTurmaShouldBeFound("sala.notEquals=" + UPDATED_SALA);
    }

    @Test
    @Transactional
    public void getAllTurmasBySalaIsInShouldWork() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where sala in DEFAULT_SALA or UPDATED_SALA
        defaultTurmaShouldBeFound("sala.in=" + DEFAULT_SALA + "," + UPDATED_SALA);

        // Get all the turmaList where sala equals to UPDATED_SALA
        defaultTurmaShouldNotBeFound("sala.in=" + UPDATED_SALA);
    }

    @Test
    @Transactional
    public void getAllTurmasBySalaIsNullOrNotNull() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where sala is not null
        defaultTurmaShouldBeFound("sala.specified=true");

        // Get all the turmaList where sala is null
        defaultTurmaShouldNotBeFound("sala.specified=false");
    }

    @Test
    @Transactional
    public void getAllTurmasBySalaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where sala is greater than or equal to DEFAULT_SALA
        defaultTurmaShouldBeFound("sala.greaterThanOrEqual=" + DEFAULT_SALA);

        // Get all the turmaList where sala is greater than or equal to UPDATED_SALA
        defaultTurmaShouldNotBeFound("sala.greaterThanOrEqual=" + UPDATED_SALA);
    }

    @Test
    @Transactional
    public void getAllTurmasBySalaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where sala is less than or equal to DEFAULT_SALA
        defaultTurmaShouldBeFound("sala.lessThanOrEqual=" + DEFAULT_SALA);

        // Get all the turmaList where sala is less than or equal to SMALLER_SALA
        defaultTurmaShouldNotBeFound("sala.lessThanOrEqual=" + SMALLER_SALA);
    }

    @Test
    @Transactional
    public void getAllTurmasBySalaIsLessThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where sala is less than DEFAULT_SALA
        defaultTurmaShouldNotBeFound("sala.lessThan=" + DEFAULT_SALA);

        // Get all the turmaList where sala is less than UPDATED_SALA
        defaultTurmaShouldBeFound("sala.lessThan=" + UPDATED_SALA);
    }

    @Test
    @Transactional
    public void getAllTurmasBySalaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where sala is greater than DEFAULT_SALA
        defaultTurmaShouldNotBeFound("sala.greaterThan=" + DEFAULT_SALA);

        // Get all the turmaList where sala is greater than SMALLER_SALA
        defaultTurmaShouldBeFound("sala.greaterThan=" + SMALLER_SALA);
    }


    @Test
    @Transactional
    public void getAllTurmasByClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where classe equals to DEFAULT_CLASSE
        defaultTurmaShouldBeFound("classe.equals=" + DEFAULT_CLASSE);

        // Get all the turmaList where classe equals to UPDATED_CLASSE
        defaultTurmaShouldNotBeFound("classe.equals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllTurmasByClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where classe not equals to DEFAULT_CLASSE
        defaultTurmaShouldNotBeFound("classe.notEquals=" + DEFAULT_CLASSE);

        // Get all the turmaList where classe not equals to UPDATED_CLASSE
        defaultTurmaShouldBeFound("classe.notEquals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllTurmasByClasseIsInShouldWork() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where classe in DEFAULT_CLASSE or UPDATED_CLASSE
        defaultTurmaShouldBeFound("classe.in=" + DEFAULT_CLASSE + "," + UPDATED_CLASSE);

        // Get all the turmaList where classe equals to UPDATED_CLASSE
        defaultTurmaShouldNotBeFound("classe.in=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllTurmasByClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where classe is not null
        defaultTurmaShouldBeFound("classe.specified=true");

        // Get all the turmaList where classe is null
        defaultTurmaShouldNotBeFound("classe.specified=false");
    }

    @Test
    @Transactional
    public void getAllTurmasByClasseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where classe is greater than or equal to DEFAULT_CLASSE
        defaultTurmaShouldBeFound("classe.greaterThanOrEqual=" + DEFAULT_CLASSE);

        // Get all the turmaList where classe is greater than or equal to UPDATED_CLASSE
        defaultTurmaShouldNotBeFound("classe.greaterThanOrEqual=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllTurmasByClasseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where classe is less than or equal to DEFAULT_CLASSE
        defaultTurmaShouldBeFound("classe.lessThanOrEqual=" + DEFAULT_CLASSE);

        // Get all the turmaList where classe is less than or equal to SMALLER_CLASSE
        defaultTurmaShouldNotBeFound("classe.lessThanOrEqual=" + SMALLER_CLASSE);
    }

    @Test
    @Transactional
    public void getAllTurmasByClasseIsLessThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where classe is less than DEFAULT_CLASSE
        defaultTurmaShouldNotBeFound("classe.lessThan=" + DEFAULT_CLASSE);

        // Get all the turmaList where classe is less than UPDATED_CLASSE
        defaultTurmaShouldBeFound("classe.lessThan=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllTurmasByClasseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList where classe is greater than DEFAULT_CLASSE
        defaultTurmaShouldNotBeFound("classe.greaterThan=" + DEFAULT_CLASSE);

        // Get all the turmaList where classe is greater than SMALLER_CLASSE
        defaultTurmaShouldBeFound("classe.greaterThan=" + SMALLER_CLASSE);
    }


    @Test
    @Transactional
    public void getAllTurmasByHorarioIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);
        Horario horario = HorarioResourceIT.createEntity(em);
        em.persist(horario);
        em.flush();
        turma.addHorario(horario);
        turmaRepository.saveAndFlush(turma);
        Long horarioId = horario.getId();

        // Get all the turmaList where horario equals to horarioId
        defaultTurmaShouldBeFound("horarioId.equals=" + horarioId);

        // Get all the turmaList where horario equals to horarioId + 1
        defaultTurmaShouldNotBeFound("horarioId.equals=" + (horarioId + 1));
    }


    @Test
    @Transactional
    public void getAllTurmasByPlanoActividadeIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);
        PlanoActividade planoActividade = PlanoActividadeResourceIT.createEntity(em);
        em.persist(planoActividade);
        em.flush();
        turma.addPlanoActividade(planoActividade);
        turmaRepository.saveAndFlush(turma);
        Long planoActividadeId = planoActividade.getId();

        // Get all the turmaList where planoActividade equals to planoActividadeId
        defaultTurmaShouldBeFound("planoActividadeId.equals=" + planoActividadeId);

        // Get all the turmaList where planoActividade equals to planoActividadeId + 1
        defaultTurmaShouldNotBeFound("planoActividadeId.equals=" + (planoActividadeId + 1));
    }


    @Test
    @Transactional
    public void getAllTurmasByNotaIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);
        Nota nota = NotaResourceIT.createEntity(em);
        em.persist(nota);
        em.flush();
        turma.addNota(nota);
        turmaRepository.saveAndFlush(turma);
        Long notaId = nota.getId();

        // Get all the turmaList where nota equals to notaId
        defaultTurmaShouldBeFound("notaId.equals=" + notaId);

        // Get all the turmaList where nota equals to notaId + 1
        defaultTurmaShouldNotBeFound("notaId.equals=" + (notaId + 1));
    }


    @Test
    @Transactional
    public void getAllTurmasByAulaIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);
        Aula aula = AulaResourceIT.createEntity(em);
        em.persist(aula);
        em.flush();
        turma.addAula(aula);
        turmaRepository.saveAndFlush(turma);
        Long aulaId = aula.getId();

        // Get all the turmaList where aula equals to aulaId
        defaultTurmaShouldBeFound("aulaId.equals=" + aulaId);

        // Get all the turmaList where aula equals to aulaId + 1
        defaultTurmaShouldNotBeFound("aulaId.equals=" + (aulaId + 1));
    }


    @Test
    @Transactional
    public void getAllTurmasByTesteConhecimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);
        TesteConhecimento testeConhecimento = TesteConhecimentoResourceIT.createEntity(em);
        em.persist(testeConhecimento);
        em.flush();
        turma.addTesteConhecimento(testeConhecimento);
        turmaRepository.saveAndFlush(turma);
        Long testeConhecimentoId = testeConhecimento.getId();

        // Get all the turmaList where testeConhecimento equals to testeConhecimentoId
        defaultTurmaShouldBeFound("testeConhecimentoId.equals=" + testeConhecimentoId);

        // Get all the turmaList where testeConhecimento equals to testeConhecimentoId + 1
        defaultTurmaShouldNotBeFound("testeConhecimentoId.equals=" + (testeConhecimentoId + 1));
    }


    @Test
    @Transactional
    public void getAllTurmasByCursoIsEqualToSomething() throws Exception {
        // Get already existing entity
        Curso curso = turma.getCurso();
        turmaRepository.saveAndFlush(turma);
        Long cursoId = curso.getId();

        // Get all the turmaList where curso equals to cursoId
        defaultTurmaShouldBeFound("cursoId.equals=" + cursoId);

        // Get all the turmaList where curso equals to cursoId + 1
        defaultTurmaShouldNotBeFound("cursoId.equals=" + (cursoId + 1));
    }


    @Test
    @Transactional
    public void getAllTurmasByCoordenadorIsEqualToSomething() throws Exception {
        // Get already existing entity
        Professor coordenador = turma.getCoordenador();
        turmaRepository.saveAndFlush(turma);
        Long coordenadorId = coordenador.getId();

        // Get all the turmaList where coordenador equals to coordenadorId
        defaultTurmaShouldBeFound("coordenadorId.equals=" + coordenadorId);

        // Get all the turmaList where coordenador equals to coordenadorId + 1
        defaultTurmaShouldNotBeFound("coordenadorId.equals=" + (coordenadorId + 1));
    }


    @Test
    @Transactional
    public void getAllTurmasByPlanoAulaTurmaIsEqualToSomething() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);
        PlanoAula planoAulaTurma = PlanoAulaResourceIT.createEntity(em);
        em.persist(planoAulaTurma);
        em.flush();
        turma.addPlanoAulaTurma(planoAulaTurma);
        turmaRepository.saveAndFlush(turma);
        Long planoAulaTurmaId = planoAulaTurma.getId();

        // Get all the turmaList where planoAulaTurma equals to planoAulaTurmaId
        defaultTurmaShouldBeFound("planoAulaTurmaId.equals=" + planoAulaTurmaId);

        // Get all the turmaList where planoAulaTurma equals to planoAulaTurmaId + 1
        defaultTurmaShouldNotBeFound("planoAulaTurmaId.equals=" + (planoAulaTurmaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTurmaShouldBeFound(String filter) throws Exception {
        restTurmaMockMvc.perform(get("/api/turmas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turma.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].abertura").value(hasItem(DEFAULT_ABERTURA.toString())))
            .andExpect(jsonPath("$.[*].encerramento").value(hasItem(DEFAULT_ENCERRAMENTO.toString())))
            .andExpect(jsonPath("$.[*].lotacao").value(hasItem(DEFAULT_LOTACAO)))
            .andExpect(jsonPath("$.[*].aberta").value(hasItem(DEFAULT_ABERTA.booleanValue())))
            .andExpect(jsonPath("$.[*].periodoLectivo").value(hasItem(DEFAULT_PERIODO_LECTIVO)))
            .andExpect(jsonPath("$.[*].turno").value(hasItem(DEFAULT_TURNO)))
            .andExpect(jsonPath("$.[*].sala").value(hasItem(DEFAULT_SALA)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)));

        // Check, that the count call also returns 1
        restTurmaMockMvc.perform(get("/api/turmas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTurmaShouldNotBeFound(String filter) throws Exception {
        restTurmaMockMvc.perform(get("/api/turmas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTurmaMockMvc.perform(get("/api/turmas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTurma() throws Exception {
        // Get the turma
        restTurmaMockMvc.perform(get("/api/turmas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTurma() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();

        // Update the turma
        Turma updatedTurma = turmaRepository.findById(turma.getId()).get();
        // Disconnect from session so that the updates on updatedTurma are not directly saved in db
        em.detach(updatedTurma);
        updatedTurma
            .descricao(UPDATED_DESCRICAO)
            .anoLectivo(UPDATED_ANO_LECTIVO)
            .data(UPDATED_DATA)
            .abertura(UPDATED_ABERTURA)
            .encerramento(UPDATED_ENCERRAMENTO)
            .lotacao(UPDATED_LOTACAO)
            .aberta(UPDATED_ABERTA)
            .periodoLectivo(UPDATED_PERIODO_LECTIVO)
            .turno(UPDATED_TURNO)
            .sala(UPDATED_SALA)
            .classe(UPDATED_CLASSE);
        TurmaDTO turmaDTO = turmaMapper.toDto(updatedTurma);

        restTurmaMockMvc.perform(put("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isOk());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
        Turma testTurma = turmaList.get(turmaList.size() - 1);
        assertThat(testTurma.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTurma.getAnoLectivo()).isEqualTo(UPDATED_ANO_LECTIVO);
        assertThat(testTurma.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testTurma.getAbertura()).isEqualTo(UPDATED_ABERTURA);
        assertThat(testTurma.getEncerramento()).isEqualTo(UPDATED_ENCERRAMENTO);
        assertThat(testTurma.getLotacao()).isEqualTo(UPDATED_LOTACAO);
        assertThat(testTurma.isAberta()).isEqualTo(UPDATED_ABERTA);
        assertThat(testTurma.getPeriodoLectivo()).isEqualTo(UPDATED_PERIODO_LECTIVO);
        assertThat(testTurma.getTurno()).isEqualTo(UPDATED_TURNO);
        assertThat(testTurma.getSala()).isEqualTo(UPDATED_SALA);
        assertThat(testTurma.getClasse()).isEqualTo(UPDATED_CLASSE);

        // Validate the Turma in Elasticsearch
        verify(mockTurmaSearchRepository, times(1)).save(testTurma);
    }

    @Test
    @Transactional
    public void updateNonExistingTurma() throws Exception {
        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();

        // Create the Turma
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurmaMockMvc.perform(put("/api/turmas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Turma in Elasticsearch
        verify(mockTurmaSearchRepository, times(0)).save(turma);
    }

    @Test
    @Transactional
    public void deleteTurma() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        int databaseSizeBeforeDelete = turmaRepository.findAll().size();

        // Delete the turma
        restTurmaMockMvc.perform(delete("/api/turmas/{id}", turma.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Turma in Elasticsearch
        verify(mockTurmaSearchRepository, times(1)).deleteById(turma.getId());
    }

    @Test
    @Transactional
    public void searchTurma() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);
        when(mockTurmaSearchRepository.search(queryStringQuery("id:" + turma.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(turma), PageRequest.of(0, 1), 1));
        // Search the turma
        restTurmaMockMvc.perform(get("/api/_search/turmas?query=id:" + turma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turma.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].abertura").value(hasItem(DEFAULT_ABERTURA.toString())))
            .andExpect(jsonPath("$.[*].encerramento").value(hasItem(DEFAULT_ENCERRAMENTO.toString())))
            .andExpect(jsonPath("$.[*].lotacao").value(hasItem(DEFAULT_LOTACAO)))
            .andExpect(jsonPath("$.[*].aberta").value(hasItem(DEFAULT_ABERTA.booleanValue())))
            .andExpect(jsonPath("$.[*].periodoLectivo").value(hasItem(DEFAULT_PERIODO_LECTIVO)))
            .andExpect(jsonPath("$.[*].turno").value(hasItem(DEFAULT_TURNO)))
            .andExpect(jsonPath("$.[*].sala").value(hasItem(DEFAULT_SALA)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)));
    }
}
