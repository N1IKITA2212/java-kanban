public class Main {
    public static void main(String[] args) {
        int taskId1 = TaskManager.createTask(new Task("Посетить врача", "Нужно сходить к зубному вылечить зуб"));
        int taskId2 = TaskManager.createTask(new Task("Поесть", "Приготовить суп"));
        int epicId1 = TaskManager.createEpic(new Epic("Переезд", "Уезжаю в новый дом"));

        int subTaskId11 = TaskManager.createSubTask(new SubTask("Собрать вещи", "Сложить все вещи в коробки"), epicId1);
        int subTaskId12 = TaskManager.createSubTask(new SubTask("Найти квартиру", "Найти на авито объявления о квартирах"), epicId1);

        int epicId2 = TaskManager.createEpic(new Epic("Собака", "Купить собаку"));

        int subTaskId21 = TaskManager.createSubTask(new SubTask("Выбрать собаку", "Сходить в приют и выбрать собаку"), epicId2);

        System.out.println(TaskManager.getTasks());
        System.out.println(TaskManager.getEpics());
        System.out.println(TaskManager.getSubTasks());

        System.out.println("-".repeat(20));
        System.out.println("Обновляем задачи");
        System.out.println("-".repeat(20));
        //Вызовем здесь конструктор, в котором сразу передадим id и статус задаче
        Task task12 = new Task("Посетить врача", "Сходить к Лору", taskId1, Status.IN_PROGRESS);
        TaskManager.updateTask(task12);
        System.out.println(TaskManager.getTasks());
        SubTask subTask112 = new SubTask("Собрать вещи", "Коробки уже собраны", subTaskId11, Status.IN_PROGRESS);
        subTask112.setEpicId(epicId1); // свяжем обновленную сабтаску с нужным нам эпиком
        SubTask subTask122 = new SubTask("Найти квартиру", "Квартира найдена", subTaskId12, Status.DONE);
        subTask122.setEpicId(epicId1);
        TaskManager.updateSubTask(subTask112);
        TaskManager.updateSubTask(subTask122);
        System.out.println(TaskManager.getSubTasks());
        System.out.println(TaskManager.getEpics());

        System.out.println("-".repeat(20));
        System.out.println("Удалем задачи и эпики");
        System.out.println("-".repeat(20));

        TaskManager.removeTask(taskId1);
        System.out.println(TaskManager.getTasks());
        TaskManager.removeEpic(epicId1);

        System.out.println(TaskManager.getEpics());
        System.out.println(TaskManager.getSubTasks());


    }
}
