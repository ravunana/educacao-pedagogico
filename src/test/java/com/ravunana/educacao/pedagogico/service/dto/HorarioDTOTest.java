package com.ravunana.educacao.pedagogico.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class HorarioDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HorarioDTO.class);
        HorarioDTO horarioDTO1 = new HorarioDTO();
        horarioDTO1.setId(1L);
        HorarioDTO horarioDTO2 = new HorarioDTO();
        assertThat(horarioDTO1).isNotEqualTo(horarioDTO2);
        horarioDTO2.setId(horarioDTO1.getId());
        assertThat(horarioDTO1).isEqualTo(horarioDTO2);
        horarioDTO2.setId(2L);
        assertThat(horarioDTO1).isNotEqualTo(horarioDTO2);
        horarioDTO1.setId(null);
        assertThat(horarioDTO1).isNotEqualTo(horarioDTO2);
    }
}
