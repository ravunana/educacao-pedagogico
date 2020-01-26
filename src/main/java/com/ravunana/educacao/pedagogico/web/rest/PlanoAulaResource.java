package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.service.PlanoAulaService;
import com.ravunana.educacao.pedagogico.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pedagogico.service.dto.PlanoAulaDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pedagogico.domain.PlanoAula}.
 */
@RestController
@RequestMapping("/api")
public class PlanoAulaResource {

    private final Logger log = LoggerFactory.getLogger(PlanoAulaResource.class);

    private static final String ENTITY_NAME = "pedagogicoPlanoAula";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanoAulaService planoAulaService;

    public PlanoAulaResource(PlanoAulaService planoAulaService) {
        this.planoAulaService = planoAulaService;
    }

    /**
     * {@code POST  /plano-aulas} : Create a new planoAula.
     *
     * @param planoAulaDTO the planoAulaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planoAulaDTO, or with status {@code 400 (Bad Request)} if the planoAula has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plano-aulas")
    public ResponseEntity<PlanoAulaDTO> createPlanoAula(@Valid @RequestBody PlanoAulaDTO planoAulaDTO) throws URISyntaxException {
        log.debug("REST request to save PlanoAula : {}", planoAulaDTO);
        if (planoAulaDTO.getId() != null) {
            throw new BadRequestAlertException("A new planoAula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanoAulaDTO result = planoAulaService.save(planoAulaDTO);
        return ResponseEntity.created(new URI("/api/plano-aulas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plano-aulas} : Updates an existing planoAula.
     *
     * @param planoAulaDTO the planoAulaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planoAulaDTO,
     * or with status {@code 400 (Bad Request)} if the planoAulaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planoAulaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plano-aulas")
    public ResponseEntity<PlanoAulaDTO> updatePlanoAula(@Valid @RequestBody PlanoAulaDTO planoAulaDTO) throws URISyntaxException {
        log.debug("REST request to update PlanoAula : {}", planoAulaDTO);
        if (planoAulaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanoAulaDTO result = planoAulaService.save(planoAulaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planoAulaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plano-aulas} : get all the planoAulas.
     *

     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planoAulas in body.
     */
    @GetMapping("/plano-aulas")
    public ResponseEntity<List<PlanoAulaDTO>> getAllPlanoAulas(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of PlanoAulas");
        Page<PlanoAulaDTO> page;
        if (eagerload) {
            page = planoAulaService.findAllWithEagerRelationships(pageable);
        } else {
            page = planoAulaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plano-aulas/:id} : get the "id" planoAula.
     *
     * @param id the id of the planoAulaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planoAulaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plano-aulas/{id}")
    public ResponseEntity<PlanoAulaDTO> getPlanoAula(@PathVariable Long id) {
        log.debug("REST request to get PlanoAula : {}", id);
        Optional<PlanoAulaDTO> planoAulaDTO = planoAulaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planoAulaDTO);
    }

    /**
     * {@code DELETE  /plano-aulas/:id} : delete the "id" planoAula.
     *
     * @param id the id of the planoAulaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plano-aulas/{id}")
    public ResponseEntity<Void> deletePlanoAula(@PathVariable Long id) {
        log.debug("REST request to delete PlanoAula : {}", id);
        planoAulaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/plano-aulas?query=:query} : search for the planoAula corresponding
     * to the query.
     *
     * @param query the query of the planoAula search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/plano-aulas")
    public ResponseEntity<List<PlanoAulaDTO>> searchPlanoAulas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PlanoAulas for query {}", query);
        Page<PlanoAulaDTO> page = planoAulaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
