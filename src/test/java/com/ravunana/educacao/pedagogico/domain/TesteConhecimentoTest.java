package com.ravunana.educacao.pedagogico.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class TesteConhecimentoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TesteConhecimento.class);
        TesteConhecimento testeConhecimento1 = new TesteConhecimento();
        testeConhecimento1.setId(1L);
        TesteConhecimento testeConhecimento2 = new TesteConhecimento();
        testeConhecimento2.setId(testeConhecimento1.getId());
        assertThat(testeConhecimento1).isEqualTo(testeConhecimento2);
        testeConhecimento2.setId(2L);
        assertThat(testeConhecimento1).isNotEqualTo(testeConhecimento2);
        testeConhecimento1.setId(null);
        assertThat(testeConhecimento1).isNotEqualTo(testeConhecimento2);
    }
}
