package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.model.Task;

public class TaskTest {

    // Тест на равенство задач при одинаковом id
    @Test
    public void tasksShouldBeEqualsWhenIdMatches() {
        Task task1 = new Task("Раз", "Сделать раз");
        task1.setId(1);
        Task task2 = new Task("Раз", "Сделать раз");
        task2.setId(1);

        Assertions.assertEquals(task1, task2, "Задачи не равны при одинаковом id");
    }
}
