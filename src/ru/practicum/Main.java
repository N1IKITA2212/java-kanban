package ru.practicum;

import ru.practicum.manager.TaskManager;
import ru.practicum.model.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        int taskId1 = taskManager.createTask(new Task("Посетить врача", "Нужно сходить к зубному вылечить зуб"));
        int taskId2 = taskManager.createTask(new Task("Поесть", "Приготовить суп"));
        int epicId1 = taskManager.createEpic(new Epic("Переезд", "Уезжаю в новый дом"));

        int subTaskId11 = taskManager.createSubTask(new SubTask("Собрать вещи", "Сложить все вещи в коробки", epicId1));
        int subTaskId12 = taskManager.createSubTask(new SubTask("Найти квартиру", "Найти на авито объявления о квартирах", epicId1));

        int epicId2 = taskManager.createEpic(new Epic("Собака", "Купить собаку"));

        int subTaskId21 = taskManager.createSubTask(new SubTask("Выбрать собаку", "Сходить в приют и выбрать собаку", epicId2));

        System.out.println("Задачи типа Task:");
        System.out.println(taskManager.getTasks());
        System.out.println();
        System.out.println("Задачи типа Epic");
        System.out.println(taskManager.getEpics());
        System.out.println();
        System.out.println("Задачи типа SubTask");
        System.out.println(taskManager.getSubTasks());

        taskManager.updateTask(new Task("Посетить врача", "Один зуб есть, осталось два", taskId1, Status.IN_PROGRESS));
        taskManager.updateTask(new Task("Поесть", "Съел борщ", taskId2, Status.DONE));
        taskManager.updateEpic(new Epic("Переезд", "Коробки собраны, скоро поеду", epicId1));
        taskManager.updateSubTask(new SubTask("Вещи собраны", "Все в коробках", subTaskId11, Status.DONE, epicId1));
        taskManager.updateSubTask(new SubTask("Найти квартиру", "Прозвонить объявления", subTaskId12, Status.IN_PROGRESS, epicId1));
        taskManager.updateEpic(new Epic("Собака", "Я купил собаку",epicId2));
        taskManager.updateSubTask(new SubTask("Собака выбрана", "Выполнено", subTaskId21,Status.DONE,epicId2));

        System.out.println("-".repeat(20));
        System.out.println("Обновляем задачи");
        System.out.println("-".repeat(20));

        System.out.println("Задачи типа Task:");
        System.out.println(taskManager.getTasks());
        System.out.println();
        System.out.println("Задачи типа Epic");
        System.out.println(taskManager.getEpics());
        System.out.println();
        System.out.println("Задачи типа SubTask");
        System.out.println(taskManager.getSubTasks());

        taskManager.removeTask(taskId1);
        taskManager.removeEpic(epicId1);

        System.out.println("-".repeat(20));
        System.out.println("Удаляем задачи");
        System.out.println("-".repeat(20));

        System.out.println("Задачи типа Task:");
        System.out.println(taskManager.getTasks());
        System.out.println();
        System.out.println("Задачи типа Epic");
        System.out.println(taskManager.getEpics());
        System.out.println();
        System.out.println("Задачи типа SubTask");
        System.out.println(taskManager.getSubTasks());

    }
}
