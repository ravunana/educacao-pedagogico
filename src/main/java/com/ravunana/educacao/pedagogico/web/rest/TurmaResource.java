package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.service.TurmaService;
import com.ravunana.educacao.pedagogico.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pedagogico.service.dto.TurmaDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pedagogico.domain.Turma}.
 */
@RestController
@RequestMapping("/api")
public class TurmaResource {

    private final Logger log = LoggerFactory.getLogger(TurmaResource.class);

    private static final String ENTITY_NAME = "pedagogicoTurma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TurmaService turmaService;

    public TurmaResource(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    /**
     * {@code POST  /turmas} : Create a new turma.
     *
     * @param turmaDTO the turmaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new turmaDTO, or with status {@code 400 (Bad Request)} if the turma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/turmas")
    public ResponseEntity<TurmaDTO> createTurma(@Valid @RequestBody TurmaDTO turmaDTO) throws URISyntaxException {
        log.debug("REST request to save Turma : {}", turmaDTO);
        if (turmaDTO.getId() != null) {
            throw new BadRequestAlertException("A new turma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TurmaDTO result = turmaService.save(turmaDTO);
        return ResponseEntity.created(new URI("/api/turmas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /turmas} : Updates an existing turma.
     *
     * @param turmaDTO the turmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turmaDTO,
     * or with status {@code 400 (Bad Request)} if the turmaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the turmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/turmas")
    public ResponseEntity<TurmaDTO> updateTurma(@Valid @RequestBody TurmaDTO turmaDTO) throws URISyntaxException {
        log.debug("REST request to update Turma : {}", turmaDTO);
        if (turmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TurmaDTO result = turmaService.save(turmaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, turmaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /turmas} : get all the turmas.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of turmas in body.
     */
    @GetMapping("/turmas")
    public ResponseEntity<List<TurmaDTO>> getAllTurmas(Pageable pageable) {
        log.debug("REST request to get a page of Turmas");
        Page<TurmaDTO> page = turmaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /turmas/:id} : get the "id" turma.
     *
     * @param id the id of the turmaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the turmaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/turmas/{id}")
    public ResponseEntity<TurmaDTO> getTurma(@PathVariable Long id) {
        log.debug("REST request to get Turma : {}", id);
        Optional<TurmaDTO> turmaDTO = turmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(turmaDTO);
    }

    /**
     * {@code DELETE  /turmas/:id} : delete the "id" turma.
     *
     * @param id the id of the turmaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/turmas/{id}")
    public ResponseEntity<Void> deleteTurma(@PathVariable Long id) {
        log.debug("REST request to delete Turma : {}", id);
        turmaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/turmas?query=:query} : search for the turma corresponding
     * to the query.
     *
     * @param query the query of the turma search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/turmas")
    public ResponseEntity<List<TurmaDTO>> searchTurmas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Turmas for query {}", query);
        Page<TurmaDTO> page = turmaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
