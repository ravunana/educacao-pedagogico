package com.ravunana.educacao.pedagogico.service;

import com.ravunana.educacao.pedagogico.domain.TesteConhecimento;
import com.ravunana.educacao.pedagogico.repository.TesteConhecimentoRepository;
import com.ravunana.educacao.pedagogico.repository.search.TesteConhecimentoSearchRepository;
import com.ravunana.educacao.pedagogico.service.dto.TesteConhecimentoDTO;
import com.ravunana.educacao.pedagogico.service.mapper.TesteConhecimentoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link TesteConhecimento}.
 */
@Service
@Transactional
public class TesteConhecimentoService {

    private final Logger log = LoggerFactory.getLogger(TesteConhecimentoService.class);

    private final TesteConhecimentoRepository testeConhecimentoRepository;

    private final TesteConhecimentoMapper testeConhecimentoMapper;

    private final TesteConhecimentoSearchRepository testeConhecimentoSearchRepository;

    public TesteConhecimentoService(TesteConhecimentoRepository testeConhecimentoRepository, TesteConhecimentoMapper testeConhecimentoMapper, TesteConhecimentoSearchRepository testeConhecimentoSearchRepository) {
        this.testeConhecimentoRepository = testeConhecimentoRepository;
        this.testeConhecimentoMapper = testeConhecimentoMapper;
        this.testeConhecimentoSearchRepository = testeConhecimentoSearchRepository;
    }

    /**
     * Save a testeConhecimento.
     *
     * @param testeConhecimentoDTO the entity to save.
     * @return the persisted entity.
     */
    public TesteConhecimentoDTO save(TesteConhecimentoDTO testeConhecimentoDTO) {
        log.debug("Request to save TesteConhecimento : {}", testeConhecimentoDTO);
        TesteConhecimento testeConhecimento = testeConhecimentoMapper.toEntity(testeConhecimentoDTO);
        testeConhecimento = testeConhecimentoRepository.save(testeConhecimento);
        TesteConhecimentoDTO result = testeConhecimentoMapper.toDto(testeConhecimento);
        testeConhecimentoSearchRepository.save(testeConhecimento);
        return result;
    }

    /**
     * Get all the testeConhecimentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TesteConhecimentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TesteConhecimentos");
        return testeConhecimentoRepository.findAll(pageable)
            .map(testeConhecimentoMapper::toDto);
    }


    /**
     * Get one testeConhecimento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TesteConhecimentoDTO> findOne(Long id) {
        log.debug("Request to get TesteConhecimento : {}", id);
        return testeConhecimentoRepository.findById(id)
            .map(testeConhecimentoMapper::toDto);
    }

    /**
     * Delete the testeConhecimento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TesteConhecimento : {}", id);
        testeConhecimentoRepository.deleteById(id);
        testeConhecimentoSearchRepository.deleteById(id);
    }

    /**
     * Search for the testeConhecimento corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TesteConhecimentoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TesteConhecimentos for query {}", query);
        return testeConhecimentoSearchRepository.search(queryStringQuery(query), pageable)
            .map(testeConhecimentoMapper::toDto);
    }
}
