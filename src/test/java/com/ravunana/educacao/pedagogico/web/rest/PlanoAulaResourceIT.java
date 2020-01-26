package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.PlanoAula;
import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.domain.Professor;
import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import com.ravunana.educacao.pedagogico.repository.PlanoAulaRepository;
import com.ravunana.educacao.pedagogico.repository.search.PlanoAulaSearchRepository;
import com.ravunana.educacao.pedagogico.service.PlanoAulaService;
import com.ravunana.educacao.pedagogico.service.dto.PlanoAulaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.PlanoAulaMapper;
import com.ravunana.educacao.pedagogico.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
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
 * Integration tests for the {@link PlanoAulaResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class PlanoAulaResourceIT {

    private static final String DEFAULT_OBJECTIVO_GERAL = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIVO_GERAL = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECTIVO_ESPECIFICO = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIVO_ESPECIFICO = "BBBBBBBBBB";

    private static final String DEFAULT_CONTEUDO = "AAAAAAAAAA";
    private static final String UPDATED_CONTEUDO = "BBBBBBBBBB";

    private static final String DEFAULT_ESTRATEGIA = "AAAAAAAAAA";
    private static final String UPDATED_ESTRATEGIA = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVIDADES = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVIDADES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TEMPO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TEMPO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_RECURSOS_ENSINO = "AAAAAAAAAA";
    private static final String UPDATED_RECURSOS_ENSINO = "BBBBBBBBBB";

    private static final String DEFAULT_AVALIACAO = "AAAAAAAAAA";
    private static final String UPDATED_AVALIACAO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final String DEFAULT_BIBLIOGRAFIA = "AAAAAAAAAA";
    private static final String UPDATED_BIBLIOGRAFIA = "BBBBBBBBBB";

    private static final String DEFAULT_PERFIL_ENTRADA = "AAAAAAAAAA";
    private static final String UPDATED_PERFIL_ENTRADA = "BBBBBBBBBB";

    private static final String DEFAULT_PERFIL_SAIDA = "AAAAAAAAAA";
    private static final String UPDATED_PERFIL_SAIDA = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ANEXO_1 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ANEXO_1 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ANEXO_1_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ANEXO_1_CONTENT_TYPE = "image/png";

    @Autowired
    private PlanoAulaRepository planoAulaRepository;

    @Mock
    private PlanoAulaRepository planoAulaRepositoryMock;

    @Autowired
    private PlanoAulaMapper planoAulaMapper;

    @Mock
    private PlanoAulaService planoAulaServiceMock;

    @Autowired
    private PlanoAulaService planoAulaService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.PlanoAulaSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlanoAulaSearchRepository mockPlanoAulaSearchRepository;

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

    private MockMvc restPlanoAulaMockMvc;

    private PlanoAula planoAula;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanoAulaResource planoAulaResource = new PlanoAulaResource(planoAulaService);
        this.restPlanoAulaMockMvc = MockMvcBuilders.standaloneSetup(planoAulaResource)
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
    public static PlanoAula createEntity(EntityManager em) {
        PlanoAula planoAula = new PlanoAula()
            .objectivoGeral(DEFAULT_OBJECTIVO_GERAL)
            .objectivoEspecifico(DEFAULT_OBJECTIVO_ESPECIFICO)
            .conteudo(DEFAULT_CONTEUDO)
            .estrategia(DEFAULT_ESTRATEGIA)
            .actividades(DEFAULT_ACTIVIDADES)
            .tempo(DEFAULT_TEMPO)
            .recursosEnsino(DEFAULT_RECURSOS_ENSINO)
            .avaliacao(DEFAULT_AVALIACAO)
            .observacao(DEFAULT_OBSERVACAO)
            .bibliografia(DEFAULT_BIBLIOGRAFIA)
            .perfilEntrada(DEFAULT_PERFIL_ENTRADA)
            .perfilSaida(DEFAULT_PERFIL_SAIDA)
            .anexo1(DEFAULT_ANEXO_1)
            .anexo1ContentType(DEFAULT_ANEXO_1_CONTENT_TYPE);
        // Add required entity
        Turma turma;
        if (TestUtil.findAll(em, Turma.class).isEmpty()) {
            turma = TurmaResourceIT.createEntity(em);
            em.persist(turma);
            em.flush();
        } else {
            turma = TestUtil.findAll(em, Turma.class).get(0);
        }
        planoAula.getTurmas().add(turma);
        // Add required entity
        Professor professor;
        if (TestUtil.findAll(em, Professor.class).isEmpty()) {
            professor = ProfessorResourceIT.createEntity(em);
            em.persist(professor);
            em.flush();
        } else {
            professor = TestUtil.findAll(em, Professor.class).get(0);
        }
        planoAula.setProfessor(professor);
        // Add required entity
        PlanoCurricular planoCurricular;
        if (TestUtil.findAll(em, PlanoCurricular.class).isEmpty()) {
            planoCurricular = PlanoCurricularResourceIT.createEntity(em);
            em.persist(planoCurricular);
            em.flush();
        } else {
            planoCurricular = TestUtil.findAll(em, PlanoCurricular.class).get(0);
        }
        planoAula.setCurriculo(planoCurricular);
        return planoAula;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanoAula createUpdatedEntity(EntityManager em) {
        PlanoAula planoAula = new PlanoAula()
            .objectivoGeral(UPDATED_OBJECTIVO_GERAL)
            .objectivoEspecifico(UPDATED_OBJECTIVO_ESPECIFICO)
            .conteudo(UPDATED_CONTEUDO)
            .estrategia(UPDATED_ESTRATEGIA)
            .actividades(UPDATED_ACTIVIDADES)
            .tempo(UPDATED_TEMPO)
            .recursosEnsino(UPDATED_RECURSOS_ENSINO)
            .avaliacao(UPDATED_AVALIACAO)
            .observacao(UPDATED_OBSERVACAO)
            .bibliografia(UPDATED_BIBLIOGRAFIA)
            .perfilEntrada(UPDATED_PERFIL_ENTRADA)
            .perfilSaida(UPDATED_PERFIL_SAIDA)
            .anexo1(UPDATED_ANEXO_1)
            .anexo1ContentType(UPDATED_ANEXO_1_CONTENT_TYPE);
        // Add required entity
        Turma turma;
        if (TestUtil.findAll(em, Turma.class).isEmpty()) {
            turma = TurmaResourceIT.createUpdatedEntity(em);
            em.persist(turma);
            em.flush();
        } else {
            turma = TestUtil.findAll(em, Turma.class).get(0);
        }
        planoAula.getTurmas().add(turma);
        // Add required entity
        Professor professor;
        if (TestUtil.findAll(em, Professor.class).isEmpty()) {
            professor = ProfessorResourceIT.createUpdatedEntity(em);
            em.persist(professor);
            em.flush();
        } else {
            professor = TestUtil.findAll(em, Professor.class).get(0);
        }
        planoAula.setProfessor(professor);
        // Add required entity
        PlanoCurricular planoCurricular;
        if (TestUtil.findAll(em, PlanoCurricular.class).isEmpty()) {
            planoCurricular = PlanoCurricularResourceIT.createUpdatedEntity(em);
            em.persist(planoCurricular);
            em.flush();
        } else {
            planoCurricular = TestUtil.findAll(em, PlanoCurricular.class).get(0);
        }
        planoAula.setCurriculo(planoCurricular);
        return planoAula;
    }

    @BeforeEach
    public void initTest() {
        planoAula = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanoAula() throws Exception {
        int databaseSizeBeforeCreate = planoAulaRepository.findAll().size();

        // Create the PlanoAula
        PlanoAulaDTO planoAulaDTO = planoAulaMapper.toDto(planoAula);
        restPlanoAulaMockMvc.perform(post("/api/plano-aulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoAulaDTO)))
            .andExpect(status().isCreated());

        // Validate the PlanoAula in the database
        List<PlanoAula> planoAulaList = planoAulaRepository.findAll();
        assertThat(planoAulaList).hasSize(databaseSizeBeforeCreate + 1);
        PlanoAula testPlanoAula = planoAulaList.get(planoAulaList.size() - 1);
        assertThat(testPlanoAula.getObjectivoGeral()).isEqualTo(DEFAULT_OBJECTIVO_GERAL);
        assertThat(testPlanoAula.getObjectivoEspecifico()).isEqualTo(DEFAULT_OBJECTIVO_ESPECIFICO);
        assertThat(testPlanoAula.getConteudo()).isEqualTo(DEFAULT_CONTEUDO);
        assertThat(testPlanoAula.getEstrategia()).isEqualTo(DEFAULT_ESTRATEGIA);
        assertThat(testPlanoAula.getActividades()).isEqualTo(DEFAULT_ACTIVIDADES);
        assertThat(testPlanoAula.getTempo()).isEqualTo(DEFAULT_TEMPO);
        assertThat(testPlanoAula.getRecursosEnsino()).isEqualTo(DEFAULT_RECURSOS_ENSINO);
        assertThat(testPlanoAula.getAvaliacao()).isEqualTo(DEFAULT_AVALIACAO);
        assertThat(testPlanoAula.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testPlanoAula.getBibliografia()).isEqualTo(DEFAULT_BIBLIOGRAFIA);
        assertThat(testPlanoAula.getPerfilEntrada()).isEqualTo(DEFAULT_PERFIL_ENTRADA);
        assertThat(testPlanoAula.getPerfilSaida()).isEqualTo(DEFAULT_PERFIL_SAIDA);
        assertThat(testPlanoAula.getAnexo1()).isEqualTo(DEFAULT_ANEXO_1);
        assertThat(testPlanoAula.getAnexo1ContentType()).isEqualTo(DEFAULT_ANEXO_1_CONTENT_TYPE);

        // Validate the PlanoAula in Elasticsearch
        verify(mockPlanoAulaSearchRepository, times(1)).save(testPlanoAula);
    }

    @Test
    @Transactional
    public void createPlanoAulaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planoAulaRepository.findAll().size();

        // Create the PlanoAula with an existing ID
        planoAula.setId(1L);
        PlanoAulaDTO planoAulaDTO = planoAulaMapper.toDto(planoAula);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanoAulaMockMvc.perform(post("/api/plano-aulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoAulaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanoAula in the database
        List<PlanoAula> planoAulaList = planoAulaRepository.findAll();
        assertThat(planoAulaList).hasSize(databaseSizeBeforeCreate);

        // Validate the PlanoAula in Elasticsearch
        verify(mockPlanoAulaSearchRepository, times(0)).save(planoAula);
    }


    @Test
    @Transactional
    public void checkTempoIsRequired() throws Exception {
        int databaseSizeBeforeTest = planoAulaRepository.findAll().size();
        // set the field null
        planoAula.setTempo(null);

        // Create the PlanoAula, which fails.
        PlanoAulaDTO planoAulaDTO = planoAulaMapper.toDto(planoAula);

        restPlanoAulaMockMvc.perform(post("/api/plano-aulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoAulaDTO)))
            .andExpect(status().isBadRequest());

        List<PlanoAula> planoAulaList = planoAulaRepository.findAll();
        assertThat(planoAulaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlanoAulas() throws Exception {
        // Initialize the database
        planoAulaRepository.saveAndFlush(planoAula);

        // Get all the planoAulaList
        restPlanoAulaMockMvc.perform(get("/api/plano-aulas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planoAula.getId().intValue())))
            .andExpect(jsonPath("$.[*].objectivoGeral").value(hasItem(DEFAULT_OBJECTIVO_GERAL.toString())))
            .andExpect(jsonPath("$.[*].objectivoEspecifico").value(hasItem(DEFAULT_OBJECTIVO_ESPECIFICO.toString())))
            .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO.toString())))
            .andExpect(jsonPath("$.[*].estrategia").value(hasItem(DEFAULT_ESTRATEGIA.toString())))
            .andExpect(jsonPath("$.[*].actividades").value(hasItem(DEFAULT_ACTIVIDADES.toString())))
            .andExpect(jsonPath("$.[*].tempo").value(hasItem(sameInstant(DEFAULT_TEMPO))))
            .andExpect(jsonPath("$.[*].recursosEnsino").value(hasItem(DEFAULT_RECURSOS_ENSINO.toString())))
            .andExpect(jsonPath("$.[*].avaliacao").value(hasItem(DEFAULT_AVALIACAO.toString())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].bibliografia").value(hasItem(DEFAULT_BIBLIOGRAFIA.toString())))
            .andExpect(jsonPath("$.[*].perfilEntrada").value(hasItem(DEFAULT_PERFIL_ENTRADA.toString())))
            .andExpect(jsonPath("$.[*].perfilSaida").value(hasItem(DEFAULT_PERFIL_SAIDA.toString())))
            .andExpect(jsonPath("$.[*].anexo1ContentType").value(hasItem(DEFAULT_ANEXO_1_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anexo1").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANEXO_1))));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPlanoAulasWithEagerRelationshipsIsEnabled() throws Exception {
        PlanoAulaResource planoAulaResource = new PlanoAulaResource(planoAulaServiceMock);
        when(planoAulaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restPlanoAulaMockMvc = MockMvcBuilders.standaloneSetup(planoAulaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPlanoAulaMockMvc.perform(get("/api/plano-aulas?eagerload=true"))
        .andExpect(status().isOk());

        verify(planoAulaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPlanoAulasWithEagerRelationshipsIsNotEnabled() throws Exception {
        PlanoAulaResource planoAulaResource = new PlanoAulaResource(planoAulaServiceMock);
            when(planoAulaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restPlanoAulaMockMvc = MockMvcBuilders.standaloneSetup(planoAulaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPlanoAulaMockMvc.perform(get("/api/plano-aulas?eagerload=true"))
        .andExpect(status().isOk());

            verify(planoAulaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPlanoAula() throws Exception {
        // Initialize the database
        planoAulaRepository.saveAndFlush(planoAula);

        // Get the planoAula
        restPlanoAulaMockMvc.perform(get("/api/plano-aulas/{id}", planoAula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planoAula.getId().intValue()))
            .andExpect(jsonPath("$.objectivoGeral").value(DEFAULT_OBJECTIVO_GERAL.toString()))
            .andExpect(jsonPath("$.objectivoEspecifico").value(DEFAULT_OBJECTIVO_ESPECIFICO.toString()))
            .andExpect(jsonPath("$.conteudo").value(DEFAULT_CONTEUDO.toString()))
            .andExpect(jsonPath("$.estrategia").value(DEFAULT_ESTRATEGIA.toString()))
            .andExpect(jsonPath("$.actividades").value(DEFAULT_ACTIVIDADES.toString()))
            .andExpect(jsonPath("$.tempo").value(sameInstant(DEFAULT_TEMPO)))
            .andExpect(jsonPath("$.recursosEnsino").value(DEFAULT_RECURSOS_ENSINO.toString()))
            .andExpect(jsonPath("$.avaliacao").value(DEFAULT_AVALIACAO.toString()))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO.toString()))
            .andExpect(jsonPath("$.bibliografia").value(DEFAULT_BIBLIOGRAFIA.toString()))
            .andExpect(jsonPath("$.perfilEntrada").value(DEFAULT_PERFIL_ENTRADA.toString()))
            .andExpect(jsonPath("$.perfilSaida").value(DEFAULT_PERFIL_SAIDA.toString()))
            .andExpect(jsonPath("$.anexo1ContentType").value(DEFAULT_ANEXO_1_CONTENT_TYPE))
            .andExpect(jsonPath("$.anexo1").value(Base64Utils.encodeToString(DEFAULT_ANEXO_1)));
    }

    @Test
    @Transactional
    public void getNonExistingPlanoAula() throws Exception {
        // Get the planoAula
        restPlanoAulaMockMvc.perform(get("/api/plano-aulas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanoAula() throws Exception {
        // Initialize the database
        planoAulaRepository.saveAndFlush(planoAula);

        int databaseSizeBeforeUpdate = planoAulaRepository.findAll().size();

        // Update the planoAula
        PlanoAula updatedPlanoAula = planoAulaRepository.findById(planoAula.getId()).get();
        // Disconnect from session so that the updates on updatedPlanoAula are not directly saved in db
        em.detach(updatedPlanoAula);
        updatedPlanoAula
            .objectivoGeral(UPDATED_OBJECTIVO_GERAL)
            .objectivoEspecifico(UPDATED_OBJECTIVO_ESPECIFICO)
            .conteudo(UPDATED_CONTEUDO)
            .estrategia(UPDATED_ESTRATEGIA)
            .actividades(UPDATED_ACTIVIDADES)
            .tempo(UPDATED_TEMPO)
            .recursosEnsino(UPDATED_RECURSOS_ENSINO)
            .avaliacao(UPDATED_AVALIACAO)
            .observacao(UPDATED_OBSERVACAO)
            .bibliografia(UPDATED_BIBLIOGRAFIA)
            .perfilEntrada(UPDATED_PERFIL_ENTRADA)
            .perfilSaida(UPDATED_PERFIL_SAIDA)
            .anexo1(UPDATED_ANEXO_1)
            .anexo1ContentType(UPDATED_ANEXO_1_CONTENT_TYPE);
        PlanoAulaDTO planoAulaDTO = planoAulaMapper.toDto(updatedPlanoAula);

        restPlanoAulaMockMvc.perform(put("/api/plano-aulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoAulaDTO)))
            .andExpect(status().isOk());

        // Validate the PlanoAula in the database
        List<PlanoAula> planoAulaList = planoAulaRepository.findAll();
        assertThat(planoAulaList).hasSize(databaseSizeBeforeUpdate);
        PlanoAula testPlanoAula = planoAulaList.get(planoAulaList.size() - 1);
        assertThat(testPlanoAula.getObjectivoGeral()).isEqualTo(UPDATED_OBJECTIVO_GERAL);
        assertThat(testPlanoAula.getObjectivoEspecifico()).isEqualTo(UPDATED_OBJECTIVO_ESPECIFICO);
        assertThat(testPlanoAula.getConteudo()).isEqualTo(UPDATED_CONTEUDO);
        assertThat(testPlanoAula.getEstrategia()).isEqualTo(UPDATED_ESTRATEGIA);
        assertThat(testPlanoAula.getActividades()).isEqualTo(UPDATED_ACTIVIDADES);
        assertThat(testPlanoAula.getTempo()).isEqualTo(UPDATED_TEMPO);
        assertThat(testPlanoAula.getRecursosEnsino()).isEqualTo(UPDATED_RECURSOS_ENSINO);
        assertThat(testPlanoAula.getAvaliacao()).isEqualTo(UPDATED_AVALIACAO);
        assertThat(testPlanoAula.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testPlanoAula.getBibliografia()).isEqualTo(UPDATED_BIBLIOGRAFIA);
        assertThat(testPlanoAula.getPerfilEntrada()).isEqualTo(UPDATED_PERFIL_ENTRADA);
        assertThat(testPlanoAula.getPerfilSaida()).isEqualTo(UPDATED_PERFIL_SAIDA);
        assertThat(testPlanoAula.getAnexo1()).isEqualTo(UPDATED_ANEXO_1);
        assertThat(testPlanoAula.getAnexo1ContentType()).isEqualTo(UPDATED_ANEXO_1_CONTENT_TYPE);

        // Validate the PlanoAula in Elasticsearch
        verify(mockPlanoAulaSearchRepository, times(1)).save(testPlanoAula);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanoAula() throws Exception {
        int databaseSizeBeforeUpdate = planoAulaRepository.findAll().size();

        // Create the PlanoAula
        PlanoAulaDTO planoAulaDTO = planoAulaMapper.toDto(planoAula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanoAulaMockMvc.perform(put("/api/plano-aulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planoAulaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanoAula in the database
        List<PlanoAula> planoAulaList = planoAulaRepository.findAll();
        assertThat(planoAulaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanoAula in Elasticsearch
        verify(mockPlanoAulaSearchRepository, times(0)).save(planoAula);
    }

    @Test
    @Transactional
    public void deletePlanoAula() throws Exception {
        // Initialize the database
        planoAulaRepository.saveAndFlush(planoAula);

        int databaseSizeBeforeDelete = planoAulaRepository.findAll().size();

        // Delete the planoAula
        restPlanoAulaMockMvc.perform(delete("/api/plano-aulas/{id}", planoAula.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlanoAula> planoAulaList = planoAulaRepository.findAll();
        assertThat(planoAulaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PlanoAula in Elasticsearch
        verify(mockPlanoAulaSearchRepository, times(1)).deleteById(planoAula.getId());
    }

    @Test
    @Transactional
    public void searchPlanoAula() throws Exception {
        // Initialize the database
        planoAulaRepository.saveAndFlush(planoAula);
        when(mockPlanoAulaSearchRepository.search(queryStringQuery("id:" + planoAula.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(planoAula), PageRequest.of(0, 1), 1));
        // Search the planoAula
        restPlanoAulaMockMvc.perform(get("/api/_search/plano-aulas?query=id:" + planoAula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planoAula.getId().intValue())))
            .andExpect(jsonPath("$.[*].objectivoGeral").value(hasItem(DEFAULT_OBJECTIVO_GERAL.toString())))
            .andExpect(jsonPath("$.[*].objectivoEspecifico").value(hasItem(DEFAULT_OBJECTIVO_ESPECIFICO.toString())))
            .andExpect(jsonPath("$.[*].conteudo").value(hasItem(DEFAULT_CONTEUDO.toString())))
            .andExpect(jsonPath("$.[*].estrategia").value(hasItem(DEFAULT_ESTRATEGIA.toString())))
            .andExpect(jsonPath("$.[*].actividades").value(hasItem(DEFAULT_ACTIVIDADES.toString())))
            .andExpect(jsonPath("$.[*].tempo").value(hasItem(sameInstant(DEFAULT_TEMPO))))
            .andExpect(jsonPath("$.[*].recursosEnsino").value(hasItem(DEFAULT_RECURSOS_ENSINO.toString())))
            .andExpect(jsonPath("$.[*].avaliacao").value(hasItem(DEFAULT_AVALIACAO.toString())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].bibliografia").value(hasItem(DEFAULT_BIBLIOGRAFIA.toString())))
            .andExpect(jsonPath("$.[*].perfilEntrada").value(hasItem(DEFAULT_PERFIL_ENTRADA.toString())))
            .andExpect(jsonPath("$.[*].perfilSaida").value(hasItem(DEFAULT_PERFIL_SAIDA.toString())))
            .andExpect(jsonPath("$.[*].anexo1ContentType").value(hasItem(DEFAULT_ANEXO_1_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anexo1").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANEXO_1))));
    }
}
