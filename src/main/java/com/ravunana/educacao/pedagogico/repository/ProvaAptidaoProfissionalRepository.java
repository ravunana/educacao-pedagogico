package com.ravunana.educacao.pedagogico.repository;

import com.ravunana.educacao.pedagogico.domain.ProvaAptidaoProfissional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProvaAptidaoProfissional entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProvaAptidaoProfissionalRepository extends JpaRepository<ProvaAptidaoProfissional, Long> {

}
