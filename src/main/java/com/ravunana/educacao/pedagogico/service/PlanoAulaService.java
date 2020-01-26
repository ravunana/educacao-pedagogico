package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.PlanoAula;
import com.ravunana.educacao.pedagogico.repository.PlanoAulaRepository;
import com.ravunana.educacao.pedagogico.repository.search.PlanoAulaSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.PlanoAulaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.PlanoAulaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link PlanoAula}.
 */
@Service
@Transactional
public class PlanoAulaService {

    private final Logger log = LoggerFactory.getLogger(PlanoAulaService.class);

    private final PlanoAulaRepository planoAulaRepository;

    private final PlanoAulaMapper planoAulaMapper;

    private final PlanoAulaSearchRepository planoAulaSearchRepository;

    public PlanoAulaService(PlanoAulaRepository planoAulaRepository, PlanoAulaMapper planoAulaMapper, PlanoAulaSearchRepository planoAulaSearchRepository) {
        this.planoAulaRepository = planoAulaRepository;
        this.planoAulaMapper = planoAulaMapper;
        this.planoAulaSearchRepository = planoAulaSearchRepository;
    }

    /**
     * Save a planoAula.
     *
     * @param planoAulaDTO the entity to save.
     * @return the persisted entity.
     */
    public PlanoAulaDTO save(PlanoAulaDTO planoAulaDTO) {
        log.debug("Request to save PlanoAula : {}", planoAulaDTO);
        PlanoAula planoAula = planoAulaMapper.toEntity(planoAulaDTO);
        planoAula = planoAulaRepository.save(planoAula);
        PlanoAulaDTO result = planoAulaMapper.toDto(planoAula);
        planoAulaSearchRepository.save(planoAula);
        return result;
    }

    /**
     * Get all the planoAulas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanoAulaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlanoAulas");
        return planoAulaRepository.findAll(pageable)
            .map(planoAulaMapper::toDto);
    }

    /**
     * Get all the planoAulas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PlanoAulaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return planoAulaRepository.findAllWithEagerRelationships(pageable).map(planoAulaMapper::toDto);
    }
    

    /**
     * Get one planoAula by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlanoAulaDTO> findOne(Long id) {
        log.debug("Request to get PlanoAula : {}", id);
        return planoAulaRepository.findOneWithEagerRelationships(id)
            .map(planoAulaMapper::toDto);
    }

    /**
     * Delete the planoAula by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PlanoAula : {}", id);
        planoAulaRepository.deleteById(id);
        planoAulaSearchRepository.deleteById(id);
    }

    /**
     * Search for the planoAula corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanoAulaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PlanoAulas for query {}", query);
        return planoAulaSearchRepository.search(queryStringQuery(query), pageable)
            .map(planoAulaMapper::toDto);
    }
}
