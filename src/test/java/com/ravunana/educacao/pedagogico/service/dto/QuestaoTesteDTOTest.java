package com.ravunana.educacao.pedagogico.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class QuestaoTesteDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestaoTesteDTO.class);
        QuestaoTesteDTO questaoTesteDTO1 = new QuestaoTesteDTO();
        questaoTesteDTO1.setId(1L);
        QuestaoTesteDTO questaoTesteDTO2 = new QuestaoTesteDTO();
        assertThat(questaoTesteDTO1).isNotEqualTo(questaoTesteDTO2);
        questaoTesteDTO2.setId(questaoTesteDTO1.getId());
        assertThat(questaoTesteDTO1).isEqualTo(questaoTesteDTO2);
        questaoTesteDTO2.setId(2L);
        assertThat(questaoTesteDTO1).isNotEqualTo(questaoTesteDTO2);
        questaoTesteDTO1.setId(null);
        assertThat(questaoTesteDTO1).isNotEqualTo(questaoTesteDTO2);
    }
}
