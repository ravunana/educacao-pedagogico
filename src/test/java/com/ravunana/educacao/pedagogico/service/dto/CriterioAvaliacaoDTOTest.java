package com.ravunana.educacao.pedagogico.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class CriterioAvaliacaoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CriterioAvaliacaoDTO.class);
        CriterioAvaliacaoDTO criterioAvaliacaoDTO1 = new CriterioAvaliacaoDTO();
        criterioAvaliacaoDTO1.setId(1L);
        CriterioAvaliacaoDTO criterioAvaliacaoDTO2 = new CriterioAvaliacaoDTO();
        assertThat(criterioAvaliacaoDTO1).isNotEqualTo(criterioAvaliacaoDTO2);
        criterioAvaliacaoDTO2.setId(criterioAvaliacaoDTO1.getId());
        assertThat(criterioAvaliacaoDTO1).isEqualTo(criterioAvaliacaoDTO2);
        criterioAvaliacaoDTO2.setId(2L);
        assertThat(criterioAvaliacaoDTO1).isNotEqualTo(criterioAvaliacaoDTO2);
        criterioAvaliacaoDTO1.setId(null);
        assertThat(criterioAvaliacaoDTO1).isNotEqualTo(criterioAvaliacaoDTO2);
    }
}
