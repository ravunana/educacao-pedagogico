package com.ravunana.educacao.pedagogico.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class TurmaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TurmaDTO.class);
        TurmaDTO turmaDTO1 = new TurmaDTO();
        turmaDTO1.setId(1L);
        TurmaDTO turmaDTO2 = new TurmaDTO();
        assertThat(turmaDTO1).isNotEqualTo(turmaDTO2);
        turmaDTO2.setId(turmaDTO1.getId());
        assertThat(turmaDTO1).isEqualTo(turmaDTO2);
        turmaDTO2.setId(2L);
        assertThat(turmaDTO1).isNotEqualTo(turmaDTO2);
        turmaDTO1.setId(null);
        assertThat(turmaDTO1).isNotEqualTo(turmaDTO2);
    }
}
