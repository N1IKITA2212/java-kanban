package ru.practicum.model;

public class SubTask extends Task {
    private final int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public SubTask(String name, String description, int id, Status status, int epicId) {
        this(name, description, epicId);
        this.id = id;
        this.status = status;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        // id,type,name,status,description,epic
        return super.toString() + "," + epicId;
    }
}
