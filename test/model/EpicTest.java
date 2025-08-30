package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.model.Epic;

public class EpicTest {

    // Тест на равенство задач при одинаковом id
    @Test
    public void epicsShouldBeEqualsWhenIdMatches() {
        Epic epic1 = new Epic("Раз", "Сделать раз");
        epic1.setId(1);
        Epic epic2 = new Epic("Два", "Сделать раз");
        epic2.setId(1);

        Assertions.assertEquals(epic1, epic2, "Задачи не равны при одинаковом id");
    }
}