package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.QuestaoTeste;
import com.ravunana.educacao.pedagogico.repository.QuestaoTesteRepository;
import com.ravunana.educacao.pedagogico.repository.search.QuestaoTesteSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.QuestaoTesteDTO;
import com.ravunana.educacao.pedagogico.service.mapper.QuestaoTesteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link QuestaoTeste}.
 */
@Service
@Transactional
public class QuestaoTesteService {

    private final Logger log = LoggerFactory.getLogger(QuestaoTesteService.class);

    private final QuestaoTesteRepository questaoTesteRepository;

    private final QuestaoTesteMapper questaoTesteMapper;

    private final QuestaoTesteSearchRepository questaoTesteSearchRepository;

    public QuestaoTesteService(QuestaoTesteRepository questaoTesteRepository, QuestaoTesteMapper questaoTesteMapper, QuestaoTesteSearchRepository questaoTesteSearchRepository) {
        this.questaoTesteRepository = questaoTesteRepository;
        this.questaoTesteMapper = questaoTesteMapper;
        this.questaoTesteSearchRepository = questaoTesteSearchRepository;
    }

    /**
     * Save a questaoTeste.
     *
     * @param questaoTesteDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestaoTesteDTO save(QuestaoTesteDTO questaoTesteDTO) {
        log.debug("Request to save QuestaoTeste : {}", questaoTesteDTO);
        QuestaoTeste questaoTeste = questaoTesteMapper.toEntity(questaoTesteDTO);
        questaoTeste = questaoTesteRepository.save(questaoTeste);
        QuestaoTesteDTO result = questaoTesteMapper.toDto(questaoTeste);
        questaoTesteSearchRepository.save(questaoTeste);
        return result;
    }

    /**
     * Get all the questaoTestes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestaoTesteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuestaoTestes");
        return questaoTesteRepository.findAll(pageable)
            .map(questaoTesteMapper::toDto);
    }


    /**
     * Get one questaoTeste by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestaoTesteDTO> findOne(Long id) {
        log.debug("Request to get QuestaoTeste : {}", id);
        return questaoTesteRepository.findById(id)
            .map(questaoTesteMapper::toDto);
    }

    /**
     * Delete the questaoTeste by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuestaoTeste : {}", id);
        questaoTesteRepository.deleteById(id);
        questaoTesteSearchRepository.deleteById(id);
    }

    /**
     * Search for the questaoTeste corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestaoTesteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of QuestaoTestes for query {}", query);
        return questaoTesteSearchRepository.search(queryStringQuery(query), pageable)
            .map(questaoTesteMapper::toDto);
    }
}
