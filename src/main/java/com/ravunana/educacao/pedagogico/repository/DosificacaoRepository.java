package com.ravunana.educacao.pedagogico.repository;

import com.ravunana.educacao.pedagogico.domain.Dosificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Dosificacao entity.
 */
@Repository
public interface DosificacaoRepository extends JpaRepository<Dosificacao, Long>, JpaSpecificationExecutor<Dosificacao> {

    @Query(value = "select distinct dosificacao from Dosificacao dosificacao left join fetch dosificacao.cursos",
        countQuery = "select count(distinct dosificacao) from Dosificacao dosificacao")
    Page<Dosificacao> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct dosificacao from Dosificacao dosificacao left join fetch dosificacao.cursos")
    List<Dosificacao> findAllWithEagerRelationships();

    @Query("select dosificacao from Dosificacao dosificacao left join fetch dosificacao.cursos where dosificacao.id =:id")
    Optional<Dosificacao> findOneWithEagerRelationships(@Param("id") Long id);

}
