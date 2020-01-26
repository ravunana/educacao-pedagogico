package com.ravunana.educacao.pedagogico.repository;

import com.ravunana.educacao.pedagogico.domain.QuestaoTeste;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the QuestaoTeste entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestaoTesteRepository extends JpaRepository<QuestaoTeste, Long> {

}
