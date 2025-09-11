package ru.practicum.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<SubTask> subTasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
    }

    public Epic(String name, String description, int id) { // Конструктор для теста в Main, статус не передаем, он считается по подзадачам
        this(name, description);
        this.id = id;
    }

    //Конструктор для создания эпика из строки, используется в классе FileBackedTaskManager
    public Epic(String name, String description, int id, Status status) {
        this(name, description, id);
        this.status = status;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        updateStatus();
        calculateStartTime();
        calculateDuration();
        calculateEndTime();
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask);
        updateStatus();
        calculateDuration();
        calculateEndTime();
        calculateStartTime();
    }

    public void removeAllSubtasks() {
        subTasks.clear();
        updateStatus();
    }

    // Расчет длительности эпика по длительности его подзадач
    private void calculateDuration() {
        this.duration = Duration.ZERO;
        this.subTasks.forEach(subTask -> duration = duration.plus(subTask.getDuration()));
    }

    // Расчет времени начала эпика по самому раннему времени начала подзадачи
    private void calculateStartTime() {
        if (subTasks.isEmpty()) {
            this.startTime = null;
            return;
        }
        LocalDateTime currentTime = LocalDateTime.MAX;
        for (SubTask subTask : subTasks) {
            if (subTask.getStartTime().isBefore(currentTime)) {
                currentTime = subTask.getStartTime();
            }
        }
        this.startTime = currentTime;
    }

    private void calculateEndTime() {
        LocalDateTime currentTime = LocalDateTime.MIN;
        for (SubTask subTask : subTasks) {
            if (subTask.getEndTime().isAfter(currentTime)) {
                currentTime = subTask.getEndTime();
            }
        }
        this.endTime = currentTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
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
