package com.sobexim.muturamaapp.service;

import com.sobexim.muturamaapp.domain.Job;
import com.sobexim.muturamaapp.repository.JobRepository;
import com.sobexim.muturamaapp.repository.search.JobSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Job.
 */
@Service
@Transactional
public class JobService {

    private final Logger log = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;

    private final JobSearchRepository jobSearchRepository;

    public JobService(JobRepository jobRepository, JobSearchRepository jobSearchRepository) {
        this.jobRepository = jobRepository;
        this.jobSearchRepository = jobSearchRepository;
    }

    /**
     * Save a job.
     *
     * @param job the entity to save
     * @return the persisted entity
     */
    public Job save(Job job) {
        log.debug("Request to save Job : {}", job);
        Job result = jobRepository.save(job);
        jobSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the jobs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Job> findAll(Pageable pageable) {
        log.debug("Request to get all Jobs");
        return jobRepository.findAll(pageable);
    }

    /**
     * Get one job by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Job findOne(Long id) {
        log.debug("Request to get Job : {}", id);
        return jobRepository.findOne(id);
    }

    /**
     * Delete the job by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Job : {}", id);
        jobRepository.delete(id);
        jobSearchRepository.delete(id);
    }

    /**
     * Search for the job corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Job> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Jobs for query {}", query);
        Page<Job> result = jobSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
