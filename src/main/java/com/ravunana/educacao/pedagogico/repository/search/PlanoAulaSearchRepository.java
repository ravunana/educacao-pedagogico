package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.PlanoAula;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PlanoAula} entity.
 */
public interface PlanoAulaSearchRepository extends ElasticsearchRepository<PlanoAula, Long> {
}
