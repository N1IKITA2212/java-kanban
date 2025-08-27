package ru.practicum;

import ru.practicum.manager.FileBackedTaskManager;
import ru.practicum.manager.Managers;
import ru.practicum.manager.TaskManager;
import ru.practicum.model.Epic;
import ru.practicum.model.SubTask;
import ru.practicum.model.Task;

import java.io.File;


public class Main {
    public static void main(String[] args) {
        TaskManager fileBackedTaskManager = Managers.getFileBackedTaskManager();

        int taskId1 = fileBackedTaskManager.createTask(new Task("Посетить врача", "Нужно сходить к зубному вылечить зуб"));
        int taskId2 = fileBackedTaskManager.createTask(new Task("Поесть", "Приготовить суп"));
        int epicId1 = fileBackedTaskManager.createEpic(new Epic("Переезд", "Уезжаю в новый дом"));

        int subTaskId11 = fileBackedTaskManager.createSubTask(new SubTask("Собрать вещи", "Сложить все вещи в коробки", epicId1));
        int subTaskId12 = fileBackedTaskManager.createSubTask(new SubTask("Найти квартиру", "Найти на авито объявления о квартирах", epicId1));

        int epicId2 = fileBackedTaskManager.createEpic(new Epic("Собака", "Купить собаку"));

        int subTaskId21 = fileBackedTaskManager.createSubTask(new SubTask("Выбрать собаку", "Сходить в приют и выбрать собаку", epicId2));

        System.out.println(fileBackedTaskManager.getTaskById(taskId1));
        System.out.println(fileBackedTaskManager.getSubTaskById(subTaskId11));
        System.out.println(fileBackedTaskManager.getEpicById(epicId1));

        TaskManager loadedFileBackedTaskedManager = FileBackedTaskManager.loadFromFile(new File("test.csv"));

        System.out.println("-".repeat(20));
        System.out.println("Загруженный менеджер задач");
        System.out.println(loadedFileBackedTaskedManager.getTasks());
        System.out.println(loadedFileBackedTaskedManager.getEpics());
        System.out.println(loadedFileBackedTaskedManager.getSubTasks());
    }
}
