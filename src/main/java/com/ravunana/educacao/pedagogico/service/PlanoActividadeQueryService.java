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

import com.ravunana.educacao.pedagogico.domain.PlanoActividade;
import com.ravunana.educacao.pedagogico.domain.*; // for static metamodels
import com.ravunana.educacao.pedagogico.repository.PlanoActividadeRepository;
import com.ravunana.educacao.pedagogico.repository.search.PlanoActividadeSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.PlanoActividadeCriteria;
import com.ravunana.educacao.pedagogico.service.dto.PlanoActividadeDTO;
import com.ravunana.educacao.pedagogico.service.mapper.PlanoActividadeMapper;

/**
 * Service for executing complex queries for {@link PlanoActividade} entities in the database.
 * The main input is a {@link PlanoActividadeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlanoActividadeDTO} or a {@link Page} of {@link PlanoActividadeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlanoActividadeQueryService extends QueryService<PlanoActividade> {

    private final Logger log = LoggerFactory.getLogger(PlanoActividadeQueryService.class);

    private final PlanoActividadeRepository planoActividadeRepository;

    private final PlanoActividadeMapper planoActividadeMapper;

    private final PlanoActividadeSearchRepository planoActividadeSearchRepository;

    public PlanoActividadeQueryService(PlanoActividadeRepository planoActividadeRepository, PlanoActividadeMapper planoActividadeMapper, PlanoActividadeSearchRepository planoActividadeSearchRepository) {
        this.planoActividadeRepository = planoActividadeRepository;
        this.planoActividadeMapper = planoActividadeMapper;
        this.planoActividadeSearchRepository = planoActividadeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PlanoActividadeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlanoActividadeDTO> findByCriteria(PlanoActividadeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PlanoActividade> specification = createSpecification(criteria);
        return planoActividadeMapper.toDto(planoActividadeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlanoActividadeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanoActividadeDTO> findByCriteria(PlanoActividadeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PlanoActividade> specification = createSpecification(criteria);
        return planoActividadeRepository.findAll(specification, page)
            .map(planoActividadeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlanoActividadeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PlanoActividade> specification = createSpecification(criteria);
        return planoActividadeRepository.count(specification);
    }

    /**
     * Function to convert {@link PlanoActividadeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PlanoActividade> createSpecification(PlanoActividadeCriteria criteria) {
        Specification<PlanoActividade> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PlanoActividade_.id));
            }
            if (criteria.getNumeroActividade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumeroActividade(), PlanoActividade_.numeroActividade));
            }
            if (criteria.getAtividade() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAtividade(), PlanoActividade_.atividade));
            }
            if (criteria.getDe() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDe(), PlanoActividade_.de));
            }
            if (criteria.getAte() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAte(), PlanoActividade_.ate));
            }
            if (criteria.getResponsavel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResponsavel(), PlanoActividade_.responsavel));
            }
            if (criteria.getLocal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocal(), PlanoActividade_.local));
            }
            if (criteria.getParticipantes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getParticipantes(), PlanoActividade_.participantes));
            }
            if (criteria.getCoResponsavel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoResponsavel(), PlanoActividade_.coResponsavel));
            }
            if (criteria.getAnoLectivo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAnoLectivo(), PlanoActividade_.anoLectivo));
            }
            if (criteria.getStatusActividade() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatusActividade(), PlanoActividade_.statusActividade));
            }
            if (criteria.getPeriodoLectivo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeriodoLectivo(), PlanoActividade_.periodoLectivo));
            }
            if (criteria.getTurno() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTurno(), PlanoActividade_.turno));
            }
            if (criteria.getClasse() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClasse(), PlanoActividade_.classe));
            }
            if (criteria.getCursoId() != null) {
                specification = specification.and(buildSpecification(criteria.getCursoId(),
                    root -> root.join(PlanoActividade_.curso, JoinType.LEFT).get(Curso_.id)));
            }
            if (criteria.getTurmaId() != null) {
                specification = specification.and(buildSpecification(criteria.getTurmaId(),
                    root -> root.join(PlanoActividade_.turma, JoinType.LEFT).get(Turma_.id)));
            }
        }
        return specification;
    }
}
