package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.Nota;
import com.ravunana.educacao.pedagogico.repository.NotaRepository;
import com.ravunana.educacao.pedagogico.repository.search.NotaSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.NotaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.NotaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Nota}.
 */
@Service
@Transactional
public class NotaService {

    private final Logger log = LoggerFactory.getLogger(NotaService.class);

    private final NotaRepository notaRepository;

    private final NotaMapper notaMapper;

    private final NotaSearchRepository notaSearchRepository;

    public NotaService(NotaRepository notaRepository, NotaMapper notaMapper, NotaSearchRepository notaSearchRepository) {
        this.notaRepository = notaRepository;
        this.notaMapper = notaMapper;
        this.notaSearchRepository = notaSearchRepository;
    }

    /**
     * Save a nota.
     *
     * @param notaDTO the entity to save.
     * @return the persisted entity.
     */
    public NotaDTO save(NotaDTO notaDTO) {
        log.debug("Request to save Nota : {}", notaDTO);
        Nota nota = notaMapper.toEntity(notaDTO);
        nota = notaRepository.save(nota);
        NotaDTO result = notaMapper.toDto(nota);
        notaSearchRepository.save(nota);
        return result;
    }

    /**
     * Get all the notas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NotaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notas");
        return notaRepository.findAll(pageable)
            .map(notaMapper::toDto);
    }


    /**
     * Get one nota by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotaDTO> findOne(Long id) {
        log.debug("Request to get Nota : {}", id);
        return notaRepository.findById(id)
            .map(notaMapper::toDto);
    }

    /**
     * Delete the nota by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Nota : {}", id);
        notaRepository.deleteById(id);
        notaSearchRepository.deleteById(id);
    }

    /**
     * Search for the nota corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NotaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Notas for query {}", query);
        return notaSearchRepository.search(queryStringQuery(query), pageable)
            .map(notaMapper::toDto);
    }
}
