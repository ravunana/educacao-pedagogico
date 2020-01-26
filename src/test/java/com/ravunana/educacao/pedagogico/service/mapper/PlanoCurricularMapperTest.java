package com.ravunana.educacao.pedagogico.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class PlanoCurricularMapperTest {

    private PlanoCurricularMapper planoCurricularMapper;

    @BeforeEach
    public void setUp() {
        planoCurricularMapper = new PlanoCurricularMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(planoCurricularMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(planoCurricularMapper.fromId(null)).isNull();
    }
}
