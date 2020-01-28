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

import com.ravunana.educacao.pedagogico.domain.Curso;
import com.ravunana.educacao.pedagogico.domain.*; // for static metamodels
import com.ravunana.educacao.pedagogico.repository.CursoRepository;
import com.ravunana.educacao.pedagogico.repository.search.CursoSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.CursoCriteria;
import com.ravunana.educacao.pedagogico.service.dto.CursoDTO;
import com.ravunana.educacao.pedagogico.service.mapper.CursoMapper;

/**
 * Service for executing complex queries for {@link Curso} entities in the database.
 * The main input is a {@link CursoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CursoDTO} or a {@link Page} of {@link CursoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CursoQueryService extends QueryService<Curso> {

    private final Logger log = LoggerFactory.getLogger(CursoQueryService.class);

    private final CursoRepository cursoRepository;

    private final CursoMapper cursoMapper;

    private final CursoSearchRepository cursoSearchRepository;

    public CursoQueryService(CursoRepository cursoRepository, CursoMapper cursoMapper, CursoSearchRepository cursoSearchRepository) {
        this.cursoRepository = cursoRepository;
        this.cursoMapper = cursoMapper;
        this.cursoSearchRepository = cursoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CursoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CursoDTO> findByCriteria(CursoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Curso> specification = createSpecification(criteria);
        return cursoMapper.toDto(cursoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CursoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CursoDTO> findByCriteria(CursoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Curso> specification = createSpecification(criteria);
        return cursoRepository.findAll(specification, page)
            .map(cursoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CursoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Curso> specification = createSpecification(criteria);
        return cursoRepository.count(specification);
    }

    /**
     * Function to convert {@link CursoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Curso> createSpecification(CursoCriteria criteria) {
        Specification<Curso> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Curso_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Curso_.nome));
            }
            if (criteria.getSigla() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSigla(), Curso_.sigla));
            }
            if (criteria.getAreaFormacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAreaFormacao(), Curso_.areaFormacao));
            }
            if (criteria.getPlanoCurricularId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanoCurricularId(),
                    root -> root.join(Curso_.planoCurriculars, JoinType.LEFT).get(PlanoCurricular_.id)));
            }
            if (criteria.getTurmaId() != null) {
                specification = specification.and(buildSpecification(criteria.getTurmaId(),
                    root -> root.join(Curso_.turmas, JoinType.LEFT).get(Turma_.id)));
            }
            if (criteria.getPlanoActividadeId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanoActividadeId(),
                    root -> root.join(Curso_.planoActividades, JoinType.LEFT).get(PlanoActividade_.id)));
            }
            if (criteria.getDosificacaoCursoId() != null) {
                specification = specification.and(buildSpecification(criteria.getDosificacaoCursoId(),
                    root -> root.join(Curso_.dosificacaoCursos, JoinType.LEFT).get(Dosificacao_.id)));
            }
        }
        return specification;
    }
}
