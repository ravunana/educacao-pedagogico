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

import com.ravunana.educacao.pedagogico.domain.PlanoAula;
import com.ravunana.educacao.pedagogico.domain.*; // for static metamodels
import com.ravunana.educacao.pedagogico.repository.PlanoAulaRepository;
import com.ravunana.educacao.pedagogico.repository.search.PlanoAulaSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.PlanoAulaCriteria;
import com.ravunana.educacao.pedagogico.service.dto.PlanoAulaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.PlanoAulaMapper;

/**
 * Service for executing complex queries for {@link PlanoAula} entities in the database.
 * The main input is a {@link PlanoAulaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlanoAulaDTO} or a {@link Page} of {@link PlanoAulaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlanoAulaQueryService extends QueryService<PlanoAula> {

    private final Logger log = LoggerFactory.getLogger(PlanoAulaQueryService.class);

    private final PlanoAulaRepository planoAulaRepository;

    private final PlanoAulaMapper planoAulaMapper;

    private final PlanoAulaSearchRepository planoAulaSearchRepository;

    public PlanoAulaQueryService(PlanoAulaRepository planoAulaRepository, PlanoAulaMapper planoAulaMapper, PlanoAulaSearchRepository planoAulaSearchRepository) {
        this.planoAulaRepository = planoAulaRepository;
        this.planoAulaMapper = planoAulaMapper;
        this.planoAulaSearchRepository = planoAulaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PlanoAulaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlanoAulaDTO> findByCriteria(PlanoAulaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PlanoAula> specification = createSpecification(criteria);
        return planoAulaMapper.toDto(planoAulaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlanoAulaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanoAulaDTO> findByCriteria(PlanoAulaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PlanoAula> specification = createSpecification(criteria);
        return planoAulaRepository.findAll(specification, page)
            .map(planoAulaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlanoAulaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PlanoAula> specification = createSpecification(criteria);
        return planoAulaRepository.count(specification);
    }

    /**
     * Function to convert {@link PlanoAulaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PlanoAula> createSpecification(PlanoAulaCriteria criteria) {
        Specification<PlanoAula> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PlanoAula_.id));
            }
            if (criteria.getTempo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTempo(), PlanoAula_.tempo));
            }
            if (criteria.getTurmaId() != null) {
                specification = specification.and(buildSpecification(criteria.getTurmaId(),
                    root -> root.join(PlanoAula_.turmas, JoinType.LEFT).get(Turma_.id)));
            }
            if (criteria.getDosificacaoId() != null) {
                specification = specification.and(buildSpecification(criteria.getDosificacaoId(),
                    root -> root.join(PlanoAula_.dosificacao, JoinType.LEFT).get(Dosificacao_.id)));
            }
            if (criteria.getProfessorId() != null) {
                specification = specification.and(buildSpecification(criteria.getProfessorId(),
                    root -> root.join(PlanoAula_.professor, JoinType.LEFT).get(Professor_.id)));
            }
            if (criteria.getCurriculoId() != null) {
                specification = specification.and(buildSpecification(criteria.getCurriculoId(),
                    root -> root.join(PlanoAula_.curriculo, JoinType.LEFT).get(PlanoCurricular_.id)));
            }
            if (criteria.getAulaPlanoAulaId() != null) {
                specification = specification.and(buildSpecification(criteria.getAulaPlanoAulaId(),
                    root -> root.join(PlanoAula_.aulaPlanoAulas, JoinType.LEFT).get(Aula_.id)));
            }
        }
        return specification;
    }
}
