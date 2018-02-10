package com.sobexim.muturamaapp.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Job entity. This class is used in JobResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /jobs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter titre;

    private IntegerFilter points;

    private StringFilter description;

    private StringFilter etat;

    private StringFilter tempsderealisation;

    private LongFilter jobcategorieId;

    private LongFilter jobtoutilisateurId;

    private LongFilter postulantId;

    public JobCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitre() {
        return titre;
    }

    public void setTitre(StringFilter titre) {
        this.titre = titre;
    }

    public IntegerFilter getPoints() {
        return points;
    }

    public void setPoints(IntegerFilter points) {
        this.points = points;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getEtat() {
        return etat;
    }

    public void setEtat(StringFilter etat) {
        this.etat = etat;
    }

    public StringFilter getTempsderealisation() {
        return tempsderealisation;
    }

    public void setTempsderealisation(StringFilter tempsderealisation) {
        this.tempsderealisation = tempsderealisation;
    }

    public LongFilter getJobcategorieId() {
        return jobcategorieId;
    }

    public void setJobcategorieId(LongFilter jobcategorieId) {
        this.jobcategorieId = jobcategorieId;
    }

    public LongFilter getJobtoutilisateurId() {
        return jobtoutilisateurId;
    }

    public void setJobtoutilisateurId(LongFilter jobtoutilisateurId) {
        this.jobtoutilisateurId = jobtoutilisateurId;
    }

    public LongFilter getPostulantId() {
        return postulantId;
    }

    public void setPostulantId(LongFilter postulantId) {
        this.postulantId = postulantId;
    }

    @Override
    public String toString() {
        return "JobCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (titre != null ? "titre=" + titre + ", " : "") +
                (points != null ? "points=" + points + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (etat != null ? "etat=" + etat + ", " : "") +
                (tempsderealisation != null ? "tempsderealisation=" + tempsderealisation + ", " : "") +
                (jobcategorieId != null ? "jobcategorieId=" + jobcategorieId + ", " : "") +
                (jobtoutilisateurId != null ? "jobtoutilisateurId=" + jobtoutilisateurId + ", " : "") +
                (postulantId != null ? "postulantId=" + postulantId + ", " : "") +
            "}";
    }

}
