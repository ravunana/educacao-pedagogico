package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.service.AulaService;
import com.ravunana.educacao.pedagogico.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pedagogico.service.dto.AulaDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pedagogico.domain.Aula}.
 */
@RestController
@RequestMapping("/api")
public class AulaResource {

    private final Logger log = LoggerFactory.getLogger(AulaResource.class);

    private static final String ENTITY_NAME = "pedagogicoAula";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AulaService aulaService;

    public AulaResource(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    /**
     * {@code POST  /aulas} : Create a new aula.
     *
     * @param aulaDTO the aulaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aulaDTO, or with status {@code 400 (Bad Request)} if the aula has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aulas")
    public ResponseEntity<AulaDTO> createAula(@Valid @RequestBody AulaDTO aulaDTO) throws URISyntaxException {
        log.debug("REST request to save Aula : {}", aulaDTO);
        if (aulaDTO.getId() != null) {
            throw new BadRequestAlertException("A new aula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AulaDTO result = aulaService.save(aulaDTO);
        return ResponseEntity.created(new URI("/api/aulas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aulas} : Updates an existing aula.
     *
     * @param aulaDTO the aulaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aulaDTO,
     * or with status {@code 400 (Bad Request)} if the aulaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aulaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aulas")
    public ResponseEntity<AulaDTO> updateAula(@Valid @RequestBody AulaDTO aulaDTO) throws URISyntaxException {
        log.debug("REST request to update Aula : {}", aulaDTO);
        if (aulaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AulaDTO result = aulaService.save(aulaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aulaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /aulas} : get all the aulas.
     *

     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aulas in body.
     */
    @GetMapping("/aulas")
    public ResponseEntity<List<AulaDTO>> getAllAulas(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Aulas");
        Page<AulaDTO> page;
        if (eagerload) {
            page = aulaService.findAllWithEagerRelationships(pageable);
        } else {
            page = aulaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aulas/:id} : get the "id" aula.
     *
     * @param id the id of the aulaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aulaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aulas/{id}")
    public ResponseEntity<AulaDTO> getAula(@PathVariable Long id) {
        log.debug("REST request to get Aula : {}", id);
        Optional<AulaDTO> aulaDTO = aulaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aulaDTO);
    }

    /**
     * {@code DELETE  /aulas/:id} : delete the "id" aula.
     *
     * @param id the id of the aulaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aulas/{id}")
    public ResponseEntity<Void> deleteAula(@PathVariable Long id) {
        log.debug("REST request to delete Aula : {}", id);
        aulaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/aulas?query=:query} : search for the aula corresponding
     * to the query.
     *
     * @param query the query of the aula search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/aulas")
    public ResponseEntity<List<AulaDTO>> searchAulas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Aulas for query {}", query);
        Page<AulaDTO> page = aulaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
