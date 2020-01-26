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

    private static final Integer DEFAULT_FALTA_JUSTIFICADA = 1;
    private static final Integer UPDATED_FALTA_JUSTIFICADA = 2;

    private static final Integer DEFAULT_FALTA_INJUSTIFICADA = 1;
    private static final Integer UPDATED_FALTA_INJUSTIFICADA = 2;

    private static final Double DEFAULT_AVALIACAO_CONTINUCA = 0D;
    private static final Double UPDATED_AVALIACAO_CONTINUCA = 1D;

    private static final Double DEFAULT_PRIMEIRA_PROVA = 0D;
    private static final Double UPDATED_PRIMEIRA_PROVA = 1D;

    private static final Double DEFAULT_SEGUNDA_PROVA = 0D;
    private static final Double UPDATED_SEGUNDA_PROVA = 1D;

    private static final Double DEFAULT_EXAME = 0D;
    private static final Double UPDATED_EXAME = 1D;

    private static final Double DEFAULT_RECURSO = 0D;
    private static final Double UPDATED_RECURSO = 1D;

    private static final Double DEFAULT_EXAME_ESPECIAL = 0D;
    private static final Double UPDATED_EXAME_ESPECIAL = 1D;

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
        final NotaResource notaResource = new NotaResource(notaService);
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
