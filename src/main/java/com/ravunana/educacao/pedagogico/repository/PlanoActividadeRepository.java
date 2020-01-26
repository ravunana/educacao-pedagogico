package com.ravunana.educacao.pedagogico.repository;

import com.ravunana.educacao.pedagogico.domain.PlanoActividade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PlanoActividade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanoActividadeRepository extends JpaRepository<PlanoActividade, Long> {

}
