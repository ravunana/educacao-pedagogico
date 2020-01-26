package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.ProfessorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Professor} and its DTO {@link ProfessorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProfessorMapper extends EntityMapper<ProfessorDTO, Professor> {


    @Mapping(target = "horarios", ignore = true)
    @Mapping(target = "removeHorario", ignore = true)
    @Mapping(target = "turmas", ignore = true)
    @Mapping(target = "removeTurma", ignore = true)
    @Mapping(target = "planoAulas", ignore = true)
    @Mapping(target = "removePlanoAula", ignore = true)
    @Mapping(target = "notas", ignore = true)
    @Mapping(target = "removeNota", ignore = true)
    @Mapping(target = "testeConhecimentos", ignore = true)
    @Mapping(target = "removeTesteConhecimento", ignore = true)
    Professor toEntity(ProfessorDTO professorDTO);

    default Professor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Professor professor = new Professor();
        professor.setId(id);
        return professor;
    }
}
