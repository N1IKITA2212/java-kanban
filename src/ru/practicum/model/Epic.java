package ru.practicum.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<SubTask> subTasks = new ArrayList<>();

    {
        type = TaskType.EPIC;
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, int id) { // Конструктор для теста в Main, статус не передаем, он считается по подзадачам
        super(name, description);
        this.id = id;
    }

    //Конструктор для создания эпика из строки, используется в классе FileBackedTaskManager
    private Epic(String name, String description, int id, Status status) {
        super(name, description, id, status);
    }

    public static Epic fromString(String line) {
        String[] fields = line.split(",");
        return new Epic(fields[2], fields[4], Integer.parseInt(fields[0]), Status.valueOf(fields[3]));
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        updateStatus();
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask);
        updateStatus();
    }

    public void removeAllSubtasks() {
        subTasks.clear();
        updateStatus();
    }

    private void updateStatus() {
        boolean isAnyInProgress = false;
        boolean isAllNew = true;

        for (SubTask subTask : subTasks) {
            if (!subTask.getStatus().equals(Status.DONE)) { //Тут проверили можем ли поставить статус IN_PROGRESS
                isAnyInProgress = true;
            }
            if (!subTask.getStatus().equals(Status.NEW)) {
                isAllNew = false;
            }
        }
        if (isAllNew) {
            status = Status.NEW;
        } else if (isAnyInProgress) {
            status = Status.IN_PROGRESS;
        } else {
            status = Status.DONE;
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
