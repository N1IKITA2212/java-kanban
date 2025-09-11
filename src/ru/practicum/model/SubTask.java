package ru.practicum.model;

import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int epicId;

    public SubTask(String name, String description, long duration, LocalDateTime startTime, int epicId) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public SubTask(String name, String description, long duration, LocalDateTime startTime, int id, Status status, int epicId) {
        this(name, description, duration, startTime, epicId);
        this.id = id;
        this.status = status;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        // id,type,name,status,description,duration,startTime,epic
        return super.toString() + "," + epicId;
    }
}
