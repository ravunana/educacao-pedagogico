package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.domain.Curso;
import com.ravunana.educacao.pedagogico.domain.Professor;
import com.ravunana.educacao.pedagogico.repository.TurmaRepository;
import com.ravunana.educacao.pedagogico.repository.search.TurmaSearchRepository;
import com.ravunana.educacao.pedagogico.service.TurmaService;
import com.ravunana.educacao.pedagogico.service.dto.TurmaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.TurmaMapper;
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

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final LocalDate DEFAULT_ABERTURA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ABERTURA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ENCERRAMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENCERRAMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_LOTACAO = 1;
    private static final Integer UPDATED_LOTACAO = 2;

    private static final Boolean DEFAULT_ABERTA = false;
    private static final Boolean UPDATED_ABERTA = true;

    private static final String DEFAULT_PERIODO_LECTIVO = "AAAAAAAAAA";
    private static final String UPDATED_PERIODO_LECTIVO = "BBBBBBBBBB";

    private static final String DEFAULT_TURNO = "AAAAAAAAAA";
    private static final String UPDATED_TURNO = "BBBBBBBBBB";

    private static final Integer DEFAULT_SALA = 1;
    private static final Integer UPDATED_SALA = 2;

    private static final Integer DEFAULT_CLASSE = 1;
    private static final Integer UPDATED_CLASSE = 2;

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
        final TurmaResource turmaResource = new TurmaResource(turmaService);
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
