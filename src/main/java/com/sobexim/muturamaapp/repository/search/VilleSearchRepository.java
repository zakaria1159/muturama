package com.sobexim.muturamaapp.repository.search;

import com.sobexim.muturamaapp.domain.Ville;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Ville entity.
 */
public interface VilleSearchRepository extends ElasticsearchRepository<Ville, Long> {
}
