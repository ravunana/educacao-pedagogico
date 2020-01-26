package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.Professor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Professor} entity.
 */
public interface ProfessorSearchRepository extends ElasticsearchRepository<Professor, Long> {
}
