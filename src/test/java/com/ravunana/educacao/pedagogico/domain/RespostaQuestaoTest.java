package com.ravunana.educacao.pedagogico.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class RespostaQuestaoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RespostaQuestao.class);
        RespostaQuestao respostaQuestao1 = new RespostaQuestao();
        respostaQuestao1.setId(1L);
        RespostaQuestao respostaQuestao2 = new RespostaQuestao();
        respostaQuestao2.setId(respostaQuestao1.getId());
        assertThat(respostaQuestao1).isEqualTo(respostaQuestao2);
        respostaQuestao2.setId(2L);
        assertThat(respostaQuestao1).isNotEqualTo(respostaQuestao2);
        respostaQuestao1.setId(null);
        assertThat(respostaQuestao1).isNotEqualTo(respostaQuestao2);
    }
}
