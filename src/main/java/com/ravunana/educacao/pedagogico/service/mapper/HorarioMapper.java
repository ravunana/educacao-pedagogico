package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.HorarioDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Horario} and its DTO {@link HorarioDTO}.
 */
@Mapper(componentModel = "spring", uses = {TurmaMapper.class, ProfessorMapper.class, PlanoCurricularMapper.class})
public interface HorarioMapper extends EntityMapper<HorarioDTO, Horario> {

    @Mapping(source = "turma.id", target = "turmaId")
    @Mapping(source = "turma.descricao", target = "turmaDescricao")
    @Mapping(source = "professor.id", target = "professorId")
    @Mapping(source = "professor.numeroAgente", target = "professorNumeroAgente")
    @Mapping(source = "curriculo.id", target = "curriculoId")
    @Mapping(source = "curriculo.descricao", target = "curriculoDescricao")
    HorarioDTO toDto(Horario horario);

    @Mapping(source = "turmaId", target = "turma")
    @Mapping(source = "professorId", target = "professor")
    @Mapping(source = "curriculoId", target = "curriculo")
    Horario toEntity(HorarioDTO horarioDTO);

    default Horario fromId(Long id) {
        if (id == null) {
            return null;
        }
        Horario horario = new Horario();
        horario.setId(id);
        return horario;
    }
}
