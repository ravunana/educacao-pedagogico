package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.repository.TurmaRepository;
import com.ravunana.educacao.pedagogico.repository.search.TurmaSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.TurmaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.TurmaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Turma}.
 */
@Service
@Transactional
public class TurmaService {

    private final Logger log = LoggerFactory.getLogger(TurmaService.class);

    private final TurmaRepository turmaRepository;

    private final TurmaMapper turmaMapper;

    private final TurmaSearchRepository turmaSearchRepository;

    public TurmaService(TurmaRepository turmaRepository, TurmaMapper turmaMapper, TurmaSearchRepository turmaSearchRepository) {
        this.turmaRepository = turmaRepository;
        this.turmaMapper = turmaMapper;
        this.turmaSearchRepository = turmaSearchRepository;
    }

    /**
     * Save a turma.
     *
     * @param turmaDTO the entity to save.
     * @return the persisted entity.
     */
    public TurmaDTO save(TurmaDTO turmaDTO) {
        log.debug("Request to save Turma : {}", turmaDTO);
        Turma turma = turmaMapper.toEntity(turmaDTO);
        turma = turmaRepository.save(turma);
        TurmaDTO result = turmaMapper.toDto(turma);
        turmaSearchRepository.save(turma);
        return result;
    }

    /**
     * Get all the turmas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TurmaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Turmas");
        return turmaRepository.findAll(pageable)
            .map(turmaMapper::toDto);
    }


    /**
     * Get one turma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TurmaDTO> findOne(Long id) {
        log.debug("Request to get Turma : {}", id);
        return turmaRepository.findById(id)
            .map(turmaMapper::toDto);
    }

    /**
     * Delete the turma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Turma : {}", id);
        turmaRepository.deleteById(id);
        turmaSearchRepository.deleteById(id);
    }

    /**
     * Search for the turma corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TurmaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Turmas for query {}", query);
        return turmaSearchRepository.search(queryStringQuery(query), pageable)
            .map(turmaMapper::toDto);
    }
}
