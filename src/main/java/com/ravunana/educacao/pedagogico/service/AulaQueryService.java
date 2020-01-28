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

import com.ravunana.educacao.pedagogico.domain.Aula;
import com.ravunana.educacao.pedagogico.domain.*; // for static metamodels
import com.ravunana.educacao.pedagogico.repository.AulaRepository;
import com.ravunana.educacao.pedagogico.repository.search.AulaSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.AulaCriteria;
import com.ravunana.educacao.pedagogico.service.dto.AulaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.AulaMapper;

/**
 * Service for executing complex queries for {@link Aula} entities in the database.
 * The main input is a {@link AulaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AulaDTO} or a {@link Page} of {@link AulaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AulaQueryService extends QueryService<Aula> {

    private final Logger log = LoggerFactory.getLogger(AulaQueryService.class);

    private final AulaRepository aulaRepository;

    private final AulaMapper aulaMapper;

    private final AulaSearchRepository aulaSearchRepository;

    public AulaQueryService(AulaRepository aulaRepository, AulaMapper aulaMapper, AulaSearchRepository aulaSearchRepository) {
        this.aulaRepository = aulaRepository;
        this.aulaMapper = aulaMapper;
        this.aulaSearchRepository = aulaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AulaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AulaDTO> findByCriteria(AulaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Aula> specification = createSpecification(criteria);
        return aulaMapper.toDto(aulaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AulaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AulaDTO> findByCriteria(AulaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Aula> specification = createSpecification(criteria);
        return aulaRepository.findAll(specification, page)
            .map(aulaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AulaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Aula> specification = createSpecification(criteria);
        return aulaRepository.count(specification);
    }

    /**
     * Function to convert {@link AulaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Aula> createSpecification(AulaCriteria criteria) {
        Specification<Aula> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Aula_.id));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getData(), Aula_.data));
            }
            if (criteria.getSumario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSumario(), Aula_.sumario));
            }
            if (criteria.getLicao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLicao(), Aula_.licao));
            }
            if (criteria.getDada() != null) {
                specification = specification.and(buildSpecification(criteria.getDada(), Aula_.dada));
            }
            if (criteria.getChamadaId() != null) {
                specification = specification.and(buildSpecification(criteria.getChamadaId(),
                    root -> root.join(Aula_.chamadas, JoinType.LEFT).get(Chamada_.id)));
            }
            if (criteria.getPlanoAulaId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanoAulaId(),
                    root -> root.join(Aula_.planoAulas, JoinType.LEFT).get(PlanoAula_.id)));
            }
            if (criteria.getTurmaId() != null) {
                specification = specification.and(buildSpecification(criteria.getTurmaId(),
                    root -> root.join(Aula_.turma, JoinType.LEFT).get(Turma_.id)));
            }
            if (criteria.getCurriuloId() != null) {
                specification = specification.and(buildSpecification(criteria.getCurriuloId(),
                    root -> root.join(Aula_.curriulo, JoinType.LEFT).get(PlanoCurricular_.id)));
            }
        }
        return specification;
    }
}
