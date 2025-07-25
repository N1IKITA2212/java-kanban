package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.manager.HistoryManager;
import ru.practicum.manager.InMemoryHistoryManager;
import ru.practicum.model.Task;

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
}
