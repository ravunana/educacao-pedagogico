package com.ravunana.educacao.pedagogico.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class DosificacaoMapperTest {

    private DosificacaoMapper dosificacaoMapper;

    @BeforeEach
    public void setUp() {
        dosificacaoMapper = new DosificacaoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(dosificacaoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(dosificacaoMapper.fromId(null)).isNull();
    }
}
