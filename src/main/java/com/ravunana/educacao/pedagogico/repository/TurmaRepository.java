package com.ravunana.educacao.pedagogico.repository;

import com.ravunana.educacao.pedagogico.domain.Turma;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Turma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long>, JpaSpecificationExecutor<Turma> {

}
