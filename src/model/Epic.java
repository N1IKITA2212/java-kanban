package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>(); // Мапа <id, subTask> в каждом эпике

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        updateStatus();
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask);
        updateStatus();
    }

    public void updateStatus() {
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
        return "Это Epic!! " + super.toString() + "Все подзадачи данного Epic- " + subTasks.toString();
    }
}
