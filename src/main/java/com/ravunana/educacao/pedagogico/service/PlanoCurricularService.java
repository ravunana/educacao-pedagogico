package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import com.ravunana.educacao.pedagogico.repository.PlanoCurricularRepository;
import com.ravunana.educacao.pedagogico.repository.search.PlanoCurricularSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.PlanoCurricularDTO;
import com.ravunana.educacao.pedagogico.service.mapper.PlanoCurricularMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link PlanoCurricular}.
 */
@Service
@Transactional
public class PlanoCurricularService {

    private final Logger log = LoggerFactory.getLogger(PlanoCurricularService.class);

    private final PlanoCurricularRepository planoCurricularRepository;

    private final PlanoCurricularMapper planoCurricularMapper;

    private final PlanoCurricularSearchRepository planoCurricularSearchRepository;

    public PlanoCurricularService(PlanoCurricularRepository planoCurricularRepository, PlanoCurricularMapper planoCurricularMapper, PlanoCurricularSearchRepository planoCurricularSearchRepository) {
        this.planoCurricularRepository = planoCurricularRepository;
        this.planoCurricularMapper = planoCurricularMapper;
        this.planoCurricularSearchRepository = planoCurricularSearchRepository;
    }

    /**
     * Save a planoCurricular.
     *
     * @param planoCurricularDTO the entity to save.
     * @return the persisted entity.
     */
    public PlanoCurricularDTO save(PlanoCurricularDTO planoCurricularDTO) {
        log.debug("Request to save PlanoCurricular : {}", planoCurricularDTO);
        PlanoCurricular planoCurricular = planoCurricularMapper.toEntity(planoCurricularDTO);
        planoCurricular = planoCurricularRepository.save(planoCurricular);
        PlanoCurricularDTO result = planoCurricularMapper.toDto(planoCurricular);
        planoCurricularSearchRepository.save(planoCurricular);
        return result;
    }

    /**
     * Get all the planoCurriculars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanoCurricularDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlanoCurriculars");
        return planoCurricularRepository.findAll(pageable)
            .map(planoCurricularMapper::toDto);
    }


    /**
     * Get one planoCurricular by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlanoCurricularDTO> findOne(Long id) {
        log.debug("Request to get PlanoCurricular : {}", id);
        return planoCurricularRepository.findById(id)
            .map(planoCurricularMapper::toDto);
    }

    /**
     * Delete the planoCurricular by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PlanoCurricular : {}", id);
        planoCurricularRepository.deleteById(id);
        planoCurricularSearchRepository.deleteById(id);
    }

    /**
     * Search for the planoCurricular corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanoCurricularDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PlanoCurriculars for query {}", query);
        return planoCurricularSearchRepository.search(queryStringQuery(query), pageable)
            .map(planoCurricularMapper::toDto);
    }
}
