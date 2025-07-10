import manager.TaskManager;
import model.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        int taskId1 = taskManager.createTask(new Task("Посетить врача", "Нужно сходить к зубному вылечить зуб"));
        int taskId2 = taskManager.createTask(new Task("Поесть", "Приготовить суп"));
        int epicId1 = taskManager.createEpic(new Epic("Переезд", "Уезжаю в новый дом"));

        int subTaskId11 = taskManager.createSubTask(new SubTask("Собрать вещи", "Сложить все вещи в коробки", 25));
        int subTaskId12 = taskManager.createSubTask(new SubTask("Найти квартиру", "Найти на авито объявления о квартирах", epicId1));

        int epicId2 = taskManager.createEpic(new Epic("Собака", "Купить собаку"));

        int subTaskId21 = taskManager.createSubTask(new SubTask("Выбрать собаку", "Сходить в приют и выбрать собаку", epicId2));

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());

    }
}
