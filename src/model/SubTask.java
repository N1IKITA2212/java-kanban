package model;

public class SubTask extends Task {
    private final int epicId;

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
        return "Это subTask!! " + super.toString() + " (" +
                "epicId=" + epicId +
                ')';
    }
}
