package com.ravunana.educacao.pedagogico.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class PlanoAulaMapperTest {

    private PlanoAulaMapper planoAulaMapper;

    @BeforeEach
    public void setUp() {
        planoAulaMapper = new PlanoAulaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(planoAulaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(planoAulaMapper.fromId(null)).isNull();
    }
}
