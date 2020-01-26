package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.Horario;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Horario} entity.
 */
public interface HorarioSearchRepository extends ElasticsearchRepository<Horario, Long> {
}
