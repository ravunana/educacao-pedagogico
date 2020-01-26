package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.QuestaoTeste;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link QuestaoTeste} entity.
 */
public interface QuestaoTesteSearchRepository extends ElasticsearchRepository<QuestaoTeste, Long> {
}
