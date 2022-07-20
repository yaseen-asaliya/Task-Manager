package com.training.TaskManger.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = 0;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,
                    CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
    private String description;
    private int completed;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }


    @Override
    public String toString() {
        return "Task{" +
                "userId=" + getUser() +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                '}';
    }
}