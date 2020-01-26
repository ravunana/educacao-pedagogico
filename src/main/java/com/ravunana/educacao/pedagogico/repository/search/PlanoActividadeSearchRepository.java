package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.PlanoActividade;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PlanoActividade} entity.
 */
public interface PlanoActividadeSearchRepository extends ElasticsearchRepository<PlanoActividade, Long> {
}
