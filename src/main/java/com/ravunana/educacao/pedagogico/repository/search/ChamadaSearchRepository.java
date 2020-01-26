package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.Chamada;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Chamada} entity.
 */
public interface ChamadaSearchRepository extends ElasticsearchRepository<Chamada, Long> {
}
