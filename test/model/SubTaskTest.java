package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.model.SubTask;

import java.time.LocalDateTime;

public class SubTaskTest {

    // Тест на равенство задач при одинаковом id
    @Test
    public void tasksShouldBeEqualsWhenIdMatches() {
        SubTask subTask1 = new SubTask("Раз", "Сделать раз", 10, LocalDateTime.now(), 1);
        subTask1.setId(1);
        SubTask subTask2 = new SubTask("Раз", "Сделать раз", 10, LocalDateTime.now(), 1);
        subTask2.setId(1);

        Assertions.assertEquals(subTask1, subTask2, "Задачи не равны при одинаковом id");
    }
}
