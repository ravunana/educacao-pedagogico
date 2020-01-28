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

import com.ravunana.educacao.pedagogico.domain.Dosificacao;
import com.ravunana.educacao.pedagogico.domain.*; // for static metamodels
import com.ravunana.educacao.pedagogico.repository.DosificacaoRepository;
import com.ravunana.educacao.pedagogico.repository.search.DosificacaoSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.DosificacaoCriteria;
import com.ravunana.educacao.pedagogico.service.dto.DosificacaoDTO;
import com.ravunana.educacao.pedagogico.service.mapper.DosificacaoMapper;

/**
 * Service for executing complex queries for {@link Dosificacao} entities in the database.
 * The main input is a {@link DosificacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DosificacaoDTO} or a {@link Page} of {@link DosificacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DosificacaoQueryService extends QueryService<Dosificacao> {

    private final Logger log = LoggerFactory.getLogger(DosificacaoQueryService.class);

    private final DosificacaoRepository dosificacaoRepository;

    private final DosificacaoMapper dosificacaoMapper;

    private final DosificacaoSearchRepository dosificacaoSearchRepository;

    public DosificacaoQueryService(DosificacaoRepository dosificacaoRepository, DosificacaoMapper dosificacaoMapper, DosificacaoSearchRepository dosificacaoSearchRepository) {
        this.dosificacaoRepository = dosificacaoRepository;
        this.dosificacaoMapper = dosificacaoMapper;
        this.dosificacaoSearchRepository = dosificacaoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DosificacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DosificacaoDTO> findByCriteria(DosificacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Dosificacao> specification = createSpecification(criteria);
        return dosificacaoMapper.toDto(dosificacaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DosificacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DosificacaoDTO> findByCriteria(DosificacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Dosificacao> specification = createSpecification(criteria);
        return dosificacaoRepository.findAll(specification, page)
            .map(dosificacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DosificacaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Dosificacao> specification = createSpecification(criteria);
        return dosificacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link DosificacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Dosificacao> createSpecification(DosificacaoCriteria criteria) {
        Specification<Dosificacao> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Dosificacao_.id));
            }
            if (criteria.getPeridoLective() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeridoLective(), Dosificacao_.peridoLective));
            }
            if (criteria.getSemanaLectiva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSemanaLectiva(), Dosificacao_.semanaLectiva));
            }
            if (criteria.getDe() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDe(), Dosificacao_.de));
            }
            if (criteria.getAte() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAte(), Dosificacao_.ate));
            }
            if (criteria.getUnidadeTematica() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnidadeTematica(), Dosificacao_.unidadeTematica));
            }
            if (criteria.getTempoAula() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTempoAula(), Dosificacao_.tempoAula));
            }
            if (criteria.getFormaAvaliacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFormaAvaliacao(), Dosificacao_.formaAvaliacao));
            }
            if (criteria.getPlanoAulaId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanoAulaId(),
                    root -> root.join(Dosificacao_.planoAulas, JoinType.LEFT).get(PlanoAula_.id)));
            }
            if (criteria.getCursoId() != null) {
                specification = specification.and(buildSpecification(criteria.getCursoId(),
                    root -> root.join(Dosificacao_.cursos, JoinType.LEFT).get(Curso_.id)));
            }
            if (criteria.getCurriuloId() != null) {
                specification = specification.and(buildSpecification(criteria.getCurriuloId(),
                    root -> root.join(Dosificacao_.curriulo, JoinType.LEFT).get(PlanoCurricular_.id)));
            }
        }
        return specification;
    }
}
