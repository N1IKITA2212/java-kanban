package ru.practicum.model;

public class SubTask extends Task {
    private final int epicId;
    {
        type = TaskType.SUBTASK;
    }
    public int getEpicId() {
        return epicId;
    }

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, int id, Status status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        // id,type,name,status,description,epic
        return super.toString() + "," + epicId;
    }

    public static SubTask fromString(String line) {
        String[] fields = line.split(",");
        return new SubTask(fields[2], fields[4], Integer.parseInt(fields[0]), Status.valueOf(fields[3]),
                Integer.parseInt(fields[5]));
    }
}
