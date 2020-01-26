package com.ravunana.educacao.pedagogico.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class PlanoAulaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanoAulaDTO.class);
        PlanoAulaDTO planoAulaDTO1 = new PlanoAulaDTO();
        planoAulaDTO1.setId(1L);
        PlanoAulaDTO planoAulaDTO2 = new PlanoAulaDTO();
        assertThat(planoAulaDTO1).isNotEqualTo(planoAulaDTO2);
        planoAulaDTO2.setId(planoAulaDTO1.getId());
        assertThat(planoAulaDTO1).isEqualTo(planoAulaDTO2);
        planoAulaDTO2.setId(2L);
        assertThat(planoAulaDTO1).isNotEqualTo(planoAulaDTO2);
        planoAulaDTO1.setId(null);
        assertThat(planoAulaDTO1).isNotEqualTo(planoAulaDTO2);
    }
}
