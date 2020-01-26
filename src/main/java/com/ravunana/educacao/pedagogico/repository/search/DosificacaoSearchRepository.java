package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.Dosificacao;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Dosificacao} entity.
 */
public interface DosificacaoSearchRepository extends ElasticsearchRepository<Dosificacao, Long> {
}
