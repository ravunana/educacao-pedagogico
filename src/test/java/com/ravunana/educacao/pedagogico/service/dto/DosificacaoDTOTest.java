package com.ravunana.educacao.pedagogico.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class DosificacaoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DosificacaoDTO.class);
        DosificacaoDTO dosificacaoDTO1 = new DosificacaoDTO();
        dosificacaoDTO1.setId(1L);
        DosificacaoDTO dosificacaoDTO2 = new DosificacaoDTO();
        assertThat(dosificacaoDTO1).isNotEqualTo(dosificacaoDTO2);
        dosificacaoDTO2.setId(dosificacaoDTO1.getId());
        assertThat(dosificacaoDTO1).isEqualTo(dosificacaoDTO2);
        dosificacaoDTO2.setId(2L);
        assertThat(dosificacaoDTO1).isNotEqualTo(dosificacaoDTO2);
        dosificacaoDTO1.setId(null);
        assertThat(dosificacaoDTO1).isNotEqualTo(dosificacaoDTO2);
    }
}
