package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.CriterioAvaliacao;
import com.ravunana.educacao.pedagogico.repository.CriterioAvaliacaoRepository;
import com.ravunana.educacao.pedagogico.repository.search.CriterioAvaliacaoSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.CriterioAvaliacaoDTO;
import com.ravunana.educacao.pedagogico.service.mapper.CriterioAvaliacaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link CriterioAvaliacao}.
 */
@Service
@Transactional
public class CriterioAvaliacaoService {

    private final Logger log = LoggerFactory.getLogger(CriterioAvaliacaoService.class);

    private final CriterioAvaliacaoRepository criterioAvaliacaoRepository;

    private final CriterioAvaliacaoMapper criterioAvaliacaoMapper;

    private final CriterioAvaliacaoSearchRepository criterioAvaliacaoSearchRepository;

    public CriterioAvaliacaoService(CriterioAvaliacaoRepository criterioAvaliacaoRepository, CriterioAvaliacaoMapper criterioAvaliacaoMapper, CriterioAvaliacaoSearchRepository criterioAvaliacaoSearchRepository) {
        this.criterioAvaliacaoRepository = criterioAvaliacaoRepository;
        this.criterioAvaliacaoMapper = criterioAvaliacaoMapper;
        this.criterioAvaliacaoSearchRepository = criterioAvaliacaoSearchRepository;
    }

    /**
     * Save a criterioAvaliacao.
     *
     * @param criterioAvaliacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public CriterioAvaliacaoDTO save(CriterioAvaliacaoDTO criterioAvaliacaoDTO) {
        log.debug("Request to save CriterioAvaliacao : {}", criterioAvaliacaoDTO);
        CriterioAvaliacao criterioAvaliacao = criterioAvaliacaoMapper.toEntity(criterioAvaliacaoDTO);
        criterioAvaliacao = criterioAvaliacaoRepository.save(criterioAvaliacao);
        CriterioAvaliacaoDTO result = criterioAvaliacaoMapper.toDto(criterioAvaliacao);
        criterioAvaliacaoSearchRepository.save(criterioAvaliacao);
        return result;
    }

    /**
     * Get all the criterioAvaliacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CriterioAvaliacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CriterioAvaliacaos");
        return criterioAvaliacaoRepository.findAll(pageable)
            .map(criterioAvaliacaoMapper::toDto);
    }


    /**
     * Get one criterioAvaliacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CriterioAvaliacaoDTO> findOne(Long id) {
        log.debug("Request to get CriterioAvaliacao : {}", id);
        return criterioAvaliacaoRepository.findById(id)
            .map(criterioAvaliacaoMapper::toDto);
    }

    /**
     * Delete the criterioAvaliacao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CriterioAvaliacao : {}", id);
        criterioAvaliacaoRepository.deleteById(id);
        criterioAvaliacaoSearchRepository.deleteById(id);
    }

    /**
     * Search for the criterioAvaliacao corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CriterioAvaliacaoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CriterioAvaliacaos for query {}", query);
        return criterioAvaliacaoSearchRepository.search(queryStringQuery(query), pageable)
            .map(criterioAvaliacaoMapper::toDto);
    }
}
