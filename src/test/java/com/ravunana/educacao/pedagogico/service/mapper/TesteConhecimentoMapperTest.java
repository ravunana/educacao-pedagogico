package com.ravunana.educacao.pedagogico.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class TesteConhecimentoMapperTest {

    private TesteConhecimentoMapper testeConhecimentoMapper;

    @BeforeEach
    public void setUp() {
        testeConhecimentoMapper = new TesteConhecimentoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(testeConhecimentoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(testeConhecimentoMapper.fromId(null)).isNull();
    }
}
