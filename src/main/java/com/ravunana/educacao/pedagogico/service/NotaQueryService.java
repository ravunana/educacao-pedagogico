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

import com.ravunana.educacao.pedagogico.domain.Nota;
import com.ravunana.educacao.pedagogico.domain.*; // for static metamodels
import com.ravunana.educacao.pedagogico.repository.NotaRepository;
import com.ravunana.educacao.pedagogico.repository.search.NotaSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.NotaCriteria;
import com.ravunana.educacao.pedagogico.service.dto.NotaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.NotaMapper;

/**
 * Service for executing complex queries for {@link Nota} entities in the database.
 * The main input is a {@link NotaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NotaDTO} or a {@link Page} of {@link NotaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotaQueryService extends QueryService<Nota> {

    private final Logger log = LoggerFactory.getLogger(NotaQueryService.class);

    private final NotaRepository notaRepository;

    private final NotaMapper notaMapper;

    private final NotaSearchRepository notaSearchRepository;

    public NotaQueryService(NotaRepository notaRepository, NotaMapper notaMapper, NotaSearchRepository notaSearchRepository) {
        this.notaRepository = notaRepository;
        this.notaMapper = notaMapper;
        this.notaSearchRepository = notaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link NotaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NotaDTO> findByCriteria(NotaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Nota> specification = createSpecification(criteria);
        return notaMapper.toDto(notaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NotaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotaDTO> findByCriteria(NotaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Nota> specification = createSpecification(criteria);
        return notaRepository.findAll(specification, page)
            .map(notaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Nota> specification = createSpecification(criteria);
        return notaRepository.count(specification);
    }

    /**
     * Function to convert {@link NotaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Nota> createSpecification(NotaCriteria criteria) {
        Specification<Nota> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Nota_.id));
            }
            if (criteria.getNumeroProcesso() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroProcesso(), Nota_.numeroProcesso));
            }
            if (criteria.getNomeAluno() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomeAluno(), Nota_.nomeAluno));
            }
            if (criteria.getDisciplina() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisciplina(), Nota_.disciplina));
            }
            if (criteria.getPeridoLectivo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeridoLectivo(), Nota_.peridoLectivo));
            }
            if (criteria.getAnoLectivo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAnoLectivo(), Nota_.anoLectivo));
            }
            if (criteria.getFaltaJustificada() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFaltaJustificada(), Nota_.faltaJustificada));
            }
            if (criteria.getFaltaInjustificada() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFaltaInjustificada(), Nota_.faltaInjustificada));
            }
            if (criteria.getAvaliacaoContinuca() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAvaliacaoContinuca(), Nota_.avaliacaoContinuca));
            }
            if (criteria.getPrimeiraProva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrimeiraProva(), Nota_.primeiraProva));
            }
            if (criteria.getSegundaProva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSegundaProva(), Nota_.segundaProva));
            }
            if (criteria.getExame() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExame(), Nota_.exame));
            }
            if (criteria.getRecurso() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRecurso(), Nota_.recurso));
            }
            if (criteria.getExameEspecial() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExameEspecial(), Nota_.exameEspecial));
            }
            if (criteria.getSituacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSituacao(), Nota_.situacao));
            }
            if (criteria.getTurmaId() != null) {
                specification = specification.and(buildSpecification(criteria.getTurmaId(),
                    root -> root.join(Nota_.turma, JoinType.LEFT).get(Turma_.id)));
            }
            if (criteria.getCurriculoId() != null) {
                specification = specification.and(buildSpecification(criteria.getCurriculoId(),
                    root -> root.join(Nota_.curriculo, JoinType.LEFT).get(PlanoCurricular_.id)));
            }
            if (criteria.getProfessorId() != null) {
                specification = specification.and(buildSpecification(criteria.getProfessorId(),
                    root -> root.join(Nota_.professor, JoinType.LEFT).get(Professor_.id)));
            }
        }
        return specification;
    }
}
