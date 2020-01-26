package com.ravunana.educacao.pedagogico.repository;

import com.ravunana.educacao.pedagogico.domain.Chamada;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Chamada entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChamadaRepository extends JpaRepository<Chamada, Long> {

}
