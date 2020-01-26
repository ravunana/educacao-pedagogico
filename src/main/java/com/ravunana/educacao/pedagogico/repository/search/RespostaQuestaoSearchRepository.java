package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.RespostaQuestao;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link RespostaQuestao} entity.
 */
public interface RespostaQuestaoSearchRepository extends ElasticsearchRepository<RespostaQuestao, Long> {
}
