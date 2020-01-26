package com.ravunana.educacao.pedagogico.service.mapper;

import com.ravunana.educacao.pedagogico.domain.*;
import com.ravunana.educacao.pedagogico.service.dto.DosificacaoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dosificacao} and its DTO {@link DosificacaoDTO}.
 */
@Mapper(componentModel = "spring", uses = {CursoMapper.class, PlanoCurricularMapper.class})
public interface DosificacaoMapper extends EntityMapper<DosificacaoDTO, Dosificacao> {

    @Mapping(source = "curriulo.id", target = "curriuloId")
    @Mapping(source = "curriulo.descricao", target = "curriuloDescricao")
    DosificacaoDTO toDto(Dosificacao dosificacao);

    @Mapping(target = "planoAulas", ignore = true)
    @Mapping(target = "removePlanoAula", ignore = true)
    @Mapping(target = "removeCurso", ignore = true)
    @Mapping(source = "curriuloId", target = "curriulo")
    Dosificacao toEntity(DosificacaoDTO dosificacaoDTO);

    default Dosificacao fromId(Long id) {
        if (id == null) {
            return null;
        }
        Dosificacao dosificacao = new Dosificacao();
        dosificacao.setId(id);
        return dosificacao;
    }
}
