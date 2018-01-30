package com.sobexim.muturamaapp.web.rest;

import com.sobexim.muturamaapp.MuturamaApp;

import com.sobexim.muturamaapp.domain.Jobcategorie;
import com.sobexim.muturamaapp.repository.JobcategorieRepository;
import com.sobexim.muturamaapp.repository.search.JobcategorieSearchRepository;
import com.sobexim.muturamaapp.web.rest.errors.ExceptionTranslator;

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
 * Test class for the JobcategorieResource REST controller.
 *
 * @see JobcategorieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MuturamaApp.class)
public class JobcategorieResourceIntTest {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private JobcategorieRepository jobcategorieRepository;

    @Autowired
    private JobcategorieSearchRepository jobcategorieSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobcategorieMockMvc;

    private Jobcategorie jobcategorie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobcategorieResource jobcategorieResource = new JobcategorieResource(jobcategorieRepository, jobcategorieSearchRepository);
        this.restJobcategorieMockMvc = MockMvcBuilders.standaloneSetup(jobcategorieResource)
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
    public static Jobcategorie createEntity(EntityManager em) {
        Jobcategorie jobcategorie = new Jobcategorie()
            .titre(DEFAULT_TITRE)
            .description(DEFAULT_DESCRIPTION);
        return jobcategorie;
    }

    @Before
    public void initTest() {
        jobcategorieSearchRepository.deleteAll();
        jobcategorie = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobcategorie() throws Exception {
        int databaseSizeBeforeCreate = jobcategorieRepository.findAll().size();

        // Create the Jobcategorie
        restJobcategorieMockMvc.perform(post("/api/jobcategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobcategorie)))
            .andExpect(status().isCreated());

        // Validate the Jobcategorie in the database
        List<Jobcategorie> jobcategorieList = jobcategorieRepository.findAll();
        assertThat(jobcategorieList).hasSize(databaseSizeBeforeCreate + 1);
        Jobcategorie testJobcategorie = jobcategorieList.get(jobcategorieList.size() - 1);
        assertThat(testJobcategorie.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testJobcategorie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Jobcategorie in Elasticsearch
        Jobcategorie jobcategorieEs = jobcategorieSearchRepository.findOne(testJobcategorie.getId());
        assertThat(jobcategorieEs).isEqualToIgnoringGivenFields(testJobcategorie);
    }

    @Test
    @Transactional
    public void createJobcategorieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobcategorieRepository.findAll().size();

        // Create the Jobcategorie with an existing ID
        jobcategorie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobcategorieMockMvc.perform(post("/api/jobcategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobcategorie)))
            .andExpect(status().isBadRequest());

        // Validate the Jobcategorie in the database
        List<Jobcategorie> jobcategorieList = jobcategorieRepository.findAll();
        assertThat(jobcategorieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobcategorieRepository.findAll().size();
        // set the field null
        jobcategorie.setTitre(null);

        // Create the Jobcategorie, which fails.

        restJobcategorieMockMvc.perform(post("/api/jobcategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobcategorie)))
            .andExpect(status().isBadRequest());

        List<Jobcategorie> jobcategorieList = jobcategorieRepository.findAll();
        assertThat(jobcategorieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobcategories() throws Exception {
        // Initialize the database
        jobcategorieRepository.saveAndFlush(jobcategorie);

        // Get all the jobcategorieList
        restJobcategorieMockMvc.perform(get("/api/jobcategories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobcategorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getJobcategorie() throws Exception {
        // Initialize the database
        jobcategorieRepository.saveAndFlush(jobcategorie);

        // Get the jobcategorie
        restJobcategorieMockMvc.perform(get("/api/jobcategories/{id}", jobcategorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobcategorie.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobcategorie() throws Exception {
        // Get the jobcategorie
        restJobcategorieMockMvc.perform(get("/api/jobcategories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobcategorie() throws Exception {
        // Initialize the database
        jobcategorieRepository.saveAndFlush(jobcategorie);
        jobcategorieSearchRepository.save(jobcategorie);
        int databaseSizeBeforeUpdate = jobcategorieRepository.findAll().size();

        // Update the jobcategorie
        Jobcategorie updatedJobcategorie = jobcategorieRepository.findOne(jobcategorie.getId());
        // Disconnect from session so that the updates on updatedJobcategorie are not directly saved in db
        em.detach(updatedJobcategorie);
        updatedJobcategorie
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION);

        restJobcategorieMockMvc.perform(put("/api/jobcategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobcategorie)))
            .andExpect(status().isOk());

        // Validate the Jobcategorie in the database
        List<Jobcategorie> jobcategorieList = jobcategorieRepository.findAll();
        assertThat(jobcategorieList).hasSize(databaseSizeBeforeUpdate);
        Jobcategorie testJobcategorie = jobcategorieList.get(jobcategorieList.size() - 1);
        assertThat(testJobcategorie.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testJobcategorie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Jobcategorie in Elasticsearch
        Jobcategorie jobcategorieEs = jobcategorieSearchRepository.findOne(testJobcategorie.getId());
        assertThat(jobcategorieEs).isEqualToIgnoringGivenFields(testJobcategorie);
    }

    @Test
    @Transactional
    public void updateNonExistingJobcategorie() throws Exception {
        int databaseSizeBeforeUpdate = jobcategorieRepository.findAll().size();

        // Create the Jobcategorie

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobcategorieMockMvc.perform(put("/api/jobcategories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobcategorie)))
            .andExpect(status().isCreated());

        // Validate the Jobcategorie in the database
        List<Jobcategorie> jobcategorieList = jobcategorieRepository.findAll();
        assertThat(jobcategorieList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobcategorie() throws Exception {
        // Initialize the database
        jobcategorieRepository.saveAndFlush(jobcategorie);
        jobcategorieSearchRepository.save(jobcategorie);
        int databaseSizeBeforeDelete = jobcategorieRepository.findAll().size();

        // Get the jobcategorie
        restJobcategorieMockMvc.perform(delete("/api/jobcategories/{id}", jobcategorie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jobcategorieExistsInEs = jobcategorieSearchRepository.exists(jobcategorie.getId());
        assertThat(jobcategorieExistsInEs).isFalse();

        // Validate the database is empty
        List<Jobcategorie> jobcategorieList = jobcategorieRepository.findAll();
        assertThat(jobcategorieList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobcategorie() throws Exception {
        // Initialize the database
        jobcategorieRepository.saveAndFlush(jobcategorie);
        jobcategorieSearchRepository.save(jobcategorie);

        // Search the jobcategorie
        restJobcategorieMockMvc.perform(get("/api/_search/jobcategories?query=id:" + jobcategorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobcategorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Jobcategorie.class);
        Jobcategorie jobcategorie1 = new Jobcategorie();
        jobcategorie1.setId(1L);
        Jobcategorie jobcategorie2 = new Jobcategorie();
        jobcategorie2.setId(jobcategorie1.getId());
        assertThat(jobcategorie1).isEqualTo(jobcategorie2);
        jobcategorie2.setId(2L);
        assertThat(jobcategorie1).isNotEqualTo(jobcategorie2);
        jobcategorie1.setId(null);
        assertThat(jobcategorie1).isNotEqualTo(jobcategorie2);
    }
}
