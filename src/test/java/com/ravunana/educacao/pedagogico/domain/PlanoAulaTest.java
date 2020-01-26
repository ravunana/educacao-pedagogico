package com.ravunana.educacao.pedagogico.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class PlanoAulaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanoAula.class);
        PlanoAula planoAula1 = new PlanoAula();
        planoAula1.setId(1L);
        PlanoAula planoAula2 = new PlanoAula();
        planoAula2.setId(planoAula1.getId());
        assertThat(planoAula1).isEqualTo(planoAula2);
        planoAula2.setId(2L);
        assertThat(planoAula1).isNotEqualTo(planoAula2);
        planoAula1.setId(null);
        assertThat(planoAula1).isNotEqualTo(planoAula2);
    }
}
