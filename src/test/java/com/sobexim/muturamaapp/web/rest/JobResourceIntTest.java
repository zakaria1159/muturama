package com.sobexim.muturamaapp.web.rest;

import com.sobexim.muturamaapp.MuturamaApp;

import com.sobexim.muturamaapp.domain.Job;
import com.sobexim.muturamaapp.domain.Jobcategorie;
import com.sobexim.muturamaapp.domain.Utilisateur;
import com.sobexim.muturamaapp.domain.Utilisateur;
import com.sobexim.muturamaapp.repository.JobRepository;
import com.sobexim.muturamaapp.service.JobService;
import com.sobexim.muturamaapp.repository.search.JobSearchRepository;
import com.sobexim.muturamaapp.web.rest.errors.ExceptionTranslator;
import com.sobexim.muturamaapp.service.dto.JobCriteria;
import com.sobexim.muturamaapp.service.JobQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sobexim.muturamaapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JobResource REST controller.
 *
 * @see JobResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MuturamaApp.class)
public class JobResourceIntTest {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPSDEREALISATION = "AAAAAAAAAA";
    private static final String UPDATED_TEMPSDEREALISATION = "BBBBBBBBBB";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobSearchRepository jobSearchRepository;

    @Autowired
    private JobQueryService jobQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobMockMvc;

