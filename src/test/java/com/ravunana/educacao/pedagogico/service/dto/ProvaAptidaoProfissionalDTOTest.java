package com.ravunana.educacao.pedagogico.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class ProvaAptidaoProfissionalDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProvaAptidaoProfissionalDTO.class);
        ProvaAptidaoProfissionalDTO provaAptidaoProfissionalDTO1 = new ProvaAptidaoProfissionalDTO();
        provaAptidaoProfissionalDTO1.setId(1L);
        ProvaAptidaoProfissionalDTO provaAptidaoProfissionalDTO2 = new ProvaAptidaoProfissionalDTO();
        assertThat(provaAptidaoProfissionalDTO1).isNotEqualTo(provaAptidaoProfissionalDTO2);
        provaAptidaoProfissionalDTO2.setId(provaAptidaoProfissionalDTO1.getId());
        assertThat(provaAptidaoProfissionalDTO1).isEqualTo(provaAptidaoProfissionalDTO2);
        provaAptidaoProfissionalDTO2.setId(2L);
        assertThat(provaAptidaoProfissionalDTO1).isNotEqualTo(provaAptidaoProfissionalDTO2);
        provaAptidaoProfissionalDTO1.setId(null);
        assertThat(provaAptidaoProfissionalDTO1).isNotEqualTo(provaAptidaoProfissionalDTO2);
    }
}
