package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.AulaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Aula} and its DTO {@link AulaDTO}.
 */
@Mapper(componentModel = "spring", uses = {PlanoAulaMapper.class, TurmaMapper.class, PlanoCurricularMapper.class})
public interface AulaMapper extends EntityMapper<AulaDTO, Aula> {

    @Mapping(source = "turma.id", target = "turmaId")
    @Mapping(source = "turma.descricao", target = "turmaDescricao")
    @Mapping(source = "curriulo.id", target = "curriuloId")
    @Mapping(source = "curriulo.descricao", target = "curriuloDescricao")
    AulaDTO toDto(Aula aula);

    @Mapping(target = "chamadas", ignore = true)
    @Mapping(target = "removeChamada", ignore = true)
    @Mapping(target = "removePlanoAula", ignore = true)
    @Mapping(source = "turmaId", target = "turma")
    @Mapping(source = "curriuloId", target = "curriulo")
    Aula toEntity(AulaDTO aulaDTO);

    default Aula fromId(Long id) {
        if (id == null) {
            return null;
        }
        Aula aula = new Aula();
        aula.setId(id);
        return aula;
    }
}
