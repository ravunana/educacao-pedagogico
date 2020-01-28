package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.Nota;
import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import com.ravunana.educacao.pedagogico.domain.Professor;
import com.ravunana.educacao.pedagogico.repository.NotaRepository;
import com.ravunana.educacao.pedagogico.repository.search.NotaSearchRepository;
import com.ravunana.educacao.pedagogico.service.NotaService;
import com.ravunana.educacao.pedagogico.service.dto.NotaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.NotaMapper;
import com.ravunana.educacao.pedagogico.web.rest.errors.ExceptionTranslator;
import com.ravunana.educacao.pedagogico.service.dto.NotaCriteria;
import com.ravunana.educacao.pedagogico.service.NotaQueryService;

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
 * Integration tests for the {@link NotaResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class NotaResourceIT {

    private static final String DEFAULT_NUMERO_PROCESSO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_PROCESSO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_ALUNO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_ALUNO = "BBBBBBBBBB";

    private static final String DEFAULT_DISCIPLINA = "AAAAAAAAAA";
    private static final String UPDATED_DISCIPLINA = "BBBBBBBBBB";

    private static final String DEFAULT_PERIDO_LECTIVO = "AAAAAAAAAA";
    private static final String UPDATED_PERIDO_LECTIVO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANO_LECTIVO = 1;
    private static final Integer UPDATED_ANO_LECTIVO = 2;
    private static final Integer SMALLER_ANO_LECTIVO = 1 - 1;

    private static final Integer DEFAULT_FALTA_JUSTIFICADA = 1;
    private static final Integer UPDATED_FALTA_JUSTIFICADA = 2;
    private static final Integer SMALLER_FALTA_JUSTIFICADA = 1 - 1;

    private static final Integer DEFAULT_FALTA_INJUSTIFICADA = 1;
    private static final Integer UPDATED_FALTA_INJUSTIFICADA = 2;
    private static final Integer SMALLER_FALTA_INJUSTIFICADA = 1 - 1;

    private static final Double DEFAULT_AVALIACAO_CONTINUCA = 0D;
    private static final Double UPDATED_AVALIACAO_CONTINUCA = 1D;
    private static final Double SMALLER_AVALIACAO_CONTINUCA = 0D - 1D;

    private static final Double DEFAULT_PRIMEIRA_PROVA = 0D;
    private static final Double UPDATED_PRIMEIRA_PROVA = 1D;
    private static final Double SMALLER_PRIMEIRA_PROVA = 0D - 1D;

    private static final Double DEFAULT_SEGUNDA_PROVA = 0D;
    private static final Double UPDATED_SEGUNDA_PROVA = 1D;
    private static final Double SMALLER_SEGUNDA_PROVA = 0D - 1D;

    private static final Double DEFAULT_EXAME = 0D;
    private static final Double UPDATED_EXAME = 1D;
    private static final Double SMALLER_EXAME = 0D - 1D;

    private static final Double DEFAULT_RECURSO = 0D;
    private static final Double UPDATED_RECURSO = 1D;
    private static final Double SMALLER_RECURSO = 0D - 1D;

    private static final Double DEFAULT_EXAME_ESPECIAL = 0D;
    private static final Double UPDATED_EXAME_ESPECIAL = 1D;
    private static final Double SMALLER_EXAME_ESPECIAL = 0D - 1D;

    private static final byte[] DEFAULT_PROVA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROVA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROVA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROVA_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_SITUACAO = "AAAAAAAAAA";
    private static final String UPDATED_SITUACAO = "BBBBBBBBBB";

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private NotaMapper notaMapper;

    @Autowired
    private NotaService notaService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.NotaSearchRepositoryMockConfiguration
     */
    @Autowired
    private NotaSearchRepository mockNotaSearchRepository;

    @Autowired
    private NotaQueryService notaQueryService;

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

    private MockMvc restNotaMockMvc;

    private Nota nota;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotaResource notaResource = new NotaResource(notaService, notaQueryService);
        this.restNotaMockMvc = MockMvcBuilders.standaloneSetup(notaResource)
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
    public static Nota createEntity(EntityManager em) {
        Nota nota = new Nota()
            .numeroProcesso(DEFAULT_NUMERO_PROCESSO)
            .nomeAluno(DEFAULT_NOME_ALUNO)
            .disciplina(DEFAULT_DISCIPLINA)
            .peridoLectivo(DEFAULT_PERIDO_LECTIVO)
            .anoLectivo(DEFAULT_ANO_LECTIVO)
            .faltaJustificada(DEFAULT_FALTA_JUSTIFICADA)
            .faltaInjustificada(DEFAULT_FALTA_INJUSTIFICADA)
            .avaliacaoContinuca(DEFAULT_AVALIACAO_CONTINUCA)
            .primeiraProva(DEFAULT_PRIMEIRA_PROVA)
            .segundaProva(DEFAULT_SEGUNDA_PROVA)
            .exame(DEFAULT_EXAME)
            .recurso(DEFAULT_RECURSO)
            .exameEspecial(DEFAULT_EXAME_ESPECIAL)
            .prova(DEFAULT_PROVA)
            .provaContentType(DEFAULT_PROVA_CONTENT_TYPE)
            .situacao(DEFAULT_SITUACAO);
        // Add required entity
        Turma turma;
        if (TestUtil.findAll(em, Turma.class).isEmpty()) {
            turma = TurmaResourceIT.createEntity(em);
            em.persist(turma);
            em.flush();
        } else {
            turma = TestUtil.findAll(em, Turma.class).get(0);
        }
        nota.setTurma(turma);
        // Add required entity
        PlanoCurricular planoCurricular;
        if (TestUtil.findAll(em, PlanoCurricular.class).isEmpty()) {
            planoCurricular = PlanoCurricularResourceIT.createEntity(em);
            em.persist(planoCurricular);
            em.flush();
        } else {
            planoCurricular = TestUtil.findAll(em, PlanoCurricular.class).get(0);
        }
        nota.setCurriculo(planoCurricular);
        // Add required entity
        Professor professor;
        if (TestUtil.findAll(em, Professor.class).isEmpty()) {
            professor = ProfessorResourceIT.createEntity(em);
            em.persist(professor);
            em.flush();
        } else {
            professor = TestUtil.findAll(em, Professor.class).get(0);
        }
        nota.setProfessor(professor);
        return nota;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nota createUpdatedEntity(EntityManager em) {
        Nota nota = new Nota()
            .numeroProcesso(UPDATED_NUMERO_PROCESSO)
            .nomeAluno(UPDATED_NOME_ALUNO)
            .disciplina(UPDATED_DISCIPLINA)
            .peridoLectivo(UPDATED_PERIDO_LECTIVO)
            .anoLectivo(UPDATED_ANO_LECTIVO)
            .faltaJustificada(UPDATED_FALTA_JUSTIFICADA)
            .faltaInjustificada(UPDATED_FALTA_INJUSTIFICADA)
            .avaliacaoContinuca(UPDATED_AVALIACAO_CONTINUCA)
            .primeiraProva(UPDATED_PRIMEIRA_PROVA)
            .segundaProva(UPDATED_SEGUNDA_PROVA)
            .exame(UPDATED_EXAME)
            .recurso(UPDATED_RECURSO)
            .exameEspecial(UPDATED_EXAME_ESPECIAL)
            .prova(UPDATED_PROVA)
            .provaContentType(UPDATED_PROVA_CONTENT_TYPE)
            .situacao(UPDATED_SITUACAO);
        // Add required entity
        Turma turma;
        if (TestUtil.findAll(em, Turma.class).isEmpty()) {
            turma = TurmaResourceIT.createUpdatedEntity(em);
            em.persist(turma);
            em.flush();
        } else {
            turma = TestUtil.findAll(em, Turma.class).get(0);
        }
        nota.setTurma(turma);
        // Add required entity
        PlanoCurricular planoCurricular;
        if (TestUtil.findAll(em, PlanoCurricular.class).isEmpty()) {
            planoCurricular = PlanoCurricularResourceIT.createUpdatedEntity(em);
            em.persist(planoCurricular);
            em.flush();
        } else {
            planoCurricular = TestUtil.findAll(em, PlanoCurricular.class).get(0);
        }
        nota.setCurriculo(planoCurricular);
        // Add required entity
        Professor professor;
        if (TestUtil.findAll(em, Professor.class).isEmpty()) {
            professor = ProfessorResourceIT.createUpdatedEntity(em);
            em.persist(professor);
            em.flush();
        } else {
            professor = TestUtil.findAll(em, Professor.class).get(0);
        }
        nota.setProfessor(professor);
        return nota;
    }

    @BeforeEach
    public void initTest() {
        nota = createEntity(em);
    }

    @Test
    @Transactional
    public void createNota() throws Exception {
        int databaseSizeBeforeCreate = notaRepository.findAll().size();

        // Create the Nota
        NotaDTO notaDTO = notaMapper.toDto(nota);
        restNotaMockMvc.perform(post("/api/notas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notaDTO)))
            .andExpect(status().isCreated());

        // Validate the Nota in the database
        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeCreate + 1);
        Nota testNota = notaList.get(notaList.size() - 1);
        assertThat(testNota.getNumeroProcesso()).isEqualTo(DEFAULT_NUMERO_PROCESSO);
        assertThat(testNota.getNomeAluno()).isEqualTo(DEFAULT_NOME_ALUNO);
        assertThat(testNota.getDisciplina()).isEqualTo(DEFAULT_DISCIPLINA);
        assertThat(testNota.getPeridoLectivo()).isEqualTo(DEFAULT_PERIDO_LECTIVO);
        assertThat(testNota.getAnoLectivo()).isEqualTo(DEFAULT_ANO_LECTIVO);
        assertThat(testNota.getFaltaJustificada()).isEqualTo(DEFAULT_FALTA_JUSTIFICADA);
        assertThat(testNota.getFaltaInjustificada()).isEqualTo(DEFAULT_FALTA_INJUSTIFICADA);
        assertThat(testNota.getAvaliacaoContinuca()).isEqualTo(DEFAULT_AVALIACAO_CONTINUCA);
        assertThat(testNota.getPrimeiraProva()).isEqualTo(DEFAULT_PRIMEIRA_PROVA);
        assertThat(testNota.getSegundaProva()).isEqualTo(DEFAULT_SEGUNDA_PROVA);
        assertThat(testNota.getExame()).isEqualTo(DEFAULT_EXAME);
        assertThat(testNota.getRecurso()).isEqualTo(DEFAULT_RECURSO);
        assertThat(testNota.getExameEspecial()).isEqualTo(DEFAULT_EXAME_ESPECIAL);
        assertThat(testNota.getProva()).isEqualTo(DEFAULT_PROVA);
        assertThat(testNota.getProvaContentType()).isEqualTo(DEFAULT_PROVA_CONTENT_TYPE);
        assertThat(testNota.getSituacao()).isEqualTo(DEFAULT_SITUACAO);

        // Validate the Nota in Elasticsearch
        verify(mockNotaSearchRepository, times(1)).save(testNota);
    }

    @Test
    @Transactional
    public void createNotaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notaRepository.findAll().size();

        // Create the Nota with an existing ID
        nota.setId(1L);
        NotaDTO notaDTO = notaMapper.toDto(nota);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotaMockMvc.perform(post("/api/notas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Nota in the database
        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Nota in Elasticsearch
        verify(mockNotaSearchRepository, times(0)).save(nota);
    }


    @Test
    @Transactional
    public void checkNumeroProcessoIsRequired() throws Exception {
        int databaseSizeBeforeTest = notaRepository.findAll().size();
        // set the field null
        nota.setNumeroProcesso(null);

        // Create the Nota, which fails.
        NotaDTO notaDTO = notaMapper.toDto(nota);

        restNotaMockMvc.perform(post("/api/notas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notaDTO)))
            .andExpect(status().isBadRequest());

        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomeAlunoIsRequired() throws Exception {
        int databaseSizeBeforeTest = notaRepository.findAll().size();
        // set the field null
        nota.setNomeAluno(null);

        // Create the Nota, which fails.
        NotaDTO notaDTO = notaMapper.toDto(nota);

        restNotaMockMvc.perform(post("/api/notas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notaDTO)))
            .andExpect(status().isBadRequest());

        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotas() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList
        restNotaMockMvc.perform(get("/api/notas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nota.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroProcesso").value(hasItem(DEFAULT_NUMERO_PROCESSO)))
            .andExpect(jsonPath("$.[*].nomeAluno").value(hasItem(DEFAULT_NOME_ALUNO)))
            .andExpect(jsonPath("$.[*].disciplina").value(hasItem(DEFAULT_DISCIPLINA)))
            .andExpect(jsonPath("$.[*].peridoLectivo").value(hasItem(DEFAULT_PERIDO_LECTIVO)))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].faltaJustificada").value(hasItem(DEFAULT_FALTA_JUSTIFICADA)))
            .andExpect(jsonPath("$.[*].faltaInjustificada").value(hasItem(DEFAULT_FALTA_INJUSTIFICADA)))
            .andExpect(jsonPath("$.[*].avaliacaoContinuca").value(hasItem(DEFAULT_AVALIACAO_CONTINUCA.doubleValue())))
            .andExpect(jsonPath("$.[*].primeiraProva").value(hasItem(DEFAULT_PRIMEIRA_PROVA.doubleValue())))
            .andExpect(jsonPath("$.[*].segundaProva").value(hasItem(DEFAULT_SEGUNDA_PROVA.doubleValue())))
            .andExpect(jsonPath("$.[*].exame").value(hasItem(DEFAULT_EXAME.doubleValue())))
            .andExpect(jsonPath("$.[*].recurso").value(hasItem(DEFAULT_RECURSO.doubleValue())))
            .andExpect(jsonPath("$.[*].exameEspecial").value(hasItem(DEFAULT_EXAME_ESPECIAL.doubleValue())))
            .andExpect(jsonPath("$.[*].provaContentType").value(hasItem(DEFAULT_PROVA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].prova").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROVA))))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO)));
    }
    
    @Test
    @Transactional
    public void getNota() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get the nota
        restNotaMockMvc.perform(get("/api/notas/{id}", nota.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(nota.getId().intValue()))
            .andExpect(jsonPath("$.numeroProcesso").value(DEFAULT_NUMERO_PROCESSO))
            .andExpect(jsonPath("$.nomeAluno").value(DEFAULT_NOME_ALUNO))
            .andExpect(jsonPath("$.disciplina").value(DEFAULT_DISCIPLINA))
            .andExpect(jsonPath("$.peridoLectivo").value(DEFAULT_PERIDO_LECTIVO))
            .andExpect(jsonPath("$.anoLectivo").value(DEFAULT_ANO_LECTIVO))
            .andExpect(jsonPath("$.faltaJustificada").value(DEFAULT_FALTA_JUSTIFICADA))
            .andExpect(jsonPath("$.faltaInjustificada").value(DEFAULT_FALTA_INJUSTIFICADA))
            .andExpect(jsonPath("$.avaliacaoContinuca").value(DEFAULT_AVALIACAO_CONTINUCA.doubleValue()))
            .andExpect(jsonPath("$.primeiraProva").value(DEFAULT_PRIMEIRA_PROVA.doubleValue()))
            .andExpect(jsonPath("$.segundaProva").value(DEFAULT_SEGUNDA_PROVA.doubleValue()))
            .andExpect(jsonPath("$.exame").value(DEFAULT_EXAME.doubleValue()))
            .andExpect(jsonPath("$.recurso").value(DEFAULT_RECURSO.doubleValue()))
            .andExpect(jsonPath("$.exameEspecial").value(DEFAULT_EXAME_ESPECIAL.doubleValue()))
            .andExpect(jsonPath("$.provaContentType").value(DEFAULT_PROVA_CONTENT_TYPE))
            .andExpect(jsonPath("$.prova").value(Base64Utils.encodeToString(DEFAULT_PROVA)))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO));
    }


    @Test
    @Transactional
    public void getNotasByIdFiltering() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        Long id = nota.getId();

        defaultNotaShouldBeFound("id.equals=" + id);
        defaultNotaShouldNotBeFound("id.notEquals=" + id);

        defaultNotaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotaShouldNotBeFound("id.greaterThan=" + id);

        defaultNotaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNotasByNumeroProcessoIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where numeroProcesso equals to DEFAULT_NUMERO_PROCESSO
        defaultNotaShouldBeFound("numeroProcesso.equals=" + DEFAULT_NUMERO_PROCESSO);

        // Get all the notaList where numeroProcesso equals to UPDATED_NUMERO_PROCESSO
        defaultNotaShouldNotBeFound("numeroProcesso.equals=" + UPDATED_NUMERO_PROCESSO);
    }

    @Test
    @Transactional
    public void getAllNotasByNumeroProcessoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where numeroProcesso not equals to DEFAULT_NUMERO_PROCESSO
        defaultNotaShouldNotBeFound("numeroProcesso.notEquals=" + DEFAULT_NUMERO_PROCESSO);

        // Get all the notaList where numeroProcesso not equals to UPDATED_NUMERO_PROCESSO
        defaultNotaShouldBeFound("numeroProcesso.notEquals=" + UPDATED_NUMERO_PROCESSO);
    }

    @Test
    @Transactional
    public void getAllNotasByNumeroProcessoIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where numeroProcesso in DEFAULT_NUMERO_PROCESSO or UPDATED_NUMERO_PROCESSO
        defaultNotaShouldBeFound("numeroProcesso.in=" + DEFAULT_NUMERO_PROCESSO + "," + UPDATED_NUMERO_PROCESSO);

        // Get all the notaList where numeroProcesso equals to UPDATED_NUMERO_PROCESSO
        defaultNotaShouldNotBeFound("numeroProcesso.in=" + UPDATED_NUMERO_PROCESSO);
    }

    @Test
    @Transactional
    public void getAllNotasByNumeroProcessoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where numeroProcesso is not null
        defaultNotaShouldBeFound("numeroProcesso.specified=true");

        // Get all the notaList where numeroProcesso is null
        defaultNotaShouldNotBeFound("numeroProcesso.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotasByNumeroProcessoContainsSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where numeroProcesso contains DEFAULT_NUMERO_PROCESSO
        defaultNotaShouldBeFound("numeroProcesso.contains=" + DEFAULT_NUMERO_PROCESSO);

        // Get all the notaList where numeroProcesso contains UPDATED_NUMERO_PROCESSO
        defaultNotaShouldNotBeFound("numeroProcesso.contains=" + UPDATED_NUMERO_PROCESSO);
    }

    @Test
    @Transactional
    public void getAllNotasByNumeroProcessoNotContainsSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where numeroProcesso does not contain DEFAULT_NUMERO_PROCESSO
        defaultNotaShouldNotBeFound("numeroProcesso.doesNotContain=" + DEFAULT_NUMERO_PROCESSO);

        // Get all the notaList where numeroProcesso does not contain UPDATED_NUMERO_PROCESSO
        defaultNotaShouldBeFound("numeroProcesso.doesNotContain=" + UPDATED_NUMERO_PROCESSO);
    }


    @Test
    @Transactional
    public void getAllNotasByNomeAlunoIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where nomeAluno equals to DEFAULT_NOME_ALUNO
        defaultNotaShouldBeFound("nomeAluno.equals=" + DEFAULT_NOME_ALUNO);

        // Get all the notaList where nomeAluno equals to UPDATED_NOME_ALUNO
        defaultNotaShouldNotBeFound("nomeAluno.equals=" + UPDATED_NOME_ALUNO);
    }

    @Test
    @Transactional
    public void getAllNotasByNomeAlunoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where nomeAluno not equals to DEFAULT_NOME_ALUNO
        defaultNotaShouldNotBeFound("nomeAluno.notEquals=" + DEFAULT_NOME_ALUNO);

        // Get all the notaList where nomeAluno not equals to UPDATED_NOME_ALUNO
        defaultNotaShouldBeFound("nomeAluno.notEquals=" + UPDATED_NOME_ALUNO);
    }

    @Test
    @Transactional
    public void getAllNotasByNomeAlunoIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where nomeAluno in DEFAULT_NOME_ALUNO or UPDATED_NOME_ALUNO
        defaultNotaShouldBeFound("nomeAluno.in=" + DEFAULT_NOME_ALUNO + "," + UPDATED_NOME_ALUNO);

        // Get all the notaList where nomeAluno equals to UPDATED_NOME_ALUNO
        defaultNotaShouldNotBeFound("nomeAluno.in=" + UPDATED_NOME_ALUNO);
    }

    @Test
    @Transactional
    public void getAllNotasByNomeAlunoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where nomeAluno is not null
        defaultNotaShouldBeFound("nomeAluno.specified=true");

        // Get all the notaList where nomeAluno is null
        defaultNotaShouldNotBeFound("nomeAluno.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotasByNomeAlunoContainsSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where nomeAluno contains DEFAULT_NOME_ALUNO
        defaultNotaShouldBeFound("nomeAluno.contains=" + DEFAULT_NOME_ALUNO);

        // Get all the notaList where nomeAluno contains UPDATED_NOME_ALUNO
        defaultNotaShouldNotBeFound("nomeAluno.contains=" + UPDATED_NOME_ALUNO);
    }

    @Test
    @Transactional
    public void getAllNotasByNomeAlunoNotContainsSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where nomeAluno does not contain DEFAULT_NOME_ALUNO
        defaultNotaShouldNotBeFound("nomeAluno.doesNotContain=" + DEFAULT_NOME_ALUNO);

        // Get all the notaList where nomeAluno does not contain UPDATED_NOME_ALUNO
        defaultNotaShouldBeFound("nomeAluno.doesNotContain=" + UPDATED_NOME_ALUNO);
    }


    @Test
    @Transactional
    public void getAllNotasByDisciplinaIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where disciplina equals to DEFAULT_DISCIPLINA
        defaultNotaShouldBeFound("disciplina.equals=" + DEFAULT_DISCIPLINA);

        // Get all the notaList where disciplina equals to UPDATED_DISCIPLINA
        defaultNotaShouldNotBeFound("disciplina.equals=" + UPDATED_DISCIPLINA);
    }

    @Test
    @Transactional
    public void getAllNotasByDisciplinaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where disciplina not equals to DEFAULT_DISCIPLINA
        defaultNotaShouldNotBeFound("disciplina.notEquals=" + DEFAULT_DISCIPLINA);

        // Get all the notaList where disciplina not equals to UPDATED_DISCIPLINA
        defaultNotaShouldBeFound("disciplina.notEquals=" + UPDATED_DISCIPLINA);
    }

    @Test
    @Transactional
    public void getAllNotasByDisciplinaIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where disciplina in DEFAULT_DISCIPLINA or UPDATED_DISCIPLINA
        defaultNotaShouldBeFound("disciplina.in=" + DEFAULT_DISCIPLINA + "," + UPDATED_DISCIPLINA);

        // Get all the notaList where disciplina equals to UPDATED_DISCIPLINA
        defaultNotaShouldNotBeFound("disciplina.in=" + UPDATED_DISCIPLINA);
    }

    @Test
    @Transactional
    public void getAllNotasByDisciplinaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where disciplina is not null
        defaultNotaShouldBeFound("disciplina.specified=true");

        // Get all the notaList where disciplina is null
        defaultNotaShouldNotBeFound("disciplina.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotasByDisciplinaContainsSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where disciplina contains DEFAULT_DISCIPLINA
        defaultNotaShouldBeFound("disciplina.contains=" + DEFAULT_DISCIPLINA);

        // Get all the notaList where disciplina contains UPDATED_DISCIPLINA
        defaultNotaShouldNotBeFound("disciplina.contains=" + UPDATED_DISCIPLINA);
    }

    @Test
    @Transactional
    public void getAllNotasByDisciplinaNotContainsSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where disciplina does not contain DEFAULT_DISCIPLINA
        defaultNotaShouldNotBeFound("disciplina.doesNotContain=" + DEFAULT_DISCIPLINA);

        // Get all the notaList where disciplina does not contain UPDATED_DISCIPLINA
        defaultNotaShouldBeFound("disciplina.doesNotContain=" + UPDATED_DISCIPLINA);
    }


    @Test
    @Transactional
    public void getAllNotasByPeridoLectivoIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where peridoLectivo equals to DEFAULT_PERIDO_LECTIVO
        defaultNotaShouldBeFound("peridoLectivo.equals=" + DEFAULT_PERIDO_LECTIVO);

        // Get all the notaList where peridoLectivo equals to UPDATED_PERIDO_LECTIVO
        defaultNotaShouldNotBeFound("peridoLectivo.equals=" + UPDATED_PERIDO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllNotasByPeridoLectivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where peridoLectivo not equals to DEFAULT_PERIDO_LECTIVO
        defaultNotaShouldNotBeFound("peridoLectivo.notEquals=" + DEFAULT_PERIDO_LECTIVO);

        // Get all the notaList where peridoLectivo not equals to UPDATED_PERIDO_LECTIVO
        defaultNotaShouldBeFound("peridoLectivo.notEquals=" + UPDATED_PERIDO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllNotasByPeridoLectivoIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where peridoLectivo in DEFAULT_PERIDO_LECTIVO or UPDATED_PERIDO_LECTIVO
        defaultNotaShouldBeFound("peridoLectivo.in=" + DEFAULT_PERIDO_LECTIVO + "," + UPDATED_PERIDO_LECTIVO);

        // Get all the notaList where peridoLectivo equals to UPDATED_PERIDO_LECTIVO
        defaultNotaShouldNotBeFound("peridoLectivo.in=" + UPDATED_PERIDO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllNotasByPeridoLectivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where peridoLectivo is not null
        defaultNotaShouldBeFound("peridoLectivo.specified=true");

        // Get all the notaList where peridoLectivo is null
        defaultNotaShouldNotBeFound("peridoLectivo.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotasByPeridoLectivoContainsSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where peridoLectivo contains DEFAULT_PERIDO_LECTIVO
        defaultNotaShouldBeFound("peridoLectivo.contains=" + DEFAULT_PERIDO_LECTIVO);

        // Get all the notaList where peridoLectivo contains UPDATED_PERIDO_LECTIVO
        defaultNotaShouldNotBeFound("peridoLectivo.contains=" + UPDATED_PERIDO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllNotasByPeridoLectivoNotContainsSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where peridoLectivo does not contain DEFAULT_PERIDO_LECTIVO
        defaultNotaShouldNotBeFound("peridoLectivo.doesNotContain=" + DEFAULT_PERIDO_LECTIVO);

        // Get all the notaList where peridoLectivo does not contain UPDATED_PERIDO_LECTIVO
        defaultNotaShouldBeFound("peridoLectivo.doesNotContain=" + UPDATED_PERIDO_LECTIVO);
    }


    @Test
    @Transactional
    public void getAllNotasByAnoLectivoIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where anoLectivo equals to DEFAULT_ANO_LECTIVO
        defaultNotaShouldBeFound("anoLectivo.equals=" + DEFAULT_ANO_LECTIVO);

        // Get all the notaList where anoLectivo equals to UPDATED_ANO_LECTIVO
        defaultNotaShouldNotBeFound("anoLectivo.equals=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllNotasByAnoLectivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where anoLectivo not equals to DEFAULT_ANO_LECTIVO
        defaultNotaShouldNotBeFound("anoLectivo.notEquals=" + DEFAULT_ANO_LECTIVO);

        // Get all the notaList where anoLectivo not equals to UPDATED_ANO_LECTIVO
        defaultNotaShouldBeFound("anoLectivo.notEquals=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllNotasByAnoLectivoIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where anoLectivo in DEFAULT_ANO_LECTIVO or UPDATED_ANO_LECTIVO
        defaultNotaShouldBeFound("anoLectivo.in=" + DEFAULT_ANO_LECTIVO + "," + UPDATED_ANO_LECTIVO);

        // Get all the notaList where anoLectivo equals to UPDATED_ANO_LECTIVO
        defaultNotaShouldNotBeFound("anoLectivo.in=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllNotasByAnoLectivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where anoLectivo is not null
        defaultNotaShouldBeFound("anoLectivo.specified=true");

        // Get all the notaList where anoLectivo is null
        defaultNotaShouldNotBeFound("anoLectivo.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotasByAnoLectivoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where anoLectivo is greater than or equal to DEFAULT_ANO_LECTIVO
        defaultNotaShouldBeFound("anoLectivo.greaterThanOrEqual=" + DEFAULT_ANO_LECTIVO);

        // Get all the notaList where anoLectivo is greater than or equal to UPDATED_ANO_LECTIVO
        defaultNotaShouldNotBeFound("anoLectivo.greaterThanOrEqual=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllNotasByAnoLectivoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where anoLectivo is less than or equal to DEFAULT_ANO_LECTIVO
        defaultNotaShouldBeFound("anoLectivo.lessThanOrEqual=" + DEFAULT_ANO_LECTIVO);

        // Get all the notaList where anoLectivo is less than or equal to SMALLER_ANO_LECTIVO
        defaultNotaShouldNotBeFound("anoLectivo.lessThanOrEqual=" + SMALLER_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllNotasByAnoLectivoIsLessThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where anoLectivo is less than DEFAULT_ANO_LECTIVO
        defaultNotaShouldNotBeFound("anoLectivo.lessThan=" + DEFAULT_ANO_LECTIVO);

        // Get all the notaList where anoLectivo is less than UPDATED_ANO_LECTIVO
        defaultNotaShouldBeFound("anoLectivo.lessThan=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllNotasByAnoLectivoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where anoLectivo is greater than DEFAULT_ANO_LECTIVO
        defaultNotaShouldNotBeFound("anoLectivo.greaterThan=" + DEFAULT_ANO_LECTIVO);

        // Get all the notaList where anoLectivo is greater than SMALLER_ANO_LECTIVO
        defaultNotaShouldBeFound("anoLectivo.greaterThan=" + SMALLER_ANO_LECTIVO);
    }


    @Test
    @Transactional
    public void getAllNotasByFaltaJustificadaIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaJustificada equals to DEFAULT_FALTA_JUSTIFICADA
        defaultNotaShouldBeFound("faltaJustificada.equals=" + DEFAULT_FALTA_JUSTIFICADA);

        // Get all the notaList where faltaJustificada equals to UPDATED_FALTA_JUSTIFICADA
        defaultNotaShouldNotBeFound("faltaJustificada.equals=" + UPDATED_FALTA_JUSTIFICADA);
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaJustificadaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaJustificada not equals to DEFAULT_FALTA_JUSTIFICADA
        defaultNotaShouldNotBeFound("faltaJustificada.notEquals=" + DEFAULT_FALTA_JUSTIFICADA);

        // Get all the notaList where faltaJustificada not equals to UPDATED_FALTA_JUSTIFICADA
        defaultNotaShouldBeFound("faltaJustificada.notEquals=" + UPDATED_FALTA_JUSTIFICADA);
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaJustificadaIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaJustificada in DEFAULT_FALTA_JUSTIFICADA or UPDATED_FALTA_JUSTIFICADA
        defaultNotaShouldBeFound("faltaJustificada.in=" + DEFAULT_FALTA_JUSTIFICADA + "," + UPDATED_FALTA_JUSTIFICADA);

        // Get all the notaList where faltaJustificada equals to UPDATED_FALTA_JUSTIFICADA
        defaultNotaShouldNotBeFound("faltaJustificada.in=" + UPDATED_FALTA_JUSTIFICADA);
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaJustificadaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaJustificada is not null
        defaultNotaShouldBeFound("faltaJustificada.specified=true");

        // Get all the notaList where faltaJustificada is null
        defaultNotaShouldNotBeFound("faltaJustificada.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaJustificadaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaJustificada is greater than or equal to DEFAULT_FALTA_JUSTIFICADA
        defaultNotaShouldBeFound("faltaJustificada.greaterThanOrEqual=" + DEFAULT_FALTA_JUSTIFICADA);

        // Get all the notaList where faltaJustificada is greater than or equal to UPDATED_FALTA_JUSTIFICADA
        defaultNotaShouldNotBeFound("faltaJustificada.greaterThanOrEqual=" + UPDATED_FALTA_JUSTIFICADA);
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaJustificadaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaJustificada is less than or equal to DEFAULT_FALTA_JUSTIFICADA
        defaultNotaShouldBeFound("faltaJustificada.lessThanOrEqual=" + DEFAULT_FALTA_JUSTIFICADA);

        // Get all the notaList where faltaJustificada is less than or equal to SMALLER_FALTA_JUSTIFICADA
        defaultNotaShouldNotBeFound("faltaJustificada.lessThanOrEqual=" + SMALLER_FALTA_JUSTIFICADA);
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaJustificadaIsLessThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaJustificada is less than DEFAULT_FALTA_JUSTIFICADA
        defaultNotaShouldNotBeFound("faltaJustificada.lessThan=" + DEFAULT_FALTA_JUSTIFICADA);

        // Get all the notaList where faltaJustificada is less than UPDATED_FALTA_JUSTIFICADA
        defaultNotaShouldBeFound("faltaJustificada.lessThan=" + UPDATED_FALTA_JUSTIFICADA);
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaJustificadaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaJustificada is greater than DEFAULT_FALTA_JUSTIFICADA
        defaultNotaShouldNotBeFound("faltaJustificada.greaterThan=" + DEFAULT_FALTA_JUSTIFICADA);

        // Get all the notaList where faltaJustificada is greater than SMALLER_FALTA_JUSTIFICADA
        defaultNotaShouldBeFound("faltaJustificada.greaterThan=" + SMALLER_FALTA_JUSTIFICADA);
    }


    @Test
    @Transactional
    public void getAllNotasByFaltaInjustificadaIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaInjustificada equals to DEFAULT_FALTA_INJUSTIFICADA
        defaultNotaShouldBeFound("faltaInjustificada.equals=" + DEFAULT_FALTA_INJUSTIFICADA);

        // Get all the notaList where faltaInjustificada equals to UPDATED_FALTA_INJUSTIFICADA
        defaultNotaShouldNotBeFound("faltaInjustificada.equals=" + UPDATED_FALTA_INJUSTIFICADA);
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaInjustificadaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaInjustificada not equals to DEFAULT_FALTA_INJUSTIFICADA
        defaultNotaShouldNotBeFound("faltaInjustificada.notEquals=" + DEFAULT_FALTA_INJUSTIFICADA);

        // Get all the notaList where faltaInjustificada not equals to UPDATED_FALTA_INJUSTIFICADA
        defaultNotaShouldBeFound("faltaInjustificada.notEquals=" + UPDATED_FALTA_INJUSTIFICADA);
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaInjustificadaIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaInjustificada in DEFAULT_FALTA_INJUSTIFICADA or UPDATED_FALTA_INJUSTIFICADA
        defaultNotaShouldBeFound("faltaInjustificada.in=" + DEFAULT_FALTA_INJUSTIFICADA + "," + UPDATED_FALTA_INJUSTIFICADA);

        // Get all the notaList where faltaInjustificada equals to UPDATED_FALTA_INJUSTIFICADA
        defaultNotaShouldNotBeFound("faltaInjustificada.in=" + UPDATED_FALTA_INJUSTIFICADA);
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaInjustificadaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaInjustificada is not null
        defaultNotaShouldBeFound("faltaInjustificada.specified=true");

        // Get all the notaList where faltaInjustificada is null
        defaultNotaShouldNotBeFound("faltaInjustificada.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaInjustificadaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaInjustificada is greater than or equal to DEFAULT_FALTA_INJUSTIFICADA
        defaultNotaShouldBeFound("faltaInjustificada.greaterThanOrEqual=" + DEFAULT_FALTA_INJUSTIFICADA);

        // Get all the notaList where faltaInjustificada is greater than or equal to UPDATED_FALTA_INJUSTIFICADA
        defaultNotaShouldNotBeFound("faltaInjustificada.greaterThanOrEqual=" + UPDATED_FALTA_INJUSTIFICADA);
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaInjustificadaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaInjustificada is less than or equal to DEFAULT_FALTA_INJUSTIFICADA
        defaultNotaShouldBeFound("faltaInjustificada.lessThanOrEqual=" + DEFAULT_FALTA_INJUSTIFICADA);

        // Get all the notaList where faltaInjustificada is less than or equal to SMALLER_FALTA_INJUSTIFICADA
        defaultNotaShouldNotBeFound("faltaInjustificada.lessThanOrEqual=" + SMALLER_FALTA_INJUSTIFICADA);
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaInjustificadaIsLessThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaInjustificada is less than DEFAULT_FALTA_INJUSTIFICADA
        defaultNotaShouldNotBeFound("faltaInjustificada.lessThan=" + DEFAULT_FALTA_INJUSTIFICADA);

        // Get all the notaList where faltaInjustificada is less than UPDATED_FALTA_INJUSTIFICADA
        defaultNotaShouldBeFound("faltaInjustificada.lessThan=" + UPDATED_FALTA_INJUSTIFICADA);
    }

    @Test
    @Transactional
    public void getAllNotasByFaltaInjustificadaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where faltaInjustificada is greater than DEFAULT_FALTA_INJUSTIFICADA
        defaultNotaShouldNotBeFound("faltaInjustificada.greaterThan=" + DEFAULT_FALTA_INJUSTIFICADA);

        // Get all the notaList where faltaInjustificada is greater than SMALLER_FALTA_INJUSTIFICADA
        defaultNotaShouldBeFound("faltaInjustificada.greaterThan=" + SMALLER_FALTA_INJUSTIFICADA);
    }


    @Test
    @Transactional
    public void getAllNotasByAvaliacaoContinucaIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where avaliacaoContinuca equals to DEFAULT_AVALIACAO_CONTINUCA
        defaultNotaShouldBeFound("avaliacaoContinuca.equals=" + DEFAULT_AVALIACAO_CONTINUCA);

        // Get all the notaList where avaliacaoContinuca equals to UPDATED_AVALIACAO_CONTINUCA
        defaultNotaShouldNotBeFound("avaliacaoContinuca.equals=" + UPDATED_AVALIACAO_CONTINUCA);
    }

    @Test
    @Transactional
    public void getAllNotasByAvaliacaoContinucaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where avaliacaoContinuca not equals to DEFAULT_AVALIACAO_CONTINUCA
        defaultNotaShouldNotBeFound("avaliacaoContinuca.notEquals=" + DEFAULT_AVALIACAO_CONTINUCA);

        // Get all the notaList where avaliacaoContinuca not equals to UPDATED_AVALIACAO_CONTINUCA
        defaultNotaShouldBeFound("avaliacaoContinuca.notEquals=" + UPDATED_AVALIACAO_CONTINUCA);
    }

    @Test
    @Transactional
    public void getAllNotasByAvaliacaoContinucaIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where avaliacaoContinuca in DEFAULT_AVALIACAO_CONTINUCA or UPDATED_AVALIACAO_CONTINUCA
        defaultNotaShouldBeFound("avaliacaoContinuca.in=" + DEFAULT_AVALIACAO_CONTINUCA + "," + UPDATED_AVALIACAO_CONTINUCA);

        // Get all the notaList where avaliacaoContinuca equals to UPDATED_AVALIACAO_CONTINUCA
        defaultNotaShouldNotBeFound("avaliacaoContinuca.in=" + UPDATED_AVALIACAO_CONTINUCA);
    }

    @Test
    @Transactional
    public void getAllNotasByAvaliacaoContinucaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where avaliacaoContinuca is not null
        defaultNotaShouldBeFound("avaliacaoContinuca.specified=true");

        // Get all the notaList where avaliacaoContinuca is null
        defaultNotaShouldNotBeFound("avaliacaoContinuca.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotasByAvaliacaoContinucaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where avaliacaoContinuca is greater than or equal to DEFAULT_AVALIACAO_CONTINUCA
        defaultNotaShouldBeFound("avaliacaoContinuca.greaterThanOrEqual=" + DEFAULT_AVALIACAO_CONTINUCA);

        // Get all the notaList where avaliacaoContinuca is greater than or equal to (DEFAULT_AVALIACAO_CONTINUCA + 1)
        defaultNotaShouldNotBeFound("avaliacaoContinuca.greaterThanOrEqual=" + (DEFAULT_AVALIACAO_CONTINUCA + 1));
    }

    @Test
    @Transactional
    public void getAllNotasByAvaliacaoContinucaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where avaliacaoContinuca is less than or equal to DEFAULT_AVALIACAO_CONTINUCA
        defaultNotaShouldBeFound("avaliacaoContinuca.lessThanOrEqual=" + DEFAULT_AVALIACAO_CONTINUCA);

        // Get all the notaList where avaliacaoContinuca is less than or equal to SMALLER_AVALIACAO_CONTINUCA
        defaultNotaShouldNotBeFound("avaliacaoContinuca.lessThanOrEqual=" + SMALLER_AVALIACAO_CONTINUCA);
    }

    @Test
    @Transactional
    public void getAllNotasByAvaliacaoContinucaIsLessThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where avaliacaoContinuca is less than DEFAULT_AVALIACAO_CONTINUCA
        defaultNotaShouldNotBeFound("avaliacaoContinuca.lessThan=" + DEFAULT_AVALIACAO_CONTINUCA);

        // Get all the notaList where avaliacaoContinuca is less than (DEFAULT_AVALIACAO_CONTINUCA + 1)
        defaultNotaShouldBeFound("avaliacaoContinuca.lessThan=" + (DEFAULT_AVALIACAO_CONTINUCA + 1));
    }

    @Test
    @Transactional
    public void getAllNotasByAvaliacaoContinucaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where avaliacaoContinuca is greater than DEFAULT_AVALIACAO_CONTINUCA
        defaultNotaShouldNotBeFound("avaliacaoContinuca.greaterThan=" + DEFAULT_AVALIACAO_CONTINUCA);

        // Get all the notaList where avaliacaoContinuca is greater than SMALLER_AVALIACAO_CONTINUCA
        defaultNotaShouldBeFound("avaliacaoContinuca.greaterThan=" + SMALLER_AVALIACAO_CONTINUCA);
    }


    @Test
    @Transactional
    public void getAllNotasByPrimeiraProvaIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where primeiraProva equals to DEFAULT_PRIMEIRA_PROVA
        defaultNotaShouldBeFound("primeiraProva.equals=" + DEFAULT_PRIMEIRA_PROVA);

        // Get all the notaList where primeiraProva equals to UPDATED_PRIMEIRA_PROVA
        defaultNotaShouldNotBeFound("primeiraProva.equals=" + UPDATED_PRIMEIRA_PROVA);
    }

    @Test
    @Transactional
    public void getAllNotasByPrimeiraProvaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where primeiraProva not equals to DEFAULT_PRIMEIRA_PROVA
        defaultNotaShouldNotBeFound("primeiraProva.notEquals=" + DEFAULT_PRIMEIRA_PROVA);

        // Get all the notaList where primeiraProva not equals to UPDATED_PRIMEIRA_PROVA
        defaultNotaShouldBeFound("primeiraProva.notEquals=" + UPDATED_PRIMEIRA_PROVA);
    }

    @Test
    @Transactional
    public void getAllNotasByPrimeiraProvaIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where primeiraProva in DEFAULT_PRIMEIRA_PROVA or UPDATED_PRIMEIRA_PROVA
        defaultNotaShouldBeFound("primeiraProva.in=" + DEFAULT_PRIMEIRA_PROVA + "," + UPDATED_PRIMEIRA_PROVA);

        // Get all the notaList where primeiraProva equals to UPDATED_PRIMEIRA_PROVA
        defaultNotaShouldNotBeFound("primeiraProva.in=" + UPDATED_PRIMEIRA_PROVA);
    }

    @Test
    @Transactional
    public void getAllNotasByPrimeiraProvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where primeiraProva is not null
        defaultNotaShouldBeFound("primeiraProva.specified=true");

        // Get all the notaList where primeiraProva is null
        defaultNotaShouldNotBeFound("primeiraProva.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotasByPrimeiraProvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where primeiraProva is greater than or equal to DEFAULT_PRIMEIRA_PROVA
        defaultNotaShouldBeFound("primeiraProva.greaterThanOrEqual=" + DEFAULT_PRIMEIRA_PROVA);

        // Get all the notaList where primeiraProva is greater than or equal to (DEFAULT_PRIMEIRA_PROVA + 1)
        defaultNotaShouldNotBeFound("primeiraProva.greaterThanOrEqual=" + (DEFAULT_PRIMEIRA_PROVA + 1));
    }

    @Test
    @Transactional
    public void getAllNotasByPrimeiraProvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where primeiraProva is less than or equal to DEFAULT_PRIMEIRA_PROVA
        defaultNotaShouldBeFound("primeiraProva.lessThanOrEqual=" + DEFAULT_PRIMEIRA_PROVA);

        // Get all the notaList where primeiraProva is less than or equal to SMALLER_PRIMEIRA_PROVA
        defaultNotaShouldNotBeFound("primeiraProva.lessThanOrEqual=" + SMALLER_PRIMEIRA_PROVA);
    }

    @Test
    @Transactional
    public void getAllNotasByPrimeiraProvaIsLessThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where primeiraProva is less than DEFAULT_PRIMEIRA_PROVA
        defaultNotaShouldNotBeFound("primeiraProva.lessThan=" + DEFAULT_PRIMEIRA_PROVA);

        // Get all the notaList where primeiraProva is less than (DEFAULT_PRIMEIRA_PROVA + 1)
        defaultNotaShouldBeFound("primeiraProva.lessThan=" + (DEFAULT_PRIMEIRA_PROVA + 1));
    }

    @Test
    @Transactional
    public void getAllNotasByPrimeiraProvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where primeiraProva is greater than DEFAULT_PRIMEIRA_PROVA
        defaultNotaShouldNotBeFound("primeiraProva.greaterThan=" + DEFAULT_PRIMEIRA_PROVA);

        // Get all the notaList where primeiraProva is greater than SMALLER_PRIMEIRA_PROVA
        defaultNotaShouldBeFound("primeiraProva.greaterThan=" + SMALLER_PRIMEIRA_PROVA);
    }


    @Test
    @Transactional
    public void getAllNotasBySegundaProvaIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where segundaProva equals to DEFAULT_SEGUNDA_PROVA
        defaultNotaShouldBeFound("segundaProva.equals=" + DEFAULT_SEGUNDA_PROVA);

        // Get all the notaList where segundaProva equals to UPDATED_SEGUNDA_PROVA
        defaultNotaShouldNotBeFound("segundaProva.equals=" + UPDATED_SEGUNDA_PROVA);
    }

    @Test
    @Transactional
    public void getAllNotasBySegundaProvaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where segundaProva not equals to DEFAULT_SEGUNDA_PROVA
        defaultNotaShouldNotBeFound("segundaProva.notEquals=" + DEFAULT_SEGUNDA_PROVA);

        // Get all the notaList where segundaProva not equals to UPDATED_SEGUNDA_PROVA
        defaultNotaShouldBeFound("segundaProva.notEquals=" + UPDATED_SEGUNDA_PROVA);
    }

    @Test
    @Transactional
    public void getAllNotasBySegundaProvaIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where segundaProva in DEFAULT_SEGUNDA_PROVA or UPDATED_SEGUNDA_PROVA
        defaultNotaShouldBeFound("segundaProva.in=" + DEFAULT_SEGUNDA_PROVA + "," + UPDATED_SEGUNDA_PROVA);

        // Get all the notaList where segundaProva equals to UPDATED_SEGUNDA_PROVA
        defaultNotaShouldNotBeFound("segundaProva.in=" + UPDATED_SEGUNDA_PROVA);
    }

    @Test
    @Transactional
    public void getAllNotasBySegundaProvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where segundaProva is not null
        defaultNotaShouldBeFound("segundaProva.specified=true");

        // Get all the notaList where segundaProva is null
        defaultNotaShouldNotBeFound("segundaProva.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotasBySegundaProvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where segundaProva is greater than or equal to DEFAULT_SEGUNDA_PROVA
        defaultNotaShouldBeFound("segundaProva.greaterThanOrEqual=" + DEFAULT_SEGUNDA_PROVA);

        // Get all the notaList where segundaProva is greater than or equal to (DEFAULT_SEGUNDA_PROVA + 1)
        defaultNotaShouldNotBeFound("segundaProva.greaterThanOrEqual=" + (DEFAULT_SEGUNDA_PROVA + 1));
    }

    @Test
    @Transactional
    public void getAllNotasBySegundaProvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where segundaProva is less than or equal to DEFAULT_SEGUNDA_PROVA
        defaultNotaShouldBeFound("segundaProva.lessThanOrEqual=" + DEFAULT_SEGUNDA_PROVA);

        // Get all the notaList where segundaProva is less than or equal to SMALLER_SEGUNDA_PROVA
        defaultNotaShouldNotBeFound("segundaProva.lessThanOrEqual=" + SMALLER_SEGUNDA_PROVA);
    }

    @Test
    @Transactional
    public void getAllNotasBySegundaProvaIsLessThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where segundaProva is less than DEFAULT_SEGUNDA_PROVA
        defaultNotaShouldNotBeFound("segundaProva.lessThan=" + DEFAULT_SEGUNDA_PROVA);

        // Get all the notaList where segundaProva is less than (DEFAULT_SEGUNDA_PROVA + 1)
        defaultNotaShouldBeFound("segundaProva.lessThan=" + (DEFAULT_SEGUNDA_PROVA + 1));
    }

    @Test
    @Transactional
    public void getAllNotasBySegundaProvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where segundaProva is greater than DEFAULT_SEGUNDA_PROVA
        defaultNotaShouldNotBeFound("segundaProva.greaterThan=" + DEFAULT_SEGUNDA_PROVA);

        // Get all the notaList where segundaProva is greater than SMALLER_SEGUNDA_PROVA
        defaultNotaShouldBeFound("segundaProva.greaterThan=" + SMALLER_SEGUNDA_PROVA);
    }


    @Test
    @Transactional
    public void getAllNotasByExameIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exame equals to DEFAULT_EXAME
        defaultNotaShouldBeFound("exame.equals=" + DEFAULT_EXAME);

        // Get all the notaList where exame equals to UPDATED_EXAME
        defaultNotaShouldNotBeFound("exame.equals=" + UPDATED_EXAME);
    }

    @Test
    @Transactional
    public void getAllNotasByExameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exame not equals to DEFAULT_EXAME
        defaultNotaShouldNotBeFound("exame.notEquals=" + DEFAULT_EXAME);

        // Get all the notaList where exame not equals to UPDATED_EXAME
        defaultNotaShouldBeFound("exame.notEquals=" + UPDATED_EXAME);
    }

    @Test
    @Transactional
    public void getAllNotasByExameIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exame in DEFAULT_EXAME or UPDATED_EXAME
        defaultNotaShouldBeFound("exame.in=" + DEFAULT_EXAME + "," + UPDATED_EXAME);

        // Get all the notaList where exame equals to UPDATED_EXAME
        defaultNotaShouldNotBeFound("exame.in=" + UPDATED_EXAME);
    }

    @Test
    @Transactional
    public void getAllNotasByExameIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exame is not null
        defaultNotaShouldBeFound("exame.specified=true");

        // Get all the notaList where exame is null
        defaultNotaShouldNotBeFound("exame.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotasByExameIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exame is greater than or equal to DEFAULT_EXAME
        defaultNotaShouldBeFound("exame.greaterThanOrEqual=" + DEFAULT_EXAME);

        // Get all the notaList where exame is greater than or equal to (DEFAULT_EXAME + 1)
        defaultNotaShouldNotBeFound("exame.greaterThanOrEqual=" + (DEFAULT_EXAME + 1));
    }

    @Test
    @Transactional
    public void getAllNotasByExameIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exame is less than or equal to DEFAULT_EXAME
        defaultNotaShouldBeFound("exame.lessThanOrEqual=" + DEFAULT_EXAME);

        // Get all the notaList where exame is less than or equal to SMALLER_EXAME
        defaultNotaShouldNotBeFound("exame.lessThanOrEqual=" + SMALLER_EXAME);
    }

    @Test
    @Transactional
    public void getAllNotasByExameIsLessThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exame is less than DEFAULT_EXAME
        defaultNotaShouldNotBeFound("exame.lessThan=" + DEFAULT_EXAME);

        // Get all the notaList where exame is less than (DEFAULT_EXAME + 1)
        defaultNotaShouldBeFound("exame.lessThan=" + (DEFAULT_EXAME + 1));
    }

    @Test
    @Transactional
    public void getAllNotasByExameIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exame is greater than DEFAULT_EXAME
        defaultNotaShouldNotBeFound("exame.greaterThan=" + DEFAULT_EXAME);

        // Get all the notaList where exame is greater than SMALLER_EXAME
        defaultNotaShouldBeFound("exame.greaterThan=" + SMALLER_EXAME);
    }


    @Test
    @Transactional
    public void getAllNotasByRecursoIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where recurso equals to DEFAULT_RECURSO
        defaultNotaShouldBeFound("recurso.equals=" + DEFAULT_RECURSO);

        // Get all the notaList where recurso equals to UPDATED_RECURSO
        defaultNotaShouldNotBeFound("recurso.equals=" + UPDATED_RECURSO);
    }

    @Test
    @Transactional
    public void getAllNotasByRecursoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where recurso not equals to DEFAULT_RECURSO
        defaultNotaShouldNotBeFound("recurso.notEquals=" + DEFAULT_RECURSO);

        // Get all the notaList where recurso not equals to UPDATED_RECURSO
        defaultNotaShouldBeFound("recurso.notEquals=" + UPDATED_RECURSO);
    }

    @Test
    @Transactional
    public void getAllNotasByRecursoIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where recurso in DEFAULT_RECURSO or UPDATED_RECURSO
        defaultNotaShouldBeFound("recurso.in=" + DEFAULT_RECURSO + "," + UPDATED_RECURSO);

        // Get all the notaList where recurso equals to UPDATED_RECURSO
        defaultNotaShouldNotBeFound("recurso.in=" + UPDATED_RECURSO);
    }

    @Test
    @Transactional
    public void getAllNotasByRecursoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where recurso is not null
        defaultNotaShouldBeFound("recurso.specified=true");

        // Get all the notaList where recurso is null
        defaultNotaShouldNotBeFound("recurso.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotasByRecursoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where recurso is greater than or equal to DEFAULT_RECURSO
        defaultNotaShouldBeFound("recurso.greaterThanOrEqual=" + DEFAULT_RECURSO);

        // Get all the notaList where recurso is greater than or equal to (DEFAULT_RECURSO + 1)
        defaultNotaShouldNotBeFound("recurso.greaterThanOrEqual=" + (DEFAULT_RECURSO + 1));
    }

    @Test
    @Transactional
    public void getAllNotasByRecursoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where recurso is less than or equal to DEFAULT_RECURSO
        defaultNotaShouldBeFound("recurso.lessThanOrEqual=" + DEFAULT_RECURSO);

        // Get all the notaList where recurso is less than or equal to SMALLER_RECURSO
        defaultNotaShouldNotBeFound("recurso.lessThanOrEqual=" + SMALLER_RECURSO);
    }

    @Test
    @Transactional
    public void getAllNotasByRecursoIsLessThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where recurso is less than DEFAULT_RECURSO
        defaultNotaShouldNotBeFound("recurso.lessThan=" + DEFAULT_RECURSO);

        // Get all the notaList where recurso is less than (DEFAULT_RECURSO + 1)
        defaultNotaShouldBeFound("recurso.lessThan=" + (DEFAULT_RECURSO + 1));
    }

    @Test
    @Transactional
    public void getAllNotasByRecursoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where recurso is greater than DEFAULT_RECURSO
        defaultNotaShouldNotBeFound("recurso.greaterThan=" + DEFAULT_RECURSO);

        // Get all the notaList where recurso is greater than SMALLER_RECURSO
        defaultNotaShouldBeFound("recurso.greaterThan=" + SMALLER_RECURSO);
    }


    @Test
    @Transactional
    public void getAllNotasByExameEspecialIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exameEspecial equals to DEFAULT_EXAME_ESPECIAL
        defaultNotaShouldBeFound("exameEspecial.equals=" + DEFAULT_EXAME_ESPECIAL);

        // Get all the notaList where exameEspecial equals to UPDATED_EXAME_ESPECIAL
        defaultNotaShouldNotBeFound("exameEspecial.equals=" + UPDATED_EXAME_ESPECIAL);
    }

    @Test
    @Transactional
    public void getAllNotasByExameEspecialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exameEspecial not equals to DEFAULT_EXAME_ESPECIAL
        defaultNotaShouldNotBeFound("exameEspecial.notEquals=" + DEFAULT_EXAME_ESPECIAL);

        // Get all the notaList where exameEspecial not equals to UPDATED_EXAME_ESPECIAL
        defaultNotaShouldBeFound("exameEspecial.notEquals=" + UPDATED_EXAME_ESPECIAL);
    }

    @Test
    @Transactional
    public void getAllNotasByExameEspecialIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exameEspecial in DEFAULT_EXAME_ESPECIAL or UPDATED_EXAME_ESPECIAL
        defaultNotaShouldBeFound("exameEspecial.in=" + DEFAULT_EXAME_ESPECIAL + "," + UPDATED_EXAME_ESPECIAL);

        // Get all the notaList where exameEspecial equals to UPDATED_EXAME_ESPECIAL
        defaultNotaShouldNotBeFound("exameEspecial.in=" + UPDATED_EXAME_ESPECIAL);
    }

    @Test
    @Transactional
    public void getAllNotasByExameEspecialIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exameEspecial is not null
        defaultNotaShouldBeFound("exameEspecial.specified=true");

        // Get all the notaList where exameEspecial is null
        defaultNotaShouldNotBeFound("exameEspecial.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotasByExameEspecialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exameEspecial is greater than or equal to DEFAULT_EXAME_ESPECIAL
        defaultNotaShouldBeFound("exameEspecial.greaterThanOrEqual=" + DEFAULT_EXAME_ESPECIAL);

        // Get all the notaList where exameEspecial is greater than or equal to (DEFAULT_EXAME_ESPECIAL + 1)
        defaultNotaShouldNotBeFound("exameEspecial.greaterThanOrEqual=" + (DEFAULT_EXAME_ESPECIAL + 1));
    }

    @Test
    @Transactional
    public void getAllNotasByExameEspecialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exameEspecial is less than or equal to DEFAULT_EXAME_ESPECIAL
        defaultNotaShouldBeFound("exameEspecial.lessThanOrEqual=" + DEFAULT_EXAME_ESPECIAL);

        // Get all the notaList where exameEspecial is less than or equal to SMALLER_EXAME_ESPECIAL
        defaultNotaShouldNotBeFound("exameEspecial.lessThanOrEqual=" + SMALLER_EXAME_ESPECIAL);
    }

    @Test
    @Transactional
    public void getAllNotasByExameEspecialIsLessThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exameEspecial is less than DEFAULT_EXAME_ESPECIAL
        defaultNotaShouldNotBeFound("exameEspecial.lessThan=" + DEFAULT_EXAME_ESPECIAL);

        // Get all the notaList where exameEspecial is less than (DEFAULT_EXAME_ESPECIAL + 1)
        defaultNotaShouldBeFound("exameEspecial.lessThan=" + (DEFAULT_EXAME_ESPECIAL + 1));
    }

    @Test
    @Transactional
    public void getAllNotasByExameEspecialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where exameEspecial is greater than DEFAULT_EXAME_ESPECIAL
        defaultNotaShouldNotBeFound("exameEspecial.greaterThan=" + DEFAULT_EXAME_ESPECIAL);

        // Get all the notaList where exameEspecial is greater than SMALLER_EXAME_ESPECIAL
        defaultNotaShouldBeFound("exameEspecial.greaterThan=" + SMALLER_EXAME_ESPECIAL);
    }


    @Test
    @Transactional
    public void getAllNotasBySituacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where situacao equals to DEFAULT_SITUACAO
        defaultNotaShouldBeFound("situacao.equals=" + DEFAULT_SITUACAO);

        // Get all the notaList where situacao equals to UPDATED_SITUACAO
        defaultNotaShouldNotBeFound("situacao.equals=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    public void getAllNotasBySituacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where situacao not equals to DEFAULT_SITUACAO
        defaultNotaShouldNotBeFound("situacao.notEquals=" + DEFAULT_SITUACAO);

        // Get all the notaList where situacao not equals to UPDATED_SITUACAO
        defaultNotaShouldBeFound("situacao.notEquals=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    public void getAllNotasBySituacaoIsInShouldWork() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where situacao in DEFAULT_SITUACAO or UPDATED_SITUACAO
        defaultNotaShouldBeFound("situacao.in=" + DEFAULT_SITUACAO + "," + UPDATED_SITUACAO);

        // Get all the notaList where situacao equals to UPDATED_SITUACAO
        defaultNotaShouldNotBeFound("situacao.in=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    public void getAllNotasBySituacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where situacao is not null
        defaultNotaShouldBeFound("situacao.specified=true");

        // Get all the notaList where situacao is null
        defaultNotaShouldNotBeFound("situacao.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotasBySituacaoContainsSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where situacao contains DEFAULT_SITUACAO
        defaultNotaShouldBeFound("situacao.contains=" + DEFAULT_SITUACAO);

        // Get all the notaList where situacao contains UPDATED_SITUACAO
        defaultNotaShouldNotBeFound("situacao.contains=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    public void getAllNotasBySituacaoNotContainsSomething() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList where situacao does not contain DEFAULT_SITUACAO
        defaultNotaShouldNotBeFound("situacao.doesNotContain=" + DEFAULT_SITUACAO);

        // Get all the notaList where situacao does not contain UPDATED_SITUACAO
        defaultNotaShouldBeFound("situacao.doesNotContain=" + UPDATED_SITUACAO);
    }


    @Test
    @Transactional
    public void getAllNotasByTurmaIsEqualToSomething() throws Exception {
        // Get already existing entity
        Turma turma = nota.getTurma();
        notaRepository.saveAndFlush(nota);
        Long turmaId = turma.getId();

        // Get all the notaList where turma equals to turmaId
        defaultNotaShouldBeFound("turmaId.equals=" + turmaId);

        // Get all the notaList where turma equals to turmaId + 1
        defaultNotaShouldNotBeFound("turmaId.equals=" + (turmaId + 1));
    }


    @Test
    @Transactional
    public void getAllNotasByCurriculoIsEqualToSomething() throws Exception {
        // Get already existing entity
        PlanoCurricular curriculo = nota.getCurriculo();
        notaRepository.saveAndFlush(nota);
        Long curriculoId = curriculo.getId();

        // Get all the notaList where curriculo equals to curriculoId
        defaultNotaShouldBeFound("curriculoId.equals=" + curriculoId);

        // Get all the notaList where curriculo equals to curriculoId + 1
        defaultNotaShouldNotBeFound("curriculoId.equals=" + (curriculoId + 1));
    }


    @Test
    @Transactional
    public void getAllNotasByProfessorIsEqualToSomething() throws Exception {
        // Get already existing entity
        Professor professor = nota.getProfessor();
        notaRepository.saveAndFlush(nota);
        Long professorId = professor.getId();

        // Get all the notaList where professor equals to professorId
        defaultNotaShouldBeFound("professorId.equals=" + professorId);

        // Get all the notaList where professor equals to professorId + 1
        defaultNotaShouldNotBeFound("professorId.equals=" + (professorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotaShouldBeFound(String filter) throws Exception {
        restNotaMockMvc.perform(get("/api/notas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nota.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroProcesso").value(hasItem(DEFAULT_NUMERO_PROCESSO)))
            .andExpect(jsonPath("$.[*].nomeAluno").value(hasItem(DEFAULT_NOME_ALUNO)))
            .andExpect(jsonPath("$.[*].disciplina").value(hasItem(DEFAULT_DISCIPLINA)))
            .andExpect(jsonPath("$.[*].peridoLectivo").value(hasItem(DEFAULT_PERIDO_LECTIVO)))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].faltaJustificada").value(hasItem(DEFAULT_FALTA_JUSTIFICADA)))
            .andExpect(jsonPath("$.[*].faltaInjustificada").value(hasItem(DEFAULT_FALTA_INJUSTIFICADA)))
            .andExpect(jsonPath("$.[*].avaliacaoContinuca").value(hasItem(DEFAULT_AVALIACAO_CONTINUCA.doubleValue())))
            .andExpect(jsonPath("$.[*].primeiraProva").value(hasItem(DEFAULT_PRIMEIRA_PROVA.doubleValue())))
            .andExpect(jsonPath("$.[*].segundaProva").value(hasItem(DEFAULT_SEGUNDA_PROVA.doubleValue())))
            .andExpect(jsonPath("$.[*].exame").value(hasItem(DEFAULT_EXAME.doubleValue())))
            .andExpect(jsonPath("$.[*].recurso").value(hasItem(DEFAULT_RECURSO.doubleValue())))
            .andExpect(jsonPath("$.[*].exameEspecial").value(hasItem(DEFAULT_EXAME_ESPECIAL.doubleValue())))
            .andExpect(jsonPath("$.[*].provaContentType").value(hasItem(DEFAULT_PROVA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].prova").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROVA))))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO)));

        // Check, that the count call also returns 1
        restNotaMockMvc.perform(get("/api/notas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotaShouldNotBeFound(String filter) throws Exception {
        restNotaMockMvc.perform(get("/api/notas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotaMockMvc.perform(get("/api/notas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingNota() throws Exception {
        // Get the nota
        restNotaMockMvc.perform(get("/api/notas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNota() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        int databaseSizeBeforeUpdate = notaRepository.findAll().size();

        // Update the nota
        Nota updatedNota = notaRepository.findById(nota.getId()).get();
        // Disconnect from session so that the updates on updatedNota are not directly saved in db
        em.detach(updatedNota);
        updatedNota
            .numeroProcesso(UPDATED_NUMERO_PROCESSO)
            .nomeAluno(UPDATED_NOME_ALUNO)
            .disciplina(UPDATED_DISCIPLINA)
            .peridoLectivo(UPDATED_PERIDO_LECTIVO)
            .anoLectivo(UPDATED_ANO_LECTIVO)
            .faltaJustificada(UPDATED_FALTA_JUSTIFICADA)
            .faltaInjustificada(UPDATED_FALTA_INJUSTIFICADA)
            .avaliacaoContinuca(UPDATED_AVALIACAO_CONTINUCA)
            .primeiraProva(UPDATED_PRIMEIRA_PROVA)
            .segundaProva(UPDATED_SEGUNDA_PROVA)
            .exame(UPDATED_EXAME)
            .recurso(UPDATED_RECURSO)
            .exameEspecial(UPDATED_EXAME_ESPECIAL)
            .prova(UPDATED_PROVA)
            .provaContentType(UPDATED_PROVA_CONTENT_TYPE)
            .situacao(UPDATED_SITUACAO);
        NotaDTO notaDTO = notaMapper.toDto(updatedNota);

        restNotaMockMvc.perform(put("/api/notas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notaDTO)))
            .andExpect(status().isOk());

        // Validate the Nota in the database
        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeUpdate);
        Nota testNota = notaList.get(notaList.size() - 1);
        assertThat(testNota.getNumeroProcesso()).isEqualTo(UPDATED_NUMERO_PROCESSO);
        assertThat(testNota.getNomeAluno()).isEqualTo(UPDATED_NOME_ALUNO);
        assertThat(testNota.getDisciplina()).isEqualTo(UPDATED_DISCIPLINA);
        assertThat(testNota.getPeridoLectivo()).isEqualTo(UPDATED_PERIDO_LECTIVO);
        assertThat(testNota.getAnoLectivo()).isEqualTo(UPDATED_ANO_LECTIVO);
        assertThat(testNota.getFaltaJustificada()).isEqualTo(UPDATED_FALTA_JUSTIFICADA);
        assertThat(testNota.getFaltaInjustificada()).isEqualTo(UPDATED_FALTA_INJUSTIFICADA);
        assertThat(testNota.getAvaliacaoContinuca()).isEqualTo(UPDATED_AVALIACAO_CONTINUCA);
        assertThat(testNota.getPrimeiraProva()).isEqualTo(UPDATED_PRIMEIRA_PROVA);
        assertThat(testNota.getSegundaProva()).isEqualTo(UPDATED_SEGUNDA_PROVA);
        assertThat(testNota.getExame()).isEqualTo(UPDATED_EXAME);
        assertThat(testNota.getRecurso()).isEqualTo(UPDATED_RECURSO);
        assertThat(testNota.getExameEspecial()).isEqualTo(UPDATED_EXAME_ESPECIAL);
        assertThat(testNota.getProva()).isEqualTo(UPDATED_PROVA);
        assertThat(testNota.getProvaContentType()).isEqualTo(UPDATED_PROVA_CONTENT_TYPE);
        assertThat(testNota.getSituacao()).isEqualTo(UPDATED_SITUACAO);

        // Validate the Nota in Elasticsearch
        verify(mockNotaSearchRepository, times(1)).save(testNota);
    }

    @Test
    @Transactional
    public void updateNonExistingNota() throws Exception {
        int databaseSizeBeforeUpdate = notaRepository.findAll().size();

        // Create the Nota
        NotaDTO notaDTO = notaMapper.toDto(nota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotaMockMvc.perform(put("/api/notas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Nota in the database
        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Nota in Elasticsearch
        verify(mockNotaSearchRepository, times(0)).save(nota);
    }

    @Test
    @Transactional
    public void deleteNota() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        int databaseSizeBeforeDelete = notaRepository.findAll().size();

        // Delete the nota
        restNotaMockMvc.perform(delete("/api/notas/{id}", nota.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Nota in Elasticsearch
        verify(mockNotaSearchRepository, times(1)).deleteById(nota.getId());
    }

    @Test
    @Transactional
    public void searchNota() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);
        when(mockNotaSearchRepository.search(queryStringQuery("id:" + nota.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(nota), PageRequest.of(0, 1), 1));
        // Search the nota
        restNotaMockMvc.perform(get("/api/_search/notas?query=id:" + nota.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nota.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroProcesso").value(hasItem(DEFAULT_NUMERO_PROCESSO)))
            .andExpect(jsonPath("$.[*].nomeAluno").value(hasItem(DEFAULT_NOME_ALUNO)))
            .andExpect(jsonPath("$.[*].disciplina").value(hasItem(DEFAULT_DISCIPLINA)))
            .andExpect(jsonPath("$.[*].peridoLectivo").value(hasItem(DEFAULT_PERIDO_LECTIVO)))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].faltaJustificada").value(hasItem(DEFAULT_FALTA_JUSTIFICADA)))
            .andExpect(jsonPath("$.[*].faltaInjustificada").value(hasItem(DEFAULT_FALTA_INJUSTIFICADA)))
            .andExpect(jsonPath("$.[*].avaliacaoContinuca").value(hasItem(DEFAULT_AVALIACAO_CONTINUCA.doubleValue())))
            .andExpect(jsonPath("$.[*].primeiraProva").value(hasItem(DEFAULT_PRIMEIRA_PROVA.doubleValue())))
            .andExpect(jsonPath("$.[*].segundaProva").value(hasItem(DEFAULT_SEGUNDA_PROVA.doubleValue())))
            .andExpect(jsonPath("$.[*].exame").value(hasItem(DEFAULT_EXAME.doubleValue())))
            .andExpect(jsonPath("$.[*].recurso").value(hasItem(DEFAULT_RECURSO.doubleValue())))
            .andExpect(jsonPath("$.[*].exameEspecial").value(hasItem(DEFAULT_EXAME_ESPECIAL.doubleValue())))
            .andExpect(jsonPath("$.[*].provaContentType").value(hasItem(DEFAULT_PROVA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].prova").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROVA))))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO)));
    }
}
