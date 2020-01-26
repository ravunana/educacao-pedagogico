package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.Dosificacao;
import com.ravunana.educacao.pedagogico.repository.DosificacaoRepository;
import com.ravunana.educacao.pedagogico.repository.search.DosificacaoSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.DosificacaoDTO;
import com.ravunana.educacao.pedagogico.service.mapper.DosificacaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Dosificacao}.
 */
@Service
@Transactional
public class DosificacaoService {

    private final Logger log = LoggerFactory.getLogger(DosificacaoService.class);

    private final DosificacaoRepository dosificacaoRepository;

    private final DosificacaoMapper dosificacaoMapper;

    private final DosificacaoSearchRepository dosificacaoSearchRepository;

    public DosificacaoService(DosificacaoRepository dosificacaoRepository, DosificacaoMapper dosificacaoMapper, DosificacaoSearchRepository dosificacaoSearchRepository) {
        this.dosificacaoRepository = dosificacaoRepository;
        this.dosificacaoMapper = dosificacaoMapper;
        this.dosificacaoSearchRepository = dosificacaoSearchRepository;
    }

    /**
     * Save a dosificacao.
     *
     * @param dosificacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public DosificacaoDTO save(DosificacaoDTO dosificacaoDTO) {
        log.debug("Request to save Dosificacao : {}", dosificacaoDTO);
        Dosificacao dosificacao = dosificacaoMapper.toEntity(dosificacaoDTO);
        dosificacao = dosificacaoRepository.save(dosificacao);
        DosificacaoDTO result = dosificacaoMapper.toDto(dosificacao);
        dosificacaoSearchRepository.save(dosificacao);
        return result;
    }

    /**
     * Get all the dosificacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DosificacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dosificacaos");
        return dosificacaoRepository.findAll(pageable)
            .map(dosificacaoMapper::toDto);
    }

    /**
     * Get all the dosificacaos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DosificacaoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return dosificacaoRepository.findAllWithEagerRelationships(pageable).map(dosificacaoMapper::toDto);
    }
    

    /**
     * Get one dosificacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DosificacaoDTO> findOne(Long id) {
        log.debug("Request to get Dosificacao : {}", id);
        return dosificacaoRepository.findOneWithEagerRelationships(id)
            .map(dosificacaoMapper::toDto);
    }

    /**
     * Delete the dosificacao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dosificacao : {}", id);
        dosificacaoRepository.deleteById(id);
        dosificacaoSearchRepository.deleteById(id);
    }

    /**
     * Search for the dosificacao corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DosificacaoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Dosificacaos for query {}", query);
        return dosificacaoSearchRepository.search(queryStringQuery(query), pageable)
            .map(dosificacaoMapper::toDto);
    }
}
