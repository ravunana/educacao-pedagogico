package com.ravunana.educacao.pedagogico.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class CriterioAvaliacaoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CriterioAvaliacao.class);
        CriterioAvaliacao criterioAvaliacao1 = new CriterioAvaliacao();
        criterioAvaliacao1.setId(1L);
        CriterioAvaliacao criterioAvaliacao2 = new CriterioAvaliacao();
        criterioAvaliacao2.setId(criterioAvaliacao1.getId());
        assertThat(criterioAvaliacao1).isEqualTo(criterioAvaliacao2);
        criterioAvaliacao2.setId(2L);
        assertThat(criterioAvaliacao1).isNotEqualTo(criterioAvaliacao2);
        criterioAvaliacao1.setId(null);
        assertThat(criterioAvaliacao1).isNotEqualTo(criterioAvaliacao2);
    }
}
