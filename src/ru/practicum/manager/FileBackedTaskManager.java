package ru.practicum.manager;

import ru.practicum.exceptions.ManagerLoadException;
import ru.practicum.exceptions.ManagerSaveException;
import ru.practicum.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final File savedFile; //файл .csv
    private final static String HEAD_IN_FILE = "ID,TYPE,NAME,STATUS,DESCRIPTION,EPIC";

    public FileBackedTaskManager(File file) { //Конструктор получает путь к файлу в виде строки
        savedFile = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        int currentId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = reader.readLine(); //Здесь считывается заголовок
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) { //Валидация пустых строк
                    continue;
                }
                String[] fields = line.split(",");
                //Восстанавливаем ID менеджера
                if (Integer.parseInt(fields[0]) > currentId) {
                    currentId = Integer.parseInt(fields[0]);
                }
                TaskType type = TaskType.valueOf(fields[1]);
                switch (type) {
                    case TASK:
                        Task task = FileBackedTaskManager.fromString(line);
                        fileBackedTaskManager.tasks.put(task.getId(), task);
                        break;

                    case EPIC:
                        Epic epic = (Epic) FileBackedTaskManager.fromString(line);
                        fileBackedTaskManager.epics.put(epic.getId(), epic);
                        break;

                    case SUBTASK:
                        SubTask subTask = (SubTask) FileBackedTaskManager.fromString(line);
                        fileBackedTaskManager.subTasks.put(subTask.getId(), subTask);
                        break;
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException();
        }
        fileBackedTaskManager.id = currentId;
        // Восстанавливаем подзадачи для эпиков
        for (SubTask subTask : fileBackedTaskManager.getSubTasks()) {
            Epic subTaskEpic = fileBackedTaskManager.epics.get(subTask.getEpicId());
            subTaskEpic.addSubTask(subTask);
        }
        return fileBackedTaskManager;
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(savedFile, StandardCharsets.UTF_8)) {
            // Первой строкой запишем заголовок типа id,type,name,status,description,epic
            fileWriter.write(HEAD_IN_FILE);
            fileWriter.write(System.lineSeparator());
            for (Task task : getTasks()) {
                fileWriter.write(task.toString());
                fileWriter.write(System.lineSeparator());
            }
            for (Epic epic : getEpics()) {
                fileWriter.write(epic.toString());
                fileWriter.write(System.lineSeparator());
            }
            for (SubTask subTask : getSubTasks()) {
                fileWriter.write(subTask.toString());
                fileWriter.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private static Task fromString(String line) {
        String[] fields = line.split(",");
        TaskType type = TaskType.valueOf(fields[1]);
        switch (type) {
            case TASK:
                return new Task(fields[2], fields[4], Integer.parseInt(fields[0]), Status.valueOf(fields[3]));
            case EPIC:
                return new Epic(fields[2], fields[4], Integer.parseInt(fields[0]), Status.valueOf(fields[3]));
            case SUBTASK:
                return new SubTask(fields[2], fields[4], Integer.parseInt(fields[0]), Status.valueOf(fields[3]),
                        Integer.parseInt(fields[5]));
        }
        throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
    }

    @Override
    public int createTask(Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        int id = super.createSubTask(subTask);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeSubTask(int subTaskId) {
        super.removeSubTask(subTaskId);
        save();
    }

    @Override
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }
}
