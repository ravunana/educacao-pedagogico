package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.ProvaAptidaoProfissional;
import com.ravunana.educacao.pedagogico.repository.ProvaAptidaoProfissionalRepository;
import com.ravunana.educacao.pedagogico.repository.search.ProvaAptidaoProfissionalSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.ProvaAptidaoProfissionalDTO;
import com.ravunana.educacao.pedagogico.service.mapper.ProvaAptidaoProfissionalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ProvaAptidaoProfissional}.
 */
@Service
@Transactional
public class ProvaAptidaoProfissionalService {

    private final Logger log = LoggerFactory.getLogger(ProvaAptidaoProfissionalService.class);

    private final ProvaAptidaoProfissionalRepository provaAptidaoProfissionalRepository;

    private final ProvaAptidaoProfissionalMapper provaAptidaoProfissionalMapper;

    private final ProvaAptidaoProfissionalSearchRepository provaAptidaoProfissionalSearchRepository;

    public ProvaAptidaoProfissionalService(ProvaAptidaoProfissionalRepository provaAptidaoProfissionalRepository, ProvaAptidaoProfissionalMapper provaAptidaoProfissionalMapper, ProvaAptidaoProfissionalSearchRepository provaAptidaoProfissionalSearchRepository) {
        this.provaAptidaoProfissionalRepository = provaAptidaoProfissionalRepository;
        this.provaAptidaoProfissionalMapper = provaAptidaoProfissionalMapper;
        this.provaAptidaoProfissionalSearchRepository = provaAptidaoProfissionalSearchRepository;
    }

    /**
     * Save a provaAptidaoProfissional.
     *
     * @param provaAptidaoProfissionalDTO the entity to save.
     * @return the persisted entity.
     */
    public ProvaAptidaoProfissionalDTO save(ProvaAptidaoProfissionalDTO provaAptidaoProfissionalDTO) {
        log.debug("Request to save ProvaAptidaoProfissional : {}", provaAptidaoProfissionalDTO);
        ProvaAptidaoProfissional provaAptidaoProfissional = provaAptidaoProfissionalMapper.toEntity(provaAptidaoProfissionalDTO);
        provaAptidaoProfissional = provaAptidaoProfissionalRepository.save(provaAptidaoProfissional);
        ProvaAptidaoProfissionalDTO result = provaAptidaoProfissionalMapper.toDto(provaAptidaoProfissional);
        provaAptidaoProfissionalSearchRepository.save(provaAptidaoProfissional);
        return result;
    }

    /**
     * Get all the provaAptidaoProfissionals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProvaAptidaoProfissionalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProvaAptidaoProfissionals");
        return provaAptidaoProfissionalRepository.findAll(pageable)
            .map(provaAptidaoProfissionalMapper::toDto);
    }


    /**
     * Get one provaAptidaoProfissional by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProvaAptidaoProfissionalDTO> findOne(Long id) {
        log.debug("Request to get ProvaAptidaoProfissional : {}", id);
        return provaAptidaoProfissionalRepository.findById(id)
            .map(provaAptidaoProfissionalMapper::toDto);
    }

    /**
     * Delete the provaAptidaoProfissional by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProvaAptidaoProfissional : {}", id);
        provaAptidaoProfissionalRepository.deleteById(id);
        provaAptidaoProfissionalSearchRepository.deleteById(id);
    }

    /**
     * Search for the provaAptidaoProfissional corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProvaAptidaoProfissionalDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProvaAptidaoProfissionals for query {}", query);
        return provaAptidaoProfissionalSearchRepository.search(queryStringQuery(query), pageable)
            .map(provaAptidaoProfissionalMapper::toDto);
    }
}
