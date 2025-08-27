package ru.practicum.manager;

import ru.practicum.model.Epic;
import ru.practicum.model.Status;
import ru.practicum.model.SubTask;
import ru.practicum.model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();

    protected int id = 1;

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    //Метод для получения листа всех задач в менеджере
    public List<Task> getAllTasks() {
        List<Task> all = new ArrayList<>();
        all.addAll(tasks.values());
        all.addAll(subTasks.values());
        all.addAll(epics.values());
        all.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });
        return all;
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
        historyManager.remove(taskId);
    }

    @Override
    public void removeSubTask(int subTaskId) {
        if (!subTasks.containsKey(subTaskId)) {
            return;
        }
        int epicId = subTasks.get(subTaskId).getEpicId();
        epics.get(epicId).removeSubTask(subTasks.get(subTaskId)); //Удаление подзадачи из мапы эпика
        subTasks.remove(subTaskId); //Удаление подзадачи из общей мапы
        historyManager.remove(subTaskId);
    }

    @Override
    public void removeEpic(int epicId) {
        if (!epics.containsKey(epicId)) {
            return;
        }
        for (SubTask subTask : epics.get(epicId).getSubTasks()) { // Удаление всех подзадач удаляемого эпика из общего хранилища
            subTasks.remove(subTask.getId());
            historyManager.remove(subTask.getId());
        }
        epics.remove(epicId);
        historyManager.remove(epicId);
    }

    @Override
    public List<SubTask> getSubTasksByEpic(int epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }
        return epics.get(epicId).getSubTasks();
    }

    @Override
    public void deleteTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
        }
        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
        }
        subTasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return tasks.equals(that.tasks) && subTasks.equals(that.subTasks) && epics.equals(that.epics);
    }

    @Override
    public int hashCode() {
        int result = tasks.hashCode();
        result = 31 * result + subTasks.hashCode();
        result = 31 * result + epics.hashCode();
        return result;
    }
}
