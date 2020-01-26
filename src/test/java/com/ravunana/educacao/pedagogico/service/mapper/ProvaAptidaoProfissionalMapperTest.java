package com.ravunana.educacao.pedagogico.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ProvaAptidaoProfissionalMapperTest {

    private ProvaAptidaoProfissionalMapper provaAptidaoProfissionalMapper;

    @BeforeEach
    public void setUp() {
        provaAptidaoProfissionalMapper = new ProvaAptidaoProfissionalMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(provaAptidaoProfissionalMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(provaAptidaoProfissionalMapper.fromId(null)).isNull();
    }
}
