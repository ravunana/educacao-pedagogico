package com.ravunana.educacao.pedagogico.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class PlanoActividadeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanoActividade.class);
        PlanoActividade planoActividade1 = new PlanoActividade();
        planoActividade1.setId(1L);
        PlanoActividade planoActividade2 = new PlanoActividade();
        planoActividade2.setId(planoActividade1.getId());
        assertThat(planoActividade1).isEqualTo(planoActividade2);
        planoActividade2.setId(2L);
        assertThat(planoActividade1).isNotEqualTo(planoActividade2);
        planoActividade1.setId(null);
        assertThat(planoActividade1).isNotEqualTo(planoActividade2);
    }
}
