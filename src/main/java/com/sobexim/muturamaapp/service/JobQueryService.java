package com.sobexim.muturamaapp.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.sobexim.muturamaapp.domain.Job;
import com.sobexim.muturamaapp.domain.*; // for static metamodels
import com.sobexim.muturamaapp.repository.JobRepository;
import com.sobexim.muturamaapp.repository.search.JobSearchRepository;
import com.sobexim.muturamaapp.service.dto.JobCriteria;


/**
 * Service for executing complex queries for Job entities in the database.
 * The main input is a {@link JobCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Job} or a {@link Page} of {@link Job} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobQueryService extends QueryService<Job> {

    private final Logger log = LoggerFactory.getLogger(JobQueryService.class);


    private final JobRepository jobRepository;

    private final JobSearchRepository jobSearchRepository;

    public JobQueryService(JobRepository jobRepository, JobSearchRepository jobSearchRepository) {
        this.jobRepository = jobRepository;
        this.jobSearchRepository = jobSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Job} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Job> findByCriteria(JobCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Job> specification = createSpecification(criteria);
        return jobRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Job} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Job> findByCriteria(JobCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Job> specification = createSpecification(criteria);
        return jobRepository.findAll(specification, page);
    }

    /**
     * Function to convert JobCriteria to a {@link Specifications}
     */
    private Specifications<Job> createSpecification(JobCriteria criteria) {
        Specifications<Job> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Job_.id));
            }
            if (criteria.getTitre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitre(), Job_.titre));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), Job_.points));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Job_.description));
            }
            if (criteria.getEtat() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEtat(), Job_.etat));
            }
            if (criteria.getTempsderealisation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTempsderealisation(), Job_.tempsderealisation));
            }
            if (criteria.getJobcategorieId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getJobcategorieId(), Job_.jobcategorie, Jobcategorie_.id));
            }
        }
        return specification;
    }

}
