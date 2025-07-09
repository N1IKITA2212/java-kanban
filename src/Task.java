import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    protected Status status; // поле protected для изменения его значения в Эпике

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + id;
        result = 31 * result + Objects.hashCode(status);
        return result;
    }

    // ToString() для теста в Main
    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
