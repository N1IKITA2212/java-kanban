package ru.practicum.manager;

import ru.practicum.model.Epic;
import ru.practicum.model.SubTask;
import ru.practicum.model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getTasks();

    List<SubTask> getSubTasks();

    List<Epic> getEpics();

    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicById(int id);

    // Методы создания тасков возвращают id созданной таски для возможности дальнейшего обращения к ней
    int createTask(Task task);

    int createSubTask(SubTask subTask);

    int createEpic(Epic epic);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void removeTask(int taskId);

    void removeSubTask(int subTaskId);

    void removeEpic(int epicId);

    List<SubTask> getSubTasksByEpic(int epicId);

    void deleteTasks();

    void deleteSubTasks();

    void deleteEpics();

    List<Task> getHistory(); // Метод для получения поля HistroyManager из InMemoryTaskManager

}
