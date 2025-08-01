package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.manager.*;
import ru.practicum.model.*;

public class InMemoryTaskManagerTest {
    TaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    // Проверяем, что SubTask нельзя сделать своим же Epic
    @Test
    public void shouldNotCreateSubTaskIfEpicIsSameSubTask() {
        // Должно вернуться -1, так как в мапе с Epic отсутствует объект с id 1
        int errorCode = inMemoryTaskManager.createSubTask(new SubTask("Раз", "Делай раз", 1, Status.NEW, 1));

        Assertions.assertEquals(-1, errorCode);
    }

    @Test
    public void createTask() {
        Task task = new Task("Раз", "Два");

        int taskId = inMemoryTaskManager.createTask(task);
        Task savedTask = inMemoryTaskManager.getTaskById(taskId);

        Assertions.assertNotNull(savedTask, "Задача не найдена"); //Проверяем корректность сохранения задачи в мапе
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают");//Проверяем, что задача полученная по id верна
        //Проверка равенства всех полей задач
        Assertions.assertEquals(task.getName(), savedTask.getName(), "Имена задач не совпадают");
        Assertions.assertEquals(task.getDescription(), savedTask.getDescription(), "Описания задач не совпадают");
        Assertions.assertEquals(Status.NEW, savedTask.getStatus(), "Статусы не совпадают");
        Assertions.assertEquals(taskId, savedTask.getId(), "id не совпадают");
    }

    @Test
    public void createEpic() {
        Epic epic = new Epic("Раз", "Два");

        int epicId = inMemoryTaskManager.createEpic(epic);
        Epic savedEpic = inMemoryTaskManager.getEpicById(epicId);

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
        int epicId = inMemoryTaskManager.createEpic(epic);

        SubTask subTask = new SubTask("Два", "Делай два", epicId);
        int subTaskId = inMemoryTaskManager.createSubTask(subTask);
        SubTask savedSubTask = inMemoryTaskManager.getSubTaskById(subTaskId);

        Assertions.assertNotNull(savedSubTask, "Подзадача не найдена");
        Assertions.assertEquals(subTask, savedSubTask, "Подзадачи не совпадают");
        Assertions.assertEquals(epicId, savedSubTask.getEpicId(), "Id эпиков не совпадают");

        Assertions.assertEquals(subTask.getName(), savedSubTask.getName(), "Имена эпиков не совпадают");
        Assertions.assertEquals(subTask.getDescription(), savedSubTask.getDescription(), "Описания эпиков не совпадают");
        Assertions.assertEquals(Status.NEW, savedSubTask.getStatus(), "Статусы не совпадают");
        Assertions.assertEquals(subTaskId, savedSubTask.getId(), "id не совпадают");
    }

    @Test
    public void updateTask() {
        Task oldTask = new Task("Старая задача", "Описание");
        int taskId = inMemoryTaskManager.createTask(oldTask);

        Task newTask = new Task("Обновленная задача", "Описание", taskId, Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(newTask);
        Task savedTask = inMemoryTaskManager.getTaskById(taskId);
        Assertions.assertNotNull(savedTask, "Задача не сохранилась");
        Assertions.assertEquals(newTask, savedTask, "Обновление задачи не выполнилось");
    }
}
