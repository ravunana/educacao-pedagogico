package com.ravunana.educacao.pedagogico.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class QuestaoTesteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestaoTeste.class);
        QuestaoTeste questaoTeste1 = new QuestaoTeste();
        questaoTeste1.setId(1L);
        QuestaoTeste questaoTeste2 = new QuestaoTeste();
        questaoTeste2.setId(questaoTeste1.getId());
        assertThat(questaoTeste1).isEqualTo(questaoTeste2);
        questaoTeste2.setId(2L);
        assertThat(questaoTeste1).isNotEqualTo(questaoTeste2);
        questaoTeste1.setId(null);
        assertThat(questaoTeste1).isNotEqualTo(questaoTeste2);
    }
}
