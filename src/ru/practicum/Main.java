package ru.practicum;

import ru.practicum.manager.HistoryManager;
import ru.practicum.manager.Managers;
import ru.practicum.manager.TaskManager;
import ru.practicum.model.*;

public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        HistoryManager historyManager = inMemoryTaskManager.getHistoryManager();
        int taskId1 = inMemoryTaskManager.createTask(new Task("Посетить врача", "Нужно сходить к зубному вылечить зуб"));
        int taskId2 = inMemoryTaskManager.createTask(new Task("Поесть", "Приготовить суп"));
        int epicId1 = inMemoryTaskManager.createEpic(new Epic("Переезд", "Уезжаю в новый дом"));

        int subTaskId11 = inMemoryTaskManager.createSubTask(new SubTask("Собрать вещи", "Сложить все вещи в коробки", epicId1));
        int subTaskId12 = inMemoryTaskManager.createSubTask(new SubTask("Найти квартиру", "Найти на авито объявления о квартирах", epicId1));

        int epicId2 = inMemoryTaskManager.createEpic(new Epic("Собака", "Купить собаку"));

        int subTaskId21 = inMemoryTaskManager.createSubTask(new SubTask("Выбрать собаку", "Сходить в приют и выбрать собаку", epicId2));

        System.out.println(inMemoryTaskManager.getTaskById(taskId1));
        System.out.println(inMemoryTaskManager.getSubTaskById(subTaskId11));
        System.out.println(inMemoryTaskManager.getEpicById(epicId1));

        System.out.println("История:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }

    }
}
