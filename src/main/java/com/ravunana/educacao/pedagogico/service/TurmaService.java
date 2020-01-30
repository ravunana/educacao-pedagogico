package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.Curso;
import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.repository.CursoRepository;
import com.ravunana.educacao.pedagogico.repository.TurmaRepository;
import com.ravunana.educacao.pedagogico.repository.search.TurmaSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.TurmaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.TurmaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
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

    @Autowired
    private CursoRepository cursoRepository;

    public TurmaService(final TurmaRepository turmaRepository, final TurmaMapper turmaMapper,
            final TurmaSearchRepository turmaSearchRepository) {
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
    public TurmaDTO save(final TurmaDTO turmaDTO) {
        log.debug("Request to save Turma : {}", turmaDTO);
        final int anoLectivo = ZonedDateTime.now().getYear();
        turmaDTO.setAnoLectivo( anoLectivo );
        turmaDTO.setData(ZonedDateTime.now());
        turmaDTO.setAberta( true );
        turmaDTO.setDescricao("");
        // this.getDescricaoTurma(turmaDTO.getCursoId(), turmaDTO.getClasse(), turmaDTO.getSala(), anoLectivo);
        Turma turma = turmaMapper.toEntity(turmaDTO);
        turma = turmaRepository.save(turma);
        final TurmaDTO result = turmaMapper.toDto(turma);
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
    public Page<TurmaDTO> findAll(final Pageable pageable) {
        log.debug("Request to get all Turmas");
        return turmaRepository.findAll(pageable).map(turmaMapper::toDto);
    }

    /**
     * Get one turma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TurmaDTO> findOne(final Long id) {
        log.debug("Request to get Turma : {}", id);
        return turmaRepository.findById(id).map(turmaMapper::toDto);
    }

    /**
     * Delete the turma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(final Long id) {
        log.debug("Request to delete Turma : {}", id);
        turmaRepository.deleteById(id);
        turmaSearchRepository.deleteById(id);
    }

    /**
     * Search for the turma corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TurmaDTO> search(final String query, final Pageable pageable) {
        log.debug("Request to search for a page of Turmas for query {}", query);
        return turmaSearchRepository.search(queryStringQuery(query), pageable).map(turmaMapper::toDto);
    }

    private String getDescricaoTurma(final Long cursoId, final Integer classe, final Integer sala, final String turno,
            final int anoLectivo) {
        final Curso curso = cursoRepository.findById(cursoId).get();
        final String areaFormacaoSub = curso.getAreaFormacao().substring(0, 1);
        final String siglaCurso = curso.getSigla();
        final String turnoSub = turno.substring(0, 1);

        return areaFormacaoSub + siglaCurso + classe + "ª" + "." + sala + turnoSub + "/" + anoLectivo;
    }
}
