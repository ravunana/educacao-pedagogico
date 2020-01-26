package com.ravunana.educacao.pedagogico.repository;

import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PlanoCurricular entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanoCurricularRepository extends JpaRepository<PlanoCurricular, Long> {

}
