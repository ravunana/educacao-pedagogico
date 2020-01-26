package com.ravunana.educacao.pedagogico.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class QuestaoTesteMapperTest {

    private QuestaoTesteMapper questaoTesteMapper;

    @BeforeEach
    public void setUp() {
        questaoTesteMapper = new QuestaoTesteMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(questaoTesteMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(questaoTesteMapper.fromId(null)).isNull();
    }
}
