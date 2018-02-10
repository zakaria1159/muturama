package com.sobexim.muturamaapp.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Job.class)
public abstract class Job_ {

	public static volatile SingularAttribute<Job, Jobcategorie> jobcategorie;
	public static volatile SingularAttribute<Job, Utilisateur> jobtoutilisateur;
	public static volatile SingularAttribute<Job, String> titre;
	public static volatile SingularAttribute<Job, String> description;
	public static volatile SingularAttribute<Job, Long> id;
	public static volatile SingularAttribute<Job, String> tempsderealisation;
	public static volatile SingularAttribute<Job, String> etat;
	public static volatile SetAttribute<Job, Utilisateur> postulants;
	public static volatile SingularAttribute<Job, Integer> points;

}

