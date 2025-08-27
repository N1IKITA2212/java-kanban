package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.manager.FileBackedTaskManager;
import ru.practicum.manager.TaskManager;
import ru.practicum.model.Epic;
import ru.practicum.model.Status;
import ru.practicum.model.SubTask;
import ru.practicum.model.Task;

import java.io.File;
import java.util.List;

public class FileBackedTaskManagerTest {
    TaskManager fileBackedTaskManager;

    @BeforeEach
    public void beforeEach() {
        fileBackedTaskManager = new FileBackedTaskManager(new File("test.csv"));
    }

    @Test
    public void shouldNotCreateSubTaskIfEpicIsSameSubTask() {
        // Должно вернуться -1, так как в мапе с Epic отсутствует объект с id 1
        int errorCode = fileBackedTaskManager.createSubTask(new SubTask("Раз", "Делай раз", 1, Status.NEW, 1));

        Assertions.assertEquals(-1, errorCode);
    }

    @Test
    public void createTask() {
        Task task = new Task("Раз", "Два");

        int taskId = fileBackedTaskManager.createTask(task);
        Task savedTask = fileBackedTaskManager.getTaskById(taskId);

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

        int epicId = fileBackedTaskManager.createEpic(epic);
        Epic savedEpic = fileBackedTaskManager.getEpicById(epicId);

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
        int epicId = fileBackedTaskManager.createEpic(epic);

        SubTask subTask = new SubTask("Два", "Делай два", epicId);
        int subTaskId = fileBackedTaskManager.createSubTask(subTask);
        SubTask savedSubTask = fileBackedTaskManager.getSubTaskById(subTaskId);

        Assertions.assertNotNull(savedSubTask, "Подзадача не найдена");
        Assertions.assertEquals(subTask, savedSubTask, "Подзадачи не совпадают");
        Assertions.assertEquals(epicId, savedSubTask.getEpicId(), "Id эпиков не совпадают");

        Assertions.assertEquals(subTask.getName(), savedSubTask.getName(), "Имена эпиков не совпадают");
        Assertions.assertEquals(subTask.getDescription(), savedSubTask.getDescription(), "Описания эпиков не совпадают");
        Assertions.assertEquals(Status.NEW, savedSubTask.getStatus(), "Статусы не совпадают");
        Assertions.assertEquals(subTaskId, savedSubTask.getId(), "id не совпадают");

        SubTask subTask1 = new SubTask("Описание", "Название", 33);
        int subTaskId1 = fileBackedTaskManager.createSubTask(subTask1);
        Assertions.assertEquals(-1, subTaskId1);
    }

    @Test
    public void updateTask() {
        Task oldTask = new Task("Старая задача", "Описание");
        int taskId = fileBackedTaskManager.createTask(oldTask);

        Task newTask = new Task("Обновленная задача", "Описание", taskId, Status.IN_PROGRESS);
        fileBackedTaskManager.updateTask(newTask);
        Task savedTask = fileBackedTaskManager.getTaskById(taskId);
        Assertions.assertNotNull(savedTask, "Задача не сохранилась");
        Assertions.assertEquals(newTask, savedTask, "Обновление задачи не выполнилось");
    }

    @Test
    public void getTaskShouldNotAddRepeatsToHistory() {
        Task task = new Task("Название", "Описание");
        int taskId = fileBackedTaskManager.createTask(task);

        fileBackedTaskManager.getTaskById(taskId);
        fileBackedTaskManager.getTaskById(taskId);
        fileBackedTaskManager.getTasks();

        Assertions.assertEquals(1, fileBackedTaskManager.getHistory().size());
        Assertions.assertEquals(task, fileBackedTaskManager.getHistory().getFirst());
    }

    @Test
    public void removeTaskShouldRemoveTaskFromHistory() {
        Task task = new Task("Название", "Описание");
        Task task1 = new Task("Название1", "Описание1");
        int taskId = fileBackedTaskManager.createTask(task);
        int taskId1 = fileBackedTaskManager.createTask(task1);

        fileBackedTaskManager.getTaskById(taskId);
        fileBackedTaskManager.getTaskById(taskId1);

        Assertions.assertEquals(2, fileBackedTaskManager.getHistory().size());

        fileBackedTaskManager.removeTask(taskId);

        Assertions.assertEquals(1, fileBackedTaskManager.getHistory().size());
        Assertions.assertEquals(task1, fileBackedTaskManager.getHistory().getFirst());
    }

    @Test
    public void getSubTaskByEpic() {
        Epic epic = new Epic("Описание", "Название");
        int epicId = fileBackedTaskManager.createEpic(epic);

        SubTask subTask = new SubTask("Описание", "Название", epicId);
        SubTask subTask1 = new SubTask("Описание1", "Название1", epicId);
        int subTaskId = fileBackedTaskManager.createSubTask(subTask);
        int subTaskId1 = fileBackedTaskManager.createSubTask(subTask1);

        Assertions.assertTrue(fileBackedTaskManager.getEpicById(epicId).getSubTasks().contains(subTask));
        Assertions.assertTrue(fileBackedTaskManager.getEpicById(epicId).getSubTasks().contains(subTask1));

        fileBackedTaskManager.removeSubTask(subTaskId);

        Assertions.assertFalse(fileBackedTaskManager.getEpicById(epicId).getSubTasks().contains(subTask));
        Assertions.assertTrue(fileBackedTaskManager.getEpicById(epicId).getSubTasks().contains(subTask1));
    }

    @Test
    public void getHistoryTestWithGetById() {
        Task task = new Task("Название", "Описание");
        Task task1 = new Task("Название1", "Описание1");
        int taskId = fileBackedTaskManager.createTask(task);
        int taskId1 = fileBackedTaskManager.createTask(task1);
        Epic epic = new Epic("Описание", "Название");
        int epicId = fileBackedTaskManager.createEpic(epic);

        SubTask subTask = new SubTask("Описание", "Название", epicId);
        SubTask subTask1 = new SubTask("Описание1", "Название1", epicId);
        int subTaskId = fileBackedTaskManager.createSubTask(subTask);
        int subTaskId1 = fileBackedTaskManager.createSubTask(subTask1);

        fileBackedTaskManager.getTaskById(taskId);
        fileBackedTaskManager.getTaskById(taskId1);
        fileBackedTaskManager.getEpicById(epicId);
        fileBackedTaskManager.getSubTaskById(subTaskId);
        fileBackedTaskManager.getSubTaskById(subTaskId1);

        List<Task> history = fileBackedTaskManager.getHistory();

        Assertions.assertEquals(5, history.size());
        Assertions.assertEquals(task, history.getFirst());
        Assertions.assertEquals(subTask1, history.getLast());
        Assertions.assertEquals(epic, history.get(2));
    }

    @Test
    public void loadFromFile() {
        Task task1 = new Task("Task1", "Task1 description");
        Epic epic1 = new Epic("Epic1", "Epic1 description");

        int taskId = fileBackedTaskManager.createTask(task1);
        int epicId = fileBackedTaskManager.createEpic(epic1);

        FileBackedTaskManager loadedFileBackedTaskManager = FileBackedTaskManager.loadFromFile(new File("test.csv"));

        Assertions.assertEquals(fileBackedTaskManager, loadedFileBackedTaskManager);
    }
}
