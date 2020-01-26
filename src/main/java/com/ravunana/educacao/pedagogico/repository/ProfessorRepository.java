package com.ravunana.educacao.pedagogico.repository;

import com.ravunana.educacao.pedagogico.domain.Professor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Professor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

}
