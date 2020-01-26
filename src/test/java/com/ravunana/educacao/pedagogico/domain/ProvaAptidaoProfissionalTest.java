package com.ravunana.educacao.pedagogico.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class ProvaAptidaoProfissionalTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProvaAptidaoProfissional.class);
        ProvaAptidaoProfissional provaAptidaoProfissional1 = new ProvaAptidaoProfissional();
        provaAptidaoProfissional1.setId(1L);
        ProvaAptidaoProfissional provaAptidaoProfissional2 = new ProvaAptidaoProfissional();
        provaAptidaoProfissional2.setId(provaAptidaoProfissional1.getId());
        assertThat(provaAptidaoProfissional1).isEqualTo(provaAptidaoProfissional2);
        provaAptidaoProfissional2.setId(2L);
        assertThat(provaAptidaoProfissional1).isNotEqualTo(provaAptidaoProfissional2);
        provaAptidaoProfissional1.setId(null);
        assertThat(provaAptidaoProfissional1).isNotEqualTo(provaAptidaoProfissional2);
    }
}
