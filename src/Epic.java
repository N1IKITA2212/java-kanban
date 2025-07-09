import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, SubTask> subTasks = new HashMap<>(); // Мапа <id, subTask> в каждом эпике

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public ArrayList<SubTask> getSubTasksList() { // возвращает лист со всеми сабтасками для данного эпика
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        for (Integer i : subTasks.keySet()) {
            subTaskArrayList.add(subTasks.get(i));
        }
        return subTaskArrayList;
    }

    public Epic(String name, String description) {
        super(name, description);

    }

    public void updateStatus() {
        boolean isAnyInProgress = false;
        boolean isAllNew = true;

        for (SubTask subTask : getSubTasksList()) {
            if (!subTask.getStatus().equals(Status.DONE)) { //Тут проверили можем ли поставить статус IN_PROGRESS
                isAnyInProgress = true;
            }
        }

        for (SubTask subTask : getSubTasksList()) {
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
        return "Это Epic!! " + super.toString();
    }
}
