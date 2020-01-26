package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.PlanoActividade;
import com.ravunana.educacao.pedagogico.repository.PlanoActividadeRepository;
import com.ravunana.educacao.pedagogico.repository.search.PlanoActividadeSearchRepository;
import com.ravunana.educacao.pedagogico.service.PlanoActividadeService;
import com.ravunana.educacao.pedagogico.service.dto.PlanoActividadeDTO;
import com.ravunana.educacao.pedagogico.service.mapper.PlanoActividadeMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PlanoActividadeResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class PlanoActividadeResourceIT {

    private static final Integer DEFAULT_NUMERO_ACTIVIDADE = 1;
    private static final Integer UPDATED_NUMERO_ACTIVIDADE = 2;

    private static final String DEFAULT_ATIVIDADE = "AAAAAAAAAA";
    private static final String UPDATED_ATIVIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECTIVOS = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIVOS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_RESPONSAVEL = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSAVEL = "BBBBBBBBBB";

    private static final String DEFAULT_LOCAL = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final String DEFAULT_PARTICIPANTES = "AAAAAAAAAA";
    private static final String UPDATED_PARTICIPANTES = "BBBBBBBBBB";

    private static final String DEFAULT_CO_RESPONSAVEL = "AAAAAAAAAA";
    private static final String UPDATED_CO_RESPONSAVEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANO_LECTIVO = 1;
    private static final Integer UPDATED_ANO_LECTIVO = 2;

    private static final String DEFAULT_STATUS_ACTIVIDADE = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_ACTIVIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_PERIODO_LECTIVO = "AAAAAAAAAA";
    private static final String UPDATED_PERIODO_LECTIVO = "BBBBBBBBBB";

    private static final String DEFAULT_TURNO = "AAAAAAAAAA";
    private static final String UPDATED_TURNO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CLASSE = 1;
    private static final Integer UPDATED_CLASSE = 2;

    @Autowired
    private PlanoActividadeRepository planoActividadeRepository;

    @Autowired
    private PlanoActividadeMapper planoActividadeMapper;

    @Autowired
    private PlanoActividadeService planoActividadeService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.PlanoActividadeSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlanoActividadeSearchRepository mockPlanoActividadeSearchRepository;

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

    private MockMvc restPlanoActividadeMockMvc;

    private PlanoActividade planoActividade;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanoActividadeResource planoActividadeResource = new PlanoActividadeResource(planoActividadeService);
        this.restPlanoActividadeMockMvc = MockMvcBuilders.standaloneSetup(planoActividadeResource)
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
    public static PlanoActividade createEntity(EntityManager em) {
        PlanoActividade planoActividade = new PlanoActividade()
            .numeroActividade(DEFAULT_NUMERO_ACTIVIDADE)
            .atividade(DEFAULT_ATIVIDADE)
            .objectivos(DEFAULT_OBJECTIVOS)
            .de(DEFAULT_DE)
            .ate(DEFAULT_ATE)
            .responsavel(DEFAULT_RESPONSAVEL)
            .local(DEFAULT_LOCAL)
            .observacao(DEFAULT_OBSERVACAO)
            .participantes(DEFAULT_PARTICIPANTES)
            .coResponsavel(DEFAULT_CO_RESPONSAVEL)
            .anoLectivo(DEFAULT_ANO_LECTIVO)
            .statusActividade(DEFAULT_STATUS_ACTIVIDADE)
            .periodoLectivo(DEFAULT_PERIODO_LECTIVO)
            .turno(DEFAULT_TURNO)
            .classe(DEFAULT_CLASSE);
        return planoActividade;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanoActividade createUpdatedEntity(EntityManager em) {
        PlanoActividade planoActividade = new PlanoActividade()
            .numeroActividade(UPDATED_NUMERO_ACTIVIDADE)
            .atividade(UPDATED_ATIVIDADE)
            .objectivos(UPDATED_OBJECTIVOS)
            .de(UPDATED_DE)
            .ate(UPDATED_ATE)
            .responsavel(UPDATED_RESPONSAVEL)
            .local(UPDATED_LOCAL)
            .observacao(UPDATED_OBSERVACAO)
            .participantes(UPDATED_PARTICIPANTES)
            .coResponsavel(UPDATED_CO_RESPONSAVEL)
            .anoLectivo(UPDATED_ANO_LECTIVO)
            .statusActividade(UPDATED_STATUS_ACTIVIDADE)
            .periodoLectivo(UPDATED_PERIODO_LECTIVO)
            .turno(UPDATED_TURNO)
            .classe(UPDATED_CLASSE);
        return planoActividade;
    }

    @BeforeEach
    public void initTest() {
        planoActividade = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanoActividade() throws Exception {
        int databaseSizeBeforeCreate = planoActividadeRepository.findAll().size();

        // Create the PlanoActividade
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);
        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isCreated());

        // Validate the PlanoActividade in the database
        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeCreate + 1);
        PlanoActividade testPlanoActividade = planoActividadeList.get(planoActividadeList.size() - 1);
        assertThat(testPlanoActividade.getNumeroActividade()).isEqualTo(DEFAULT_NUMERO_ACTIVIDADE);
        assertThat(testPlanoActividade.getAtividade()).isEqualTo(DEFAULT_ATIVIDADE);
        assertThat(testPlanoActividade.getObjectivos()).isEqualTo(DEFAULT_OBJECTIVOS);
        assertThat(testPlanoActividade.getDe()).isEqualTo(DEFAULT_DE);
        assertThat(testPlanoActividade.getAte()).isEqualTo(DEFAULT_ATE);
        assertThat(testPlanoActividade.getResponsavel()).isEqualTo(DEFAULT_RESPONSAVEL);
        assertThat(testPlanoActividade.getLocal()).isEqualTo(DEFAULT_LOCAL);
        assertThat(testPlanoActividade.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testPlanoActividade.getParticipantes()).isEqualTo(DEFAULT_PARTICIPANTES);
        assertThat(testPlanoActividade.getCoResponsavel()).isEqualTo(DEFAULT_CO_RESPONSAVEL);
        assertThat(testPlanoActividade.getAnoLectivo()).isEqualTo(DEFAULT_ANO_LECTIVO);
        assertThat(testPlanoActividade.getStatusActividade()).isEqualTo(DEFAULT_STATUS_ACTIVIDADE);
        assertThat(testPlanoActividade.getPeriodoLectivo()).isEqualTo(DEFAULT_PERIODO_LECTIVO);
        assertThat(testPlanoActividade.getTurno()).isEqualTo(DEFAULT_TURNO);
        assertThat(testPlanoActividade.getClasse()).isEqualTo(DEFAULT_CLASSE);

        // Validate the PlanoActividade in Elasticsearch
        verify(mockPlanoActividadeSearchRepository, times(1)).save(testPlanoActividade);
    }

    @Test
    @Transactional
    public void createPlanoActividadeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planoActividadeRepository.findAll().size();

        // Create the PlanoActividade with an existing ID
        planoActividade.setId(1L);
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanoActividade in the database
        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeCreate);

        // Validate the PlanoActividade in Elasticsearch
        verify(mockPlanoActividadeSearchRepository, times(0)).save(planoActividade);
    }


    @Test
    @Transactional
    public void checkAtividadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoActividadeRepository.findAll().size();
        // set the field null
        planoActividade.setAtividade(null);

        // Create the PlanoActividade, which fails.
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoActividadeRepository.findAll().size();
        // set the field null
        planoActividade.setDe(null);

        // Create the PlanoActividade, which fails.
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAteIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoActividadeRepository.findAll().size();
        // set the field null
        planoActividade.setAte(null);

        // Create the PlanoActividade, which fails.
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResponsavelIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoActividadeRepository.findAll().size();
        // set the field null
        planoActividade.setResponsavel(null);

        // Create the PlanoActividade, which fails.
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnoLectivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoActividadeRepository.findAll().size();
        // set the field null
        planoActividade.setAnoLectivo(null);

        // Create the PlanoActividade, which fails.
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        restPlanoActividadeMockMvc.perform(post("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlanoActividades() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get all the planoActividadeList
        restPlanoActividadeMockMvc.perform(get("/api/plano-actividades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planoActividade.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroActividade").value(hasItem(DEFAULT_NUMERO_ACTIVIDADE)))
            .andExpect(jsonPath("$.[*].atividade").value(hasItem(DEFAULT_ATIVIDADE)))
            .andExpect(jsonPath("$.[*].objectivos").value(hasItem(DEFAULT_OBJECTIVOS.toString())))
            .andExpect(jsonPath("$.[*].de").value(hasItem(DEFAULT_DE.toString())))
            .andExpect(jsonPath("$.[*].ate").value(hasItem(DEFAULT_ATE.toString())))
            .andExpect(jsonPath("$.[*].responsavel").value(hasItem(DEFAULT_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].participantes").value(hasItem(DEFAULT_PARTICIPANTES)))
            .andExpect(jsonPath("$.[*].coResponsavel").value(hasItem(DEFAULT_CO_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].statusActividade").value(hasItem(DEFAULT_STATUS_ACTIVIDADE)))
            .andExpect(jsonPath("$.[*].periodoLectivo").value(hasItem(DEFAULT_PERIODO_LECTIVO)))
            .andExpect(jsonPath("$.[*].turno").value(hasItem(DEFAULT_TURNO)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)));
    }
    
    @Test
    @Transactional
    public void getPlanoActividade() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        // Get the planoActividade
        restPlanoActividadeMockMvc.perform(get("/api/plano-actividades/{id}", planoActividade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planoActividade.getId().intValue()))
            .andExpect(jsonPath("$.numeroActividade").value(DEFAULT_NUMERO_ACTIVIDADE))
            .andExpect(jsonPath("$.atividade").value(DEFAULT_ATIVIDADE))
            .andExpect(jsonPath("$.objectivos").value(DEFAULT_OBJECTIVOS.toString()))
            .andExpect(jsonPath("$.de").value(DEFAULT_DE.toString()))
            .andExpect(jsonPath("$.ate").value(DEFAULT_ATE.toString()))
            .andExpect(jsonPath("$.responsavel").value(DEFAULT_RESPONSAVEL))
            .andExpect(jsonPath("$.local").value(DEFAULT_LOCAL))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO.toString()))
            .andExpect(jsonPath("$.participantes").value(DEFAULT_PARTICIPANTES))
            .andExpect(jsonPath("$.coResponsavel").value(DEFAULT_CO_RESPONSAVEL))
            .andExpect(jsonPath("$.anoLectivo").value(DEFAULT_ANO_LECTIVO))
            .andExpect(jsonPath("$.statusActividade").value(DEFAULT_STATUS_ACTIVIDADE))
            .andExpect(jsonPath("$.periodoLectivo").value(DEFAULT_PERIODO_LECTIVO))
            .andExpect(jsonPath("$.turno").value(DEFAULT_TURNO))
            .andExpect(jsonPath("$.classe").value(DEFAULT_CLASSE));
    }

    @Test
    @Transactional
    public void getNonExistingPlanoActividade() throws Exception {
        // Get the planoActividade
        restPlanoActividadeMockMvc.perform(get("/api/plano-actividades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanoActividade() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        int databaseSizeBeforeUpdate = planoActividadeRepository.findAll().size();

        // Update the planoActividade
        PlanoActividade updatedPlanoActividade = planoActividadeRepository.findById(planoActividade.getId()).get();
        // Disconnect from session so that the updates on updatedPlanoActividade are not directly saved in db
        em.detach(updatedPlanoActividade);
        updatedPlanoActividade
            .numeroActividade(UPDATED_NUMERO_ACTIVIDADE)
            .atividade(UPDATED_ATIVIDADE)
            .objectivos(UPDATED_OBJECTIVOS)
            .de(UPDATED_DE)
            .ate(UPDATED_ATE)
            .responsavel(UPDATED_RESPONSAVEL)
            .local(UPDATED_LOCAL)
            .observacao(UPDATED_OBSERVACAO)
            .participantes(UPDATED_PARTICIPANTES)
            .coResponsavel(UPDATED_CO_RESPONSAVEL)
            .anoLectivo(UPDATED_ANO_LECTIVO)
            .statusActividade(UPDATED_STATUS_ACTIVIDADE)
            .periodoLectivo(UPDATED_PERIODO_LECTIVO)
            .turno(UPDATED_TURNO)
            .classe(UPDATED_CLASSE);
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(updatedPlanoActividade);

        restPlanoActividadeMockMvc.perform(put("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isOk());

        // Validate the PlanoActividade in the database
        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeUpdate);
        PlanoActividade testPlanoActividade = planoActividadeList.get(planoActividadeList.size() - 1);
        assertThat(testPlanoActividade.getNumeroActividade()).isEqualTo(UPDATED_NUMERO_ACTIVIDADE);
        assertThat(testPlanoActividade.getAtividade()).isEqualTo(UPDATED_ATIVIDADE);
        assertThat(testPlanoActividade.getObjectivos()).isEqualTo(UPDATED_OBJECTIVOS);
        assertThat(testPlanoActividade.getDe()).isEqualTo(UPDATED_DE);
        assertThat(testPlanoActividade.getAte()).isEqualTo(UPDATED_ATE);
        assertThat(testPlanoActividade.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
        assertThat(testPlanoActividade.getLocal()).isEqualTo(UPDATED_LOCAL);
        assertThat(testPlanoActividade.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testPlanoActividade.getParticipantes()).isEqualTo(UPDATED_PARTICIPANTES);
        assertThat(testPlanoActividade.getCoResponsavel()).isEqualTo(UPDATED_CO_RESPONSAVEL);
        assertThat(testPlanoActividade.getAnoLectivo()).isEqualTo(UPDATED_ANO_LECTIVO);
        assertThat(testPlanoActividade.getStatusActividade()).isEqualTo(UPDATED_STATUS_ACTIVIDADE);
        assertThat(testPlanoActividade.getPeriodoLectivo()).isEqualTo(UPDATED_PERIODO_LECTIVO);
        assertThat(testPlanoActividade.getTurno()).isEqualTo(UPDATED_TURNO);
        assertThat(testPlanoActividade.getClasse()).isEqualTo(UPDATED_CLASSE);

        // Validate the PlanoActividade in Elasticsearch
        verify(mockPlanoActividadeSearchRepository, times(1)).save(testPlanoActividade);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanoActividade() throws Exception {
        int databaseSizeBeforeUpdate = planoActividadeRepository.findAll().size();

        // Create the PlanoActividade
        PlanoActividadeDTO planoActividadeDTO = planoActividadeMapper.toDto(planoActividade);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanoActividadeMockMvc.perform(put("/api/plano-actividades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoActividadeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanoActividade in the database
        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanoActividade in Elasticsearch
        verify(mockPlanoActividadeSearchRepository, times(0)).save(planoActividade);
    }

    @Test
    @Transactional
    public void deletePlanoActividade() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);

        int databaseSizeBeforeDelete = planoActividadeRepository.findAll().size();

        // Delete the planoActividade
        restPlanoActividadeMockMvc.perform(delete("/api/plano-actividades/{id}", planoActividade.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlanoActividade> planoActividadeList = planoActividadeRepository.findAll();
        assertThat(planoActividadeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PlanoActividade in Elasticsearch
        verify(mockPlanoActividadeSearchRepository, times(1)).deleteById(planoActividade.getId());
    }

    @Test
    @Transactional
    public void searchPlanoActividade() throws Exception {
        // Initialize the database
        planoActividadeRepository.saveAndFlush(planoActividade);
        when(mockPlanoActividadeSearchRepository.search(queryStringQuery("id:" + planoActividade.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(planoActividade), PageRequest.of(0, 1), 1));
        // Search the planoActividade
        restPlanoActividadeMockMvc.perform(get("/api/_search/plano-actividades?query=id:" + planoActividade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planoActividade.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroActividade").value(hasItem(DEFAULT_NUMERO_ACTIVIDADE)))
            .andExpect(jsonPath("$.[*].atividade").value(hasItem(DEFAULT_ATIVIDADE)))
            .andExpect(jsonPath("$.[*].objectivos").value(hasItem(DEFAULT_OBJECTIVOS.toString())))
            .andExpect(jsonPath("$.[*].de").value(hasItem(DEFAULT_DE.toString())))
            .andExpect(jsonPath("$.[*].ate").value(hasItem(DEFAULT_ATE.toString())))
            .andExpect(jsonPath("$.[*].responsavel").value(hasItem(DEFAULT_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].participantes").value(hasItem(DEFAULT_PARTICIPANTES)))
            .andExpect(jsonPath("$.[*].coResponsavel").value(hasItem(DEFAULT_CO_RESPONSAVEL)))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].statusActividade").value(hasItem(DEFAULT_STATUS_ACTIVIDADE)))
            .andExpect(jsonPath("$.[*].periodoLectivo").value(hasItem(DEFAULT_PERIODO_LECTIVO)))
            .andExpect(jsonPath("$.[*].turno").value(hasItem(DEFAULT_TURNO)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)));
    }
}
