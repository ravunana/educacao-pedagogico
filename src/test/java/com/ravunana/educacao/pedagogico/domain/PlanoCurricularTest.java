package com.ravunana.educacao.pedagogico.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class PlanoCurricularTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanoCurricular.class);
        PlanoCurricular planoCurricular1 = new PlanoCurricular();
        planoCurricular1.setId(1L);
        PlanoCurricular planoCurricular2 = new PlanoCurricular();
        planoCurricular2.setId(planoCurricular1.getId());
        assertThat(planoCurricular1).isEqualTo(planoCurricular2);
        planoCurricular2.setId(2L);
        assertThat(planoCurricular1).isNotEqualTo(planoCurricular2);
        planoCurricular1.setId(null);
        assertThat(planoCurricular1).isNotEqualTo(planoCurricular2);
    }
}
