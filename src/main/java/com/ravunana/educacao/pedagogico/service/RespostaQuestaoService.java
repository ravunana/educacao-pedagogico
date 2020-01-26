package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.RespostaQuestao;
import com.ravunana.educacao.pedagogico.repository.RespostaQuestaoRepository;
import com.ravunana.educacao.pedagogico.repository.search.RespostaQuestaoSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.RespostaQuestaoDTO;
import com.ravunana.educacao.pedagogico.service.mapper.RespostaQuestaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link RespostaQuestao}.
 */
@Service
@Transactional
public class RespostaQuestaoService {

    private final Logger log = LoggerFactory.getLogger(RespostaQuestaoService.class);

    private final RespostaQuestaoRepository respostaQuestaoRepository;

    private final RespostaQuestaoMapper respostaQuestaoMapper;

    private final RespostaQuestaoSearchRepository respostaQuestaoSearchRepository;

    public RespostaQuestaoService(RespostaQuestaoRepository respostaQuestaoRepository, RespostaQuestaoMapper respostaQuestaoMapper, RespostaQuestaoSearchRepository respostaQuestaoSearchRepository) {
        this.respostaQuestaoRepository = respostaQuestaoRepository;
        this.respostaQuestaoMapper = respostaQuestaoMapper;
        this.respostaQuestaoSearchRepository = respostaQuestaoSearchRepository;
    }

    /**
     * Save a respostaQuestao.
     *
     * @param respostaQuestaoDTO the entity to save.
     * @return the persisted entity.
     */
    public RespostaQuestaoDTO save(RespostaQuestaoDTO respostaQuestaoDTO) {
        log.debug("Request to save RespostaQuestao : {}", respostaQuestaoDTO);
        RespostaQuestao respostaQuestao = respostaQuestaoMapper.toEntity(respostaQuestaoDTO);
        respostaQuestao = respostaQuestaoRepository.save(respostaQuestao);
        RespostaQuestaoDTO result = respostaQuestaoMapper.toDto(respostaQuestao);
        respostaQuestaoSearchRepository.save(respostaQuestao);
        return result;
    }

    /**
     * Get all the respostaQuestaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RespostaQuestaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RespostaQuestaos");
        return respostaQuestaoRepository.findAll(pageable)
            .map(respostaQuestaoMapper::toDto);
    }


    /**
     * Get one respostaQuestao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RespostaQuestaoDTO> findOne(Long id) {
        log.debug("Request to get RespostaQuestao : {}", id);
        return respostaQuestaoRepository.findById(id)
            .map(respostaQuestaoMapper::toDto);
    }

    /**
     * Delete the respostaQuestao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RespostaQuestao : {}", id);
        respostaQuestaoRepository.deleteById(id);
        respostaQuestaoSearchRepository.deleteById(id);
    }

    /**
     * Search for the respostaQuestao corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RespostaQuestaoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RespostaQuestaos for query {}", query);
        return respostaQuestaoSearchRepository.search(queryStringQuery(query), pageable)
            .map(respostaQuestaoMapper::toDto);
    }
}
