package com.wbs.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Timesheet.
 */

@Document(collection = "timesheet")
public class Timesheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("user")
    private String user;

    @NotNull
    @Min(value = 0)
    @Max(value = 53)
    @Field("week")
    private Integer week;

    @Field("year")
    private Integer year;

    @Field("submit_at")
    private ZonedDateTime submitAt;

    @Field("updated_at")
    private ZonedDateTime updatedAt;

    @Field("updated_by")
    private String updatedBy;

    @Field("summary")
    private String summary;

    @Field("total_hours")
    private Integer totalHours;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public Timesheet user(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getWeek() {
        return week;
    }

    public Timesheet week(Integer week) {
        this.week = week;
        return this;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getYear() {
        return year;
    }

    public Timesheet year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public ZonedDateTime getSubmitAt() {
        return submitAt;
    }

    public Timesheet submitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
        return this;
    }

    public void setSubmitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Timesheet updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Timesheet updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getSummary() {
        return summary;
    }

    public Timesheet summary(String summary) {
        this.summary = summary;
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public Timesheet totalHours(Integer totalHours) {
        this.totalHours = totalHours;
        return this;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Timesheet timesheet = (Timesheet) o;
        if (timesheet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), timesheet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Timesheet{" +
            "id=" + getId() +
            ", user='" + getUser() + "'" +
            ", week='" + getWeek() + "'" +
            ", year='" + getYear() + "'" +
            ", submitAt='" + getSubmitAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", summary='" + getSummary() + "'" +
            ", totalHours='" + getTotalHours() + "'" +
            "}";
    }
}
