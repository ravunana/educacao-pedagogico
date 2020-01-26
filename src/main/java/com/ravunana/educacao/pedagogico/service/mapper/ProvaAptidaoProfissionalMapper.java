package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.ProvaAptidaoProfissionalDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProvaAptidaoProfissional} and its DTO {@link ProvaAptidaoProfissionalDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProvaAptidaoProfissionalMapper extends EntityMapper<ProvaAptidaoProfissionalDTO, ProvaAptidaoProfissional> {



    default ProvaAptidaoProfissional fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProvaAptidaoProfissional provaAptidaoProfissional = new ProvaAptidaoProfissional();
        provaAptidaoProfissional.setId(id);
        return provaAptidaoProfissional;
    }
}
