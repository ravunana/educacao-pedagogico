package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.Horario;
import com.ravunana.educacao.pedagogico.repository.HorarioRepository;
import com.ravunana.educacao.pedagogico.repository.search.HorarioSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.HorarioDTO;
import com.ravunana.educacao.pedagogico.service.mapper.HorarioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Horario}.
 */
@Service
@Transactional
public class HorarioService {

    private final Logger log = LoggerFactory.getLogger(HorarioService.class);

    private final HorarioRepository horarioRepository;

    private final HorarioMapper horarioMapper;

    private final HorarioSearchRepository horarioSearchRepository;

    public HorarioService(HorarioRepository horarioRepository, HorarioMapper horarioMapper, HorarioSearchRepository horarioSearchRepository) {
        this.horarioRepository = horarioRepository;
        this.horarioMapper = horarioMapper;
        this.horarioSearchRepository = horarioSearchRepository;
    }

    /**
     * Save a horario.
     *
     * @param horarioDTO the entity to save.
     * @return the persisted entity.
     */
    public HorarioDTO save(HorarioDTO horarioDTO) {
        log.debug("Request to save Horario : {}", horarioDTO);
        Horario horario = horarioMapper.toEntity(horarioDTO);
        horario = horarioRepository.save(horario);
        HorarioDTO result = horarioMapper.toDto(horario);
        horarioSearchRepository.save(horario);
        return result;
    }

    /**
     * Get all the horarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HorarioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Horarios");
        return horarioRepository.findAll(pageable)
            .map(horarioMapper::toDto);
    }


    /**
     * Get one horario by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HorarioDTO> findOne(Long id) {
        log.debug("Request to get Horario : {}", id);
        return horarioRepository.findById(id)
            .map(horarioMapper::toDto);
    }

    /**
     * Delete the horario by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Horario : {}", id);
        horarioRepository.deleteById(id);
        horarioSearchRepository.deleteById(id);
    }

    /**
     * Search for the horario corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HorarioDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Horarios for query {}", query);
        return horarioSearchRepository.search(queryStringQuery(query), pageable)
            .map(horarioMapper::toDto);
    }
}
