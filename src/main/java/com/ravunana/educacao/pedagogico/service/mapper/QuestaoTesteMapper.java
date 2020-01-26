package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.QuestaoTesteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestaoTeste} and its DTO {@link QuestaoTesteDTO}.
 */
@Mapper(componentModel = "spring", uses = {TesteConhecimentoMapper.class})
public interface QuestaoTesteMapper extends EntityMapper<QuestaoTesteDTO, QuestaoTeste> {

    @Mapping(source = "teste.id", target = "testeId")
    QuestaoTesteDTO toDto(QuestaoTeste questaoTeste);

    @Mapping(source = "testeId", target = "teste")
    QuestaoTeste toEntity(QuestaoTesteDTO questaoTesteDTO);

    default QuestaoTeste fromId(Long id) {
        if (id == null) {
            return null;
        }
        QuestaoTeste questaoTeste = new QuestaoTeste();
        questaoTeste.setId(id);
        return questaoTeste;
    }
}
