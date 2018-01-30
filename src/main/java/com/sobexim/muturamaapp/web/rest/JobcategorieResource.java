package com.sobexim.muturamaapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sobexim.muturamaapp.domain.Jobcategorie;

import com.sobexim.muturamaapp.repository.JobcategorieRepository;
import com.sobexim.muturamaapp.repository.search.JobcategorieSearchRepository;
import com.sobexim.muturamaapp.web.rest.errors.BadRequestAlertException;
import com.sobexim.muturamaapp.web.rest.util.HeaderUtil;
import com.sobexim.muturamaapp.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Jobcategorie.
 */
@RestController
@RequestMapping("/api")
public class JobcategorieResource {

    private final Logger log = LoggerFactory.getLogger(JobcategorieResource.class);

    private static final String ENTITY_NAME = "jobcategorie";

    private final JobcategorieRepository jobcategorieRepository;

    private final JobcategorieSearchRepository jobcategorieSearchRepository;

    public JobcategorieResource(JobcategorieRepository jobcategorieRepository, JobcategorieSearchRepository jobcategorieSearchRepository) {
        this.jobcategorieRepository = jobcategorieRepository;
        this.jobcategorieSearchRepository = jobcategorieSearchRepository;
    }

    /**
     * POST  /jobcategories : Create a new jobcategorie.
     *
     * @param jobcategorie the jobcategorie to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobcategorie, or with status 400 (Bad Request) if the jobcategorie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/jobcategories")
    @Timed
    public ResponseEntity<Jobcategorie> createJobcategorie(@Valid @RequestBody Jobcategorie jobcategorie) throws URISyntaxException {
        log.debug("REST request to save Jobcategorie : {}", jobcategorie);
        if (jobcategorie.getId() != null) {
            throw new BadRequestAlertException("A new jobcategorie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Jobcategorie result = jobcategorieRepository.save(jobcategorie);
        jobcategorieSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/jobcategories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jobcategories : Updates an existing jobcategorie.
     *
     * @param jobcategorie the jobcategorie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobcategorie,
     * or with status 400 (Bad Request) if the jobcategorie is not valid,
     * or with status 500 (Internal Server Error) if the jobcategorie couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/jobcategories")
    @Timed
    public ResponseEntity<Jobcategorie> updateJobcategorie(@Valid @RequestBody Jobcategorie jobcategorie) throws URISyntaxException {
        log.debug("REST request to update Jobcategorie : {}", jobcategorie);
        if (jobcategorie.getId() == null) {
            return createJobcategorie(jobcategorie);
        }
        Jobcategorie result = jobcategorieRepository.save(jobcategorie);
        jobcategorieSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobcategorie.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jobcategories : get all the jobcategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jobcategories in body
     */
    @GetMapping("/jobcategories")
    @Timed
    public ResponseEntity<List<Jobcategorie>> getAllJobcategories(Pageable pageable) {
        log.debug("REST request to get a page of Jobcategories");
        Page<Jobcategorie> page = jobcategorieRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jobcategories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /jobcategories/:id : get the "id" jobcategorie.
     *
     * @param id the id of the jobcategorie to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobcategorie, or with status 404 (Not Found)
     */
    @GetMapping("/jobcategories/{id}")
    @Timed
    public ResponseEntity<Jobcategorie> getJobcategorie(@PathVariable Long id) {
        log.debug("REST request to get Jobcategorie : {}", id);
        Jobcategorie jobcategorie = jobcategorieRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobcategorie));
    }

    /**
     * DELETE  /jobcategories/:id : delete the "id" jobcategorie.
     *
     * @param id the id of the jobcategorie to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/jobcategories/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobcategorie(@PathVariable Long id) {
        log.debug("REST request to delete Jobcategorie : {}", id);
        jobcategorieRepository.delete(id);
        jobcategorieSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/jobcategories?query=:query : search for the jobcategorie corresponding
     * to the query.
     *
     * @param query the query of the jobcategorie search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/jobcategories")
    @Timed
    public ResponseEntity<List<Jobcategorie>> searchJobcategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Jobcategories for query {}", query);
        Page<Jobcategorie> page = jobcategorieSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/jobcategories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
