package com.sobexim.muturamaapp.repository.search;

import com.sobexim.muturamaapp.domain.Jobcategorie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Jobcategorie entity.
 */
public interface JobcategorieSearchRepository extends ElasticsearchRepository<Jobcategorie, Long> {
}
