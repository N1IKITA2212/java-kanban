package manager;

import org.junit.jupiter.api.BeforeEach;
import ru.practicum.manager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        super.taskManager = new InMemoryTaskManager();
    }

}
