package com.sobexim.muturamaapp.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Message.class)
public abstract class Message_ {

	public static volatile SingularAttribute<Message, Utilisateur> emeteur;
	public static volatile SingularAttribute<Message, Utilisateur> recepteur;
	public static volatile SingularAttribute<Message, Long> id;
	public static volatile SingularAttribute<Message, String> contenu;

}

