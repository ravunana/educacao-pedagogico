package com.ravunana.educacao.pedagogico.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class NotaMapperTest {

    private NotaMapper notaMapper;

    @BeforeEach
    public void setUp() {
        notaMapper = new NotaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(notaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(notaMapper.fromId(null)).isNull();
    }
}
