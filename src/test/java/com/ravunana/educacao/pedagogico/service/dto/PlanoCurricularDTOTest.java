package com.ravunana.educacao.pedagogico.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class PlanoCurricularDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanoCurricularDTO.class);
        PlanoCurricularDTO planoCurricularDTO1 = new PlanoCurricularDTO();
        planoCurricularDTO1.setId(1L);
        PlanoCurricularDTO planoCurricularDTO2 = new PlanoCurricularDTO();
        assertThat(planoCurricularDTO1).isNotEqualTo(planoCurricularDTO2);
        planoCurricularDTO2.setId(planoCurricularDTO1.getId());
        assertThat(planoCurricularDTO1).isEqualTo(planoCurricularDTO2);
        planoCurricularDTO2.setId(2L);
        assertThat(planoCurricularDTO1).isNotEqualTo(planoCurricularDTO2);
        planoCurricularDTO1.setId(null);
        assertThat(planoCurricularDTO1).isNotEqualTo(planoCurricularDTO2);
    }
}
