package com.ravunana.educacao.pedagogico.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class PlanoActividadeMapperTest {

    private PlanoActividadeMapper planoActividadeMapper;

    @BeforeEach
    public void setUp() {
        planoActividadeMapper = new PlanoActividadeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(planoActividadeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(planoActividadeMapper.fromId(null)).isNull();
    }
}
