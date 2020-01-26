package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.PlanoCurricularDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlanoCurricular} and its DTO {@link PlanoCurricularDTO}.
 */
@Mapper(componentModel = "spring", uses = {CursoMapper.class})
public interface PlanoCurricularMapper extends EntityMapper<PlanoCurricularDTO, PlanoCurricular> {

    @Mapping(source = "curso.id", target = "cursoId")
    @Mapping(source = "curso.nome", target = "cursoNome")
    PlanoCurricularDTO toDto(PlanoCurricular planoCurricular);

    @Mapping(target = "planoAulas", ignore = true)
    @Mapping(target = "removePlanoAula", ignore = true)
    @Mapping(target = "dosificacaos", ignore = true)
    @Mapping(target = "removeDosificacao", ignore = true)
    @Mapping(target = "notas", ignore = true)
    @Mapping(target = "removeNota", ignore = true)
    @Mapping(target = "aulas", ignore = true)
    @Mapping(target = "removeAula", ignore = true)
    @Mapping(target = "horarios", ignore = true)
    @Mapping(target = "removeHorario", ignore = true)
    @Mapping(target = "testeConhecimentos", ignore = true)
    @Mapping(target = "removeTesteConhecimento", ignore = true)
    @Mapping(source = "cursoId", target = "curso")
    PlanoCurricular toEntity(PlanoCurricularDTO planoCurricularDTO);

    default PlanoCurricular fromId(Long id) {
        if (id == null) {
            return null;
        }
        PlanoCurricular planoCurricular = new PlanoCurricular();
        planoCurricular.setId(id);
        return planoCurricular;
    }
}
