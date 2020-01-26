package com.ravunana.educacao.pedagogico.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class TurmaMapperTest {

    private TurmaMapper turmaMapper;

    @BeforeEach
    public void setUp() {
        turmaMapper = new TurmaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(turmaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(turmaMapper.fromId(null)).isNull();
    }
}
