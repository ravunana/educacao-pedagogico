package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.CursoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Curso} and its DTO {@link CursoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CursoMapper extends EntityMapper<CursoDTO, Curso> {


    @Mapping(target = "planoCurriculars", ignore = true)
    @Mapping(target = "removePlanoCurricular", ignore = true)
    @Mapping(target = "turmas", ignore = true)
    @Mapping(target = "removeTurma", ignore = true)
    @Mapping(target = "planoActividades", ignore = true)
    @Mapping(target = "removePlanoActividade", ignore = true)
    @Mapping(target = "dosificacaoCursos", ignore = true)
    @Mapping(target = "removeDosificacaoCurso", ignore = true)
    Curso toEntity(CursoDTO cursoDTO);

    default Curso fromId(Long id) {
        if (id == null) {
            return null;
        }
        Curso curso = new Curso();
        curso.setId(id);
        return curso;
    }
}
