package com.ravunana.educacao.pedagogico.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ChamadaMapperTest {

    private ChamadaMapper chamadaMapper;

    @BeforeEach
    public void setUp() {
        chamadaMapper = new ChamadaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(chamadaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(chamadaMapper.fromId(null)).isNull();
    }
}
