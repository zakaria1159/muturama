package com.sobexim.muturamaapp.domain;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Agent.class)
public abstract class Agent_ {

	public static volatile SingularAttribute<Agent, String> address;
	public static volatile SingularAttribute<Agent, String> cin;
	public static volatile SingularAttribute<Agent, BigDecimal> lon;
	public static volatile SingularAttribute<Agent, Long> id;
	public static volatile SingularAttribute<Agent, String> nom;
	public static volatile SingularAttribute<Agent, String> prenom;
	public static volatile SingularAttribute<Agent, BigDecimal> lat;
	public static volatile SingularAttribute<Agent, Ville> agentville;
	public static volatile SingularAttribute<Agent, Boolean> status;

}

