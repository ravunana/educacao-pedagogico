package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.ProvaAptidaoProfissional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ProvaAptidaoProfissional} entity.
 */
public interface ProvaAptidaoProfissionalSearchRepository extends ElasticsearchRepository<ProvaAptidaoProfissional, Long> {
}
