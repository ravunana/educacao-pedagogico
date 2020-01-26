package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.Professor;
import com.ravunana.educacao.pedagogico.repository.ProfessorRepository;
import com.ravunana.educacao.pedagogico.repository.search.ProfessorSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.ProfessorDTO;
import com.ravunana.educacao.pedagogico.service.mapper.ProfessorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Professor}.
 */
@Service
@Transactional
public class ProfessorService {

    private final Logger log = LoggerFactory.getLogger(ProfessorService.class);

    private final ProfessorRepository professorRepository;

    private final ProfessorMapper professorMapper;

    private final ProfessorSearchRepository professorSearchRepository;

    public ProfessorService(ProfessorRepository professorRepository, ProfessorMapper professorMapper, ProfessorSearchRepository professorSearchRepository) {
        this.professorRepository = professorRepository;
        this.professorMapper = professorMapper;
        this.professorSearchRepository = professorSearchRepository;
    }

    /**
     * Save a professor.
     *
     * @param professorDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessorDTO save(ProfessorDTO professorDTO) {
        log.debug("Request to save Professor : {}", professorDTO);
        Professor professor = professorMapper.toEntity(professorDTO);
        professor = professorRepository.save(professor);
        ProfessorDTO result = professorMapper.toDto(professor);
        professorSearchRepository.save(professor);
        return result;
    }

    /**
     * Get all the professors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Professors");
        return professorRepository.findAll(pageable)
            .map(professorMapper::toDto);
    }


    /**
     * Get one professor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessorDTO> findOne(Long id) {
        log.debug("Request to get Professor : {}", id);
        return professorRepository.findById(id)
            .map(professorMapper::toDto);
    }

    /**
     * Delete the professor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Professor : {}", id);
        professorRepository.deleteById(id);
        professorSearchRepository.deleteById(id);
    }

    /**
     * Search for the professor corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessorDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Professors for query {}", query);
        return professorSearchRepository.search(queryStringQuery(query), pageable)
            .map(professorMapper::toDto);
    }
}
