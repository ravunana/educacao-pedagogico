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

import com.ravunana.educacao.pedagogico.domain.Professor;
import com.ravunana.educacao.pedagogico.domain.*; // for static metamodels
import com.ravunana.educacao.pedagogico.repository.ProfessorRepository;
import com.ravunana.educacao.pedagogico.repository.search.ProfessorSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.ProfessorCriteria;
import com.ravunana.educacao.pedagogico.service.dto.ProfessorDTO;
import com.ravunana.educacao.pedagogico.service.mapper.ProfessorMapper;

/**
 * Service for executing complex queries for {@link Professor} entities in the database.
 * The main input is a {@link ProfessorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProfessorDTO} or a {@link Page} of {@link ProfessorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfessorQueryService extends QueryService<Professor> {

    private final Logger log = LoggerFactory.getLogger(ProfessorQueryService.class);

    private final ProfessorRepository professorRepository;

    private final ProfessorMapper professorMapper;

    private final ProfessorSearchRepository professorSearchRepository;

    public ProfessorQueryService(ProfessorRepository professorRepository, ProfessorMapper professorMapper, ProfessorSearchRepository professorSearchRepository) {
        this.professorRepository = professorRepository;
        this.professorMapper = professorMapper;
        this.professorSearchRepository = professorSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProfessorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProfessorDTO> findByCriteria(ProfessorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Professor> specification = createSpecification(criteria);
        return professorMapper.toDto(professorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProfessorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessorDTO> findByCriteria(ProfessorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Professor> specification = createSpecification(criteria);
        return professorRepository.findAll(specification, page)
            .map(professorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProfessorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Professor> specification = createSpecification(criteria);
        return professorRepository.count(specification);
    }

    /**
     * Function to convert {@link ProfessorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Professor> createSpecification(ProfessorCriteria criteria) {
        Specification<Professor> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Professor_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Professor_.nome));
            }
            if (criteria.getSexo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSexo(), Professor_.sexo));
            }
            if (criteria.getContacto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContacto(), Professor_.contacto));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Professor_.email));
            }
            if (criteria.getResidencia() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResidencia(), Professor_.residencia));
            }
            if (criteria.getNumeroAgente() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumeroAgente(), Professor_.numeroAgente));
            }
            if (criteria.getUtilizadorId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUtilizadorId(), Professor_.utilizadorId));
            }
            if (criteria.getGrauAcademico() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGrauAcademico(), Professor_.grauAcademico));
            }
            if (criteria.getCursoAcademico() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCursoAcademico(), Professor_.cursoAcademico));
            }
            if (criteria.getAtivo() != null) {
                specification = specification.and(buildSpecification(criteria.getAtivo(), Professor_.ativo));
            }
            if (criteria.getHorarioId() != null) {
                specification = specification.and(buildSpecification(criteria.getHorarioId(),
                    root -> root.join(Professor_.horarios, JoinType.LEFT).get(Horario_.id)));
            }
            if (criteria.getTurmaId() != null) {
                specification = specification.and(buildSpecification(criteria.getTurmaId(),
                    root -> root.join(Professor_.turmas, JoinType.LEFT).get(Turma_.id)));
            }
            if (criteria.getPlanoAulaId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanoAulaId(),
                    root -> root.join(Professor_.planoAulas, JoinType.LEFT).get(PlanoAula_.id)));
            }
            if (criteria.getNotaId() != null) {
                specification = specification.and(buildSpecification(criteria.getNotaId(),
                    root -> root.join(Professor_.notas, JoinType.LEFT).get(Nota_.id)));
            }
            if (criteria.getTesteConhecimentoId() != null) {
                specification = specification.and(buildSpecification(criteria.getTesteConhecimentoId(),
                    root -> root.join(Professor_.testeConhecimentos, JoinType.LEFT).get(TesteConhecimento_.id)));
            }
        }
        return specification;
    }
}
