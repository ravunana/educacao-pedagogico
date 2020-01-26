package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.CriterioAvaliacaoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CriterioAvaliacao} and its DTO {@link CriterioAvaliacaoDTO}.
 */
@Mapper(componentModel = "spring", uses = {PlanoCurricularMapper.class})
public interface CriterioAvaliacaoMapper extends EntityMapper<CriterioAvaliacaoDTO, CriterioAvaliacao> {

    @Mapping(source = "planoCurricular.id", target = "planoCurricularId")
    @Mapping(source = "planoCurricular.descricao", target = "planoCurricularDescricao")
    CriterioAvaliacaoDTO toDto(CriterioAvaliacao criterioAvaliacao);

    @Mapping(source = "planoCurricularId", target = "planoCurricular")
    CriterioAvaliacao toEntity(CriterioAvaliacaoDTO criterioAvaliacaoDTO);

    default CriterioAvaliacao fromId(Long id) {
        if (id == null) {
            return null;
        }
        CriterioAvaliacao criterioAvaliacao = new CriterioAvaliacao();
        criterioAvaliacao.setId(id);
        return criterioAvaliacao;
    }
}
