package com.wbs.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Entry.
 */

@Document(collection = "entry")
public class Entry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("user")
    private String user;

    @NotNull
    @Min(value = 0)
    @Max(value = 24)
    @Field("hour")
    private Integer hour;

    @Field("notes")
    private String notes;

    @NotNull
    @Field("category")
    private String category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public Entry user(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getHour() {
        return hour;
    }

    public Entry hour(Integer hour) {
        this.hour = hour;
        return this;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public String getNotes() {
        return notes;
    }

    public Entry notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCategory() {
        return category;
    }

    public Entry category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entry entry = (Entry) o;
        if (entry.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entry.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Entry{" +
            "id=" + getId() +
            ", user='" + getUser() + "'" +
            ", hour='" + getHour() + "'" +
            ", notes='" + getNotes() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
