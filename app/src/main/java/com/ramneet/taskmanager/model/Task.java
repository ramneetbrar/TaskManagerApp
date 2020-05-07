package com.ramneet.taskmanager.model;

import java.util.Date;

/**
 * Stores information about a single task.
 * Data includes the task, a note, the data and its status.
 */
public class Task {
    private String task;
    private String note;
    private Date date;
    private Status status;

    public Task(String task, String note, Date date) {
        this.task = task;
        this.note = note;
        this.date = date;
        this.status = Status.ACTIVE;
    }

    public Task(String task, String note, Date date, Status status) {
        this.task = task;
        this.note = note;
        this.date = date;
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
