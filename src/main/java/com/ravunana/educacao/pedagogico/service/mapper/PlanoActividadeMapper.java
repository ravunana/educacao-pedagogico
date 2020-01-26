package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.PlanoActividadeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlanoActividade} and its DTO {@link PlanoActividadeDTO}.
 */
@Mapper(componentModel = "spring", uses = {CursoMapper.class, TurmaMapper.class})
public interface PlanoActividadeMapper extends EntityMapper<PlanoActividadeDTO, PlanoActividade> {

    @Mapping(source = "curso.id", target = "cursoId")
    @Mapping(source = "curso.nome", target = "cursoNome")
    @Mapping(source = "turma.id", target = "turmaId")
    @Mapping(source = "turma.descricao", target = "turmaDescricao")
    PlanoActividadeDTO toDto(PlanoActividade planoActividade);

    @Mapping(source = "cursoId", target = "curso")
    @Mapping(source = "turmaId", target = "turma")
    PlanoActividade toEntity(PlanoActividadeDTO planoActividadeDTO);

    default PlanoActividade fromId(Long id) {
        if (id == null) {
            return null;
        }
        PlanoActividade planoActividade = new PlanoActividade();
        planoActividade.setId(id);
        return planoActividade;
    }
}
