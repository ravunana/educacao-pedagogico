package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.service.CriterioAvaliacaoService;
import com.ravunana.educacao.pedagogico.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pedagogico.service.dto.CriterioAvaliacaoDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.ravunana.educacao.pedagogico.domain.CriterioAvaliacao}.
 */
@RestController
@RequestMapping("/api")
public class CriterioAvaliacaoResource {

    private final Logger log = LoggerFactory.getLogger(CriterioAvaliacaoResource.class);

    private static final String ENTITY_NAME = "pedagogicoCriterioAvaliacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CriterioAvaliacaoService criterioAvaliacaoService;

    public CriterioAvaliacaoResource(CriterioAvaliacaoService criterioAvaliacaoService) {
        this.criterioAvaliacaoService = criterioAvaliacaoService;
    }

    /**
     * {@code POST  /criterio-avaliacaos} : Create a new criterioAvaliacao.
     *
     * @param criterioAvaliacaoDTO the criterioAvaliacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new criterioAvaliacaoDTO, or with status {@code 400 (Bad Request)} if the criterioAvaliacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/criterio-avaliacaos")
    public ResponseEntity<CriterioAvaliacaoDTO> createCriterioAvaliacao(@Valid @RequestBody CriterioAvaliacaoDTO criterioAvaliacaoDTO) throws URISyntaxException {
        log.debug("REST request to save CriterioAvaliacao : {}", criterioAvaliacaoDTO);
        if (criterioAvaliacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new criterioAvaliacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CriterioAvaliacaoDTO result = criterioAvaliacaoService.save(criterioAvaliacaoDTO);
        return ResponseEntity.created(new URI("/api/criterio-avaliacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /criterio-avaliacaos} : Updates an existing criterioAvaliacao.
     *
     * @param criterioAvaliacaoDTO the criterioAvaliacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated criterioAvaliacaoDTO,
     * or with status {@code 400 (Bad Request)} if the criterioAvaliacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the criterioAvaliacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/criterio-avaliacaos")
    public ResponseEntity<CriterioAvaliacaoDTO> updateCriterioAvaliacao(@Valid @RequestBody CriterioAvaliacaoDTO criterioAvaliacaoDTO) throws URISyntaxException {
        log.debug("REST request to update CriterioAvaliacao : {}", criterioAvaliacaoDTO);
        if (criterioAvaliacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CriterioAvaliacaoDTO result = criterioAvaliacaoService.save(criterioAvaliacaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, criterioAvaliacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /criterio-avaliacaos} : get all the criterioAvaliacaos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of criterioAvaliacaos in body.
     */
    @GetMapping("/criterio-avaliacaos")
    public ResponseEntity<List<CriterioAvaliacaoDTO>> getAllCriterioAvaliacaos(Pageable pageable) {
        log.debug("REST request to get a page of CriterioAvaliacaos");
        Page<CriterioAvaliacaoDTO> page = criterioAvaliacaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /criterio-avaliacaos/:id} : get the "id" criterioAvaliacao.
     *
     * @param id the id of the criterioAvaliacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the criterioAvaliacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/criterio-avaliacaos/{id}")
    public ResponseEntity<CriterioAvaliacaoDTO> getCriterioAvaliacao(@PathVariable Long id) {
        log.debug("REST request to get CriterioAvaliacao : {}", id);
        Optional<CriterioAvaliacaoDTO> criterioAvaliacaoDTO = criterioAvaliacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(criterioAvaliacaoDTO);
    }

    /**
     * {@code DELETE  /criterio-avaliacaos/:id} : delete the "id" criterioAvaliacao.
     *
     * @param id the id of the criterioAvaliacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/criterio-avaliacaos/{id}")
    public ResponseEntity<Void> deleteCriterioAvaliacao(@PathVariable Long id) {
        log.debug("REST request to delete CriterioAvaliacao : {}", id);
        criterioAvaliacaoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/criterio-avaliacaos?query=:query} : search for the criterioAvaliacao corresponding
     * to the query.
     *
     * @param query the query of the criterioAvaliacao search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/criterio-avaliacaos")
    public ResponseEntity<List<CriterioAvaliacaoDTO>> searchCriterioAvaliacaos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CriterioAvaliacaos for query {}", query);
        Page<CriterioAvaliacaoDTO> page = criterioAvaliacaoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
