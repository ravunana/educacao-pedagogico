package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.Aula;
import com.ravunana.educacao.pedagogico.repository.AulaRepository;
import com.ravunana.educacao.pedagogico.repository.search.AulaSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.AulaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.AulaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Aula}.
 */
@Service
@Transactional
public class AulaService {

    private final Logger log = LoggerFactory.getLogger(AulaService.class);

    private final AulaRepository aulaRepository;

    private final AulaMapper aulaMapper;

    private final AulaSearchRepository aulaSearchRepository;

    public AulaService(AulaRepository aulaRepository, AulaMapper aulaMapper, AulaSearchRepository aulaSearchRepository) {
        this.aulaRepository = aulaRepository;
        this.aulaMapper = aulaMapper;
        this.aulaSearchRepository = aulaSearchRepository;
    }

    /**
     * Save a aula.
     *
     * @param aulaDTO the entity to save.
     * @return the persisted entity.
     */
    public AulaDTO save(AulaDTO aulaDTO) {
        log.debug("Request to save Aula : {}", aulaDTO);
        Aula aula = aulaMapper.toEntity(aulaDTO);
        aula = aulaRepository.save(aula);
        AulaDTO result = aulaMapper.toDto(aula);
        aulaSearchRepository.save(aula);
        return result;
    }

    /**
     * Get all the aulas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AulaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Aulas");
        return aulaRepository.findAll(pageable)
            .map(aulaMapper::toDto);
    }

    /**
     * Get all the aulas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AulaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return aulaRepository.findAllWithEagerRelationships(pageable).map(aulaMapper::toDto);
    }
    

    /**
     * Get one aula by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AulaDTO> findOne(Long id) {
        log.debug("Request to get Aula : {}", id);
        return aulaRepository.findOneWithEagerRelationships(id)
            .map(aulaMapper::toDto);
    }

    /**
     * Delete the aula by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Aula : {}", id);
        aulaRepository.deleteById(id);
        aulaSearchRepository.deleteById(id);
    }

    /**
     * Search for the aula corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AulaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Aulas for query {}", query);
        return aulaSearchRepository.search(queryStringQuery(query), pageable)
            .map(aulaMapper::toDto);
    }
}
