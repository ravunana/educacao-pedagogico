package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.ProvaAptidaoProfissional;
import com.ravunana.educacao.pedagogico.repository.ProvaAptidaoProfissionalRepository;
import com.ravunana.educacao.pedagogico.repository.search.ProvaAptidaoProfissionalSearchRepository;
import com.ravunana.educacao.pedagogico.service.ProvaAptidaoProfissionalService;
import com.ravunana.educacao.pedagogico.service.dto.ProvaAptidaoProfissionalDTO;
import com.ravunana.educacao.pedagogico.service.mapper.ProvaAptidaoProfissionalMapper;
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
 * Integration tests for the {@link ProvaAptidaoProfissionalResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class ProvaAptidaoProfissionalResourceIT {

    private static final String DEFAULT_NUMERO_PROCESSO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_PROCESSO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_ALUNO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_ALUNO = "BBBBBBBBBB";

    private static final Integer DEFAULT_LIVRO_REGISTRO = 1;
    private static final Integer UPDATED_LIVRO_REGISTRO = 2;

    private static final Integer DEFAULT_FOLHA_REGISTRO = 1;
    private static final Integer UPDATED_FOLHA_REGISTRO = 2;

    private static final String DEFAULT_TEMA_PROJECTO_TECNOLOGIO = "AAAAAAAAAA";
    private static final String UPDATED_TEMA_PROJECTO_TECNOLOGIO = "BBBBBBBBBB";

    private static final Double DEFAULT_NOTA_PROJECTO_TECNOLOGIO = 0D;
    private static final Double UPDATED_NOTA_PROJECTO_TECNOLOGIO = 1D;

    private static final ZonedDateTime DEFAULT_DATA_DEFESA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_DEFESA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LOCAL_ESTAGIO = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL_ESTAGIO = "BBBBBBBBBB";

    private static final String DEFAULT_APROVEITAMENTO_ESTAGIO = "AAAAAAAAAA";
    private static final String UPDATED_APROVEITAMENTO_ESTAGIO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INICIO_ESTAGIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INICIO_ESTAGIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FINAL_ESTAGIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FINAL_ESTAGIO = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ProvaAptidaoProfissionalRepository provaAptidaoProfissionalRepository;

    @Autowired
    private ProvaAptidaoProfissionalMapper provaAptidaoProfissionalMapper;

    @Autowired
    private ProvaAptidaoProfissionalService provaAptidaoProfissionalService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.ProvaAptidaoProfissionalSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProvaAptidaoProfissionalSearchRepository mockProvaAptidaoProfissionalSearchRepository;

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

    private MockMvc restProvaAptidaoProfissionalMockMvc;

    private ProvaAptidaoProfissional provaAptidaoProfissional;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProvaAptidaoProfissionalResource provaAptidaoProfissionalResource = new ProvaAptidaoProfissionalResource(provaAptidaoProfissionalService);
        this.restProvaAptidaoProfissionalMockMvc = MockMvcBuilders.standaloneSetup(provaAptidaoProfissionalResource)
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
    public static ProvaAptidaoProfissional createEntity(EntityManager em) {
        ProvaAptidaoProfissional provaAptidaoProfissional = new ProvaAptidaoProfissional()
            .numeroProcesso(DEFAULT_NUMERO_PROCESSO)
            .nomeAluno(DEFAULT_NOME_ALUNO)
            .livroRegistro(DEFAULT_LIVRO_REGISTRO)
            .folhaRegistro(DEFAULT_FOLHA_REGISTRO)
            .temaProjectoTecnologio(DEFAULT_TEMA_PROJECTO_TECNOLOGIO)
            .notaProjectoTecnologio(DEFAULT_NOTA_PROJECTO_TECNOLOGIO)
            .dataDefesa(DEFAULT_DATA_DEFESA)
            .localEstagio(DEFAULT_LOCAL_ESTAGIO)
            .aproveitamentoEstagio(DEFAULT_APROVEITAMENTO_ESTAGIO)
            .inicioEstagio(DEFAULT_INICIO_ESTAGIO)
            .finalEstagio(DEFAULT_FINAL_ESTAGIO)
            .data(DEFAULT_DATA);
        return provaAptidaoProfissional;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProvaAptidaoProfissional createUpdatedEntity(EntityManager em) {
        ProvaAptidaoProfissional provaAptidaoProfissional = new ProvaAptidaoProfissional()
            .numeroProcesso(UPDATED_NUMERO_PROCESSO)
            .nomeAluno(UPDATED_NOME_ALUNO)
            .livroRegistro(UPDATED_LIVRO_REGISTRO)
            .folhaRegistro(UPDATED_FOLHA_REGISTRO)
            .temaProjectoTecnologio(UPDATED_TEMA_PROJECTO_TECNOLOGIO)
            .notaProjectoTecnologio(UPDATED_NOTA_PROJECTO_TECNOLOGIO)
            .dataDefesa(UPDATED_DATA_DEFESA)
            .localEstagio(UPDATED_LOCAL_ESTAGIO)
            .aproveitamentoEstagio(UPDATED_APROVEITAMENTO_ESTAGIO)
            .inicioEstagio(UPDATED_INICIO_ESTAGIO)
            .finalEstagio(UPDATED_FINAL_ESTAGIO)
            .data(UPDATED_DATA);
        return provaAptidaoProfissional;
    }

    @BeforeEach
    public void initTest() {
        provaAptidaoProfissional = createEntity(em);
    }

    @Test
    @Transactional
    public void createProvaAptidaoProfissional() throws Exception {
        int databaseSizeBeforeCreate = provaAptidaoProfissionalRepository.findAll().size();

        // Create the ProvaAptidaoProfissional
        ProvaAptidaoProfissionalDTO provaAptidaoProfissionalDTO = provaAptidaoProfissionalMapper.toDto(provaAptidaoProfissional);
        restProvaAptidaoProfissionalMockMvc.perform(post("/api/prova-aptidao-profissionals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provaAptidaoProfissionalDTO)))
            .andExpect(status().isCreated());

        // Validate the ProvaAptidaoProfissional in the database
        List<ProvaAptidaoProfissional> provaAptidaoProfissionalList = provaAptidaoProfissionalRepository.findAll();
        assertThat(provaAptidaoProfissionalList).hasSize(databaseSizeBeforeCreate + 1);
        ProvaAptidaoProfissional testProvaAptidaoProfissional = provaAptidaoProfissionalList.get(provaAptidaoProfissionalList.size() - 1);
        assertThat(testProvaAptidaoProfissional.getNumeroProcesso()).isEqualTo(DEFAULT_NUMERO_PROCESSO);
        assertThat(testProvaAptidaoProfissional.getNomeAluno()).isEqualTo(DEFAULT_NOME_ALUNO);
        assertThat(testProvaAptidaoProfissional.getLivroRegistro()).isEqualTo(DEFAULT_LIVRO_REGISTRO);
        assertThat(testProvaAptidaoProfissional.getFolhaRegistro()).isEqualTo(DEFAULT_FOLHA_REGISTRO);
        assertThat(testProvaAptidaoProfissional.getTemaProjectoTecnologio()).isEqualTo(DEFAULT_TEMA_PROJECTO_TECNOLOGIO);
        assertThat(testProvaAptidaoProfissional.getNotaProjectoTecnologio()).isEqualTo(DEFAULT_NOTA_PROJECTO_TECNOLOGIO);
        assertThat(testProvaAptidaoProfissional.getDataDefesa()).isEqualTo(DEFAULT_DATA_DEFESA);
        assertThat(testProvaAptidaoProfissional.getLocalEstagio()).isEqualTo(DEFAULT_LOCAL_ESTAGIO);
        assertThat(testProvaAptidaoProfissional.getAproveitamentoEstagio()).isEqualTo(DEFAULT_APROVEITAMENTO_ESTAGIO);
        assertThat(testProvaAptidaoProfissional.getInicioEstagio()).isEqualTo(DEFAULT_INICIO_ESTAGIO);
        assertThat(testProvaAptidaoProfissional.getFinalEstagio()).isEqualTo(DEFAULT_FINAL_ESTAGIO);
        assertThat(testProvaAptidaoProfissional.getData()).isEqualTo(DEFAULT_DATA);

        // Validate the ProvaAptidaoProfissional in Elasticsearch
        verify(mockProvaAptidaoProfissionalSearchRepository, times(1)).save(testProvaAptidaoProfissional);
    }

    @Test
    @Transactional
    public void createProvaAptidaoProfissionalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = provaAptidaoProfissionalRepository.findAll().size();

        // Create the ProvaAptidaoProfissional with an existing ID
        provaAptidaoProfissional.setId(1L);
        ProvaAptidaoProfissionalDTO provaAptidaoProfissionalDTO = provaAptidaoProfissionalMapper.toDto(provaAptidaoProfissional);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvaAptidaoProfissionalMockMvc.perform(post("/api/prova-aptidao-profissionals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provaAptidaoProfissionalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProvaAptidaoProfissional in the database
        List<ProvaAptidaoProfissional> provaAptidaoProfissionalList = provaAptidaoProfissionalRepository.findAll();
        assertThat(provaAptidaoProfissionalList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProvaAptidaoProfissional in Elasticsearch
        verify(mockProvaAptidaoProfissionalSearchRepository, times(0)).save(provaAptidaoProfissional);
    }


    @Test
    @Transactional
    public void checkNumeroProcessoIsRequired() throws Exception {
        int databaseSizeBeforeTest = provaAptidaoProfissionalRepository.findAll().size();
        // set the field null
        provaAptidaoProfissional.setNumeroProcesso(null);

        // Create the ProvaAptidaoProfissional, which fails.
        ProvaAptidaoProfissionalDTO provaAptidaoProfissionalDTO = provaAptidaoProfissionalMapper.toDto(provaAptidaoProfissional);

        restProvaAptidaoProfissionalMockMvc.perform(post("/api/prova-aptidao-profissionals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provaAptidaoProfissionalDTO)))
            .andExpect(status().isBadRequest());

        List<ProvaAptidaoProfissional> provaAptidaoProfissionalList = provaAptidaoProfissionalRepository.findAll();
        assertThat(provaAptidaoProfissionalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomeAlunoIsRequired() throws Exception {
        int databaseSizeBeforeTest = provaAptidaoProfissionalRepository.findAll().size();
        // set the field null
        provaAptidaoProfissional.setNomeAluno(null);

        // Create the ProvaAptidaoProfissional, which fails.
        ProvaAptidaoProfissionalDTO provaAptidaoProfissionalDTO = provaAptidaoProfissionalMapper.toDto(provaAptidaoProfissional);

        restProvaAptidaoProfissionalMockMvc.perform(post("/api/prova-aptidao-profissionals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provaAptidaoProfissionalDTO)))
            .andExpect(status().isBadRequest());

        List<ProvaAptidaoProfissional> provaAptidaoProfissionalList = provaAptidaoProfissionalRepository.findAll();
        assertThat(provaAptidaoProfissionalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProvaAptidaoProfissionals() throws Exception {
        // Initialize the database
        provaAptidaoProfissionalRepository.saveAndFlush(provaAptidaoProfissional);

        // Get all the provaAptidaoProfissionalList
        restProvaAptidaoProfissionalMockMvc.perform(get("/api/prova-aptidao-profissionals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(provaAptidaoProfissional.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroProcesso").value(hasItem(DEFAULT_NUMERO_PROCESSO)))
            .andExpect(jsonPath("$.[*].nomeAluno").value(hasItem(DEFAULT_NOME_ALUNO)))
            .andExpect(jsonPath("$.[*].livroRegistro").value(hasItem(DEFAULT_LIVRO_REGISTRO)))
            .andExpect(jsonPath("$.[*].folhaRegistro").value(hasItem(DEFAULT_FOLHA_REGISTRO)))
            .andExpect(jsonPath("$.[*].temaProjectoTecnologio").value(hasItem(DEFAULT_TEMA_PROJECTO_TECNOLOGIO)))
            .andExpect(jsonPath("$.[*].notaProjectoTecnologio").value(hasItem(DEFAULT_NOTA_PROJECTO_TECNOLOGIO.doubleValue())))
            .andExpect(jsonPath("$.[*].dataDefesa").value(hasItem(sameInstant(DEFAULT_DATA_DEFESA))))
            .andExpect(jsonPath("$.[*].localEstagio").value(hasItem(DEFAULT_LOCAL_ESTAGIO)))
            .andExpect(jsonPath("$.[*].aproveitamentoEstagio").value(hasItem(DEFAULT_APROVEITAMENTO_ESTAGIO)))
            .andExpect(jsonPath("$.[*].inicioEstagio").value(hasItem(DEFAULT_INICIO_ESTAGIO.toString())))
            .andExpect(jsonPath("$.[*].finalEstagio").value(hasItem(DEFAULT_FINAL_ESTAGIO.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))));
    }
    
    @Test
    @Transactional
    public void getProvaAptidaoProfissional() throws Exception {
        // Initialize the database
        provaAptidaoProfissionalRepository.saveAndFlush(provaAptidaoProfissional);

        // Get the provaAptidaoProfissional
        restProvaAptidaoProfissionalMockMvc.perform(get("/api/prova-aptidao-profissionals/{id}", provaAptidaoProfissional.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(provaAptidaoProfissional.getId().intValue()))
            .andExpect(jsonPath("$.numeroProcesso").value(DEFAULT_NUMERO_PROCESSO))
            .andExpect(jsonPath("$.nomeAluno").value(DEFAULT_NOME_ALUNO))
            .andExpect(jsonPath("$.livroRegistro").value(DEFAULT_LIVRO_REGISTRO))
            .andExpect(jsonPath("$.folhaRegistro").value(DEFAULT_FOLHA_REGISTRO))
            .andExpect(jsonPath("$.temaProjectoTecnologio").value(DEFAULT_TEMA_PROJECTO_TECNOLOGIO))
            .andExpect(jsonPath("$.notaProjectoTecnologio").value(DEFAULT_NOTA_PROJECTO_TECNOLOGIO.doubleValue()))
            .andExpect(jsonPath("$.dataDefesa").value(sameInstant(DEFAULT_DATA_DEFESA)))
            .andExpect(jsonPath("$.localEstagio").value(DEFAULT_LOCAL_ESTAGIO))
            .andExpect(jsonPath("$.aproveitamentoEstagio").value(DEFAULT_APROVEITAMENTO_ESTAGIO))
            .andExpect(jsonPath("$.inicioEstagio").value(DEFAULT_INICIO_ESTAGIO.toString()))
            .andExpect(jsonPath("$.finalEstagio").value(DEFAULT_FINAL_ESTAGIO.toString()))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)));
    }

    @Test
    @Transactional
    public void getNonExistingProvaAptidaoProfissional() throws Exception {
        // Get the provaAptidaoProfissional
        restProvaAptidaoProfissionalMockMvc.perform(get("/api/prova-aptidao-profissionals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProvaAptidaoProfissional() throws Exception {
        // Initialize the database
        provaAptidaoProfissionalRepository.saveAndFlush(provaAptidaoProfissional);

        int databaseSizeBeforeUpdate = provaAptidaoProfissionalRepository.findAll().size();

        // Update the provaAptidaoProfissional
        ProvaAptidaoProfissional updatedProvaAptidaoProfissional = provaAptidaoProfissionalRepository.findById(provaAptidaoProfissional.getId()).get();
        // Disconnect from session so that the updates on updatedProvaAptidaoProfissional are not directly saved in db
        em.detach(updatedProvaAptidaoProfissional);
        updatedProvaAptidaoProfissional
            .numeroProcesso(UPDATED_NUMERO_PROCESSO)
            .nomeAluno(UPDATED_NOME_ALUNO)
            .livroRegistro(UPDATED_LIVRO_REGISTRO)
            .folhaRegistro(UPDATED_FOLHA_REGISTRO)
            .temaProjectoTecnologio(UPDATED_TEMA_PROJECTO_TECNOLOGIO)
            .notaProjectoTecnologio(UPDATED_NOTA_PROJECTO_TECNOLOGIO)
            .dataDefesa(UPDATED_DATA_DEFESA)
            .localEstagio(UPDATED_LOCAL_ESTAGIO)
            .aproveitamentoEstagio(UPDATED_APROVEITAMENTO_ESTAGIO)
            .inicioEstagio(UPDATED_INICIO_ESTAGIO)
            .finalEstagio(UPDATED_FINAL_ESTAGIO)
            .data(UPDATED_DATA);
        ProvaAptidaoProfissionalDTO provaAptidaoProfissionalDTO = provaAptidaoProfissionalMapper.toDto(updatedProvaAptidaoProfissional);

        restProvaAptidaoProfissionalMockMvc.perform(put("/api/prova-aptidao-profissionals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provaAptidaoProfissionalDTO)))
            .andExpect(status().isOk());

        // Validate the ProvaAptidaoProfissional in the database
        List<ProvaAptidaoProfissional> provaAptidaoProfissionalList = provaAptidaoProfissionalRepository.findAll();
        assertThat(provaAptidaoProfissionalList).hasSize(databaseSizeBeforeUpdate);
        ProvaAptidaoProfissional testProvaAptidaoProfissional = provaAptidaoProfissionalList.get(provaAptidaoProfissionalList.size() - 1);
        assertThat(testProvaAptidaoProfissional.getNumeroProcesso()).isEqualTo(UPDATED_NUMERO_PROCESSO);
        assertThat(testProvaAptidaoProfissional.getNomeAluno()).isEqualTo(UPDATED_NOME_ALUNO);
        assertThat(testProvaAptidaoProfissional.getLivroRegistro()).isEqualTo(UPDATED_LIVRO_REGISTRO);
        assertThat(testProvaAptidaoProfissional.getFolhaRegistro()).isEqualTo(UPDATED_FOLHA_REGISTRO);
        assertThat(testProvaAptidaoProfissional.getTemaProjectoTecnologio()).isEqualTo(UPDATED_TEMA_PROJECTO_TECNOLOGIO);
        assertThat(testProvaAptidaoProfissional.getNotaProjectoTecnologio()).isEqualTo(UPDATED_NOTA_PROJECTO_TECNOLOGIO);
        assertThat(testProvaAptidaoProfissional.getDataDefesa()).isEqualTo(UPDATED_DATA_DEFESA);
        assertThat(testProvaAptidaoProfissional.getLocalEstagio()).isEqualTo(UPDATED_LOCAL_ESTAGIO);
        assertThat(testProvaAptidaoProfissional.getAproveitamentoEstagio()).isEqualTo(UPDATED_APROVEITAMENTO_ESTAGIO);
        assertThat(testProvaAptidaoProfissional.getInicioEstagio()).isEqualTo(UPDATED_INICIO_ESTAGIO);
        assertThat(testProvaAptidaoProfissional.getFinalEstagio()).isEqualTo(UPDATED_FINAL_ESTAGIO);
        assertThat(testProvaAptidaoProfissional.getData()).isEqualTo(UPDATED_DATA);

        // Validate the ProvaAptidaoProfissional in Elasticsearch
        verify(mockProvaAptidaoProfissionalSearchRepository, times(1)).save(testProvaAptidaoProfissional);
    }

    @Test
    @Transactional
    public void updateNonExistingProvaAptidaoProfissional() throws Exception {
        int databaseSizeBeforeUpdate = provaAptidaoProfissionalRepository.findAll().size();

        // Create the ProvaAptidaoProfissional
        ProvaAptidaoProfissionalDTO provaAptidaoProfissionalDTO = provaAptidaoProfissionalMapper.toDto(provaAptidaoProfissional);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvaAptidaoProfissionalMockMvc.perform(put("/api/prova-aptidao-profissionals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provaAptidaoProfissionalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProvaAptidaoProfissional in the database
        List<ProvaAptidaoProfissional> provaAptidaoProfissionalList = provaAptidaoProfissionalRepository.findAll();
        assertThat(provaAptidaoProfissionalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProvaAptidaoProfissional in Elasticsearch
        verify(mockProvaAptidaoProfissionalSearchRepository, times(0)).save(provaAptidaoProfissional);
    }

    @Test
    @Transactional
    public void deleteProvaAptidaoProfissional() throws Exception {
        // Initialize the database
        provaAptidaoProfissionalRepository.saveAndFlush(provaAptidaoProfissional);

        int databaseSizeBeforeDelete = provaAptidaoProfissionalRepository.findAll().size();

        // Delete the provaAptidaoProfissional
        restProvaAptidaoProfissionalMockMvc.perform(delete("/api/prova-aptidao-profissionals/{id}", provaAptidaoProfissional.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProvaAptidaoProfissional> provaAptidaoProfissionalList = provaAptidaoProfissionalRepository.findAll();
        assertThat(provaAptidaoProfissionalList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProvaAptidaoProfissional in Elasticsearch
        verify(mockProvaAptidaoProfissionalSearchRepository, times(1)).deleteById(provaAptidaoProfissional.getId());
    }

    @Test
    @Transactional
    public void searchProvaAptidaoProfissional() throws Exception {
        // Initialize the database
        provaAptidaoProfissionalRepository.saveAndFlush(provaAptidaoProfissional);
        when(mockProvaAptidaoProfissionalSearchRepository.search(queryStringQuery("id:" + provaAptidaoProfissional.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(provaAptidaoProfissional), PageRequest.of(0, 1), 1));
        // Search the provaAptidaoProfissional
        restProvaAptidaoProfissionalMockMvc.perform(get("/api/_search/prova-aptidao-profissionals?query=id:" + provaAptidaoProfissional.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(provaAptidaoProfissional.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroProcesso").value(hasItem(DEFAULT_NUMERO_PROCESSO)))
            .andExpect(jsonPath("$.[*].nomeAluno").value(hasItem(DEFAULT_NOME_ALUNO)))
            .andExpect(jsonPath("$.[*].livroRegistro").value(hasItem(DEFAULT_LIVRO_REGISTRO)))
            .andExpect(jsonPath("$.[*].folhaRegistro").value(hasItem(DEFAULT_FOLHA_REGISTRO)))
            .andExpect(jsonPath("$.[*].temaProjectoTecnologio").value(hasItem(DEFAULT_TEMA_PROJECTO_TECNOLOGIO)))
            .andExpect(jsonPath("$.[*].notaProjectoTecnologio").value(hasItem(DEFAULT_NOTA_PROJECTO_TECNOLOGIO.doubleValue())))
            .andExpect(jsonPath("$.[*].dataDefesa").value(hasItem(sameInstant(DEFAULT_DATA_DEFESA))))
            .andExpect(jsonPath("$.[*].localEstagio").value(hasItem(DEFAULT_LOCAL_ESTAGIO)))
            .andExpect(jsonPath("$.[*].aproveitamentoEstagio").value(hasItem(DEFAULT_APROVEITAMENTO_ESTAGIO)))
            .andExpect(jsonPath("$.[*].inicioEstagio").value(hasItem(DEFAULT_INICIO_ESTAGIO.toString())))
            .andExpect(jsonPath("$.[*].finalEstagio").value(hasItem(DEFAULT_FINAL_ESTAGIO.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))));
    }
}
