package com.ravunana.educacao.pedagogico.repository;

import com.ravunana.educacao.pedagogico.domain.RespostaQuestao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RespostaQuestao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RespostaQuestaoRepository extends JpaRepository<RespostaQuestao, Long> {

}
