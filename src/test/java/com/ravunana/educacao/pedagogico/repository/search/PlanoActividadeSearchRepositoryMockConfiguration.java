package com.ravunana.educacao.pedagogico.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PlanoActividadeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PlanoActividadeSearchRepositoryMockConfiguration {

    @MockBean
    private PlanoActividadeSearchRepository mockPlanoActividadeSearchRepository;

}
