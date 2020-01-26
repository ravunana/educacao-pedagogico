package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.RespostaQuestao;
import com.ravunana.educacao.pedagogico.domain.QuestaoTeste;
import com.ravunana.educacao.pedagogico.repository.RespostaQuestaoRepository;
import com.ravunana.educacao.pedagogico.repository.search.RespostaQuestaoSearchRepository;
import com.ravunana.educacao.pedagogico.service.RespostaQuestaoService;
import com.ravunana.educacao.pedagogico.service.dto.RespostaQuestaoDTO;
import com.ravunana.educacao.pedagogico.service.mapper.RespostaQuestaoMapper;
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
 * Integration tests for the {@link RespostaQuestaoResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class RespostaQuestaoResourceIT {

    private static final String DEFAULT_RESPOSTA = "AAAAAAAAAA";
    private static final String UPDATED_RESPOSTA = "BBBBBBBBBB";

    @Autowired
    private RespostaQuestaoRepository respostaQuestaoRepository;

    @Autowired
    private RespostaQuestaoMapper respostaQuestaoMapper;

    @Autowired
    private RespostaQuestaoService respostaQuestaoService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.RespostaQuestaoSearchRepositoryMockConfiguration
     */
    @Autowired
    private RespostaQuestaoSearchRepository mockRespostaQuestaoSearchRepository;

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

    private MockMvc restRespostaQuestaoMockMvc;

    private RespostaQuestao respostaQuestao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RespostaQuestaoResource respostaQuestaoResource = new RespostaQuestaoResource(respostaQuestaoService);
        this.restRespostaQuestaoMockMvc = MockMvcBuilders.standaloneSetup(respostaQuestaoResource)
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
    public static RespostaQuestao createEntity(EntityManager em) {
        RespostaQuestao respostaQuestao = new RespostaQuestao()
            .resposta(DEFAULT_RESPOSTA);
        // Add required entity
        QuestaoTeste questaoTeste;
        if (TestUtil.findAll(em, QuestaoTeste.class).isEmpty()) {
            questaoTeste = QuestaoTesteResourceIT.createEntity(em);
            em.persist(questaoTeste);
            em.flush();
        } else {
            questaoTeste = TestUtil.findAll(em, QuestaoTeste.class).get(0);
        }
        respostaQuestao.setQuestao(questaoTeste);
        return respostaQuestao;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RespostaQuestao createUpdatedEntity(EntityManager em) {
        RespostaQuestao respostaQuestao = new RespostaQuestao()
            .resposta(UPDATED_RESPOSTA);
        // Add required entity
        QuestaoTeste questaoTeste;
        if (TestUtil.findAll(em, QuestaoTeste.class).isEmpty()) {
            questaoTeste = QuestaoTesteResourceIT.createUpdatedEntity(em);
            em.persist(questaoTeste);
            em.flush();
        } else {
            questaoTeste = TestUtil.findAll(em, QuestaoTeste.class).get(0);
        }
        respostaQuestao.setQuestao(questaoTeste);
        return respostaQuestao;
    }

    @BeforeEach
    public void initTest() {
        respostaQuestao = createEntity(em);
    }

    @Test
    @Transactional
    public void createRespostaQuestao() throws Exception {
        int databaseSizeBeforeCreate = respostaQuestaoRepository.findAll().size();

        // Create the RespostaQuestao
        RespostaQuestaoDTO respostaQuestaoDTO = respostaQuestaoMapper.toDto(respostaQuestao);
        restRespostaQuestaoMockMvc.perform(post("/api/resposta-questaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respostaQuestaoDTO)))
            .andExpect(status().isCreated());

        // Validate the RespostaQuestao in the database
        List<RespostaQuestao> respostaQuestaoList = respostaQuestaoRepository.findAll();
        assertThat(respostaQuestaoList).hasSize(databaseSizeBeforeCreate + 1);
        RespostaQuestao testRespostaQuestao = respostaQuestaoList.get(respostaQuestaoList.size() - 1);
        assertThat(testRespostaQuestao.getResposta()).isEqualTo(DEFAULT_RESPOSTA);

        // Validate the RespostaQuestao in Elasticsearch
        verify(mockRespostaQuestaoSearchRepository, times(1)).save(testRespostaQuestao);
    }

    @Test
    @Transactional
    public void createRespostaQuestaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = respostaQuestaoRepository.findAll().size();

        // Create the RespostaQuestao with an existing ID
        respostaQuestao.setId(1L);
        RespostaQuestaoDTO respostaQuestaoDTO = respostaQuestaoMapper.toDto(respostaQuestao);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRespostaQuestaoMockMvc.perform(post("/api/resposta-questaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respostaQuestaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RespostaQuestao in the database
        List<RespostaQuestao> respostaQuestaoList = respostaQuestaoRepository.findAll();
        assertThat(respostaQuestaoList).hasSize(databaseSizeBeforeCreate);

        // Validate the RespostaQuestao in Elasticsearch
        verify(mockRespostaQuestaoSearchRepository, times(0)).save(respostaQuestao);
    }


    @Test
    @Transactional
    public void getAllRespostaQuestaos() throws Exception {
        // Initialize the database
        respostaQuestaoRepository.saveAndFlush(respostaQuestao);

        // Get all the respostaQuestaoList
        restRespostaQuestaoMockMvc.perform(get("/api/resposta-questaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(respostaQuestao.getId().intValue())))
            .andExpect(jsonPath("$.[*].resposta").value(hasItem(DEFAULT_RESPOSTA)));
    }
    
    @Test
    @Transactional
    public void getRespostaQuestao() throws Exception {
        // Initialize the database
        respostaQuestaoRepository.saveAndFlush(respostaQuestao);

        // Get the respostaQuestao
        restRespostaQuestaoMockMvc.perform(get("/api/resposta-questaos/{id}", respostaQuestao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(respostaQuestao.getId().intValue()))
            .andExpect(jsonPath("$.resposta").value(DEFAULT_RESPOSTA));
    }

    @Test
    @Transactional
    public void getNonExistingRespostaQuestao() throws Exception {
        // Get the respostaQuestao
        restRespostaQuestaoMockMvc.perform(get("/api/resposta-questaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRespostaQuestao() throws Exception {
        // Initialize the database
        respostaQuestaoRepository.saveAndFlush(respostaQuestao);

        int databaseSizeBeforeUpdate = respostaQuestaoRepository.findAll().size();

        // Update the respostaQuestao
        RespostaQuestao updatedRespostaQuestao = respostaQuestaoRepository.findById(respostaQuestao.getId()).get();
        // Disconnect from session so that the updates on updatedRespostaQuestao are not directly saved in db
        em.detach(updatedRespostaQuestao);
        updatedRespostaQuestao
            .resposta(UPDATED_RESPOSTA);
        RespostaQuestaoDTO respostaQuestaoDTO = respostaQuestaoMapper.toDto(updatedRespostaQuestao);

        restRespostaQuestaoMockMvc.perform(put("/api/resposta-questaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respostaQuestaoDTO)))
            .andExpect(status().isOk());

        // Validate the RespostaQuestao in the database
        List<RespostaQuestao> respostaQuestaoList = respostaQuestaoRepository.findAll();
        assertThat(respostaQuestaoList).hasSize(databaseSizeBeforeUpdate);
        RespostaQuestao testRespostaQuestao = respostaQuestaoList.get(respostaQuestaoList.size() - 1);
        assertThat(testRespostaQuestao.getResposta()).isEqualTo(UPDATED_RESPOSTA);

        // Validate the RespostaQuestao in Elasticsearch
        verify(mockRespostaQuestaoSearchRepository, times(1)).save(testRespostaQuestao);
    }

    @Test
    @Transactional
    public void updateNonExistingRespostaQuestao() throws Exception {
        int databaseSizeBeforeUpdate = respostaQuestaoRepository.findAll().size();

        // Create the RespostaQuestao
        RespostaQuestaoDTO respostaQuestaoDTO = respostaQuestaoMapper.toDto(respostaQuestao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRespostaQuestaoMockMvc.perform(put("/api/resposta-questaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(respostaQuestaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RespostaQuestao in the database
        List<RespostaQuestao> respostaQuestaoList = respostaQuestaoRepository.findAll();
        assertThat(respostaQuestaoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RespostaQuestao in Elasticsearch
        verify(mockRespostaQuestaoSearchRepository, times(0)).save(respostaQuestao);
    }

    @Test
    @Transactional
    public void deleteRespostaQuestao() throws Exception {
        // Initialize the database
        respostaQuestaoRepository.saveAndFlush(respostaQuestao);

        int databaseSizeBeforeDelete = respostaQuestaoRepository.findAll().size();

        // Delete the respostaQuestao
        restRespostaQuestaoMockMvc.perform(delete("/api/resposta-questaos/{id}", respostaQuestao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RespostaQuestao> respostaQuestaoList = respostaQuestaoRepository.findAll();
        assertThat(respostaQuestaoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RespostaQuestao in Elasticsearch
        verify(mockRespostaQuestaoSearchRepository, times(1)).deleteById(respostaQuestao.getId());
    }

    @Test
    @Transactional
    public void searchRespostaQuestao() throws Exception {
        // Initialize the database
        respostaQuestaoRepository.saveAndFlush(respostaQuestao);
        when(mockRespostaQuestaoSearchRepository.search(queryStringQuery("id:" + respostaQuestao.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(respostaQuestao), PageRequest.of(0, 1), 1));
        // Search the respostaQuestao
        restRespostaQuestaoMockMvc.perform(get("/api/_search/resposta-questaos?query=id:" + respostaQuestao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(respostaQuestao.getId().intValue())))
            .andExpect(jsonPath("$.[*].resposta").value(hasItem(DEFAULT_RESPOSTA)));
    }
}
