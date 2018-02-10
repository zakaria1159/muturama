package com.sobexim.muturamaapp.repository;

import com.sobexim.muturamaapp.domain.Job;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Job entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    @Query("select distinct job from Job job left join fetch job.postulants")
    List<Job> findAllWithEagerRelationships();

    @Query("select job from Job job left join fetch job.postulants where job.id =:id")
    Job findOneWithEagerRelationships(@Param("id") Long id);

}
