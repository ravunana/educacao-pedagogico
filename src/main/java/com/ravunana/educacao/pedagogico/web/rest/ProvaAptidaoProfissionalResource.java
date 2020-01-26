package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.service.ProvaAptidaoProfissionalService;
import com.ravunana.educacao.pedagogico.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pedagogico.service.dto.ProvaAptidaoProfissionalDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pedagogico.domain.ProvaAptidaoProfissional}.
 */
@RestController
@RequestMapping("/api")
public class ProvaAptidaoProfissionalResource {

    private final Logger log = LoggerFactory.getLogger(ProvaAptidaoProfissionalResource.class);

    private static final String ENTITY_NAME = "pedagogicoProvaAptidaoProfissional";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProvaAptidaoProfissionalService provaAptidaoProfissionalService;

    public ProvaAptidaoProfissionalResource(ProvaAptidaoProfissionalService provaAptidaoProfissionalService) {
        this.provaAptidaoProfissionalService = provaAptidaoProfissionalService;
    }

    /**
     * {@code POST  /prova-aptidao-profissionals} : Create a new provaAptidaoProfissional.
     *
     * @param provaAptidaoProfissionalDTO the provaAptidaoProfissionalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new provaAptidaoProfissionalDTO, or with status {@code 400 (Bad Request)} if the provaAptidaoProfissional has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prova-aptidao-profissionals")
    public ResponseEntity<ProvaAptidaoProfissionalDTO> createProvaAptidaoProfissional(@Valid @RequestBody ProvaAptidaoProfissionalDTO provaAptidaoProfissionalDTO) throws URISyntaxException {
        log.debug("REST request to save ProvaAptidaoProfissional : {}", provaAptidaoProfissionalDTO);
        if (provaAptidaoProfissionalDTO.getId() != null) {
            throw new BadRequestAlertException("A new provaAptidaoProfissional cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProvaAptidaoProfissionalDTO result = provaAptidaoProfissionalService.save(provaAptidaoProfissionalDTO);
        return ResponseEntity.created(new URI("/api/prova-aptidao-profissionals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prova-aptidao-profissionals} : Updates an existing provaAptidaoProfissional.
     *
     * @param provaAptidaoProfissionalDTO the provaAptidaoProfissionalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provaAptidaoProfissionalDTO,
     * or with status {@code 400 (Bad Request)} if the provaAptidaoProfissionalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the provaAptidaoProfissionalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prova-aptidao-profissionals")
    public ResponseEntity<ProvaAptidaoProfissionalDTO> updateProvaAptidaoProfissional(@Valid @RequestBody ProvaAptidaoProfissionalDTO provaAptidaoProfissionalDTO) throws URISyntaxException {
        log.debug("REST request to update ProvaAptidaoProfissional : {}", provaAptidaoProfissionalDTO);
        if (provaAptidaoProfissionalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProvaAptidaoProfissionalDTO result = provaAptidaoProfissionalService.save(provaAptidaoProfissionalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, provaAptidaoProfissionalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /prova-aptidao-profissionals} : get all the provaAptidaoProfissionals.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of provaAptidaoProfissionals in body.
     */
    @GetMapping("/prova-aptidao-profissionals")
    public ResponseEntity<List<ProvaAptidaoProfissionalDTO>> getAllProvaAptidaoProfissionals(Pageable pageable) {
        log.debug("REST request to get a page of ProvaAptidaoProfissionals");
        Page<ProvaAptidaoProfissionalDTO> page = provaAptidaoProfissionalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prova-aptidao-profissionals/:id} : get the "id" provaAptidaoProfissional.
     *
     * @param id the id of the provaAptidaoProfissionalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the provaAptidaoProfissionalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prova-aptidao-profissionals/{id}")
    public ResponseEntity<ProvaAptidaoProfissionalDTO> getProvaAptidaoProfissional(@PathVariable Long id) {
        log.debug("REST request to get ProvaAptidaoProfissional : {}", id);
        Optional<ProvaAptidaoProfissionalDTO> provaAptidaoProfissionalDTO = provaAptidaoProfissionalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(provaAptidaoProfissionalDTO);
    }

    /**
     * {@code DELETE  /prova-aptidao-profissionals/:id} : delete the "id" provaAptidaoProfissional.
     *
     * @param id the id of the provaAptidaoProfissionalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prova-aptidao-profissionals/{id}")
    public ResponseEntity<Void> deleteProvaAptidaoProfissional(@PathVariable Long id) {
        log.debug("REST request to delete ProvaAptidaoProfissional : {}", id);
        provaAptidaoProfissionalService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/prova-aptidao-profissionals?query=:query} : search for the provaAptidaoProfissional corresponding
     * to the query.
     *
     * @param query the query of the provaAptidaoProfissional search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prova-aptidao-profissionals")
    public ResponseEntity<List<ProvaAptidaoProfissionalDTO>> searchProvaAptidaoProfissionals(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProvaAptidaoProfissionals for query {}", query);
        Page<ProvaAptidaoProfissionalDTO> page = provaAptidaoProfissionalService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
