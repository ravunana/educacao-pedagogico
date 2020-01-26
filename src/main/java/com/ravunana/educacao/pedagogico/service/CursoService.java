package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.Curso;
import com.ravunana.educacao.pedagogico.repository.CursoRepository;
import com.ravunana.educacao.pedagogico.repository.search.CursoSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.CursoDTO;
import com.ravunana.educacao.pedagogico.service.mapper.CursoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Curso}.
 */
@Service
@Transactional
public class CursoService {

    private final Logger log = LoggerFactory.getLogger(CursoService.class);

    private final CursoRepository cursoRepository;

    private final CursoMapper cursoMapper;

    private final CursoSearchRepository cursoSearchRepository;

    public CursoService(CursoRepository cursoRepository, CursoMapper cursoMapper, CursoSearchRepository cursoSearchRepository) {
        this.cursoRepository = cursoRepository;
        this.cursoMapper = cursoMapper;
        this.cursoSearchRepository = cursoSearchRepository;
    }

    /**
     * Save a curso.
     *
     * @param cursoDTO the entity to save.
     * @return the persisted entity.
     */
    public CursoDTO save(CursoDTO cursoDTO) {
        log.debug("Request to save Curso : {}", cursoDTO);
        Curso curso = cursoMapper.toEntity(cursoDTO);
        curso = cursoRepository.save(curso);
        CursoDTO result = cursoMapper.toDto(curso);
        cursoSearchRepository.save(curso);
        return result;
    }

    /**
     * Get all the cursos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CursoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cursos");
        return cursoRepository.findAll(pageable)
            .map(cursoMapper::toDto);
    }


    /**
     * Get one curso by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CursoDTO> findOne(Long id) {
        log.debug("Request to get Curso : {}", id);
        return cursoRepository.findById(id)
            .map(cursoMapper::toDto);
    }

    /**
     * Delete the curso by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Curso : {}", id);
        cursoRepository.deleteById(id);
        cursoSearchRepository.deleteById(id);
    }

    /**
     * Search for the curso corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CursoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cursos for query {}", query);
        return cursoSearchRepository.search(queryStringQuery(query), pageable)
            .map(cursoMapper::toDto);
    }
}
