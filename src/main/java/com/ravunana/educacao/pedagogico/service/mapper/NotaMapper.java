package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.NotaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Nota} and its DTO {@link NotaDTO}.
 */
@Mapper(componentModel = "spring", uses = {TurmaMapper.class, PlanoCurricularMapper.class, ProfessorMapper.class})
public interface NotaMapper extends EntityMapper<NotaDTO, Nota> {

    @Mapping(source = "turma.id", target = "turmaId")
    @Mapping(source = "turma.descricao", target = "turmaDescricao")
    @Mapping(source = "curriculo.id", target = "curriculoId")
    @Mapping(source = "curriculo.descricao", target = "curriculoDescricao")
    @Mapping(source = "professor.id", target = "professorId")
    @Mapping(source = "professor.nome", target = "professorNome")
    NotaDTO toDto(Nota nota);

    @Mapping(source = "turmaId", target = "turma")
    @Mapping(source = "curriculoId", target = "curriculo")
    @Mapping(source = "professorId", target = "professor")
    Nota toEntity(NotaDTO notaDTO);

    default Nota fromId(Long id) {
        if (id == null) {
            return null;
        }
        Nota nota = new Nota();
        nota.setId(id);
        return nota;
    }
}
