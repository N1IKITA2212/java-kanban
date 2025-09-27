package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.http.HttpTaskServer;
import ru.practicum.http.adapters.DurationAdapter;
import ru.practicum.http.adapters.LocalDateTimeAdapter;
import ru.practicum.manager.InMemoryTaskManager;
import ru.practicum.manager.TaskManager;
import ru.practicum.model.Epic;
import ru.practicum.model.SubTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpSubTaskHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter()).setPrettyPrinting();
    Gson gson = gsonBuilder.create();

    public HttpSubTaskHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteTasks();
        manager.deleteSubTasks();
        manager.deleteEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void postSubTaskHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание эпика");
        int epicId = manager.createEpic(epic);
        SubTask subTask = new SubTask("Test 1", "Test 1 desc", 10, LocalDateTime.of(2025, 12, 12, 12, 12, 12), epicId);

        String taskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

        List<SubTask> list = manager.getSubTasks();

        Assertions.assertEquals("Test 1", list.getFirst().getName());
        Assertions.assertEquals(1, list.size());
    }

    @Test
    public void getSubTasksHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание эпика");
        int epicId = manager.createEpic(epic);
        SubTask subTask = new SubTask("Test 1", "Test 1 desc", 10, LocalDateTime.of(2025, 12, 12, 12, 12, 12), epicId);
        SubTask subTask1 = new SubTask("Test 1", "Test 1 desc", 10, LocalDateTime.of(2024, 12, 12, 12, 12, 12), epicId);
        int subTaskId = manager.createSubTask(subTask);
        int subTaskId1 = manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonBody = response.body();

        Assertions.assertEquals(200, response.statusCode());

        List<SubTask> list = gson.fromJson(jsonBody, new ListTypeToken());

        Assertions.assertEquals(subTask.getName(), list.getFirst().getName());
        Assertions.assertEquals(subTask1.getName(), list.getLast().getName());
        Assertions.assertEquals(2, list.size());
    }

    @Test
    public void getSubTaskByIdHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание эпика");
        int epicId = manager.createEpic(epic);
        SubTask subTask = new SubTask("Test 1", "Test 1 desc", 10, LocalDateTime.of(2025, 12, 12, 12, 12, 12), epicId);
        SubTask subTask1 = new SubTask("Test 1", "Test 1 desc", 10, LocalDateTime.of(2024, 12, 12, 12, 12, 12), epicId);
        int subTaskId = manager.createSubTask(subTask);
        int subTaskId1 = manager.createSubTask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTaskId1);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonBody = response.body();

        Assertions.assertEquals(200, response.statusCode());

        SubTask subTask1FromServer = gson.fromJson(jsonBody, SubTask.class);

        Assertions.assertEquals(subTask1.getName(), subTask1FromServer.getName());
        Assertions.assertEquals(subTask1, subTask1FromServer);
    }

    @Test
    public void deleteSubTaskHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание эпика");
        int epicId = manager.createEpic(epic);
        SubTask subTask = new SubTask("Test 1", "Test 1 desc", 10, LocalDateTime.of(2025, 12, 12, 12, 12, 12), epicId);

        int subTaskId = manager.createSubTask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTaskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        Assertions.assertEquals(0, manager.getSubTasks().size());
    }

    class ListTypeToken extends TypeToken<List<SubTask>> {

    }
}
