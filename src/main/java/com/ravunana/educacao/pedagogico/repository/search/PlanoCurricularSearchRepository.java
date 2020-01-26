package com.ravunana.educacao.pedagogico.repository.search;

import com.ravunana.educacao.pedagogico.domain.PlanoCurricular;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PlanoCurricular} entity.
 */
public interface PlanoCurricularSearchRepository extends ElasticsearchRepository<PlanoCurricular, Long> {
}
