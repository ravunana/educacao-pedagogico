package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.PlanoAulaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlanoAula} and its DTO {@link PlanoAulaDTO}.
 */
@Mapper(componentModel = "spring", uses = {TurmaMapper.class, DosificacaoMapper.class, ProfessorMapper.class, PlanoCurricularMapper.class})
public interface PlanoAulaMapper extends EntityMapper<PlanoAulaDTO, PlanoAula> {

    @Mapping(source = "dosificacao.id", target = "dosificacaoId")
    @Mapping(source = "professor.id", target = "professorId")
    @Mapping(source = "professor.nome", target = "professorNome")
    @Mapping(source = "curriculo.id", target = "curriculoId")
    @Mapping(source = "curriculo.descricao", target = "curriculoDescricao")
    PlanoAulaDTO toDto(PlanoAula planoAula);

    @Mapping(target = "removeTurma", ignore = true)
    @Mapping(source = "dosificacaoId", target = "dosificacao")
    @Mapping(source = "professorId", target = "professor")
    @Mapping(source = "curriculoId", target = "curriculo")
    @Mapping(target = "aulaPlanoAulas", ignore = true)
    @Mapping(target = "removeAulaPlanoAula", ignore = true)
    PlanoAula toEntity(PlanoAulaDTO planoAulaDTO);

    default PlanoAula fromId(Long id) {
        if (id == null) {
            return null;
        }
        PlanoAula planoAula = new PlanoAula();
        planoAula.setId(id);
        return planoAula;
    }
}
