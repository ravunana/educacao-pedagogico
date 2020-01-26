package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.service.NotaService;
import com.ravunana.educacao.pedagogico.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pedagogico.service.dto.NotaDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pedagogico.domain.Nota}.
 */
@RestController
@RequestMapping("/api")
public class NotaResource {

    private final Logger log = LoggerFactory.getLogger(NotaResource.class);

    private static final String ENTITY_NAME = "pedagogicoNota";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotaService notaService;

    public NotaResource(NotaService notaService) {
        this.notaService = notaService;
    }

    /**
     * {@code POST  /notas} : Create a new nota.
     *
     * @param notaDTO the notaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notaDTO, or with status {@code 400 (Bad Request)} if the nota has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notas")
    public ResponseEntity<NotaDTO> createNota(@Valid @RequestBody NotaDTO notaDTO) throws URISyntaxException {
        log.debug("REST request to save Nota : {}", notaDTO);
        if (notaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nota cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotaDTO result = notaService.save(notaDTO);
        return ResponseEntity.created(new URI("/api/notas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notas} : Updates an existing nota.
     *
     * @param notaDTO the notaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notaDTO,
     * or with status {@code 400 (Bad Request)} if the notaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notas")
    public ResponseEntity<NotaDTO> updateNota(@Valid @RequestBody NotaDTO notaDTO) throws URISyntaxException {
        log.debug("REST request to update Nota : {}", notaDTO);
        if (notaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NotaDTO result = notaService.save(notaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /notas} : get all the notas.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notas in body.
     */
    @GetMapping("/notas")
    public ResponseEntity<List<NotaDTO>> getAllNotas(Pageable pageable) {
        log.debug("REST request to get a page of Notas");
        Page<NotaDTO> page = notaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notas/:id} : get the "id" nota.
     *
     * @param id the id of the notaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notas/{id}")
    public ResponseEntity<NotaDTO> getNota(@PathVariable Long id) {
        log.debug("REST request to get Nota : {}", id);
        Optional<NotaDTO> notaDTO = notaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notaDTO);
    }

    /**
     * {@code DELETE  /notas/:id} : delete the "id" nota.
     *
     * @param id the id of the notaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notas/{id}")
    public ResponseEntity<Void> deleteNota(@PathVariable Long id) {
        log.debug("REST request to delete Nota : {}", id);
        notaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/notas?query=:query} : search for the nota corresponding
     * to the query.
     *
     * @param query the query of the nota search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/notas")
    public ResponseEntity<List<NotaDTO>> searchNotas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Notas for query {}", query);
        Page<NotaDTO> page = notaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
