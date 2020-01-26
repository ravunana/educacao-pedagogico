package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.service.PlanoActividadeService;
import com.ravunana.educacao.pedagogico.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pedagogico.service.dto.PlanoActividadeDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pedagogico.domain.PlanoActividade}.
 */
@RestController
@RequestMapping("/api")
public class PlanoActividadeResource {

    private final Logger log = LoggerFactory.getLogger(PlanoActividadeResource.class);

    private static final String ENTITY_NAME = "pedagogicoPlanoActividade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanoActividadeService planoActividadeService;

    public PlanoActividadeResource(PlanoActividadeService planoActividadeService) {
        this.planoActividadeService = planoActividadeService;
    }

    /**
     * {@code POST  /plano-actividades} : Create a new planoActividade.
     *
     * @param planoActividadeDTO the planoActividadeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planoActividadeDTO, or with status {@code 400 (Bad Request)} if the planoActividade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plano-actividades")
    public ResponseEntity<PlanoActividadeDTO> createPlanoActividade(@Valid @RequestBody PlanoActividadeDTO planoActividadeDTO) throws URISyntaxException {
        log.debug("REST request to save PlanoActividade : {}", planoActividadeDTO);
        if (planoActividadeDTO.getId() != null) {
            throw new BadRequestAlertException("A new planoActividade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanoActividadeDTO result = planoActividadeService.save(planoActividadeDTO);
        return ResponseEntity.created(new URI("/api/plano-actividades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plano-actividades} : Updates an existing planoActividade.
     *
     * @param planoActividadeDTO the planoActividadeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planoActividadeDTO,
     * or with status {@code 400 (Bad Request)} if the planoActividadeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planoActividadeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plano-actividades")
    public ResponseEntity<PlanoActividadeDTO> updatePlanoActividade(@Valid @RequestBody PlanoActividadeDTO planoActividadeDTO) throws URISyntaxException {
        log.debug("REST request to update PlanoActividade : {}", planoActividadeDTO);
        if (planoActividadeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanoActividadeDTO result = planoActividadeService.save(planoActividadeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planoActividadeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plano-actividades} : get all the planoActividades.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planoActividades in body.
     */
    @GetMapping("/plano-actividades")
    public ResponseEntity<List<PlanoActividadeDTO>> getAllPlanoActividades(Pageable pageable) {
        log.debug("REST request to get a page of PlanoActividades");
        Page<PlanoActividadeDTO> page = planoActividadeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plano-actividades/:id} : get the "id" planoActividade.
     *
     * @param id the id of the planoActividadeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planoActividadeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plano-actividades/{id}")
    public ResponseEntity<PlanoActividadeDTO> getPlanoActividade(@PathVariable Long id) {
        log.debug("REST request to get PlanoActividade : {}", id);
        Optional<PlanoActividadeDTO> planoActividadeDTO = planoActividadeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planoActividadeDTO);
    }

    /**
     * {@code DELETE  /plano-actividades/:id} : delete the "id" planoActividade.
     *
     * @param id the id of the planoActividadeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plano-actividades/{id}")
    public ResponseEntity<Void> deletePlanoActividade(@PathVariable Long id) {
        log.debug("REST request to delete PlanoActividade : {}", id);
        planoActividadeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/plano-actividades?query=:query} : search for the planoActividade corresponding
     * to the query.
     *
     * @param query the query of the planoActividade search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/plano-actividades")
    public ResponseEntity<List<PlanoActividadeDTO>> searchPlanoActividades(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PlanoActividades for query {}", query);
        Page<PlanoActividadeDTO> page = planoActividadeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
