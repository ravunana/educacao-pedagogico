package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.Aula;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Aula} entity.
 */
public interface AulaSearchRepository extends ElasticsearchRepository<Aula, Long> {
}
