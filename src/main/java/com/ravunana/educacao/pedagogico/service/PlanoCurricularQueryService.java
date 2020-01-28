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

import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import com.ravunana.educacao.pedagogico.domain.*; // for static metamodels
import com.ravunana.educacao.pedagogico.repository.PlanoCurricularRepository;
import com.ravunana.educacao.pedagogico.repository.search.PlanoCurricularSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.PlanoCurricularCriteria;
import com.ravunana.educacao.pedagogico.service.dto.PlanoCurricularDTO;
import com.ravunana.educacao.pedagogico.service.mapper.PlanoCurricularMapper;

/**
 * Service for executing complex queries for {@link PlanoCurricular} entities in the database.
 * The main input is a {@link PlanoCurricularCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlanoCurricularDTO} or a {@link Page} of {@link PlanoCurricularDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlanoCurricularQueryService extends QueryService<PlanoCurricular> {

    private final Logger log = LoggerFactory.getLogger(PlanoCurricularQueryService.class);

    private final PlanoCurricularRepository planoCurricularRepository;

    private final PlanoCurricularMapper planoCurricularMapper;

    private final PlanoCurricularSearchRepository planoCurricularSearchRepository;

    public PlanoCurricularQueryService(PlanoCurricularRepository planoCurricularRepository, PlanoCurricularMapper planoCurricularMapper, PlanoCurricularSearchRepository planoCurricularSearchRepository) {
        this.planoCurricularRepository = planoCurricularRepository;
        this.planoCurricularMapper = planoCurricularMapper;
        this.planoCurricularSearchRepository = planoCurricularSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PlanoCurricularDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlanoCurricularDTO> findByCriteria(PlanoCurricularCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PlanoCurricular> specification = createSpecification(criteria);
        return planoCurricularMapper.toDto(planoCurricularRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlanoCurricularDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanoCurricularDTO> findByCriteria(PlanoCurricularCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PlanoCurricular> specification = createSpecification(criteria);
        return planoCurricularRepository.findAll(specification, page)
            .map(planoCurricularMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlanoCurricularCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PlanoCurricular> specification = createSpecification(criteria);
        return planoCurricularRepository.count(specification);
    }

    /**
     * Function to convert {@link PlanoCurricularCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PlanoCurricular> createSpecification(PlanoCurricularCriteria criteria) {
        Specification<PlanoCurricular> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PlanoCurricular_.id));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), PlanoCurricular_.descricao));
            }
            if (criteria.getTerminal() != null) {
                specification = specification.and(buildSpecification(criteria.getTerminal(), PlanoCurricular_.terminal));
            }
            if (criteria.getTempoSemanal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTempoSemanal(), PlanoCurricular_.tempoSemanal));
            }
            if (criteria.getPeriodoLectivo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeriodoLectivo(), PlanoCurricular_.periodoLectivo));
            }
            if (criteria.getComponente() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComponente(), PlanoCurricular_.componente));
            }
            if (criteria.getDisciplina() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisciplina(), PlanoCurricular_.disciplina));
            }
            if (criteria.getClasse() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClasse(), PlanoCurricular_.classe));
            }
            if (criteria.getPlanoAulaId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanoAulaId(),
                    root -> root.join(PlanoCurricular_.planoAulas, JoinType.LEFT).get(PlanoAula_.id)));
            }
            if (criteria.getDosificacaoId() != null) {
                specification = specification.and(buildSpecification(criteria.getDosificacaoId(),
                    root -> root.join(PlanoCurricular_.dosificacaos, JoinType.LEFT).get(Dosificacao_.id)));
            }
            if (criteria.getNotaId() != null) {
                specification = specification.and(buildSpecification(criteria.getNotaId(),
                    root -> root.join(PlanoCurricular_.notas, JoinType.LEFT).get(Nota_.id)));
            }
            if (criteria.getAulaId() != null) {
                specification = specification.and(buildSpecification(criteria.getAulaId(),
                    root -> root.join(PlanoCurricular_.aulas, JoinType.LEFT).get(Aula_.id)));
            }
            if (criteria.getHorarioId() != null) {
                specification = specification.and(buildSpecification(criteria.getHorarioId(),
                    root -> root.join(PlanoCurricular_.horarios, JoinType.LEFT).get(Horario_.id)));
            }
            if (criteria.getTesteConhecimentoId() != null) {
                specification = specification.and(buildSpecification(criteria.getTesteConhecimentoId(),
                    root -> root.join(PlanoCurricular_.testeConhecimentos, JoinType.LEFT).get(TesteConhecimento_.id)));
            }
            if (criteria.getCursoId() != null) {
                specification = specification.and(buildSpecification(criteria.getCursoId(),
                    root -> root.join(PlanoCurricular_.curso, JoinType.LEFT).get(Curso_.id)));
            }
        }
        return specification;
    }
}
