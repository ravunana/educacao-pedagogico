package com.ravunana.educacao.pedagogico.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.ravunana.educacao.pedagogico.domain.Horario;
import com.ravunana.educacao.pedagogico.domain.*; // for static metamodels
import com.ravunana.educacao.pedagogico.repository.HorarioRepository;
import com.ravunana.educacao.pedagogico.repository.search.HorarioSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.HorarioCriteria;
import com.ravunana.educacao.pedagogico.service.dto.HorarioDTO;
import com.ravunana.educacao.pedagogico.service.mapper.HorarioMapper;

/**
 * Service for executing complex queries for {@link Horario} entities in the database.
 * The main input is a {@link HorarioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HorarioDTO} or a {@link Page} of {@link HorarioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HorarioQueryService extends QueryService<Horario> {

    private final Logger log = LoggerFactory.getLogger(HorarioQueryService.class);

    private final HorarioRepository horarioRepository;

    private final HorarioMapper horarioMapper;

    private final HorarioSearchRepository horarioSearchRepository;

    public HorarioQueryService(HorarioRepository horarioRepository, HorarioMapper horarioMapper, HorarioSearchRepository horarioSearchRepository) {
        this.horarioRepository = horarioRepository;
        this.horarioMapper = horarioMapper;
        this.horarioSearchRepository = horarioSearchRepository;
    }

    /**
     * Return a {@link List} of {@link HorarioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HorarioDTO> findByCriteria(HorarioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Horario> specification = createSpecification(criteria);
        return horarioMapper.toDto(horarioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HorarioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HorarioDTO> findByCriteria(HorarioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Horario> specification = createSpecification(criteria);
        return horarioRepository.findAll(specification, page)
            .map(horarioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HorarioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Horario> specification = createSpecification(criteria);
        return horarioRepository.count(specification);
    }

    /**
     * Function to convert {@link HorarioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Horario> createSpecification(HorarioCriteria criteria) {
        Specification<Horario> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Horario_.id));
            }
            if (criteria.getInicio() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInicio(), Horario_.inicio));
            }
            if (criteria.getFim() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFim(), Horario_.fim));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getData(), Horario_.data));
            }
            if (criteria.getAnoLectivo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAnoLectivo(), Horario_.anoLectivo));
            }
            if (criteria.getDiaSemana() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDiaSemana(), Horario_.diaSemana));
            }
            if (criteria.getCategoria() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategoria(), Horario_.categoria));
            }
            if (criteria.getTurmaId() != null) {
                specification = specification.and(buildSpecification(criteria.getTurmaId(),
                    root -> root.join(Horario_.turma, JoinType.LEFT).get(Turma_.id)));
            }
            if (criteria.getProfessorId() != null) {
                specification = specification.and(buildSpecification(criteria.getProfessorId(),
                    root -> root.join(Horario_.professor, JoinType.LEFT).get(Professor_.id)));
            }
            if (criteria.getCurriculoId() != null) {
                specification = specification.and(buildSpecification(criteria.getCurriculoId(),
                    root -> root.join(Horario_.curriculo, JoinType.LEFT).get(PlanoCurricular_.id)));
            }
        }
        return specification;
    }
}
