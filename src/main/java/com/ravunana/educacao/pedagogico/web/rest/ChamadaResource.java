package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.service.ChamadaService;
import com.ravunana.educacao.pedagogico.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pedagogico.service.dto.ChamadaDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pedagogico.domain.Chamada}.
 */
@RestController
@RequestMapping("/api")
public class ChamadaResource {

    private final Logger log = LoggerFactory.getLogger(ChamadaResource.class);

    private static final String ENTITY_NAME = "pedagogicoChamada";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChamadaService chamadaService;

    public ChamadaResource(ChamadaService chamadaService) {
        this.chamadaService = chamadaService;
    }

    /**
     * {@code POST  /chamadas} : Create a new chamada.
     *
     * @param chamadaDTO the chamadaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chamadaDTO, or with status {@code 400 (Bad Request)} if the chamada has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chamadas")
    public ResponseEntity<ChamadaDTO> createChamada(@Valid @RequestBody ChamadaDTO chamadaDTO) throws URISyntaxException {
        log.debug("REST request to save Chamada : {}", chamadaDTO);
        if (chamadaDTO.getId() != null) {
            throw new BadRequestAlertException("A new chamada cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChamadaDTO result = chamadaService.save(chamadaDTO);
        return ResponseEntity.created(new URI("/api/chamadas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chamadas} : Updates an existing chamada.
     *
     * @param chamadaDTO the chamadaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chamadaDTO,
     * or with status {@code 400 (Bad Request)} if the chamadaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chamadaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chamadas")
    public ResponseEntity<ChamadaDTO> updateChamada(@Valid @RequestBody ChamadaDTO chamadaDTO) throws URISyntaxException {
        log.debug("REST request to update Chamada : {}", chamadaDTO);
        if (chamadaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChamadaDTO result = chamadaService.save(chamadaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chamadaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /chamadas} : get all the chamadas.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chamadas in body.
     */
    @GetMapping("/chamadas")
    public ResponseEntity<List<ChamadaDTO>> getAllChamadas(Pageable pageable) {
        log.debug("REST request to get a page of Chamadas");
        Page<ChamadaDTO> page = chamadaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chamadas/:id} : get the "id" chamada.
     *
     * @param id the id of the chamadaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chamadaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chamadas/{id}")
    public ResponseEntity<ChamadaDTO> getChamada(@PathVariable Long id) {
        log.debug("REST request to get Chamada : {}", id);
        Optional<ChamadaDTO> chamadaDTO = chamadaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chamadaDTO);
    }

    /**
     * {@code DELETE  /chamadas/:id} : delete the "id" chamada.
     *
     * @param id the id of the chamadaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chamadas/{id}")
    public ResponseEntity<Void> deleteChamada(@PathVariable Long id) {
        log.debug("REST request to delete Chamada : {}", id);
        chamadaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/chamadas?query=:query} : search for the chamada corresponding
     * to the query.
     *
     * @param query the query of the chamada search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/chamadas")
    public ResponseEntity<List<ChamadaDTO>> searchChamadas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Chamadas for query {}", query);
        Page<ChamadaDTO> page = chamadaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
