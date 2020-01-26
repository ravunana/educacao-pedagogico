package com.ravunana.educacao.pedagogico.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class ChamadaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChamadaDTO.class);
        ChamadaDTO chamadaDTO1 = new ChamadaDTO();
        chamadaDTO1.setId(1L);
        ChamadaDTO chamadaDTO2 = new ChamadaDTO();
        assertThat(chamadaDTO1).isNotEqualTo(chamadaDTO2);
        chamadaDTO2.setId(chamadaDTO1.getId());
        assertThat(chamadaDTO1).isEqualTo(chamadaDTO2);
        chamadaDTO2.setId(2L);
        assertThat(chamadaDTO1).isNotEqualTo(chamadaDTO2);
        chamadaDTO1.setId(null);
        assertThat(chamadaDTO1).isNotEqualTo(chamadaDTO2);
    }
}
