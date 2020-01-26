package com.ravunana.educacao.pedagogico.repository;

import com.ravunana.educacao.pedagogico.domain.CriterioAvaliacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CriterioAvaliacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CriterioAvaliacaoRepository extends JpaRepository<CriterioAvaliacao, Long> {

}
