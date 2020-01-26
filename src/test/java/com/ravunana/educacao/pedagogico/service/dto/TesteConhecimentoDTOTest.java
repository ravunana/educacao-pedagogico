package com.ravunana.educacao.pedagogico.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class TesteConhecimentoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TesteConhecimentoDTO.class);
        TesteConhecimentoDTO testeConhecimentoDTO1 = new TesteConhecimentoDTO();
        testeConhecimentoDTO1.setId(1L);
        TesteConhecimentoDTO testeConhecimentoDTO2 = new TesteConhecimentoDTO();
        assertThat(testeConhecimentoDTO1).isNotEqualTo(testeConhecimentoDTO2);
        testeConhecimentoDTO2.setId(testeConhecimentoDTO1.getId());
        assertThat(testeConhecimentoDTO1).isEqualTo(testeConhecimentoDTO2);
        testeConhecimentoDTO2.setId(2L);
        assertThat(testeConhecimentoDTO1).isNotEqualTo(testeConhecimentoDTO2);
        testeConhecimentoDTO1.setId(null);
        assertThat(testeConhecimentoDTO1).isNotEqualTo(testeConhecimentoDTO2);
    }
}
