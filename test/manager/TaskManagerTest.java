package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.exceptions.TaskOverlapException;
import ru.practicum.manager.TaskManager;
import ru.practicum.model.Epic;
import ru.practicum.model.Status;
import ru.practicum.model.SubTask;
import ru.practicum.model.Task;

import java.time.LocalDateTime;
import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @Test
    public void shouldNotCreateSubTaskIfEpicIsSameSubTask() {
        // Должно вернуться -1, так как в мапе с Epic отсутствует объект с id 1
        int errorCode = taskManager.createSubTask(new SubTask("Раз", "Делай раз", 10, LocalDateTime.now(), 1, Status.NEW, 1));

        Assertions.assertEquals(-1, errorCode);
    }

    @Test
    public void createTask() {
        Task task = new Task("Раз", "Два", 10, LocalDateTime.now());

        int taskId = taskManager.createTask(task);
        Task savedTask = taskManager.getTaskById(taskId);

        Assertions.assertNotNull(savedTask, "Задача не найдена"); //Проверяем корректность сохранения задачи в мапе
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают");//Проверяем, что задача полученная по id верна
        //Проверка равенства всех полей задач
        Assertions.assertEquals(task.getName(), savedTask.getName(), "Имена задач не совпадают");
        Assertions.assertEquals(task.getDescription(), savedTask.getDescription(), "Описания задач не совпадают");
        Assertions.assertEquals(Status.NEW, savedTask.getStatus(), "Статусы не совпадают");
        Assertions.assertEquals(taskId, savedTask.getId(), "id не совпадают");
        Assertions.assertEquals(task.getStartTime(), savedTask.getStartTime());
        Assertions.assertEquals(task.getEndTime(), savedTask.getEndTime());
        Assertions.assertEquals(task.getDuration(), savedTask.getDuration());
    }

    @Test
    public void createEpic() {
        Epic epic = new Epic("Раз", "Два");

        int epicId = taskManager.createEpic(epic);
        Epic savedEpic = taskManager.getEpicById(epicId);

        Assertions.assertNotNull(savedEpic, "Эпик не найдена"); //Проверяем корректность сохранения задачи в мапе
        Assertions.assertEquals(epic, savedEpic, "Эпики не совпадают"); //Проверяем, что задача полученная по id верна

        Assertions.assertEquals(epic.getName(), savedEpic.getName(), "Имена эпиков не совпадают");
        Assertions.assertEquals(epic.getDescription(), savedEpic.getDescription(), "Описания эпиков не совпадают");
        Assertions.assertEquals(Status.NEW, savedEpic.getStatus(), "Статусы не совпадают");
        Assertions.assertEquals(epicId, savedEpic.getId(), "id не совпадают");
        Assertions.assertEquals(epic.getSubTasks(), savedEpic.getSubTasks(), "Подзадачи эпика не совпадают");
    }

    @Test
    public void createSubTask() {
        Epic epic = new Epic("Раз", "Два");
        int epicId = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Два", "Делай два", 10, LocalDateTime.now(), epicId);
        int subTaskId = taskManager.createSubTask(subTask);
        SubTask savedSubTask = taskManager.getSubTaskById(subTaskId);

        Assertions.assertNotNull(savedSubTask, "Подзадача не найдена");
        Assertions.assertEquals(subTask, savedSubTask, "Подзадачи не совпадают");
        Assertions.assertEquals(epicId, savedSubTask.getEpicId(), "Id эпиков не совпадают");

        Assertions.assertEquals(subTask.getName(), savedSubTask.getName(), "Имена эпиков не совпадают");
        Assertions.assertEquals(subTask.getDescription(), savedSubTask.getDescription(), "Описания эпиков не совпадают");
        Assertions.assertEquals(Status.NEW, savedSubTask.getStatus(), "Статусы не совпадают");
        Assertions.assertEquals(subTaskId, savedSubTask.getId(), "id не совпадают");

        SubTask subTask1 = new SubTask("Описание", "Название", 15, LocalDateTime.now(), 33);
        int subTaskId1 = taskManager.createSubTask(subTask1);
        Assertions.assertEquals(-1, subTaskId1);
    }

    @Test
    public void updateTask() {
        Task oldTask = new Task("Старая задача", "Описание", 10, LocalDateTime.now());
        int taskId = taskManager.createTask(oldTask);

        Task newTask = new Task("Обновленная задача", "Описание", 10, LocalDateTime.now(), taskId, Status.IN_PROGRESS);
        taskManager.updateTask(newTask);
        Task savedTask = taskManager.getTaskById(taskId);
        Assertions.assertNotNull(savedTask, "Задача не сохранилась");
        Assertions.assertEquals(newTask, savedTask, "Обновление задачи не выполнилось");
    }

    @Test
    public void getTaskShouldNotAddRepeatsToHistory() {
        Task task = new Task("Название", "Описание", 10, LocalDateTime.now());
        int taskId = taskManager.createTask(task);

        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId);
        taskManager.getTasks();

        Assertions.assertEquals(1, taskManager.getHistory().size());
        Assertions.assertEquals(task, taskManager.getHistory().getFirst());
    }

    @Test
    public void removeTaskShouldRemoveTaskFromHistory() {
        Task task = new Task("Название", "Описание", 10, LocalDateTime.of(2025, 12, 10, 13, 10));
        Task task1 = new Task("Название1", "Описание1", 10, LocalDateTime.of(2024, 12, 10, 13, 10));
        int taskId = taskManager.createTask(task);
        int taskId1 = taskManager.createTask(task1);

        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId1);

        Assertions.assertEquals(2, taskManager.getHistory().size());

        taskManager.removeTask(taskId);

        Assertions.assertEquals(1, taskManager.getHistory().size());
        Assertions.assertEquals(task1, taskManager.getHistory().getFirst());
    }

    @Test
    public void getSubTaskByEpic() {
        Epic epic = new Epic("Описание", "Название");
        int epicId = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Описание", "Название", 10, LocalDateTime.of(2025, 12, 10, 12, 10), epicId);
        SubTask subTask1 = new SubTask("Описание1", "Название1", 15, LocalDateTime.of(2025, 12, 10, 13, 10), epicId);
        int subTaskId = taskManager.createSubTask(subTask);
        int subTaskId1 = taskManager.createSubTask(subTask1);

        Assertions.assertTrue(taskManager.getEpicById(epicId).getSubTasks().contains(subTask));
        Assertions.assertTrue(taskManager.getEpicById(epicId).getSubTasks().contains(subTask1));

        taskManager.removeSubTask(subTaskId);

        Assertions.assertFalse(taskManager.getEpicById(epicId).getSubTasks().contains(subTask));
        Assertions.assertTrue(taskManager.getEpicById(epicId).getSubTasks().contains(subTask1));
    }

    @Test
    public void getHistoryTestWithGetById() {
        Task task = new Task("Название", "Описание", 10, LocalDateTime.of(2024, 12, 10, 13, 10));
        Task task1 = new Task("Название1", "Описание1", 10, LocalDateTime.of(2025, 12, 10, 13, 10));
        int taskId = taskManager.createTask(task);
        int taskId1 = taskManager.createTask(task1);
        Epic epic = new Epic("Описание", "Название");
        int epicId = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Описание", "Название", 15, LocalDateTime.of(2023, 12, 10, 13, 10), epicId);
        SubTask subTask1 = new SubTask("Описание1", "Название1", 10, LocalDateTime.of(2022, 12, 10, 13, 10), epicId);
        int subTaskId = taskManager.createSubTask(subTask);
        int subTaskId1 = taskManager.createSubTask(subTask1);

        taskManager.getTaskById(taskId);
        taskManager.getTaskById(taskId1);
        taskManager.getEpicById(epicId);
        taskManager.getSubTaskById(subTaskId);
        taskManager.getSubTaskById(subTaskId1);

        List<Task> history = taskManager.getHistory();

        Assertions.assertEquals(5, history.size());
        Assertions.assertEquals(task, history.getFirst());
        Assertions.assertEquals(subTask1, history.getLast());
        Assertions.assertEquals(epic, history.get(2));
    }

    //Если эпика для которого создается подзадача не существует, то вернется -1
    @Test
    public void subTuskShouldNotExistWithoutEpic() {
        SubTask subTask1 = new SubTask("Название", "Описание", 10, LocalDateTime.of(2025, 11, 9, 15, 42), 3);
        int errorCode = taskManager.createSubTask(subTask1);
        Assertions.assertEquals(-1, errorCode);
    }

    @Test
    public void subTaskShouldBePartOfEpic() {
        Epic epic = new Epic("Название", "Описание");
        int epicId = taskManager.createEpic(epic);

        SubTask subTask1 = new SubTask("Название", "Описание", 10, LocalDateTime.of(2025, 11, 9, 15, 42), epicId);
        int subTaskId = taskManager.createSubTask(subTask1);
        Assertions.assertTrue(taskManager.getEpics().contains(epic));
        Assertions.assertTrue(taskManager.getEpicById(epicId).getSubTasks().contains(taskManager.getSubTaskById(subTaskId)));
    }

    @Test
    public void isTaskOverlapTest() {
        Task task1 = new Task("Название", "Описание", 10, LocalDateTime.of(2025, 9, 11, 18, 30));
        Task taskEndTimeIsLater = new Task("Название", "Описание", 10, LocalDateTime.of(2025, 9, 11, 18, 35));
        Task taskContainsInTask1 = new Task("Название", "Описание", 5, LocalDateTime.of(2025, 9, 11, 18, 33));
        Task taskIsNotOverlapping = new Task("Название", "Описание", 5, LocalDateTime.of(2025, 9, 11, 18, 43));
        int task1Id = taskManager.createTask(task1);

        Assertions.assertThrows(TaskOverlapException.class, () -> taskManager.createTask(taskEndTimeIsLater));
        Assertions.assertDoesNotThrow(() -> taskManager.createTask(taskIsNotOverlapping));
        Assertions.assertThrows(TaskOverlapException.class, () -> taskManager.createTask(taskContainsInTask1));
    }
}
