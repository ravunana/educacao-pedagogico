package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.service.TesteConhecimentoService;
import com.ravunana.educacao.pedagogico.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pedagogico.service.dto.TesteConhecimentoDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pedagogico.domain.TesteConhecimento}.
 */
@RestController
@RequestMapping("/api")
public class TesteConhecimentoResource {

    private final Logger log = LoggerFactory.getLogger(TesteConhecimentoResource.class);

    private static final String ENTITY_NAME = "pedagogicoTesteConhecimento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TesteConhecimentoService testeConhecimentoService;

    public TesteConhecimentoResource(TesteConhecimentoService testeConhecimentoService) {
        this.testeConhecimentoService = testeConhecimentoService;
    }

    /**
     * {@code POST  /teste-conhecimentos} : Create a new testeConhecimento.
     *
     * @param testeConhecimentoDTO the testeConhecimentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testeConhecimentoDTO, or with status {@code 400 (Bad Request)} if the testeConhecimento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/teste-conhecimentos")
    public ResponseEntity<TesteConhecimentoDTO> createTesteConhecimento(@Valid @RequestBody TesteConhecimentoDTO testeConhecimentoDTO) throws URISyntaxException {
        log.debug("REST request to save TesteConhecimento : {}", testeConhecimentoDTO);
        if (testeConhecimentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new testeConhecimento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TesteConhecimentoDTO result = testeConhecimentoService.save(testeConhecimentoDTO);
        return ResponseEntity.created(new URI("/api/teste-conhecimentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /teste-conhecimentos} : Updates an existing testeConhecimento.
     *
     * @param testeConhecimentoDTO the testeConhecimentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testeConhecimentoDTO,
     * or with status {@code 400 (Bad Request)} if the testeConhecimentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testeConhecimentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/teste-conhecimentos")
    public ResponseEntity<TesteConhecimentoDTO> updateTesteConhecimento(@Valid @RequestBody TesteConhecimentoDTO testeConhecimentoDTO) throws URISyntaxException {
        log.debug("REST request to update TesteConhecimento : {}", testeConhecimentoDTO);
        if (testeConhecimentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TesteConhecimentoDTO result = testeConhecimentoService.save(testeConhecimentoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testeConhecimentoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /teste-conhecimentos} : get all the testeConhecimentos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testeConhecimentos in body.
     */
    @GetMapping("/teste-conhecimentos")
    public ResponseEntity<List<TesteConhecimentoDTO>> getAllTesteConhecimentos(Pageable pageable) {
        log.debug("REST request to get a page of TesteConhecimentos");
        Page<TesteConhecimentoDTO> page = testeConhecimentoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /teste-conhecimentos/:id} : get the "id" testeConhecimento.
     *
     * @param id the id of the testeConhecimentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testeConhecimentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teste-conhecimentos/{id}")
    public ResponseEntity<TesteConhecimentoDTO> getTesteConhecimento(@PathVariable Long id) {
        log.debug("REST request to get TesteConhecimento : {}", id);
        Optional<TesteConhecimentoDTO> testeConhecimentoDTO = testeConhecimentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testeConhecimentoDTO);
    }

    /**
     * {@code DELETE  /teste-conhecimentos/:id} : delete the "id" testeConhecimento.
     *
     * @param id the id of the testeConhecimentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/teste-conhecimentos/{id}")
    public ResponseEntity<Void> deleteTesteConhecimento(@PathVariable Long id) {
        log.debug("REST request to delete TesteConhecimento : {}", id);
        testeConhecimentoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/teste-conhecimentos?query=:query} : search for the testeConhecimento corresponding
     * to the query.
     *
     * @param query the query of the testeConhecimento search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/teste-conhecimentos")
    public ResponseEntity<List<TesteConhecimentoDTO>> searchTesteConhecimentos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TesteConhecimentos for query {}", query);
        Page<TesteConhecimentoDTO> page = testeConhecimentoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
