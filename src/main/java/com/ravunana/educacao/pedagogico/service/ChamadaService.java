package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.Chamada;
import com.ravunana.educacao.pedagogico.repository.ChamadaRepository;
import com.ravunana.educacao.pedagogico.repository.search.ChamadaSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.ChamadaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.ChamadaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Chamada}.
 */
@Service
@Transactional
public class ChamadaService {

    private final Logger log = LoggerFactory.getLogger(ChamadaService.class);

    private final ChamadaRepository chamadaRepository;

    private final ChamadaMapper chamadaMapper;

    private final ChamadaSearchRepository chamadaSearchRepository;

    public ChamadaService(ChamadaRepository chamadaRepository, ChamadaMapper chamadaMapper, ChamadaSearchRepository chamadaSearchRepository) {
        this.chamadaRepository = chamadaRepository;
        this.chamadaMapper = chamadaMapper;
        this.chamadaSearchRepository = chamadaSearchRepository;
    }

    /**
     * Save a chamada.
     *
     * @param chamadaDTO the entity to save.
     * @return the persisted entity.
     */
    public ChamadaDTO save(ChamadaDTO chamadaDTO) {
        log.debug("Request to save Chamada : {}", chamadaDTO);
        Chamada chamada = chamadaMapper.toEntity(chamadaDTO);
        chamada = chamadaRepository.save(chamada);
        ChamadaDTO result = chamadaMapper.toDto(chamada);
        chamadaSearchRepository.save(chamada);
        return result;
    }

    /**
     * Get all the chamadas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChamadaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Chamadas");
        return chamadaRepository.findAll(pageable)
            .map(chamadaMapper::toDto);
    }


    /**
     * Get one chamada by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChamadaDTO> findOne(Long id) {
        log.debug("Request to get Chamada : {}", id);
        return chamadaRepository.findById(id)
            .map(chamadaMapper::toDto);
    }

    /**
     * Delete the chamada by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Chamada : {}", id);
        chamadaRepository.deleteById(id);
        chamadaSearchRepository.deleteById(id);
    }

    /**
     * Search for the chamada corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChamadaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Chamadas for query {}", query);
        return chamadaSearchRepository.search(queryStringQuery(query), pageable)
            .map(chamadaMapper::toDto);
    }
}
