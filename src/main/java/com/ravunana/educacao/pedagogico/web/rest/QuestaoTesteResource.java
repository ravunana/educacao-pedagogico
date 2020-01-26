package com.ravunana.educacao.pedagogico.web.rest;

import com.ravunana.educacao.pedagogico.service.QuestaoTesteService;
import com.ravunana.educacao.pedagogico.web.rest.errors.BadRequestAlertException;
import com.ravunana.educacao.pedagogico.service.dto.QuestaoTesteDTO;

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
 * REST controller for managing {@link com.ravunana.educacao.pedagogico.domain.QuestaoTeste}.
 */
@RestController
@RequestMapping("/api")
public class QuestaoTesteResource {

    private final Logger log = LoggerFactory.getLogger(QuestaoTesteResource.class);

    private static final String ENTITY_NAME = "pedagogicoQuestaoTeste";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestaoTesteService questaoTesteService;

    public QuestaoTesteResource(QuestaoTesteService questaoTesteService) {
        this.questaoTesteService = questaoTesteService;
    }

    /**
     * {@code POST  /questao-testes} : Create a new questaoTeste.
     *
     * @param questaoTesteDTO the questaoTesteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questaoTesteDTO, or with status {@code 400 (Bad Request)} if the questaoTeste has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questao-testes")
    public ResponseEntity<QuestaoTesteDTO> createQuestaoTeste(@Valid @RequestBody QuestaoTesteDTO questaoTesteDTO) throws URISyntaxException {
        log.debug("REST request to save QuestaoTeste : {}", questaoTesteDTO);
        if (questaoTesteDTO.getId() != null) {
            throw new BadRequestAlertException("A new questaoTeste cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestaoTesteDTO result = questaoTesteService.save(questaoTesteDTO);
        return ResponseEntity.created(new URI("/api/questao-testes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /questao-testes} : Updates an existing questaoTeste.
     *
     * @param questaoTesteDTO the questaoTesteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questaoTesteDTO,
     * or with status {@code 400 (Bad Request)} if the questaoTesteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questaoTesteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/questao-testes")
    public ResponseEntity<QuestaoTesteDTO> updateQuestaoTeste(@Valid @RequestBody QuestaoTesteDTO questaoTesteDTO) throws URISyntaxException {
        log.debug("REST request to update QuestaoTeste : {}", questaoTesteDTO);
        if (questaoTesteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QuestaoTesteDTO result = questaoTesteService.save(questaoTesteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questaoTesteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /questao-testes} : get all the questaoTestes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questaoTestes in body.
     */
    @GetMapping("/questao-testes")
    public ResponseEntity<List<QuestaoTesteDTO>> getAllQuestaoTestes(Pageable pageable) {
        log.debug("REST request to get a page of QuestaoTestes");
        Page<QuestaoTesteDTO> page = questaoTesteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /questao-testes/:id} : get the "id" questaoTeste.
     *
     * @param id the id of the questaoTesteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questaoTesteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questao-testes/{id}")
    public ResponseEntity<QuestaoTesteDTO> getQuestaoTeste(@PathVariable Long id) {
        log.debug("REST request to get QuestaoTeste : {}", id);
        Optional<QuestaoTesteDTO> questaoTesteDTO = questaoTesteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questaoTesteDTO);
    }

    /**
     * {@code DELETE  /questao-testes/:id} : delete the "id" questaoTeste.
     *
     * @param id the id of the questaoTesteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/questao-testes/{id}")
    public ResponseEntity<Void> deleteQuestaoTeste(@PathVariable Long id) {
        log.debug("REST request to delete QuestaoTeste : {}", id);
        questaoTesteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/questao-testes?query=:query} : search for the questaoTeste corresponding
     * to the query.
     *
     * @param query the query of the questaoTeste search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/questao-testes")
    public ResponseEntity<List<QuestaoTesteDTO>> searchQuestaoTestes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of QuestaoTestes for query {}", query);
        Page<QuestaoTesteDTO> page = questaoTesteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
