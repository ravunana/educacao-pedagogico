package com.ravunana.educacao.pedagogico.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class CriterioAvaliacaoMapperTest {

    private CriterioAvaliacaoMapper criterioAvaliacaoMapper;

    @BeforeEach
    public void setUp() {
        criterioAvaliacaoMapper = new CriterioAvaliacaoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(criterioAvaliacaoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(criterioAvaliacaoMapper.fromId(null)).isNull();
    }
}
