package com.ravunana.educacao.pedagogico.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ravunana.educacao.pedagogico.web.rest.TestUtil;

public class NotaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotaDTO.class);
        NotaDTO notaDTO1 = new NotaDTO();
        notaDTO1.setId(1L);
        NotaDTO notaDTO2 = new NotaDTO();
        assertThat(notaDTO1).isNotEqualTo(notaDTO2);
        notaDTO2.setId(notaDTO1.getId());
        assertThat(notaDTO1).isEqualTo(notaDTO2);
        notaDTO2.setId(2L);
        assertThat(notaDTO1).isNotEqualTo(notaDTO2);
        notaDTO1.setId(null);
        assertThat(notaDTO1).isNotEqualTo(notaDTO2);
    }
}
