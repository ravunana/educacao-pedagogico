package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.Nota;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Nota} entity.
 */
public interface NotaSearchRepository extends ElasticsearchRepository<Nota, Long> {
}
