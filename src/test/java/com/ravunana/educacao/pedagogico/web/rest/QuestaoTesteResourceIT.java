package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.QuestaoTeste;
import com.ravunana.educacao.pedagogico.domain.TesteConhecimento;
import com.ravunana.educacao.pedagogico.repository.QuestaoTesteRepository;
import com.ravunana.educacao.pedagogico.repository.search.QuestaoTesteSearchRepository;
import com.ravunana.educacao.pedagogico.service.QuestaoTesteService;
import com.ravunana.educacao.pedagogico.service.dto.QuestaoTesteDTO;
import com.ravunana.educacao.pedagogico.service.mapper.QuestaoTesteMapper;
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
 * Integration tests for the {@link QuestaoTesteResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class QuestaoTesteResourceIT {

    private static final String DEFAULT_GRUPO = "AAAAAAAAAA";
    private static final String UPDATED_GRUPO = "BBBBBBBBBB";

    private static final Double DEFAULT_NUMERO = 1D;
    private static final Double UPDATED_NUMERO = 2D;

    private static final String DEFAULT_ARGUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_ARGUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTAO = "AAAAAAAAAA";
    private static final String UPDATED_QUESTAO = "BBBBBBBBBB";

    @Autowired
    private QuestaoTesteRepository questaoTesteRepository;

    @Autowired
    private QuestaoTesteMapper questaoTesteMapper;

    @Autowired
    private QuestaoTesteService questaoTesteService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.QuestaoTesteSearchRepositoryMockConfiguration
     */
    @Autowired
    private QuestaoTesteSearchRepository mockQuestaoTesteSearchRepository;

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

    private MockMvc restQuestaoTesteMockMvc;

    private QuestaoTeste questaoTeste;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuestaoTesteResource questaoTesteResource = new QuestaoTesteResource(questaoTesteService);
        this.restQuestaoTesteMockMvc = MockMvcBuilders.standaloneSetup(questaoTesteResource)
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
    public static QuestaoTeste createEntity(EntityManager em) {
        QuestaoTeste questaoTeste = new QuestaoTeste()
            .grupo(DEFAULT_GRUPO)
            .numero(DEFAULT_NUMERO)
            .argumento(DEFAULT_ARGUMENTO)
            .questao(DEFAULT_QUESTAO);
        // Add required entity
        TesteConhecimento testeConhecimento;
        if (TestUtil.findAll(em, TesteConhecimento.class).isEmpty()) {
            testeConhecimento = TesteConhecimentoResourceIT.createEntity(em);
            em.persist(testeConhecimento);
            em.flush();
        } else {
            testeConhecimento = TestUtil.findAll(em, TesteConhecimento.class).get(0);
        }
        questaoTeste.setTeste(testeConhecimento);
        return questaoTeste;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestaoTeste createUpdatedEntity(EntityManager em) {
        QuestaoTeste questaoTeste = new QuestaoTeste()
            .grupo(UPDATED_GRUPO)
            .numero(UPDATED_NUMERO)
            .argumento(UPDATED_ARGUMENTO)
            .questao(UPDATED_QUESTAO);
        // Add required entity
        TesteConhecimento testeConhecimento;
        if (TestUtil.findAll(em, TesteConhecimento.class).isEmpty()) {
            testeConhecimento = TesteConhecimentoResourceIT.createUpdatedEntity(em);
            em.persist(testeConhecimento);
            em.flush();
        } else {
            testeConhecimento = TestUtil.findAll(em, TesteConhecimento.class).get(0);
        }
        questaoTeste.setTeste(testeConhecimento);
        return questaoTeste;
    }

    @BeforeEach
    public void initTest() {
        questaoTeste = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestaoTeste() throws Exception {
        int databaseSizeBeforeCreate = questaoTesteRepository.findAll().size();

        // Create the QuestaoTeste
        QuestaoTesteDTO questaoTesteDTO = questaoTesteMapper.toDto(questaoTeste);
        restQuestaoTesteMockMvc.perform(post("/api/questao-testes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questaoTesteDTO)))
            .andExpect(status().isCreated());

        // Validate the QuestaoTeste in the database
        List<QuestaoTeste> questaoTesteList = questaoTesteRepository.findAll();
        assertThat(questaoTesteList).hasSize(databaseSizeBeforeCreate + 1);
        QuestaoTeste testQuestaoTeste = questaoTesteList.get(questaoTesteList.size() - 1);
        assertThat(testQuestaoTeste.getGrupo()).isEqualTo(DEFAULT_GRUPO);
        assertThat(testQuestaoTeste.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testQuestaoTeste.getArgumento()).isEqualTo(DEFAULT_ARGUMENTO);
        assertThat(testQuestaoTeste.getQuestao()).isEqualTo(DEFAULT_QUESTAO);

        // Validate the QuestaoTeste in Elasticsearch
        verify(mockQuestaoTesteSearchRepository, times(1)).save(testQuestaoTeste);
    }

    @Test
    @Transactional
    public void createQuestaoTesteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questaoTesteRepository.findAll().size();

        // Create the QuestaoTeste with an existing ID
        questaoTeste.setId(1L);
        QuestaoTesteDTO questaoTesteDTO = questaoTesteMapper.toDto(questaoTeste);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestaoTesteMockMvc.perform(post("/api/questao-testes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questaoTesteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestaoTeste in the database
        List<QuestaoTeste> questaoTesteList = questaoTesteRepository.findAll();
        assertThat(questaoTesteList).hasSize(databaseSizeBeforeCreate);

        // Validate the QuestaoTeste in Elasticsearch
        verify(mockQuestaoTesteSearchRepository, times(0)).save(questaoTeste);
    }


    @Test
    @Transactional
    public void checkGrupoIsRequired() throws Exception {
        int databaseSizeBeforeTest = questaoTesteRepository.findAll().size();
        // set the field null
        questaoTeste.setGrupo(null);

        // Create the QuestaoTeste, which fails.
        QuestaoTesteDTO questaoTesteDTO = questaoTesteMapper.toDto(questaoTeste);

        restQuestaoTesteMockMvc.perform(post("/api/questao-testes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questaoTesteDTO)))
            .andExpect(status().isBadRequest());

        List<QuestaoTeste> questaoTesteList = questaoTesteRepository.findAll();
        assertThat(questaoTesteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = questaoTesteRepository.findAll().size();
        // set the field null
        questaoTeste.setNumero(null);

        // Create the QuestaoTeste, which fails.
        QuestaoTesteDTO questaoTesteDTO = questaoTesteMapper.toDto(questaoTeste);

        restQuestaoTesteMockMvc.perform(post("/api/questao-testes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questaoTesteDTO)))
            .andExpect(status().isBadRequest());

        List<QuestaoTeste> questaoTesteList = questaoTesteRepository.findAll();
        assertThat(questaoTesteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuestaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = questaoTesteRepository.findAll().size();
        // set the field null
        questaoTeste.setQuestao(null);

        // Create the QuestaoTeste, which fails.
        QuestaoTesteDTO questaoTesteDTO = questaoTesteMapper.toDto(questaoTeste);

        restQuestaoTesteMockMvc.perform(post("/api/questao-testes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questaoTesteDTO)))
            .andExpect(status().isBadRequest());

        List<QuestaoTeste> questaoTesteList = questaoTesteRepository.findAll();
        assertThat(questaoTesteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuestaoTestes() throws Exception {
        // Initialize the database
        questaoTesteRepository.saveAndFlush(questaoTeste);

        // Get all the questaoTesteList
        restQuestaoTesteMockMvc.perform(get("/api/questao-testes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questaoTeste.getId().intValue())))
            .andExpect(jsonPath("$.[*].grupo").value(hasItem(DEFAULT_GRUPO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.doubleValue())))
            .andExpect(jsonPath("$.[*].argumento").value(hasItem(DEFAULT_ARGUMENTO)))
            .andExpect(jsonPath("$.[*].questao").value(hasItem(DEFAULT_QUESTAO)));
    }
    
    @Test
    @Transactional
    public void getQuestaoTeste() throws Exception {
        // Initialize the database
        questaoTesteRepository.saveAndFlush(questaoTeste);

        // Get the questaoTeste
        restQuestaoTesteMockMvc.perform(get("/api/questao-testes/{id}", questaoTeste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(questaoTeste.getId().intValue()))
            .andExpect(jsonPath("$.grupo").value(DEFAULT_GRUPO))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.doubleValue()))
            .andExpect(jsonPath("$.argumento").value(DEFAULT_ARGUMENTO))
            .andExpect(jsonPath("$.questao").value(DEFAULT_QUESTAO));
    }

    @Test
    @Transactional
    public void getNonExistingQuestaoTeste() throws Exception {
        // Get the questaoTeste
        restQuestaoTesteMockMvc.perform(get("/api/questao-testes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestaoTeste() throws Exception {
        // Initialize the database
        questaoTesteRepository.saveAndFlush(questaoTeste);

        int databaseSizeBeforeUpdate = questaoTesteRepository.findAll().size();

        // Update the questaoTeste
        QuestaoTeste updatedQuestaoTeste = questaoTesteRepository.findById(questaoTeste.getId()).get();
        // Disconnect from session so that the updates on updatedQuestaoTeste are not directly saved in db
        em.detach(updatedQuestaoTeste);
        updatedQuestaoTeste
            .grupo(UPDATED_GRUPO)
            .numero(UPDATED_NUMERO)
            .argumento(UPDATED_ARGUMENTO)
            .questao(UPDATED_QUESTAO);
        QuestaoTesteDTO questaoTesteDTO = questaoTesteMapper.toDto(updatedQuestaoTeste);

        restQuestaoTesteMockMvc.perform(put("/api/questao-testes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questaoTesteDTO)))
            .andExpect(status().isOk());

        // Validate the QuestaoTeste in the database
        List<QuestaoTeste> questaoTesteList = questaoTesteRepository.findAll();
        assertThat(questaoTesteList).hasSize(databaseSizeBeforeUpdate);
        QuestaoTeste testQuestaoTeste = questaoTesteList.get(questaoTesteList.size() - 1);
        assertThat(testQuestaoTeste.getGrupo()).isEqualTo(UPDATED_GRUPO);
        assertThat(testQuestaoTeste.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testQuestaoTeste.getArgumento()).isEqualTo(UPDATED_ARGUMENTO);
        assertThat(testQuestaoTeste.getQuestao()).isEqualTo(UPDATED_QUESTAO);

        // Validate the QuestaoTeste in Elasticsearch
        verify(mockQuestaoTesteSearchRepository, times(1)).save(testQuestaoTeste);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestaoTeste() throws Exception {
        int databaseSizeBeforeUpdate = questaoTesteRepository.findAll().size();

        // Create the QuestaoTeste
        QuestaoTesteDTO questaoTesteDTO = questaoTesteMapper.toDto(questaoTeste);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestaoTesteMockMvc.perform(put("/api/questao-testes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questaoTesteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestaoTeste in the database
        List<QuestaoTeste> questaoTesteList = questaoTesteRepository.findAll();
        assertThat(questaoTesteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the QuestaoTeste in Elasticsearch
        verify(mockQuestaoTesteSearchRepository, times(0)).save(questaoTeste);
    }

    @Test
    @Transactional
    public void deleteQuestaoTeste() throws Exception {
        // Initialize the database
        questaoTesteRepository.saveAndFlush(questaoTeste);

        int databaseSizeBeforeDelete = questaoTesteRepository.findAll().size();

        // Delete the questaoTeste
        restQuestaoTesteMockMvc.perform(delete("/api/questao-testes/{id}", questaoTeste.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestaoTeste> questaoTesteList = questaoTesteRepository.findAll();
        assertThat(questaoTesteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the QuestaoTeste in Elasticsearch
        verify(mockQuestaoTesteSearchRepository, times(1)).deleteById(questaoTeste.getId());
    }

    @Test
    @Transactional
    public void searchQuestaoTeste() throws Exception {
        // Initialize the database
        questaoTesteRepository.saveAndFlush(questaoTeste);
        when(mockQuestaoTesteSearchRepository.search(queryStringQuery("id:" + questaoTeste.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(questaoTeste), PageRequest.of(0, 1), 1));
        // Search the questaoTeste
        restQuestaoTesteMockMvc.perform(get("/api/_search/questao-testes?query=id:" + questaoTeste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questaoTeste.getId().intValue())))
            .andExpect(jsonPath("$.[*].grupo").value(hasItem(DEFAULT_GRUPO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.doubleValue())))
            .andExpect(jsonPath("$.[*].argumento").value(hasItem(DEFAULT_ARGUMENTO)))
            .andExpect(jsonPath("$.[*].questao").value(hasItem(DEFAULT_QUESTAO)));
    }
}
