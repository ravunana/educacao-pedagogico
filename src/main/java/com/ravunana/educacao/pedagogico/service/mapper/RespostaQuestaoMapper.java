package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.RespostaQuestaoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link RespostaQuestao} and its DTO {@link RespostaQuestaoDTO}.
 */
@Mapper(componentModel = "spring", uses = {QuestaoTesteMapper.class})
public interface RespostaQuestaoMapper extends EntityMapper<RespostaQuestaoDTO, RespostaQuestao> {

    @Mapping(source = "questao.id", target = "questaoId")
    @Mapping(source = "questao.questao", target = "questaoQuestao")
    RespostaQuestaoDTO toDto(RespostaQuestao respostaQuestao);

    @Mapping(source = "questaoId", target = "questao")
    RespostaQuestao toEntity(RespostaQuestaoDTO respostaQuestaoDTO);

    default RespostaQuestao fromId(Long id) {
        if (id == null) {
            return null;
        }
        RespostaQuestao respostaQuestao = new RespostaQuestao();
        respostaQuestao.setId(id);
        return respostaQuestao;
    }
}
