package com.sobexim.muturamaapp.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Utilisateur.class)
public abstract class Utilisateur_ {

	public static volatile SetAttribute<Utilisateur, Job> utilisateurtojobs;
	public static volatile SetAttribute<Utilisateur, Message> messages;
	public static volatile SingularAttribute<Utilisateur, Long> id;
	public static volatile SingularAttribute<Utilisateur, byte[]> avatar;
	public static volatile SingularAttribute<Utilisateur, LocalDate> datedenaissance;
	public static volatile SetAttribute<Utilisateur, Job> postulants;
	public static volatile SingularAttribute<Utilisateur, String> avatarContentType;
	public static volatile SingularAttribute<Utilisateur, User> utilisateuruser;

}

