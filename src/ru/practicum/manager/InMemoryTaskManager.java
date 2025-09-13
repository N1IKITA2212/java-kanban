package ru.practicum.manager;

import ru.practicum.exceptions.TaskOverlapException;
import ru.practicum.model.Epic;
import ru.practicum.model.Status;
import ru.practicum.model.SubTask;
import ru.practicum.model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected int id = 1;

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
        if (isTaskOverlap(task)) {
            throw new TaskOverlapException();
        }
        task.setId(id);
        task.setStatus(Status.NEW);
        tasks.put(id, task);
        prioritizedTasks.add(task);
        return id++;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (!epics.containsKey(subTask.getEpicId())) {
            return -1;
        }

        if (isTaskOverlap(subTask)) {
            throw new TaskOverlapException();
        }
        subTask.setId(id);
        subTask.setStatus(Status.NEW);
        subTasks.put(id, subTask);
        epics.get(subTask.getEpicId()).addSubTask(subTask);
        prioritizedTasks.add(subTask);
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
        if (isTaskOverlap(task)) {
            throw new TaskOverlapException();
        }
        prioritizedTasks.remove(task);
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (!subTasks.containsKey(subTask.getId())) {
            return;
        }
        if (isTaskOverlap(subTask)) {
            throw new TaskOverlapException();
        }
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTasks().remove(subTask); // Удаляем необновленную сабтаску из листа подзадач в эпике
        epic.addSubTask(subTask); // добавляем обновленную задачу в лист подзадач в эпике
        prioritizedTasks.remove(subTask);
        prioritizedTasks.add(subTask);
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
        prioritizedTasks.remove(tasks.get(taskId));
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void removeSubTask(int subTaskId) {
        if (!subTasks.containsKey(subTaskId)) {
            return;
        }
        int epicId = subTasks.get(subTaskId).getEpicId();
        prioritizedTasks.remove(subTasks.get(subTaskId));
        epics.get(epicId).removeSubTask(subTasks.get(subTaskId)); //Удаление подзадачи из мапы эпика
        subTasks.remove(subTaskId); //Удаление подзадачи из общей мапы
        historyManager.remove(subTaskId);
    }

    @Override
    public void removeEpic(int epicId) {
        if (!epics.containsKey(epicId)) {
            return;
        }
        epics.get(epicId).getSubTasks().forEach(subTask -> {
            subTasks.remove(subTask.getId());
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
        });
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
        tasks.values().forEach(task -> {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        });
        tasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        epics.values().forEach(Epic::removeAllSubtasks);
        subTasks.values().forEach(subTask -> {
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
        });
        subTasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.values().forEach(epic -> historyManager.remove(epic.getId()));
        subTasks.values().forEach(subTask -> {
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
        });
        epics.clear();
        subTasks.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return this.prioritizedTasks;
    }

    // Возвращает true если есть перекрытие задач по времени
    private boolean isTaskOverlap(Task task) {
        return getPrioritizedTasks().stream().filter(prioritizedTask -> task.getId() != prioritizedTask.getId())
                .anyMatch(prioritizedTask -> (prioritizedTask.getStartTime().isBefore(task.getEndTime())) &&
                        prioritizedTask.getEndTime().isAfter(task.getStartTime()));
    }
}
