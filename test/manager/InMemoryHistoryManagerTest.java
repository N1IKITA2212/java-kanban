package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.manager.HistoryManager;
import ru.practicum.manager.InMemoryHistoryManager;
import ru.practicum.model.Status;
import ru.practicum.model.Task;

import java.time.LocalDateTime;

public class InMemoryHistoryManagerTest {
    HistoryManager inMemoryHistoryManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryHistoryManager = new InMemoryHistoryManager();

    }

    @Test
    public void tasksInHistoryShouldBeEqualsToSaved() {
        Task task = new Task("Название", "Описание");
        inMemoryHistoryManager.add(task);

        Task savedTask = inMemoryHistoryManager.getHistory().getFirst();

        Assertions.assertNotNull(savedTask, "Задача не сохранилась в историю");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают");

        Assertions.assertEquals(task.getName(), savedTask.getName(), "Имена задач не совпадают");
        Assertions.assertEquals(task.getDescription(), savedTask.getDescription(), "Описания задач не совпадают");
        Assertions.assertEquals(task.getStatus(), savedTask.getStatus(), "Статус задач не совпадает");
    }

    @Test
    public void historyShouldNotContainRepeats() {
        Task task1 = new Task("Название", "Описание", 10, LocalDateTime.now(), 1, Status.NEW);
        Task task2 = new Task("Название1", "Описание1", 15, LocalDateTime.now(), 2, Status.NEW);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task1);

        Assertions.assertEquals(2, inMemoryHistoryManager.getHistory().size(), "Длины списков не совпадают");
    }

    @Test
    public void inMemoryHistoryManagerRemoveTaskFromEnd() {
        Task task1 = new Task("Название", "Описание", 10, LocalDateTime.now(), 1, Status.NEW);
        Task task2 = new Task("Название1", "Описание1", 10, LocalDateTime.now(), 2, Status.NEW);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.remove(1);

        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().size());
        Assertions.assertEquals(task2, inMemoryHistoryManager.getHistory().getFirst());
    }

    @Test
    public void inMemoryHistoryManagerRemoveTaskFromStart() {
        Task task1 = new Task("Название", "Описание", 10, LocalDateTime.now(), 1, Status.NEW);
        Task task2 = new Task("Название1", "Описание1", 10, LocalDateTime.now(), 2, Status.NEW);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.remove(2);

        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().size());
        Assertions.assertEquals(task1, inMemoryHistoryManager.getHistory().getFirst());
    }

    @Test
    public void inMemoryHistoryManagerRemoveTaskFromMiddle() {
        Task task1 = new Task("Название", "Описание", 10, LocalDateTime.now(), 1, Status.NEW);
        Task task2 = new Task("Название1", "Описание1", 10, LocalDateTime.now(), 2, Status.NEW);
        Task task3 = new Task("Название1", "Описание1", 10, LocalDateTime.now(), 3, Status.NEW);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.remove(3);

        Assertions.assertEquals(2, inMemoryHistoryManager.getHistory().size());
        Assertions.assertEquals(task1, inMemoryHistoryManager.getHistory().getFirst());
        Assertions.assertEquals(task2, inMemoryHistoryManager.getHistory().getLast());
    }

    @Test
    public void emptyHistoryTest() {
        Assertions.assertTrue(inMemoryHistoryManager.getHistory().isEmpty());
    }
}
