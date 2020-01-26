package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.TesteConhecimento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TesteConhecimento} entity.
 */
public interface TesteConhecimentoSearchRepository extends ElasticsearchRepository<TesteConhecimento, Long> {
}
