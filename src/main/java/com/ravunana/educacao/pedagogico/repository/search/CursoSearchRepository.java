package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.Curso;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Curso} entity.
 */
public interface CursoSearchRepository extends ElasticsearchRepository<Curso, Long> {
}
