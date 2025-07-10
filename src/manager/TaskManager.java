package manager;

import model.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private int id = 1;

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    // Методы создания тасков возвращают id созданной таски для возможности дальнейшего обращения к ней
    public int createTask(Task task) {
        task.setId(id);
        task.setStatus(Status.NEW);
        tasks.put(id, task);
        return id++;
    }

    public int createSubTask(SubTask subTask) {
        if (!epics.containsKey(subTask.getEpicId())) {
            return -1;
        }
        subTask.setId(id);
        subTask.setStatus(Status.NEW);
        subTasks.put(id, subTask);
        epics.get(subTask.getEpicId()).addSubTask(subTask);
        return id++;
    }

    public int createEpic(Epic epic) {
        epic.setId(id);
        epic.setStatus(Status.NEW);
        epics.put(id, epic);
        return id++;
    }

    public void updateTask(Task task) {
        if (!isIdValid(task.getId(), tasks)) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask) {
        if (!isIdValid(subTask.getId(), subTasks)) {
            return;
        }
        subTasks.put(subTask.getId(), subTask);

        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTasks().remove(subTask); // Удаляем необновленную сабтаску из листа подзадач в эпике
        epic.addSubTask(subTask); // добавляем обновленную задачу в лист подзадач в эпике

    }

    public void updateEpic(Epic epic) {
        if (!isIdValid(epic.getId(), epics)) {
            return;
        }
        Epic oldEpic = epics.get(epic.getId());
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
    }

    public void removeTask(int taskId) {
        if (!isIdValid(taskId, tasks)) {
            return;
        }
        tasks.remove(taskId);
    }

    public void removeSubTask(int subTaskId) {
        if (!isIdValid(subTaskId, subTasks)) {
            return;
        }
        int epicId = subTasks.get(subTaskId).getEpicId();
        epics.get(epicId).removeSubTask(subTasks.get(subTaskId)); //Удаление сабтаски из мапы эпика
        subTasks.remove(subTaskId); //Удаление сабтаски из общей мапы
    }

    public void removeEpic(int epicId) {
        if (!isIdValid(epicId, epics)) {
            return;
        }
        for (SubTask subTask : epics.get(epicId).getSubTasks()) { // Удаление всех подзадач удаляемого эпика из общего хранилища
            subTasks.remove(subTask.getId());
        }
        epics.remove(epicId);
    }

    public ArrayList<SubTask> getSubTasksByEpic(int epicId) {
        if (!isIdValid(epicId, epics)) {
            return null;
        }
        return epics.get(epicId).getSubTasks();
    }

    public <T extends  Task> boolean isIdValid(int id, HashMap<Integer, T> tasks) {
        return tasks.containsKey(id);
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubTasks() {
        subTasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
    }
}
