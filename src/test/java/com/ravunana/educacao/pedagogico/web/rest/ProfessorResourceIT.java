package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.Professor;
import com.ravunana.educacao.pedagogico.repository.ProfessorRepository;
import com.ravunana.educacao.pedagogico.repository.search.ProfessorSearchRepository;
import com.ravunana.educacao.pedagogico.service.ProfessorService;
import com.ravunana.educacao.pedagogico.service.dto.ProfessorDTO;
import com.ravunana.educacao.pedagogico.service.mapper.ProfessorMapper;
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
 * Integration tests for the {@link ProfessorResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class ProfessorResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SEXO = "AAAAAAAAAA";
    private static final String UPDATED_SEXO = "BBBBBBBBBB";

    private static final String DEFAULT_FOTOGRAFIA = "AAAAAAAAAA";
    private static final String UPDATED_FOTOGRAFIA = "BBBBBBBBBB";

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
        final ProfessorResource professorResource = new ProfessorResource(professorService);
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
            .contacto(DEFAULT_CONTACTO)
            .email(DEFAULT_EMAIL)
            .residencia(DEFAULT_RESIDENCIA)
            .numeroAgente(DEFAULT_NUMERO_AGENTE)
            .utilizadorId(DEFAULT_UTILIZADOR_ID)
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
            .contacto(UPDATED_CONTACTO)
            .email(UPDATED_EMAIL)
            .residencia(UPDATED_RESIDENCIA)
            .numeroAgente(UPDATED_NUMERO_AGENTE)
            .utilizadorId(UPDATED_UTILIZADOR_ID)
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
        assertThat(testProfessor.getContacto()).isEqualTo(DEFAULT_CONTACTO);
        assertThat(testProfessor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfessor.getResidencia()).isEqualTo(DEFAULT_RESIDENCIA);
        assertThat(testProfessor.getNumeroAgente()).isEqualTo(DEFAULT_NUMERO_AGENTE);
        assertThat(testProfessor.getUtilizadorId()).isEqualTo(DEFAULT_UTILIZADOR_ID);
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
            .andExpect(jsonPath("$.[*].fotografia").value(hasItem(DEFAULT_FOTOGRAFIA)))
            .andExpect(jsonPath("$.[*].contacto").value(hasItem(DEFAULT_CONTACTO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].residencia").value(hasItem(DEFAULT_RESIDENCIA)))
            .andExpect(jsonPath("$.[*].numeroAgente").value(hasItem(DEFAULT_NUMERO_AGENTE)))
            .andExpect(jsonPath("$.[*].utilizadorId").value(hasItem(DEFAULT_UTILIZADOR_ID)))
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
            .andExpect(jsonPath("$.fotografia").value(DEFAULT_FOTOGRAFIA))
            .andExpect(jsonPath("$.contacto").value(DEFAULT_CONTACTO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.residencia").value(DEFAULT_RESIDENCIA))
            .andExpect(jsonPath("$.numeroAgente").value(DEFAULT_NUMERO_AGENTE))
            .andExpect(jsonPath("$.utilizadorId").value(DEFAULT_UTILIZADOR_ID))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()));
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
            .contacto(UPDATED_CONTACTO)
            .email(UPDATED_EMAIL)
            .residencia(UPDATED_RESIDENCIA)
            .numeroAgente(UPDATED_NUMERO_AGENTE)
            .utilizadorId(UPDATED_UTILIZADOR_ID)
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
        assertThat(testProfessor.getContacto()).isEqualTo(UPDATED_CONTACTO);
        assertThat(testProfessor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfessor.getResidencia()).isEqualTo(UPDATED_RESIDENCIA);
        assertThat(testProfessor.getNumeroAgente()).isEqualTo(UPDATED_NUMERO_AGENTE);
        assertThat(testProfessor.getUtilizadorId()).isEqualTo(UPDATED_UTILIZADOR_ID);
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
            .andExpect(jsonPath("$.[*].fotografia").value(hasItem(DEFAULT_FOTOGRAFIA)))
            .andExpect(jsonPath("$.[*].contacto").value(hasItem(DEFAULT_CONTACTO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].residencia").value(hasItem(DEFAULT_RESIDENCIA)))
            .andExpect(jsonPath("$.[*].numeroAgente").value(hasItem(DEFAULT_NUMERO_AGENTE)))
            .andExpect(jsonPath("$.[*].utilizadorId").value(hasItem(DEFAULT_UTILIZADOR_ID)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }
}
