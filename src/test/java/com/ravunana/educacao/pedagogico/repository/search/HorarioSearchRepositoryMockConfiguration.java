package com.ravunana.educacao.pedagogico.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link HorarioSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class HorarioSearchRepositoryMockConfiguration {

    @MockBean
    private HorarioSearchRepository mockHorarioSearchRepository;

}
