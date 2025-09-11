package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.model.Epic;
import ru.practicum.model.Status;
import ru.practicum.model.SubTask;

import java.time.LocalDateTime;

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

    @Test
    public void epicStatusNewTest() {
        Epic epic = new Epic("Раз", "Два");
        epic.setId(1);
        SubTask newSubTask1 = new SubTask("Раз", "Два", 10,
                LocalDateTime.of(2025, 10, 9, 13, 20), 2, Status.NEW, 1);
        SubTask newSubTask2 = new SubTask("Раз", "Два", 10,
                LocalDateTime.of(2025, 10, 9, 13, 20), 3, Status.NEW, 1);
        epic.addSubTask(newSubTask1);
        epic.addSubTask(newSubTask2);
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void epicStatusDoneTest() {
        Epic epic = new Epic("Раз", "Два");
        epic.setId(1);
        SubTask newSubTask1 = new SubTask("Раз", "Два", 10,
                LocalDateTime.of(2025, 10, 9, 13, 20), 2, Status.DONE, 1);
        SubTask newSubTask2 = new SubTask("Раз", "Два", 10,
                LocalDateTime.of(2025, 10, 9, 13, 20), 3, Status.DONE, 1);
        epic.addSubTask(newSubTask1);
        epic.addSubTask(newSubTask2);
        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void epicStatusDoneAndNewTest() {
        Epic epic = new Epic("Раз", "Два");
        epic.setId(1);
        SubTask newSubTask1 = new SubTask("Раз", "Два", 10,
                LocalDateTime.of(2025, 10, 9, 13, 20), 2, Status.NEW, 1);
        SubTask newSubTask2 = new SubTask("Раз", "Два", 10,
                LocalDateTime.of(2025, 10, 9, 13, 20), 3, Status.DONE, 1);
        epic.addSubTask(newSubTask1);
        epic.addSubTask(newSubTask2);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void epicStatusInProgressTest() {
        Epic epic = new Epic("Раз", "Два");
        epic.setId(1);
        SubTask newSubTask1 = new SubTask("Раз", "Два", 10,
                LocalDateTime.of(2025, 10, 9, 13, 20), 2, Status.IN_PROGRESS, 1);
        SubTask newSubTask2 = new SubTask("Раз", "Два", 10,
                LocalDateTime.of(2025, 10, 9, 13, 20), 3, Status.IN_PROGRESS, 1);
        epic.addSubTask(newSubTask1);
        epic.addSubTask(newSubTask2);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}