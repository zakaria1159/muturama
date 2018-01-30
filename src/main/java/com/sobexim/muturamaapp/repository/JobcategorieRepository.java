package com.sobexim.muturamaapp.repository;

import com.sobexim.muturamaapp.domain.Jobcategorie;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Jobcategorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobcategorieRepository extends JpaRepository<Jobcategorie, Long> {

}
