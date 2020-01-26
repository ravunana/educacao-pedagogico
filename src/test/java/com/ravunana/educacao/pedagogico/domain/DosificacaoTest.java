package com.ravunana.educacao.pedagogico.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class DosificacaoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dosificacao.class);
        Dosificacao dosificacao1 = new Dosificacao();
        dosificacao1.setId(1L);
        Dosificacao dosificacao2 = new Dosificacao();
        dosificacao2.setId(dosificacao1.getId());
        assertThat(dosificacao1).isEqualTo(dosificacao2);
        dosificacao2.setId(2L);
        assertThat(dosificacao1).isNotEqualTo(dosificacao2);
        dosificacao1.setId(null);
        assertThat(dosificacao1).isNotEqualTo(dosificacao2);
    }
}
