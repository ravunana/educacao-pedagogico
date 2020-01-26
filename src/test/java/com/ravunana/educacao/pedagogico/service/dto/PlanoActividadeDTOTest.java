package com.ravunana.educacao.pedagogico.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class PlanoActividadeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanoActividadeDTO.class);
        PlanoActividadeDTO planoActividadeDTO1 = new PlanoActividadeDTO();
        planoActividadeDTO1.setId(1L);
        PlanoActividadeDTO planoActividadeDTO2 = new PlanoActividadeDTO();
        assertThat(planoActividadeDTO1).isNotEqualTo(planoActividadeDTO2);
        planoActividadeDTO2.setId(planoActividadeDTO1.getId());
        assertThat(planoActividadeDTO1).isEqualTo(planoActividadeDTO2);
        planoActividadeDTO2.setId(2L);
        assertThat(planoActividadeDTO1).isNotEqualTo(planoActividadeDTO2);
        planoActividadeDTO1.setId(null);
        assertThat(planoActividadeDTO1).isNotEqualTo(planoActividadeDTO2);
    }
}
