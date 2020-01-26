package com.ravunana.educacao.pedagogico.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class AulaMapperTest {

    private AulaMapper aulaMapper;

    @BeforeEach
    public void setUp() {
        aulaMapper = new AulaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(aulaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(aulaMapper.fromId(null)).isNull();
    }
}
