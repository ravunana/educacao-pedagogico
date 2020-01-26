package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.service.RespostaQuestaoService;
import com.ravunana.educacao.pedagogico.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pedagogico.service.dto.RespostaQuestaoDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pedagogico.domain.RespostaQuestao}.
 */
@RestController
@RequestMapping("/api")
public class RespostaQuestaoResource {

    private final Logger log = LoggerFactory.getLogger(RespostaQuestaoResource.class);

    private static final String ENTITY_NAME = "pedagogicoRespostaQuestao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RespostaQuestaoService respostaQuestaoService;

    public RespostaQuestaoResource(RespostaQuestaoService respostaQuestaoService) {
        this.respostaQuestaoService = respostaQuestaoService;
    }

    /**
     * {@code POST  /resposta-questaos} : Create a new respostaQuestao.
     *
     * @param respostaQuestaoDTO the respostaQuestaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new respostaQuestaoDTO, or with status {@code 400 (Bad Request)} if the respostaQuestao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resposta-questaos")
    public ResponseEntity<RespostaQuestaoDTO> createRespostaQuestao(@Valid @RequestBody RespostaQuestaoDTO respostaQuestaoDTO) throws URISyntaxException {
        log.debug("REST request to save RespostaQuestao : {}", respostaQuestaoDTO);
        if (respostaQuestaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new respostaQuestao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RespostaQuestaoDTO result = respostaQuestaoService.save(respostaQuestaoDTO);
        return ResponseEntity.created(new URI("/api/resposta-questaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resposta-questaos} : Updates an existing respostaQuestao.
     *
     * @param respostaQuestaoDTO the respostaQuestaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated respostaQuestaoDTO,
     * or with status {@code 400 (Bad Request)} if the respostaQuestaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the respostaQuestaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resposta-questaos")
    public ResponseEntity<RespostaQuestaoDTO> updateRespostaQuestao(@Valid @RequestBody RespostaQuestaoDTO respostaQuestaoDTO) throws URISyntaxException {
        log.debug("REST request to update RespostaQuestao : {}", respostaQuestaoDTO);
        if (respostaQuestaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RespostaQuestaoDTO result = respostaQuestaoService.save(respostaQuestaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, respostaQuestaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resposta-questaos} : get all the respostaQuestaos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of respostaQuestaos in body.
     */
    @GetMapping("/resposta-questaos")
    public ResponseEntity<List<RespostaQuestaoDTO>> getAllRespostaQuestaos(Pageable pageable) {
        log.debug("REST request to get a page of RespostaQuestaos");
        Page<RespostaQuestaoDTO> page = respostaQuestaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /resposta-questaos/:id} : get the "id" respostaQuestao.
     *
     * @param id the id of the respostaQuestaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the respostaQuestaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resposta-questaos/{id}")
    public ResponseEntity<RespostaQuestaoDTO> getRespostaQuestao(@PathVariable Long id) {
        log.debug("REST request to get RespostaQuestao : {}", id);
        Optional<RespostaQuestaoDTO> respostaQuestaoDTO = respostaQuestaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(respostaQuestaoDTO);
    }

    /**
     * {@code DELETE  /resposta-questaos/:id} : delete the "id" respostaQuestao.
     *
     * @param id the id of the respostaQuestaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resposta-questaos/{id}")
    public ResponseEntity<Void> deleteRespostaQuestao(@PathVariable Long id) {
        log.debug("REST request to delete RespostaQuestao : {}", id);
        respostaQuestaoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/resposta-questaos?query=:query} : search for the respostaQuestao corresponding
     * to the query.
     *
     * @param query the query of the respostaQuestao search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/resposta-questaos")
    public ResponseEntity<List<RespostaQuestaoDTO>> searchRespostaQuestaos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RespostaQuestaos for query {}", query);
        Page<RespostaQuestaoDTO> page = respostaQuestaoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
