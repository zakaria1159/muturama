package com.sobexim.muturamaapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sobexim.muturamaapp.domain.Job;
import com.sobexim.muturamaapp.service.JobService;
import com.sobexim.muturamaapp.web.rest.errors.BadRequestAlertException;
import com.sobexim.muturamaapp.web.rest.util.HeaderUtil;
import com.sobexim.muturamaapp.web.rest.util.PaginationUtil;
import com.sobexim.muturamaapp.service.dto.JobCriteria;
import com.sobexim.muturamaapp.service.JobQueryService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Job.
 */
@RestController
@RequestMapping("/api")
public class JobResource {

    private final Logger log = LoggerFactory.getLogger(JobResource.class);

    private static final String ENTITY_NAME = "job";

    private final JobService jobService;

    private final JobQueryService jobQueryService;

    public JobResource(JobService jobService, JobQueryService jobQueryService) {
        this.jobService = jobService;
        this.jobQueryService = jobQueryService;
    }

    /**
     * POST  /jobs : Create a new job.
     *
     * @param job the job to create
     * @return the ResponseEntity with status 201 (Created) and with body the new job, or with status 400 (Bad Request) if the job has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/jobs")
    @Timed
    public ResponseEntity<Job> createJob(@Valid @RequestBody Job job) throws URISyntaxException {
        log.debug("REST request to save Job : {}", job);
        if (job.getId() != null) {
            throw new BadRequestAlertException("A new job cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Job result = jobService.save(job);
        return ResponseEntity.created(new URI("/api/jobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jobs : Updates an existing job.
     *
     * @param job the job to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated job,
     * or with status 400 (Bad Request) if the job is not valid,
     * or with status 500 (Internal Server Error) if the job couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/jobs")
    @Timed
    public ResponseEntity<Job> updateJob(@Valid @RequestBody Job job) throws URISyntaxException {
        log.debug("REST request to update Job : {}", job);
        if (job.getId() == null) {
            return createJob(job);
        }
        Job result = jobService.save(job);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, job.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jobs : get all the jobs.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
     */
    @GetMapping("/jobs")
    @Timed
    public ResponseEntity<List<Job>> getAllJobs(JobCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Jobs by criteria: {}", criteria);
        Page<Job> page = jobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jobs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /jobs/:id : get the "id" job.
     *
     * @param id the id of the job to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the job, or with status 404 (Not Found)
     */
    @GetMapping("/jobs/{id}")
    @Timed
    public ResponseEntity<Job> getJob(@PathVariable Long id) {
        log.debug("REST request to get Job : {}", id);
        Job job = jobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(job));
    }

    /**
     * DELETE  /jobs/:id : delete the "id" job.
     *
     * @param id the id of the job to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/jobs/{id}")
    @Timed
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        log.debug("REST request to delete Job : {}", id);
        jobService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/jobs?query=:query : search for the job corresponding
     * to the query.
     *
     * @param query the query of the job search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/jobs")
    @Timed
    public ResponseEntity<List<Job>> searchJobs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Jobs for query {}", query);
        Page<Job> page = jobService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/jobs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
