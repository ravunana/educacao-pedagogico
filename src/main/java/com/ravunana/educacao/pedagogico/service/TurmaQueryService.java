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

import com.ravunana.educacao.pedagogico.domain.Turma;
import com.ravunana.educacao.pedagogico.domain.*; // for static metamodels
import com.ravunana.educacao.pedagogico.repository.TurmaRepository;
import com.ravunana.educacao.pedagogico.repository.search.TurmaSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.TurmaCriteria;
import com.ravunana.educacao.pedagogico.service.dto.TurmaDTO;
import com.ravunana.educacao.pedagogico.service.mapper.TurmaMapper;

/**
 * Service for executing complex queries for {@link Turma} entities in the database.
 * The main input is a {@link TurmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TurmaDTO} or a {@link Page} of {@link TurmaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TurmaQueryService extends QueryService<Turma> {

    private final Logger log = LoggerFactory.getLogger(TurmaQueryService.class);

    private final TurmaRepository turmaRepository;

    private final TurmaMapper turmaMapper;

    private final TurmaSearchRepository turmaSearchRepository;

    public TurmaQueryService(TurmaRepository turmaRepository, TurmaMapper turmaMapper, TurmaSearchRepository turmaSearchRepository) {
        this.turmaRepository = turmaRepository;
        this.turmaMapper = turmaMapper;
        this.turmaSearchRepository = turmaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TurmaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TurmaDTO> findByCriteria(TurmaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Turma> specification = createSpecification(criteria);
        return turmaMapper.toDto(turmaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TurmaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TurmaDTO> findByCriteria(TurmaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Turma> specification = createSpecification(criteria);
        return turmaRepository.findAll(specification, page)
            .map(turmaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TurmaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Turma> specification = createSpecification(criteria);
        return turmaRepository.count(specification);
    }

    /**
     * Function to convert {@link TurmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Turma> createSpecification(TurmaCriteria criteria) {
        Specification<Turma> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Turma_.id));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Turma_.descricao));
            }
            if (criteria.getAnoLectivo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAnoLectivo(), Turma_.anoLectivo));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getData(), Turma_.data));
            }
            if (criteria.getAbertura() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAbertura(), Turma_.abertura));
            }
            if (criteria.getEncerramento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEncerramento(), Turma_.encerramento));
            }
            if (criteria.getLotacao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLotacao(), Turma_.lotacao));
            }
            if (criteria.getAberta() != null) {
                specification = specification.and(buildSpecification(criteria.getAberta(), Turma_.aberta));
            }
            if (criteria.getPeriodoLectivo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeriodoLectivo(), Turma_.periodoLectivo));
            }
            if (criteria.getTurno() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTurno(), Turma_.turno));
            }
            if (criteria.getSala() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSala(), Turma_.sala));
            }
            if (criteria.getClasse() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClasse(), Turma_.classe));
            }
            if (criteria.getHorarioId() != null) {
                specification = specification.and(buildSpecification(criteria.getHorarioId(),
                    root -> root.join(Turma_.horarios, JoinType.LEFT).get(Horario_.id)));
            }
            if (criteria.getPlanoActividadeId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanoActividadeId(),
                    root -> root.join(Turma_.planoActividades, JoinType.LEFT).get(PlanoActividade_.id)));
            }
            if (criteria.getNotaId() != null) {
                specification = specification.and(buildSpecification(criteria.getNotaId(),
                    root -> root.join(Turma_.notas, JoinType.LEFT).get(Nota_.id)));
            }
            if (criteria.getAulaId() != null) {
                specification = specification.and(buildSpecification(criteria.getAulaId(),
                    root -> root.join(Turma_.aulas, JoinType.LEFT).get(Aula_.id)));
            }
            if (criteria.getTesteConhecimentoId() != null) {
                specification = specification.and(buildSpecification(criteria.getTesteConhecimentoId(),
                    root -> root.join(Turma_.testeConhecimentos, JoinType.LEFT).get(TesteConhecimento_.id)));
            }
            if (criteria.getCursoId() != null) {
                specification = specification.and(buildSpecification(criteria.getCursoId(),
                    root -> root.join(Turma_.curso, JoinType.LEFT).get(Curso_.id)));
            }
            if (criteria.getCoordenadorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCoordenadorId(),
                    root -> root.join(Turma_.coordenador, JoinType.LEFT).get(Professor_.id)));
            }
            if (criteria.getPlanoAulaTurmaId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanoAulaTurmaId(),
                    root -> root.join(Turma_.planoAulaTurmas, JoinType.LEFT).get(PlanoAula_.id)));
            }
        }
        return specification;
    }
}
