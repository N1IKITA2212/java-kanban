package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.exceptions.ManagerLoadException;
import ru.practicum.manager.FileBackedTaskManager;
import ru.practicum.model.Epic;
import ru.practicum.model.SubTask;
import ru.practicum.model.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File tempFile;

    @BeforeEach
    public void beforeEach() throws IOException {
        tempFile = File.createTempFile("test", ".csv");
        super.taskManager = new FileBackedTaskManager(tempFile);
    }

    @Test
    public void shouldSaveAndLoadFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tempFile);
        Task task1 = new Task("Task1", "Task1 description", 10, LocalDateTime.of(2025, 7, 6, 15, 20));
        Epic epic1 = new Epic("Epic1", "Epic1 description");

        int taskId = fileBackedTaskManager.createTask(task1);
        int epicId = fileBackedTaskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("SubTask1", "SubTask1 description", 5, LocalDateTime.of(2025, 7, 6, 15, 30), epicId);
        SubTask subTask2 = new SubTask("SubTask1", "SubTask1 description", 25, LocalDateTime.of(2020, 7, 6, 15, 30), epicId);
        int subTaskId = fileBackedTaskManager.createSubTask(subTask1);
        int subTaskId2 = fileBackedTaskManager.createSubTask(subTask2);

        FileBackedTaskManager loadedFileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);

        Task loadedTask1 = loadedFileBackedTaskManager.getTasks().getFirst();
        Assertions.assertEquals(task1.getId(), loadedTask1.getId());
        Assertions.assertEquals(task1.getStatus(), loadedTask1.getStatus());
        Assertions.assertEquals(task1.getName(), loadedTask1.getName());
        Assertions.assertEquals(task1.getDescription(), loadedTask1.getDescription());
        Assertions.assertEquals(task1.getStartTime(), loadedTask1.getStartTime());
        Assertions.assertEquals(task1.getDuration(), loadedTask1.getDuration());
        Assertions.assertEquals(task1.getEndTime(), loadedTask1.getEndTime());

        Epic loadedEpic1 = loadedFileBackedTaskManager.getEpics().getFirst();
        Assertions.assertEquals(epic1.getId(), loadedEpic1.getId());
        Assertions.assertEquals(epic1.getName(), loadedEpic1.getName());
        Assertions.assertEquals(epic1.getStatus(), loadedEpic1.getStatus());
        Assertions.assertEquals(epic1.getDescription(), loadedEpic1.getDescription());
        Assertions.assertEquals(epic1.getSubTasks(), loadedEpic1.getSubTasks());
        Assertions.assertEquals(epic1.getStartTime(), loadedEpic1.getStartTime());
        Assertions.assertEquals(epic1.getDuration(), loadedEpic1.getDuration());
        Assertions.assertEquals(epic1.getEndTime(), loadedEpic1.getEndTime());

        SubTask loadedSubTask1 = loadedFileBackedTaskManager.getSubTasks().getFirst();
        Assertions.assertEquals(subTask1.getId(), loadedSubTask1.getId());
        Assertions.assertEquals(subTask1.getStatus(), loadedSubTask1.getStatus());
        Assertions.assertEquals(subTask1.getName(), loadedSubTask1.getName());
        Assertions.assertEquals(subTask1.getDescription(), loadedSubTask1.getDescription());
        Assertions.assertEquals(subTask1.getEpicId(), loadedSubTask1.getEpicId());
        Assertions.assertEquals(subTask1.getStartTime(), loadedSubTask1.getStartTime());
        Assertions.assertEquals(subTask1.getDuration(), loadedSubTask1.getDuration());
        Assertions.assertEquals(subTask1.getEndTime(), loadedSubTask1.getEndTime());
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

    @Test
    public void shouldThrowExceptionWhenFileNotExist() {
        File file = new File("there_is_no_file.csv");

        Assertions.assertThrows(ManagerLoadException.class, () -> FileBackedTaskManager.loadFromFile(file));
    }
}