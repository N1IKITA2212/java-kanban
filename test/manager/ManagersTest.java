package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.manager.*;
import ru.practicum.model.Task;

public class ManagersTest {

    // Проверяем, что методы утилитарного класса не возвращают null
    @Test
    public void managersMethodsShouldNotReturnNull() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

        Assertions.assertNotNull(inMemoryHistoryManager, "Утилитарный класс не создает объект Менеджера");
        Assertions.assertNotNull(inMemoryTaskManager, "Утилитарный класс не создает объект Менеджера");
    }
}
