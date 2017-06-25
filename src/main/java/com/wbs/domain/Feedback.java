package com.wbs.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Feedback.
 */
@Document(collection = "feedback")
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @NotNull
    @Field("email")
    private String email;

    @Field("comment")
    private String comment;

    @Field("time")
    private ZonedDateTime time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public Feedback email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public Feedback comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public Feedback time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Feedback feedback = (Feedback) o;
        if (feedback.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), feedback.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Feedback{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", comment='" + getComment() + "'" +
            ", time='" + getTime() + "'" +
            "}";
    }
}
