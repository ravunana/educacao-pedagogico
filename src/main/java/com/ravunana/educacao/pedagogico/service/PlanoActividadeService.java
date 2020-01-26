package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.PlanoActividade;
import com.ravunana.educacao.pedagogico.repository.PlanoActividadeRepository;
import com.ravunana.educacao.pedagogico.repository.search.PlanoActividadeSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.PlanoActividadeDTO;
import com.ravunana.educacao.pedagogico.service.mapper.PlanoActividadeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link PlanoActividade}.
 */
@Service
@Transactional
public class PlanoActividadeService {

    private final Logger log = LoggerFactory.getLogger(PlanoActividadeService.class);

    private final PlanoActividadeRepository planoActividadeRepository;

    private final PlanoActividadeMapper planoActividadeMapper;

    private final PlanoActividadeSearchRepository planoActividadeSearchRepository;

    public PlanoActividadeService(PlanoActividadeRepository planoActividadeRepository, PlanoActividadeMapper planoActividadeMapper, PlanoActividadeSearchRepository planoActividadeSearchRepository) {
        this.planoActividadeRepository = planoActividadeRepository;
        this.planoActividadeMapper = planoActividadeMapper;
        this.planoActividadeSearchRepository = planoActividadeSearchRepository;
    }

    /**
     * Save a planoActividade.
     *
     * @param planoActividadeDTO the entity to save.
     * @return the persisted entity.
     */
    public PlanoActividadeDTO save(PlanoActividadeDTO planoActividadeDTO) {
        log.debug("Request to save PlanoActividade : {}", planoActividadeDTO);
        PlanoActividade planoActividade = planoActividadeMapper.toEntity(planoActividadeDTO);
        planoActividade = planoActividadeRepository.save(planoActividade);
        PlanoActividadeDTO result = planoActividadeMapper.toDto(planoActividade);
        planoActividadeSearchRepository.save(planoActividade);
        return result;
    }

    /**
     * Get all the planoActividades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanoActividadeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlanoActividades");
        return planoActividadeRepository.findAll(pageable)
            .map(planoActividadeMapper::toDto);
    }


    /**
     * Get one planoActividade by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlanoActividadeDTO> findOne(Long id) {
        log.debug("Request to get PlanoActividade : {}", id);
        return planoActividadeRepository.findById(id)
            .map(planoActividadeMapper::toDto);
    }

    /**
     * Delete the planoActividade by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PlanoActividade : {}", id);
        planoActividadeRepository.deleteById(id);
        planoActividadeSearchRepository.deleteById(id);
    }

    /**
     * Search for the planoActividade corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanoActividadeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PlanoActividades for query {}", query);
        return planoActividadeSearchRepository.search(queryStringQuery(query), pageable)
            .map(planoActividadeMapper::toDto);
    }
}
