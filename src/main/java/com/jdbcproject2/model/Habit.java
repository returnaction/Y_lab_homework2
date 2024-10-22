package com.jdbcproject2.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Habit {
    private int id;
    private String title;
    private String description;
    private String frequency;
    private String date;

    private boolean isCompleted;
    //nav prop
    private int userId;

    public Habit() {
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
        date = formater.format(new Date());
    }

    public Habit(int userId, boolean isCompleted, String date, String frequency, String description, String title, int id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.frequency = frequency;
        this.date = date;
        this.isCompleted = isCompleted;
        this.userId = userId;
    }

    public Habit(int id, String title, String description, String frequency) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.frequency = frequency;

        // форматируем текущую дату в строку день-месяц-год
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
        date = formater.format(new Date());
    }

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", frequency='" + frequency + '\'' +
                ", date='" + date + '\'' +
                ", isCompleted=" + isCompleted +
                ", userId=" + userId +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
