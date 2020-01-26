package com.ravunana.educacao.pedagogico.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class RespostaQuestaoMapperTest {

    private RespostaQuestaoMapper respostaQuestaoMapper;

    @BeforeEach
    public void setUp() {
        respostaQuestaoMapper = new RespostaQuestaoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(respostaQuestaoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(respostaQuestaoMapper.fromId(null)).isNull();
    }
}
