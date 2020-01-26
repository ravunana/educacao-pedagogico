package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.TurmaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Turma} and its DTO {@link TurmaDTO}.
 */
@Mapper(componentModel = "spring", uses = {CursoMapper.class, ProfessorMapper.class})
public interface TurmaMapper extends EntityMapper<TurmaDTO, Turma> {

    @Mapping(source = "curso.id", target = "cursoId")
    @Mapping(source = "curso.nome", target = "cursoNome")
    @Mapping(source = "coordenador.id", target = "coordenadorId")
    @Mapping(source = "coordenador.nome", target = "coordenadorNome")
    TurmaDTO toDto(Turma turma);

    @Mapping(target = "horarios", ignore = true)
    @Mapping(target = "removeHorario", ignore = true)
    @Mapping(target = "planoActividades", ignore = true)
    @Mapping(target = "removePlanoActividade", ignore = true)
    @Mapping(target = "notas", ignore = true)
    @Mapping(target = "removeNota", ignore = true)
    @Mapping(target = "aulas", ignore = true)
    @Mapping(target = "removeAula", ignore = true)
    @Mapping(target = "testeConhecimentos", ignore = true)
    @Mapping(target = "removeTesteConhecimento", ignore = true)
    @Mapping(source = "cursoId", target = "curso")
    @Mapping(source = "coordenadorId", target = "coordenador")
    @Mapping(target = "planoAulaTurmas", ignore = true)
    @Mapping(target = "removePlanoAulaTurma", ignore = true)
    Turma toEntity(TurmaDTO turmaDTO);

    default Turma fromId(Long id) {
        if (id == null) {
            return null;
        }
        Turma turma = new Turma();
        turma.setId(id);
        return turma;
    }
}
