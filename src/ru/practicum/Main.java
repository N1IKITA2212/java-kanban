package ru.practicum;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.practicum.manager.Managers;
import ru.practicum.manager.TaskManager;
import ru.practicum.model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();

//        int taskId1 = inMemoryTaskManager.createTask(new Task("Посетить врача", "Нужно сходить к зубному вылечить зуб", 360, LocalDateTime.of(2009, 12, 12, 12, 12, 12)));
        int taskId2 = inMemoryTaskManager.createTask(new Task("Поесть", "Приготовить суп", 45, LocalDateTime.of(2001, 12, 12, 12, 12, 12)));
//        int epicId1 = inMemoryTaskManager.createEpic(new Epic("Переезд", "Уезжаю в новый дом"));
//
//        int subTaskId11 = inMemoryTaskManager.createSubTask(new SubTask("Собрать вещи", "Сложить все вещи в коробки", 30, LocalDateTime.of(2002, 12, 12, 12, 12, 12), epicId1));
//        int subTaskId12 = inMemoryTaskManager.createSubTask(new SubTask("Найти квартиру", "Найти на авито объявления о квартирах", 10, LocalDateTime.of(2003, 12, 12, 12, 12, 12), epicId1));
//
//        int epicId2 = inMemoryTaskManager.createEpic(new Epic("Собака", "Купить собаку"));
//
//        int subTaskId21 = inMemoryTaskManager.createSubTask(new SubTask("Выбрать собаку", "Сходить в приют и выбрать собаку", 500, LocalDateTime.of(2004, 12, 12, 12, 12, 12), epicId2));

//        System.out.println(inMemoryTaskManager.getTaskById(taskId1));
//        System.out.println(inMemoryTaskManager.getSubTaskById(subTaskId11));
//        System.out.println(inMemoryTaskManager.getEpicById(epicId1));

//        System.out.println(inMemoryTaskManager.getPrioritizedTasks());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter()).setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        System.out.println(gson.toJson(inMemoryTaskManager.getTasks()));
    }

    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
            jsonWriter.value(localDateTime.format(dtf));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), dtf);
        }
    }

    static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
            jsonWriter.value(duration.toMinutes());
        }

        @Override
        public Duration read(JsonReader jsonReader) throws IOException {
            return Duration.parse(jsonReader.nextString());
        }
    }
}
