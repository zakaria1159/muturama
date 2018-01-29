package com.sobexim.muturamaapp.repository.search;

import com.sobexim.muturamaapp.domain.Agent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Agent entity.
 */
public interface AgentSearchRepository extends ElasticsearchRepository<Agent, Long> {
}