    private Job job;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobResource jobResource = new JobResource(jobService, jobQueryService);
        this.restJobMockMvc = MockMvcBuilders.standaloneSetup(jobResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createEntity(EntityManager em) {
        Job job = new Job()
            .titre(DEFAULT_TITRE)
            .points(DEFAULT_POINTS)
            .description(DEFAULT_DESCRIPTION)
            .etat(DEFAULT_ETAT)
            .tempsderealisation(DEFAULT_TEMPSDEREALISATION);
        // Add required entity
        Jobcategorie jobcategorie = JobcategorieResourceIntTest.createEntity(em);
        em.persist(jobcategorie);
        em.flush();
        job.setJobcategorie(jobcategorie);
        return job;
    }

    @Before
    public void initTest() {
        jobSearchRepository.deleteAll();
        job = createEntity(em);
    }

    @Test
    @Transactional
    public void createJob() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job
        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate + 1);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testJob.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testJob.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testJob.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testJob.getTempsderealisation()).isEqualTo(DEFAULT_TEMPSDEREALISATION);

        // Validate the Job in Elasticsearch
        Job jobEs = jobSearchRepository.findOne(testJob.getId());
        assertThat(jobEs).isEqualToIgnoringGivenFields(testJob);
    }

    @Test
    @Transactional
    public void createJobWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job with an existing ID
        job.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setTitre(null);

        // Create the Job, which fails.

        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setPoints(null);

        // Create the Job, which fails.

        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setDescription(null);

        // Create the Job, which fails.

        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobs() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].tempsderealisation").value(hasItem(DEFAULT_TEMPSDEREALISATION.toString())));
    }

    @Test
    @Transactional
    public void getJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(job.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.tempsderealisation").value(DEFAULT_TEMPSDEREALISATION.toString()));
    }

    @Test
    @Transactional
    public void getAllJobsByTitreIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where titre equals to DEFAULT_TITRE
        defaultJobShouldBeFound("titre.equals=" + DEFAULT_TITRE);

        // Get all the jobList where titre equals to UPDATED_TITRE
        defaultJobShouldNotBeFound("titre.equals=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    public void getAllJobsByTitreIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where titre in DEFAULT_TITRE or UPDATED_TITRE
        defaultJobShouldBeFound("titre.in=" + DEFAULT_TITRE + "," + UPDATED_TITRE);

        // Get all the jobList where titre equals to UPDATED_TITRE
        defaultJobShouldNotBeFound("titre.in=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    public void getAllJobsByTitreIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where titre is not null
        defaultJobShouldBeFound("titre.specified=true");

        // Get all the jobList where titre is null
        defaultJobShouldNotBeFound("titre.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where points equals to DEFAULT_POINTS
        defaultJobShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the jobList where points equals to UPDATED_POINTS
        defaultJobShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllJobsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultJobShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the jobList where points equals to UPDATED_POINTS
        defaultJobShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllJobsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where points is not null
        defaultJobShouldBeFound("points.specified=true");

        // Get all the jobList where points is null
        defaultJobShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where points greater than or equals to DEFAULT_POINTS
        defaultJobShouldBeFound("points.greaterOrEqualThan=" + DEFAULT_POINTS);

        // Get all the jobList where points greater than or equals to UPDATED_POINTS
        defaultJobShouldNotBeFound("points.greaterOrEqualThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllJobsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where points less than or equals to DEFAULT_POINTS
        defaultJobShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the jobList where points less than or equals to UPDATED_POINTS
        defaultJobShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }


    @Test
    @Transactional
    public void getAllJobsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where description equals to DEFAULT_DESCRIPTION
        defaultJobShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the jobList where description equals to UPDATED_DESCRIPTION
        defaultJobShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllJobsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultJobShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the jobList where description equals to UPDATED_DESCRIPTION
        defaultJobShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllJobsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where description is not null
        defaultJobShouldBeFound("description.specified=true");

        // Get all the jobList where description is null
        defaultJobShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where etat equals to DEFAULT_ETAT
        defaultJobShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the jobList where etat equals to UPDATED_ETAT
        defaultJobShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllJobsByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultJobShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the jobList where etat equals to UPDATED_ETAT
        defaultJobShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllJobsByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where etat is not null
        defaultJobShouldBeFound("etat.specified=true");

        // Get all the jobList where etat is null
        defaultJobShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByTempsderealisationIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tempsderealisation equals to DEFAULT_TEMPSDEREALISATION
        defaultJobShouldBeFound("tempsderealisation.equals=" + DEFAULT_TEMPSDEREALISATION);

        // Get all the jobList where tempsderealisation equals to UPDATED_TEMPSDEREALISATION
        defaultJobShouldNotBeFound("tempsderealisation.equals=" + UPDATED_TEMPSDEREALISATION);
    }

    @Test
    @Transactional
    public void getAllJobsByTempsderealisationIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tempsderealisation in DEFAULT_TEMPSDEREALISATION or UPDATED_TEMPSDEREALISATION
        defaultJobShouldBeFound("tempsderealisation.in=" + DEFAULT_TEMPSDEREALISATION + "," + UPDATED_TEMPSDEREALISATION);

        // Get all the jobList where tempsderealisation equals to UPDATED_TEMPSDEREALISATION
        defaultJobShouldNotBeFound("tempsderealisation.in=" + UPDATED_TEMPSDEREALISATION);
    }

    @Test
    @Transactional
    public void getAllJobsByTempsderealisationIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where tempsderealisation is not null
        defaultJobShouldBeFound("tempsderealisation.specified=true");

        // Get all the jobList where tempsderealisation is null
        defaultJobShouldNotBeFound("tempsderealisation.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByJobcategorieIsEqualToSomething() throws Exception {
        // Initialize the database
        Jobcategorie jobcategorie = JobcategorieResourceIntTest.createEntity(em);
        em.persist(jobcategorie);
        em.flush();
        job.setJobcategorie(jobcategorie);
        jobRepository.saveAndFlush(job);
        Long jobcategorieId = jobcategorie.getId();

        // Get all the jobList where jobcategorie equals to jobcategorieId
        defaultJobShouldBeFound("jobcategorieId.equals=" + jobcategorieId);

        // Get all the jobList where jobcategorie equals to jobcategorieId + 1
        defaultJobShouldNotBeFound("jobcategorieId.equals=" + (jobcategorieId + 1));
    }


    @Test
    @Transactional
    public void getAllJobsByJobtoutilisateurIsEqualToSomething() throws Exception {
        // Initialize the database
        Utilisateur jobtoutilisateur = UtilisateurResourceIntTest.createEntity(em);
        em.persist(jobtoutilisateur);
        em.flush();
        job.setJobtoutilisateur(jobtoutilisateur);
        jobRepository.saveAndFlush(job);
        Long jobtoutilisateurId = jobtoutilisateur.getId();

        // Get all the jobList where jobtoutilisateur equals to jobtoutilisateurId
        defaultJobShouldBeFound("jobtoutilisateurId.equals=" + jobtoutilisateurId);

        // Get all the jobList where jobtoutilisateur equals to jobtoutilisateurId + 1
        defaultJobShouldNotBeFound("jobtoutilisateurId.equals=" + (jobtoutilisateurId + 1));
    }


    @Test
    @Transactional
    public void getAllJobsByPostulantIsEqualToSomething() throws Exception {
        // Initialize the database
        Utilisateur postulant = UtilisateurResourceIntTest.createEntity(em);
        em.persist(postulant);
        em.flush();
        job.addPostulant(postulant);
        jobRepository.saveAndFlush(job);
        Long postulantId = postulant.getId();

        // Get all the jobList where postulant equals to postulantId
        defaultJobShouldBeFound("postulantId.equals=" + postulantId);

        // Get all the jobList where postulant equals to postulantId + 1
        defaultJobShouldNotBeFound("postulantId.equals=" + (postulantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultJobShouldBeFound(String filter) throws Exception {
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].tempsderealisation").value(hasItem(DEFAULT_TEMPSDEREALISATION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultJobShouldNotBeFound(String filter) throws Exception {
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingJob() throws Exception {
        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJob() throws Exception {
        // Initialize the database
        jobService.save(job);

        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job
        Job updatedJob = jobRepository.findOne(job.getId());
        // Disconnect from session so that the updates on updatedJob are not directly saved in db
        em.detach(updatedJob);
        updatedJob
            .titre(UPDATED_TITRE)
            .points(UPDATED_POINTS)
            .description(UPDATED_DESCRIPTION)
            .etat(UPDATED_ETAT)
            .tempsderealisation(UPDATED_TEMPSDEREALISATION);

        restJobMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJob)))
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testJob.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testJob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testJob.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testJob.getTempsderealisation()).isEqualTo(UPDATED_TEMPSDEREALISATION);

        // Validate the Job in Elasticsearch
        Job jobEs = jobSearchRepository.findOne(testJob.getId());
        assertThat(jobEs).isEqualToIgnoringGivenFields(testJob);
    }

    @Test
    @Transactional
    public void updateNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Create the Job

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJob() throws Exception {
        // Initialize the database
        jobService.save(job);

        int databaseSizeBeforeDelete = jobRepository.findAll().size();

        // Get the job
        restJobMockMvc.perform(delete("/api/jobs/{id}", job.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jobExistsInEs = jobSearchRepository.exists(job.getId());
        assertThat(jobExistsInEs).isFalse();

        // Validate the database is empty
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJob() throws Exception {
        // Initialize the database
        jobService.save(job);

        // Search the job
        restJobMockMvc.perform(get("/api/_search/jobs?query=id:" + job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].tempsderealisation").value(hasItem(DEFAULT_TEMPSDEREALISATION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Job.class);
        Job job1 = new Job();
        job1.setId(1L);
        Job job2 = new Job();
        job2.setId(job1.getId());
        assertThat(job1).isEqualTo(job2);
        job2.setId(2L);
        assertThat(job1).isNotEqualTo(job2);
        job1.setId(null);
        assertThat(job1).isNotEqualTo(job2);
    }
}
