package com.ravunana.educacao.pedagogico.repository;

import com.ravunana.educacao.pedagogico.domain.TesteConhecimento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TesteConhecimento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TesteConhecimentoRepository extends JpaRepository<TesteConhecimento, Long> {

}
