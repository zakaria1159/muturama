package com.sobexim.muturamaapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sobexim.muturamaapp.domain.Agent;

import com.sobexim.muturamaapp.repository.AgentRepository;
import com.sobexim.muturamaapp.repository.search.AgentSearchRepository;
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
 * REST controller for managing Agent.
 */
@RestController
@RequestMapping("/api")
public class AgentResource {

    private final Logger log = LoggerFactory.getLogger(AgentResource.class);

    private static final String ENTITY_NAME = "agent";

    private final AgentRepository agentRepository;

    private final AgentSearchRepository agentSearchRepository;

    public AgentResource(AgentRepository agentRepository, AgentSearchRepository agentSearchRepository) {
        this.agentRepository = agentRepository;
        this.agentSearchRepository = agentSearchRepository;
    }

    /**
     * POST  /agents : Create a new agent.
     *
     * @param agent the agent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agent, or with status 400 (Bad Request) if the agent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/agents")
    @Timed
    public ResponseEntity<Agent> createAgent(@Valid @RequestBody Agent agent) throws URISyntaxException {
        log.debug("REST request to save Agent : {}", agent);
        if (agent.getId() != null) {
            throw new BadRequestAlertException("A new agent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Agent result = agentRepository.save(agent);
        agentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/agents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agents : Updates an existing agent.
     *
     * @param agent the agent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agent,
     * or with status 400 (Bad Request) if the agent is not valid,
     * or with status 500 (Internal Server Error) if the agent couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/agents")
    @Timed
    public ResponseEntity<Agent> updateAgent(@Valid @RequestBody Agent agent) throws URISyntaxException {
        log.debug("REST request to update Agent : {}", agent);
        if (agent.getId() == null) {
            return createAgent(agent);
        }
        Agent result = agentRepository.save(agent);
        agentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, agent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agents : get all the agents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of agents in body
     */
    @GetMapping("/agents")
    @Timed
    public ResponseEntity<List<Agent>> getAllAgents(Pageable pageable) {
        log.debug("REST request to get a page of Agents");
        Page<Agent> page = agentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /agents/:id : get the "id" agent.
     *
     * @param id the id of the agent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agent, or with status 404 (Not Found)
     */
    @GetMapping("/agents/{id}")
    @Timed
    public ResponseEntity<Agent> getAgent(@PathVariable Long id) {
        log.debug("REST request to get Agent : {}", id);
        Agent agent = agentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(agent));
    }

    /**
     * DELETE  /agents/:id : delete the "id" agent.
     *
     * @param id the id of the agent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/agents/{id}")
    @Timed
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        log.debug("REST request to delete Agent : {}", id);
        agentRepository.delete(id);
        agentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/agents?query=:query : search for the agent corresponding
     * to the query.
     *
     * @param query the query of the agent search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/agents")
    @Timed
    public ResponseEntity<List<Agent>> searchAgents(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Agents for query {}", query);
        Page<Agent> page = agentSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/agents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
