package com.ravunana.educacao.pedagogico.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ProvaAptidaoProfissionalSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ProvaAptidaoProfissionalSearchRepositoryMockConfiguration {

    @MockBean
    private ProvaAptidaoProfissionalSearchRepository mockProvaAptidaoProfissionalSearchRepository;

}
