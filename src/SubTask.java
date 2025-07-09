public class SubTask extends Task {
    private int epicId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public SubTask(String name, String description) {
        super(name, description);
    }

    public SubTask(String name, String description, int id, Status status) {
        super(name, description, id, status);
    }

    // ToString() для теста в Main
    @Override
    public String toString() {
        return "Это subTask!! " + super.toString() + " (" +
                "epicId=" + epicId +
                ')';
    }
}
