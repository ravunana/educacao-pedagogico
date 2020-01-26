package com.ravunana.educacao.pedagogico.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class AulaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aula.class);
        Aula aula1 = new Aula();
        aula1.setId(1L);
        Aula aula2 = new Aula();
        aula2.setId(aula1.getId());
        assertThat(aula1).isEqualTo(aula2);
        aula2.setId(2L);
        assertThat(aula1).isNotEqualTo(aula2);
        aula1.setId(null);
        assertThat(aula1).isNotEqualTo(aula2);
    }
}
