package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.service.DosificacaoService;
import com.ravunana.educacao.pedagogico.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pedagogico.service.dto.DosificacaoDTO;
import com.ravunana.educacao.pedagogico.service.dto.DosificacaoCriteria;
import com.ravunana.educacao.pedagogico.service.DosificacaoQueryService;

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
 * REST controller for managing {@link com.ravunana.educacao.pedagogico.domain.Dosificacao}.
 */
@RestController
@RequestMapping("/api")
public class DosificacaoResource {

    private final Logger log = LoggerFactory.getLogger(DosificacaoResource.class);

    private static final String ENTITY_NAME = "pedagogicoDosificacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DosificacaoService dosificacaoService;

    private final DosificacaoQueryService dosificacaoQueryService;

    public DosificacaoResource(DosificacaoService dosificacaoService, DosificacaoQueryService dosificacaoQueryService) {
        this.dosificacaoService = dosificacaoService;
        this.dosificacaoQueryService = dosificacaoQueryService;
    }

    /**
     * {@code POST  /dosificacaos} : Create a new dosificacao.
     *
     * @param dosificacaoDTO the dosificacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dosificacaoDTO, or with status {@code 400 (Bad Request)} if the dosificacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dosificacaos")
    public ResponseEntity<DosificacaoDTO> createDosificacao(@Valid @RequestBody DosificacaoDTO dosificacaoDTO) throws URISyntaxException {
        log.debug("REST request to save Dosificacao : {}", dosificacaoDTO);
        if (dosificacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new dosificacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DosificacaoDTO result = dosificacaoService.save(dosificacaoDTO);
        return ResponseEntity.created(new URI("/api/dosificacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dosificacaos} : Updates an existing dosificacao.
     *
     * @param dosificacaoDTO the dosificacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dosificacaoDTO,
     * or with status {@code 400 (Bad Request)} if the dosificacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dosificacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dosificacaos")
    public ResponseEntity<DosificacaoDTO> updateDosificacao(@Valid @RequestBody DosificacaoDTO dosificacaoDTO) throws URISyntaxException {
        log.debug("REST request to update Dosificacao : {}", dosificacaoDTO);
        if (dosificacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DosificacaoDTO result = dosificacaoService.save(dosificacaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dosificacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dosificacaos} : get all the dosificacaos.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dosificacaos in body.
     */
    @GetMapping("/dosificacaos")
    public ResponseEntity<List<DosificacaoDTO>> getAllDosificacaos(DosificacaoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Dosificacaos by criteria: {}", criteria);
        Page<DosificacaoDTO> page = dosificacaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /dosificacaos/count} : count all the dosificacaos.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/dosificacaos/count")
    public ResponseEntity<Long> countDosificacaos(DosificacaoCriteria criteria) {
        log.debug("REST request to count Dosificacaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(dosificacaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dosificacaos/:id} : get the "id" dosificacao.
     *
     * @param id the id of the dosificacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dosificacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dosificacaos/{id}")
    public ResponseEntity<DosificacaoDTO> getDosificacao(@PathVariable Long id) {
        log.debug("REST request to get Dosificacao : {}", id);
        Optional<DosificacaoDTO> dosificacaoDTO = dosificacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dosificacaoDTO);
    }

    /**
     * {@code DELETE  /dosificacaos/:id} : delete the "id" dosificacao.
     *
     * @param id the id of the dosificacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dosificacaos/{id}")
    public ResponseEntity<Void> deleteDosificacao(@PathVariable Long id) {
        log.debug("REST request to delete Dosificacao : {}", id);
        dosificacaoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/dosificacaos?query=:query} : search for the dosificacao corresponding
     * to the query.
     *
     * @param query the query of the dosificacao search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/dosificacaos")
    public ResponseEntity<List<DosificacaoDTO>> searchDosificacaos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Dosificacaos for query {}", query);
        Page<DosificacaoDTO> page = dosificacaoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
