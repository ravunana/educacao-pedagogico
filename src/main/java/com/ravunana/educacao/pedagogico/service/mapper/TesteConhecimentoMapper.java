package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.TesteConhecimentoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TesteConhecimento} and its DTO {@link TesteConhecimentoDTO}.
 */
@Mapper(componentModel = "spring", uses = {PlanoCurricularMapper.class, TurmaMapper.class, ProfessorMapper.class})
public interface TesteConhecimentoMapper extends EntityMapper<TesteConhecimentoDTO, TesteConhecimento> {

    @Mapping(source = "curriculo.id", target = "curriculoId")
    @Mapping(source = "curriculo.descricao", target = "curriculoDescricao")
    @Mapping(source = "turma.id", target = "turmaId")
    @Mapping(source = "turma.descricao", target = "turmaDescricao")
    @Mapping(source = "professor.id", target = "professorId")
    @Mapping(source = "professor.nome", target = "professorNome")
    TesteConhecimentoDTO toDto(TesteConhecimento testeConhecimento);

    @Mapping(target = "questaoTestes", ignore = true)
    @Mapping(target = "removeQuestaoTeste", ignore = true)
    @Mapping(source = "curriculoId", target = "curriculo")
    @Mapping(source = "turmaId", target = "turma")
    @Mapping(source = "professorId", target = "professor")
    TesteConhecimento toEntity(TesteConhecimentoDTO testeConhecimentoDTO);

    default TesteConhecimento fromId(Long id) {
        if (id == null) {
            return null;
        }
        TesteConhecimento testeConhecimento = new TesteConhecimento();
        testeConhecimento.setId(id);
        return testeConhecimento;
    }
}
