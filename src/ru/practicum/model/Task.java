package ru.practicum.model;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected TaskType type = TaskType.TASK;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task(String name, String description) { // Конструктор используется при первоначальном создании задачи
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, int id, Status status) { // Конструктор используется при обновлении задачи
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public static Task fromString(String line) {
        String[] fields = line.split(",");
        return new Task(fields[2], fields[4], Integer.parseInt(fields[0]), Status.valueOf(fields[3]));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        //id,type,name,status,description
        return String.format("%d,%s,%s,%s,%s", id, type, name, status, description);
    }
}
