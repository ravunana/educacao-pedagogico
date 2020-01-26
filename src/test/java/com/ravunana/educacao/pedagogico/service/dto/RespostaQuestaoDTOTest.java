package com.ravunana.educacao.pedagogico.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class RespostaQuestaoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RespostaQuestaoDTO.class);
        RespostaQuestaoDTO respostaQuestaoDTO1 = new RespostaQuestaoDTO();
        respostaQuestaoDTO1.setId(1L);
        RespostaQuestaoDTO respostaQuestaoDTO2 = new RespostaQuestaoDTO();
        assertThat(respostaQuestaoDTO1).isNotEqualTo(respostaQuestaoDTO2);
        respostaQuestaoDTO2.setId(respostaQuestaoDTO1.getId());
        assertThat(respostaQuestaoDTO1).isEqualTo(respostaQuestaoDTO2);
        respostaQuestaoDTO2.setId(2L);
        assertThat(respostaQuestaoDTO1).isNotEqualTo(respostaQuestaoDTO2);
        respostaQuestaoDTO1.setId(null);
        assertThat(respostaQuestaoDTO1).isNotEqualTo(respostaQuestaoDTO2);
    }
}
