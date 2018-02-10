package com.sobexim.muturamaapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "job")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @NotNull
    @Column(name = "points", nullable = false)
    private Integer points;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "etat")
    private String etat;

    @Column(name = "tempsderealisation")
    private String tempsderealisation;

    @ManyToOne(optional = false)
    @NotNull
    private Jobcategorie jobcategorie;

    @ManyToOne
    private Utilisateur jobtoutilisateur;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "job_postulant",
               joinColumns = @JoinColumn(name="jobs_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="postulants_id", referencedColumnName="id"))
    private Set<Utilisateur> postulants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public Job titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Integer getPoints() {
        return points;
    }

    public Job points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public Job description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return etat;
    }

    public Job etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getTempsderealisation() {
        return tempsderealisation;
    }

    public Job tempsderealisation(String tempsderealisation) {
        this.tempsderealisation = tempsderealisation;
        return this;
    }

    public void setTempsderealisation(String tempsderealisation) {
        this.tempsderealisation = tempsderealisation;
    }

    public Jobcategorie getJobcategorie() {
        return jobcategorie;
    }

    public Job jobcategorie(Jobcategorie jobcategorie) {
        this.jobcategorie = jobcategorie;
        return this;
    }

    public void setJobcategorie(Jobcategorie jobcategorie) {
        this.jobcategorie = jobcategorie;
    }

    public Utilisateur getJobtoutilisateur() {
        return jobtoutilisateur;
    }

    public Job jobtoutilisateur(Utilisateur utilisateur) {
        this.jobtoutilisateur = utilisateur;
        return this;
    }

    public void setJobtoutilisateur(Utilisateur utilisateur) {
        this.jobtoutilisateur = utilisateur;
    }

    public Set<Utilisateur> getPostulants() {
        return postulants;
    }

    public Job postulants(Set<Utilisateur> utilisateurs) {
        this.postulants = utilisateurs;
        return this;
    }

    public Job addPostulant(Utilisateur utilisateur) {
        this.postulants.add(utilisateur);
        utilisateur.getPostulants().add(this);
        return this;
    }

    public Job removePostulant(Utilisateur utilisateur) {
        this.postulants.remove(utilisateur);
        utilisateur.getPostulants().remove(this);
        return this;
    }

    public void setPostulants(Set<Utilisateur> utilisateurs) {
        this.postulants = utilisateurs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Job job = (Job) o;
        if (job.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), job.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", points=" + getPoints() +
            ", description='" + getDescription() + "'" +
            ", etat='" + getEtat() + "'" +
            ", tempsderealisation='" + getTempsderealisation() + "'" +
            "}";
    }
}
