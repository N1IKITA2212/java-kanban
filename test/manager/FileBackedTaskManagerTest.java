package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.manager.FileBackedTaskManager;
import ru.practicum.model.Epic;
import ru.practicum.model.SubTask;
import ru.practicum.model.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManagerTest {
    File tempFile;

    @BeforeEach
    public void beforeEach() throws IOException {
        tempFile = File.createTempFile("test", ".csv");
    }

    @Test
    public void shouldSaveAndLoadFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tempFile);
        Task task1 = new Task("Task1", "Task1 description");
        Epic epic1 = new Epic("Epic1", "Epic1 description");

        int taskId = fileBackedTaskManager.createTask(task1);
        int epicId = fileBackedTaskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("SubTask1", "SubTask1 description", epicId);
        int subTaskId = fileBackedTaskManager.createSubTask(subTask1);

        FileBackedTaskManager loadedFileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);

        Task loadedTask1 = loadedFileBackedTaskManager.getTasks().getFirst();
        Assertions.assertEquals(task1.getId(), loadedTask1.getId());
        Assertions.assertEquals(task1.getStatus(), loadedTask1.getStatus());
        Assertions.assertEquals(task1.getName(), loadedTask1.getName());
        Assertions.assertEquals(task1.getDescription(), loadedTask1.getDescription());

        Epic loadedEpic1 = loadedFileBackedTaskManager.getEpics().getFirst();
        Assertions.assertEquals(epic1.getId(), loadedEpic1.getId());
        Assertions.assertEquals(epic1.getName(), loadedEpic1.getName());
        Assertions.assertEquals(epic1.getStatus(), loadedEpic1.getStatus());
        Assertions.assertEquals(epic1.getDescription(), loadedEpic1.getDescription());
        Assertions.assertEquals(epic1.getSubTasks(), loadedEpic1.getSubTasks());

        SubTask loadedSubTask1 = loadedFileBackedTaskManager.getSubTasks().getFirst();
        Assertions.assertEquals(subTask1.getId(), loadedSubTask1.getId());
        Assertions.assertEquals(subTask1.getStatus(), loadedSubTask1.getStatus());
        Assertions.assertEquals(subTask1.getName(), loadedSubTask1.getName());
        Assertions.assertEquals(subTask1.getDescription(), loadedSubTask1.getDescription());
        Assertions.assertEquals(subTask1.getEpicId(), loadedSubTask1.getEpicId());
    }

    @Test
    public void shouldLoadEmptyFile() {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);

        Assertions.assertTrue(fileBackedTaskManager.getTasks().isEmpty());
        Assertions.assertTrue(fileBackedTaskManager.getEpics().isEmpty());
        Assertions.assertTrue(fileBackedTaskManager.getSubTasks().isEmpty());
    }

    @Test
    public void shouldSaveEmptyFile() throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tempFile);
        List<String> lines = Files.readAllLines(tempFile.toPath());
        Assertions.assertTrue(lines.isEmpty());
    }
}