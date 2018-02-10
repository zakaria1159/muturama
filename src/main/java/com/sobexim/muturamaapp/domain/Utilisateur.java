package com.sobexim.muturamaapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "utilisateur")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "datedenaissance", nullable = false)
    private LocalDate datedenaissance;

    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "avatar_content_type")
    private String avatarContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private User utilisateuruser;

    @OneToMany(mappedBy = "emeteur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> messages = new HashSet<>();

    @OneToMany(mappedBy = "jobtoutilisateur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Job> utilisateurtojobs = new HashSet<>();

    @ManyToMany(mappedBy = "postulants")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Job> postulants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDatedenaissance() {
        return datedenaissance;
    }

    public Utilisateur datedenaissance(LocalDate datedenaissance) {
        this.datedenaissance = datedenaissance;
        return this;
    }

    public void setDatedenaissance(LocalDate datedenaissance) {
        this.datedenaissance = datedenaissance;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public Utilisateur avatar(byte[] avatar) {
        this.avatar = avatar;
        return this;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAvatarContentType() {
        return avatarContentType;
    }

    public Utilisateur avatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
        return this;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public User getUtilisateuruser() {
        return utilisateuruser;
    }

    public Utilisateur utilisateuruser(User user) {
        this.utilisateuruser = user;
        return this;
    }

    public void setUtilisateuruser(User user) {
        this.utilisateuruser = user;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public Utilisateur messages(Set<Message> messages) {
        this.messages = messages;
        return this;
    }

    public Utilisateur addMessage(Message message) {
        this.messages.add(message);
        message.setEmeteur(this);
        return this;
    }

    public Utilisateur removeMessage(Message message) {
        this.messages.remove(message);
        message.setEmeteur(null);
        return this;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<Job> getUtilisateurtojobs() {
        return utilisateurtojobs;
    }

    public Utilisateur utilisateurtojobs(Set<Job> jobs) {
        this.utilisateurtojobs = jobs;
        return this;
    }

    public Utilisateur addUtilisateurtojob(Job job) {
        this.utilisateurtojobs.add(job);
        job.setJobtoutilisateur(this);
        return this;
    }

    public Utilisateur removeUtilisateurtojob(Job job) {
        this.utilisateurtojobs.remove(job);
        job.setJobtoutilisateur(null);
        return this;
    }

    public void setUtilisateurtojobs(Set<Job> jobs) {
        this.utilisateurtojobs = jobs;
    }

    public Set<Job> getPostulants() {
        return postulants;
    }

    public Utilisateur postulants(Set<Job> jobs) {
        this.postulants = jobs;
        return this;
    }

    public Utilisateur addPostulant(Job job) {
        this.postulants.add(job);
        job.getPostulants().add(this);
        return this;
    }

    public Utilisateur removePostulant(Job job) {
        this.postulants.remove(job);
        job.getPostulants().remove(this);
        return this;
    }

    public void setPostulants(Set<Job> jobs) {
        this.postulants = jobs;
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
        Utilisateur utilisateur = (Utilisateur) o;
        if (utilisateur.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), utilisateur.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", datedenaissance='" + getDatedenaissance() + "'" +
            ", avatar='" + getAvatar() + "'" +
            ", avatarContentType='" + getAvatarContentType() + "'" +
            "}";
    }
}
