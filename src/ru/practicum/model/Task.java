package ru.practicum.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected Integer id;
    protected Status status;
    protected TaskType type;
    protected Duration duration = Duration.ofMinutes(0);
    protected LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Конструктор используется при первоначальном создании задачи
    // В качестве длительности передаем минуты
    public Task(String name, String description, long duration, LocalDateTime startTime) {
        this(name, description);
        this.type = TaskType.TASK;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
    }

    // Конструктор используется при обновлении задачи
    public Task(String name, String description, long duration, LocalDateTime startTime, int id, Status status) {
        this(name, description, duration, startTime);
        this.id = id;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public LocalDateTime getEndTime() {
        // Валидация времени начала задачи
        if (startTime == null) {
            return null;
        }
        return startTime.plusMinutes(getDuration().toMinutes());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String startTimeLine = (startTime == null) ? "null" : startTime.format(formatter);
        long minutes = (duration == null) ? 0 : duration.toMinutes();
        //id,type,name,status,description, duration, startTime
        return String.format("%d,%s,%s,%s,%s,%d,%s", id, type, name, status, description, minutes, startTimeLine);
    }
}
