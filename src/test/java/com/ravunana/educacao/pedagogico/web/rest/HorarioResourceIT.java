package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.PedagogicoApp;
import com.ravunana.educacao.pedagogico.config.SecurityBeanOverrideConfiguration;
import com.ravunana.educacao.pedagogico.domain.Horario;
import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.domain.Professor;
import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import com.ravunana.educacao.pedagogico.repository.HorarioRepository;
import com.ravunana.educacao.pedagogico.repository.search.HorarioSearchRepository;
import com.ravunana.educacao.pedagogico.service.HorarioService;
import com.ravunana.educacao.pedagogico.service.dto.HorarioDTO;
import com.ravunana.educacao.pedagogico.service.mapper.HorarioMapper;
import com.ravunana.educacao.pedagogico.web.rest.errors.ExceptionTranslator;
import com.ravunana.educacao.pedagogico.service.dto.HorarioCriteria;
import com.ravunana.educacao.pedagogico.service.HorarioQueryService;

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
 * Integration tests for the {@link HorarioResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PedagogicoApp.class})
public class HorarioResourceIT {

    private static final String DEFAULT_INICIO = "AAAAAAAAAA";
    private static final String UPDATED_INICIO = "BBBBBBBBBB";

    private static final String DEFAULT_FIM = "AAAAAAAAAA";
    private static final String UPDATED_FIM = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_ANO_LECTIVO = 1;
    private static final Integer UPDATED_ANO_LECTIVO = 2;
    private static final Integer SMALLER_ANO_LECTIVO = 1 - 1;

    private static final String DEFAULT_DIA_SEMANA = "AAAAAAAAAA";
    private static final String UPDATED_DIA_SEMANA = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORIA = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIA = "BBBBBBBBBB";

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private HorarioMapper horarioMapper;

    @Autowired
    private HorarioService horarioService;

    /**
     * This repository is mocked in the com.ravunana.educacao.pedagogico.repository.search test package.
     *
     * @see com.ravunana.educacao.pedagogico.repository.search.HorarioSearchRepositoryMockConfiguration
     */
    @Autowired
    private HorarioSearchRepository mockHorarioSearchRepository;

    @Autowired
    private HorarioQueryService horarioQueryService;

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

    private MockMvc restHorarioMockMvc;

