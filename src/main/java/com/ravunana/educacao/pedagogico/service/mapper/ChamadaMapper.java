package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.ChamadaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Chamada} and its DTO {@link ChamadaDTO}.
 */
@Mapper(componentModel = "spring", uses = {AulaMapper.class})
public interface ChamadaMapper extends EntityMapper<ChamadaDTO, Chamada> {

    @Mapping(source = "aula.id", target = "aulaId")
    @Mapping(source = "aula.data", target = "aulaData")
    ChamadaDTO toDto(Chamada chamada);

    @Mapping(source = "aulaId", target = "aula")
    Chamada toEntity(ChamadaDTO chamadaDTO);

    default Chamada fromId(Long id) {
        if (id == null) {
            return null;
        }
        Chamada chamada = new Chamada();
        chamada.setId(id);
        return chamada;
    }
}
