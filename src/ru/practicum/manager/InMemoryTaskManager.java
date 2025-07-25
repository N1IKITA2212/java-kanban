package ru.practicum.manager;

import ru.practicum.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private int id = 1;

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    // Методы создания тасков возвращают id созданной таски для возможности дальнейшего обращения к ней
    @Override
    public int createTask(Task task) {
        task.setId(id);
        task.setStatus(Status.NEW);
        tasks.put(id, task);
        return id++;
    }

    @Override
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

    @Override
    public int createEpic(Epic epic) {
        epic.setId(id);
        epic.setStatus(Status.NEW);
        epics.put(id, epic);
        return id++;
    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (!subTasks.containsKey(subTask.getId())) {
            return;
        }
        subTasks.put(subTask.getId(), subTask);

        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTasks().remove(subTask); // Удаляем необновленную сабтаску из листа подзадач в эпике
        epic.addSubTask(subTask); // добавляем обновленную задачу в лист подзадач в эпике

    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }
        Epic oldEpic = epics.get(epic.getId());
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
    }

    @Override
    public void removeTask(int taskId) {
        if (!tasks.containsKey(taskId)) {
            return;
        }
        tasks.remove(taskId);
    }

    @Override
    public void removeSubTask(int subTaskId) {
        if (!subTasks.containsKey(subTaskId)) {
            return;
        }
        int epicId = subTasks.get(subTaskId).getEpicId();
        epics.get(epicId).removeSubTask(subTasks.get(subTaskId)); //Удаление сабтаски из мапы эпика
        subTasks.remove(subTaskId); //Удаление сабтаски из общей мапы
    }

    @Override
    public void removeEpic(int epicId) {
        if (!epics.containsKey(epicId)) {
            return;
        }
        for (SubTask subTask : epics.get(epicId).getSubTasks()) { // Удаление всех подзадач удаляемого эпика из общего хранилища
            subTasks.remove(subTask.getId());
        }
        epics.remove(epicId);
    }

    @Override
    public ArrayList<SubTask> getSubTasksByEpic(int epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }
        return epics.get(epicId).getSubTasks();
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
        }
        subTasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