    private Horario horario;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HorarioResource horarioResource = new HorarioResource(horarioService, horarioQueryService);
        this.restHorarioMockMvc = MockMvcBuilders.standaloneSetup(horarioResource)
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
    public static Horario createEntity(EntityManager em) {
        Horario horario = new Horario()
            .inicio(DEFAULT_INICIO)
            .fim(DEFAULT_FIM)
            .data(DEFAULT_DATA)
            .anoLectivo(DEFAULT_ANO_LECTIVO)
            .diaSemana(DEFAULT_DIA_SEMANA)
            .categoria(DEFAULT_CATEGORIA);
        // Add required entity
        Turma turma;
        if (TestUtil.findAll(em, Turma.class).isEmpty()) {
            turma = TurmaResourceIT.createEntity(em);
            em.persist(turma);
            em.flush();
        } else {
            turma = TestUtil.findAll(em, Turma.class).get(0);
        }
        horario.setTurma(turma);
        // Add required entity
        PlanoCurricular planoCurricular;
        if (TestUtil.findAll(em, PlanoCurricular.class).isEmpty()) {
            planoCurricular = PlanoCurricularResourceIT.createEntity(em);
            em.persist(planoCurricular);
            em.flush();
        } else {
            planoCurricular = TestUtil.findAll(em, PlanoCurricular.class).get(0);
        }
        horario.setCurriculo(planoCurricular);
        return horario;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Horario createUpdatedEntity(EntityManager em) {
        Horario horario = new Horario()
            .inicio(UPDATED_INICIO)
            .fim(UPDATED_FIM)
            .data(UPDATED_DATA)
            .anoLectivo(UPDATED_ANO_LECTIVO)
            .diaSemana(UPDATED_DIA_SEMANA)
            .categoria(UPDATED_CATEGORIA);
        // Add required entity
        Turma turma;
        if (TestUtil.findAll(em, Turma.class).isEmpty()) {
            turma = TurmaResourceIT.createUpdatedEntity(em);
            em.persist(turma);
            em.flush();
        } else {
            turma = TestUtil.findAll(em, Turma.class).get(0);
        }
        horario.setTurma(turma);
        // Add required entity
        PlanoCurricular planoCurricular;
        if (TestUtil.findAll(em, PlanoCurricular.class).isEmpty()) {
            planoCurricular = PlanoCurricularResourceIT.createUpdatedEntity(em);
            em.persist(planoCurricular);
            em.flush();
        } else {
            planoCurricular = TestUtil.findAll(em, PlanoCurricular.class).get(0);
        }
        horario.setCurriculo(planoCurricular);
        return horario;
    }

    @BeforeEach
    public void initTest() {
        horario = createEntity(em);
    }

    @Test
    @Transactional
    public void createHorario() throws Exception {
        int databaseSizeBeforeCreate = horarioRepository.findAll().size();

        // Create the Horario
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);
        restHorarioMockMvc.perform(post("/api/horarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horarioDTO)))
            .andExpect(status().isCreated());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeCreate + 1);
        Horario testHorario = horarioList.get(horarioList.size() - 1);
        assertThat(testHorario.getInicio()).isEqualTo(DEFAULT_INICIO);
        assertThat(testHorario.getFim()).isEqualTo(DEFAULT_FIM);
        assertThat(testHorario.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testHorario.getAnoLectivo()).isEqualTo(DEFAULT_ANO_LECTIVO);
        assertThat(testHorario.getDiaSemana()).isEqualTo(DEFAULT_DIA_SEMANA);
        assertThat(testHorario.getCategoria()).isEqualTo(DEFAULT_CATEGORIA);

        // Validate the Horario in Elasticsearch
        verify(mockHorarioSearchRepository, times(1)).save(testHorario);
    }

    @Test
    @Transactional
    public void createHorarioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = horarioRepository.findAll().size();

        // Create the Horario with an existing ID
        horario.setId(1L);
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHorarioMockMvc.perform(post("/api/horarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeCreate);

        // Validate the Horario in Elasticsearch
        verify(mockHorarioSearchRepository, times(0)).save(horario);
    }


    @Test
    @Transactional
    public void checkInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = horarioRepository.findAll().size();
        // set the field null
        horario.setInicio(null);

        // Create the Horario, which fails.
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        restHorarioMockMvc.perform(post("/api/horarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horarioDTO)))
            .andExpect(status().isBadRequest());

        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFimIsRequired() throws Exception {
        int databaseSizeBeforeTest = horarioRepository.findAll().size();
        // set the field null
        horario.setFim(null);

        // Create the Horario, which fails.
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        restHorarioMockMvc.perform(post("/api/horarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horarioDTO)))
            .andExpect(status().isBadRequest());

        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = horarioRepository.findAll().size();
        // set the field null
        horario.setData(null);

        // Create the Horario, which fails.
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        restHorarioMockMvc.perform(post("/api/horarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horarioDTO)))
            .andExpect(status().isBadRequest());

        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnoLectivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = horarioRepository.findAll().size();
        // set the field null
        horario.setAnoLectivo(null);

        // Create the Horario, which fails.
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        restHorarioMockMvc.perform(post("/api/horarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horarioDTO)))
            .andExpect(status().isBadRequest());

        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiaSemanaIsRequired() throws Exception {
        int databaseSizeBeforeTest = horarioRepository.findAll().size();
        // set the field null
        horario.setDiaSemana(null);

        // Create the Horario, which fails.
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        restHorarioMockMvc.perform(post("/api/horarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horarioDTO)))
            .andExpect(status().isBadRequest());

        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoriaIsRequired() throws Exception {
        int databaseSizeBeforeTest = horarioRepository.findAll().size();
        // set the field null
        horario.setCategoria(null);

        // Create the Horario, which fails.
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        restHorarioMockMvc.perform(post("/api/horarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horarioDTO)))
            .andExpect(status().isBadRequest());

        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHorarios() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList
        restHorarioMockMvc.perform(get("/api/horarios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horario.getId().intValue())))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO)))
            .andExpect(jsonPath("$.[*].fim").value(hasItem(DEFAULT_FIM)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].diaSemana").value(hasItem(DEFAULT_DIA_SEMANA)))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA)));
    }
    
    @Test
    @Transactional
    public void getHorario() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get the horario
        restHorarioMockMvc.perform(get("/api/horarios/{id}", horario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(horario.getId().intValue()))
            .andExpect(jsonPath("$.inicio").value(DEFAULT_INICIO))
            .andExpect(jsonPath("$.fim").value(DEFAULT_FIM))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)))
            .andExpect(jsonPath("$.anoLectivo").value(DEFAULT_ANO_LECTIVO))
            .andExpect(jsonPath("$.diaSemana").value(DEFAULT_DIA_SEMANA))
            .andExpect(jsonPath("$.categoria").value(DEFAULT_CATEGORIA));
    }


    @Test
    @Transactional
    public void getHorariosByIdFiltering() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        Long id = horario.getId();

        defaultHorarioShouldBeFound("id.equals=" + id);
        defaultHorarioShouldNotBeFound("id.notEquals=" + id);

        defaultHorarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHorarioShouldNotBeFound("id.greaterThan=" + id);

        defaultHorarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHorarioShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllHorariosByInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where inicio equals to DEFAULT_INICIO
        defaultHorarioShouldBeFound("inicio.equals=" + DEFAULT_INICIO);

        // Get all the horarioList where inicio equals to UPDATED_INICIO
        defaultHorarioShouldNotBeFound("inicio.equals=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    public void getAllHorariosByInicioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where inicio not equals to DEFAULT_INICIO
        defaultHorarioShouldNotBeFound("inicio.notEquals=" + DEFAULT_INICIO);

        // Get all the horarioList where inicio not equals to UPDATED_INICIO
        defaultHorarioShouldBeFound("inicio.notEquals=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    public void getAllHorariosByInicioIsInShouldWork() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where inicio in DEFAULT_INICIO or UPDATED_INICIO
        defaultHorarioShouldBeFound("inicio.in=" + DEFAULT_INICIO + "," + UPDATED_INICIO);

        // Get all the horarioList where inicio equals to UPDATED_INICIO
        defaultHorarioShouldNotBeFound("inicio.in=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    public void getAllHorariosByInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where inicio is not null
        defaultHorarioShouldBeFound("inicio.specified=true");

        // Get all the horarioList where inicio is null
        defaultHorarioShouldNotBeFound("inicio.specified=false");
    }
                @Test
    @Transactional
    public void getAllHorariosByInicioContainsSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where inicio contains DEFAULT_INICIO
        defaultHorarioShouldBeFound("inicio.contains=" + DEFAULT_INICIO);

        // Get all the horarioList where inicio contains UPDATED_INICIO
        defaultHorarioShouldNotBeFound("inicio.contains=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    public void getAllHorariosByInicioNotContainsSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where inicio does not contain DEFAULT_INICIO
        defaultHorarioShouldNotBeFound("inicio.doesNotContain=" + DEFAULT_INICIO);

        // Get all the horarioList where inicio does not contain UPDATED_INICIO
        defaultHorarioShouldBeFound("inicio.doesNotContain=" + UPDATED_INICIO);
    }


    @Test
    @Transactional
    public void getAllHorariosByFimIsEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where fim equals to DEFAULT_FIM
        defaultHorarioShouldBeFound("fim.equals=" + DEFAULT_FIM);

        // Get all the horarioList where fim equals to UPDATED_FIM
        defaultHorarioShouldNotBeFound("fim.equals=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    public void getAllHorariosByFimIsNotEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where fim not equals to DEFAULT_FIM
        defaultHorarioShouldNotBeFound("fim.notEquals=" + DEFAULT_FIM);

        // Get all the horarioList where fim not equals to UPDATED_FIM
        defaultHorarioShouldBeFound("fim.notEquals=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    public void getAllHorariosByFimIsInShouldWork() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where fim in DEFAULT_FIM or UPDATED_FIM
        defaultHorarioShouldBeFound("fim.in=" + DEFAULT_FIM + "," + UPDATED_FIM);

        // Get all the horarioList where fim equals to UPDATED_FIM
        defaultHorarioShouldNotBeFound("fim.in=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    public void getAllHorariosByFimIsNullOrNotNull() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where fim is not null
        defaultHorarioShouldBeFound("fim.specified=true");

        // Get all the horarioList where fim is null
        defaultHorarioShouldNotBeFound("fim.specified=false");
    }
                @Test
    @Transactional
    public void getAllHorariosByFimContainsSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where fim contains DEFAULT_FIM
        defaultHorarioShouldBeFound("fim.contains=" + DEFAULT_FIM);

        // Get all the horarioList where fim contains UPDATED_FIM
        defaultHorarioShouldNotBeFound("fim.contains=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    public void getAllHorariosByFimNotContainsSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where fim does not contain DEFAULT_FIM
        defaultHorarioShouldNotBeFound("fim.doesNotContain=" + DEFAULT_FIM);

        // Get all the horarioList where fim does not contain UPDATED_FIM
        defaultHorarioShouldBeFound("fim.doesNotContain=" + UPDATED_FIM);
    }


    @Test
    @Transactional
    public void getAllHorariosByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where data equals to DEFAULT_DATA
        defaultHorarioShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the horarioList where data equals to UPDATED_DATA
        defaultHorarioShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllHorariosByDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where data not equals to DEFAULT_DATA
        defaultHorarioShouldNotBeFound("data.notEquals=" + DEFAULT_DATA);

        // Get all the horarioList where data not equals to UPDATED_DATA
        defaultHorarioShouldBeFound("data.notEquals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllHorariosByDataIsInShouldWork() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where data in DEFAULT_DATA or UPDATED_DATA
        defaultHorarioShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the horarioList where data equals to UPDATED_DATA
        defaultHorarioShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllHorariosByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where data is not null
        defaultHorarioShouldBeFound("data.specified=true");

        // Get all the horarioList where data is null
        defaultHorarioShouldNotBeFound("data.specified=false");
    }

    @Test
    @Transactional
    public void getAllHorariosByDataIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where data is greater than or equal to DEFAULT_DATA
        defaultHorarioShouldBeFound("data.greaterThanOrEqual=" + DEFAULT_DATA);

        // Get all the horarioList where data is greater than or equal to UPDATED_DATA
        defaultHorarioShouldNotBeFound("data.greaterThanOrEqual=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllHorariosByDataIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where data is less than or equal to DEFAULT_DATA
        defaultHorarioShouldBeFound("data.lessThanOrEqual=" + DEFAULT_DATA);

        // Get all the horarioList where data is less than or equal to SMALLER_DATA
        defaultHorarioShouldNotBeFound("data.lessThanOrEqual=" + SMALLER_DATA);
    }

    @Test
    @Transactional
    public void getAllHorariosByDataIsLessThanSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where data is less than DEFAULT_DATA
        defaultHorarioShouldNotBeFound("data.lessThan=" + DEFAULT_DATA);

        // Get all the horarioList where data is less than UPDATED_DATA
        defaultHorarioShouldBeFound("data.lessThan=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllHorariosByDataIsGreaterThanSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where data is greater than DEFAULT_DATA
        defaultHorarioShouldNotBeFound("data.greaterThan=" + DEFAULT_DATA);

        // Get all the horarioList where data is greater than SMALLER_DATA
        defaultHorarioShouldBeFound("data.greaterThan=" + SMALLER_DATA);
    }


    @Test
    @Transactional
    public void getAllHorariosByAnoLectivoIsEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where anoLectivo equals to DEFAULT_ANO_LECTIVO
        defaultHorarioShouldBeFound("anoLectivo.equals=" + DEFAULT_ANO_LECTIVO);

        // Get all the horarioList where anoLectivo equals to UPDATED_ANO_LECTIVO
        defaultHorarioShouldNotBeFound("anoLectivo.equals=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllHorariosByAnoLectivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where anoLectivo not equals to DEFAULT_ANO_LECTIVO
        defaultHorarioShouldNotBeFound("anoLectivo.notEquals=" + DEFAULT_ANO_LECTIVO);

        // Get all the horarioList where anoLectivo not equals to UPDATED_ANO_LECTIVO
        defaultHorarioShouldBeFound("anoLectivo.notEquals=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllHorariosByAnoLectivoIsInShouldWork() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where anoLectivo in DEFAULT_ANO_LECTIVO or UPDATED_ANO_LECTIVO
        defaultHorarioShouldBeFound("anoLectivo.in=" + DEFAULT_ANO_LECTIVO + "," + UPDATED_ANO_LECTIVO);

        // Get all the horarioList where anoLectivo equals to UPDATED_ANO_LECTIVO
        defaultHorarioShouldNotBeFound("anoLectivo.in=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllHorariosByAnoLectivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where anoLectivo is not null
        defaultHorarioShouldBeFound("anoLectivo.specified=true");

        // Get all the horarioList where anoLectivo is null
        defaultHorarioShouldNotBeFound("anoLectivo.specified=false");
    }

    @Test
    @Transactional
    public void getAllHorariosByAnoLectivoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where anoLectivo is greater than or equal to DEFAULT_ANO_LECTIVO
        defaultHorarioShouldBeFound("anoLectivo.greaterThanOrEqual=" + DEFAULT_ANO_LECTIVO);

        // Get all the horarioList where anoLectivo is greater than or equal to UPDATED_ANO_LECTIVO
        defaultHorarioShouldNotBeFound("anoLectivo.greaterThanOrEqual=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllHorariosByAnoLectivoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where anoLectivo is less than or equal to DEFAULT_ANO_LECTIVO
        defaultHorarioShouldBeFound("anoLectivo.lessThanOrEqual=" + DEFAULT_ANO_LECTIVO);

        // Get all the horarioList where anoLectivo is less than or equal to SMALLER_ANO_LECTIVO
        defaultHorarioShouldNotBeFound("anoLectivo.lessThanOrEqual=" + SMALLER_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllHorariosByAnoLectivoIsLessThanSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where anoLectivo is less than DEFAULT_ANO_LECTIVO
        defaultHorarioShouldNotBeFound("anoLectivo.lessThan=" + DEFAULT_ANO_LECTIVO);

        // Get all the horarioList where anoLectivo is less than UPDATED_ANO_LECTIVO
        defaultHorarioShouldBeFound("anoLectivo.lessThan=" + UPDATED_ANO_LECTIVO);
    }

    @Test
    @Transactional
    public void getAllHorariosByAnoLectivoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where anoLectivo is greater than DEFAULT_ANO_LECTIVO
        defaultHorarioShouldNotBeFound("anoLectivo.greaterThan=" + DEFAULT_ANO_LECTIVO);

        // Get all the horarioList where anoLectivo is greater than SMALLER_ANO_LECTIVO
        defaultHorarioShouldBeFound("anoLectivo.greaterThan=" + SMALLER_ANO_LECTIVO);
    }


    @Test
    @Transactional
    public void getAllHorariosByDiaSemanaIsEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where diaSemana equals to DEFAULT_DIA_SEMANA
        defaultHorarioShouldBeFound("diaSemana.equals=" + DEFAULT_DIA_SEMANA);

        // Get all the horarioList where diaSemana equals to UPDATED_DIA_SEMANA
        defaultHorarioShouldNotBeFound("diaSemana.equals=" + UPDATED_DIA_SEMANA);
    }

    @Test
    @Transactional
    public void getAllHorariosByDiaSemanaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where diaSemana not equals to DEFAULT_DIA_SEMANA
        defaultHorarioShouldNotBeFound("diaSemana.notEquals=" + DEFAULT_DIA_SEMANA);

        // Get all the horarioList where diaSemana not equals to UPDATED_DIA_SEMANA
        defaultHorarioShouldBeFound("diaSemana.notEquals=" + UPDATED_DIA_SEMANA);
    }

    @Test
    @Transactional
    public void getAllHorariosByDiaSemanaIsInShouldWork() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where diaSemana in DEFAULT_DIA_SEMANA or UPDATED_DIA_SEMANA
        defaultHorarioShouldBeFound("diaSemana.in=" + DEFAULT_DIA_SEMANA + "," + UPDATED_DIA_SEMANA);

        // Get all the horarioList where diaSemana equals to UPDATED_DIA_SEMANA
        defaultHorarioShouldNotBeFound("diaSemana.in=" + UPDATED_DIA_SEMANA);
    }

    @Test
    @Transactional
    public void getAllHorariosByDiaSemanaIsNullOrNotNull() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where diaSemana is not null
        defaultHorarioShouldBeFound("diaSemana.specified=true");

        // Get all the horarioList where diaSemana is null
        defaultHorarioShouldNotBeFound("diaSemana.specified=false");
    }
                @Test
    @Transactional
    public void getAllHorariosByDiaSemanaContainsSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where diaSemana contains DEFAULT_DIA_SEMANA
        defaultHorarioShouldBeFound("diaSemana.contains=" + DEFAULT_DIA_SEMANA);

        // Get all the horarioList where diaSemana contains UPDATED_DIA_SEMANA
        defaultHorarioShouldNotBeFound("diaSemana.contains=" + UPDATED_DIA_SEMANA);
    }

    @Test
    @Transactional
    public void getAllHorariosByDiaSemanaNotContainsSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where diaSemana does not contain DEFAULT_DIA_SEMANA
        defaultHorarioShouldNotBeFound("diaSemana.doesNotContain=" + DEFAULT_DIA_SEMANA);

        // Get all the horarioList where diaSemana does not contain UPDATED_DIA_SEMANA
        defaultHorarioShouldBeFound("diaSemana.doesNotContain=" + UPDATED_DIA_SEMANA);
    }


    @Test
    @Transactional
    public void getAllHorariosByCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where categoria equals to DEFAULT_CATEGORIA
        defaultHorarioShouldBeFound("categoria.equals=" + DEFAULT_CATEGORIA);

        // Get all the horarioList where categoria equals to UPDATED_CATEGORIA
        defaultHorarioShouldNotBeFound("categoria.equals=" + UPDATED_CATEGORIA);
    }

    @Test
    @Transactional
    public void getAllHorariosByCategoriaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where categoria not equals to DEFAULT_CATEGORIA
        defaultHorarioShouldNotBeFound("categoria.notEquals=" + DEFAULT_CATEGORIA);

        // Get all the horarioList where categoria not equals to UPDATED_CATEGORIA
        defaultHorarioShouldBeFound("categoria.notEquals=" + UPDATED_CATEGORIA);
    }

    @Test
    @Transactional
    public void getAllHorariosByCategoriaIsInShouldWork() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where categoria in DEFAULT_CATEGORIA or UPDATED_CATEGORIA
        defaultHorarioShouldBeFound("categoria.in=" + DEFAULT_CATEGORIA + "," + UPDATED_CATEGORIA);

        // Get all the horarioList where categoria equals to UPDATED_CATEGORIA
        defaultHorarioShouldNotBeFound("categoria.in=" + UPDATED_CATEGORIA);
    }

    @Test
    @Transactional
    public void getAllHorariosByCategoriaIsNullOrNotNull() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where categoria is not null
        defaultHorarioShouldBeFound("categoria.specified=true");

        // Get all the horarioList where categoria is null
        defaultHorarioShouldNotBeFound("categoria.specified=false");
    }
                @Test
    @Transactional
    public void getAllHorariosByCategoriaContainsSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where categoria contains DEFAULT_CATEGORIA
        defaultHorarioShouldBeFound("categoria.contains=" + DEFAULT_CATEGORIA);

        // Get all the horarioList where categoria contains UPDATED_CATEGORIA
        defaultHorarioShouldNotBeFound("categoria.contains=" + UPDATED_CATEGORIA);
    }

    @Test
    @Transactional
    public void getAllHorariosByCategoriaNotContainsSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        // Get all the horarioList where categoria does not contain DEFAULT_CATEGORIA
        defaultHorarioShouldNotBeFound("categoria.doesNotContain=" + DEFAULT_CATEGORIA);

        // Get all the horarioList where categoria does not contain UPDATED_CATEGORIA
        defaultHorarioShouldBeFound("categoria.doesNotContain=" + UPDATED_CATEGORIA);
    }


    @Test
    @Transactional
    public void getAllHorariosByTurmaIsEqualToSomething() throws Exception {
        // Get already existing entity
        Turma turma = horario.getTurma();
        horarioRepository.saveAndFlush(horario);
        Long turmaId = turma.getId();

        // Get all the horarioList where turma equals to turmaId
        defaultHorarioShouldBeFound("turmaId.equals=" + turmaId);

        // Get all the horarioList where turma equals to turmaId + 1
        defaultHorarioShouldNotBeFound("turmaId.equals=" + (turmaId + 1));
    }


    @Test
    @Transactional
    public void getAllHorariosByProfessorIsEqualToSomething() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);
        Professor professor = ProfessorResourceIT.createEntity(em);
        em.persist(professor);
        em.flush();
        horario.setProfessor(professor);
        horarioRepository.saveAndFlush(horario);
        Long professorId = professor.getId();

        // Get all the horarioList where professor equals to professorId
        defaultHorarioShouldBeFound("professorId.equals=" + professorId);

        // Get all the horarioList where professor equals to professorId + 1
        defaultHorarioShouldNotBeFound("professorId.equals=" + (professorId + 1));
    }


    @Test
    @Transactional
    public void getAllHorariosByCurriculoIsEqualToSomething() throws Exception {
        // Get already existing entity
        PlanoCurricular curriculo = horario.getCurriculo();
        horarioRepository.saveAndFlush(horario);
        Long curriculoId = curriculo.getId();

        // Get all the horarioList where curriculo equals to curriculoId
        defaultHorarioShouldBeFound("curriculoId.equals=" + curriculoId);

        // Get all the horarioList where curriculo equals to curriculoId + 1
        defaultHorarioShouldNotBeFound("curriculoId.equals=" + (curriculoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHorarioShouldBeFound(String filter) throws Exception {
        restHorarioMockMvc.perform(get("/api/horarios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horario.getId().intValue())))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO)))
            .andExpect(jsonPath("$.[*].fim").value(hasItem(DEFAULT_FIM)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].diaSemana").value(hasItem(DEFAULT_DIA_SEMANA)))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA)));

        // Check, that the count call also returns 1
        restHorarioMockMvc.perform(get("/api/horarios/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHorarioShouldNotBeFound(String filter) throws Exception {
        restHorarioMockMvc.perform(get("/api/horarios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHorarioMockMvc.perform(get("/api/horarios/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHorario() throws Exception {
        // Get the horario
        restHorarioMockMvc.perform(get("/api/horarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHorario() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        int databaseSizeBeforeUpdate = horarioRepository.findAll().size();

        // Update the horario
        Horario updatedHorario = horarioRepository.findById(horario.getId()).get();
        // Disconnect from session so that the updates on updatedHorario are not directly saved in db
        em.detach(updatedHorario);
        updatedHorario
            .inicio(UPDATED_INICIO)
            .fim(UPDATED_FIM)
            .data(UPDATED_DATA)
            .anoLectivo(UPDATED_ANO_LECTIVO)
            .diaSemana(UPDATED_DIA_SEMANA)
            .categoria(UPDATED_CATEGORIA);
        HorarioDTO horarioDTO = horarioMapper.toDto(updatedHorario);

        restHorarioMockMvc.perform(put("/api/horarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horarioDTO)))
            .andExpect(status().isOk());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeUpdate);
        Horario testHorario = horarioList.get(horarioList.size() - 1);
        assertThat(testHorario.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testHorario.getFim()).isEqualTo(UPDATED_FIM);
        assertThat(testHorario.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testHorario.getAnoLectivo()).isEqualTo(UPDATED_ANO_LECTIVO);
        assertThat(testHorario.getDiaSemana()).isEqualTo(UPDATED_DIA_SEMANA);
        assertThat(testHorario.getCategoria()).isEqualTo(UPDATED_CATEGORIA);

        // Validate the Horario in Elasticsearch
        verify(mockHorarioSearchRepository, times(1)).save(testHorario);
    }

    @Test
    @Transactional
    public void updateNonExistingHorario() throws Exception {
        int databaseSizeBeforeUpdate = horarioRepository.findAll().size();

        // Create the Horario
        HorarioDTO horarioDTO = horarioMapper.toDto(horario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHorarioMockMvc.perform(put("/api/horarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Horario in the database
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Horario in Elasticsearch
        verify(mockHorarioSearchRepository, times(0)).save(horario);
    }

    @Test
    @Transactional
    public void deleteHorario() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);

        int databaseSizeBeforeDelete = horarioRepository.findAll().size();

        // Delete the horario
        restHorarioMockMvc.perform(delete("/api/horarios/{id}", horario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Horario> horarioList = horarioRepository.findAll();
        assertThat(horarioList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Horario in Elasticsearch
        verify(mockHorarioSearchRepository, times(1)).deleteById(horario.getId());
    }

    @Test
    @Transactional
    public void searchHorario() throws Exception {
        // Initialize the database
        horarioRepository.saveAndFlush(horario);
        when(mockHorarioSearchRepository.search(queryStringQuery("id:" + horario.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(horario), PageRequest.of(0, 1), 1));
        // Search the horario
        restHorarioMockMvc.perform(get("/api/_search/horarios?query=id:" + horario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horario.getId().intValue())))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO)))
            .andExpect(jsonPath("$.[*].fim").value(hasItem(DEFAULT_FIM)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].anoLectivo").value(hasItem(DEFAULT_ANO_LECTIVO)))
            .andExpect(jsonPath("$.[*].diaSemana").value(hasItem(DEFAULT_DIA_SEMANA)))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA)));
    }
}
