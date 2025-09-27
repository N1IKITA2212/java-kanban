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

public class HttpEpicHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter()).setPrettyPrinting();
    Gson gson = gsonBuilder.create();

    public HttpEpicHandlerTest() throws IOException {
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
    public void getEpicHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Test 1 desc");
        int epicId = manager.createEpic(epic);
        SubTask subTask = new SubTask("Test 1", "Test 1 desc", 10, LocalDateTime.of(2025, 12, 12, 12, 12, 12), epicId);
        manager.createSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonBody = response.body();

        List<Epic> list = gson.fromJson(jsonBody, new EpicListTypeToken());

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(epic.getName(), list.getFirst().getName());
        Assertions.assertEquals(epic.getDescription(), list.getFirst().getDescription());
    }

    @Test
    public void postEpicHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Test 1 desc");
        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

        List<Epic> epics = manager.getEpics();
        SubTask subTask = new SubTask("Test 1", "Test 1 desc", 10, LocalDateTime.of(2025, 12, 12, 12, 12, 12), epics.getFirst().getId());
        manager.createSubTask(subTask);

        Assertions.assertEquals(1, epics.size());
        Assertions.assertEquals(epic.getName(), epics.getFirst().getName());
    }

    @Test
    public void getEpicByIdHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Test 1 desc");
        int epicId = manager.createEpic(epic);
        SubTask subTask = new SubTask("Test 1", "Test 1 desc", 10, LocalDateTime.of(2025, 12, 12, 12, 12, 12), epicId);
        int subTaskId = manager.createSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicFromServer = gson.fromJson(response.body(), Epic.class);

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(epic.getName(), epicFromServer.getName());
    }

    @Test
    public void deleteEpicHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Test 1 desc");
        int epicId = manager.createEpic(epic);
        SubTask subTask = new SubTask("Test 1", "Test 1 desc", 10, LocalDateTime.of(2025, 12, 12, 12, 12, 12), epicId);
        int subTaskId = manager.createSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        Assertions.assertEquals(0, manager.getEpics().size());
        Assertions.assertEquals(0, manager.getSubTasks().size());
    }

    @Test
    public void getSubTaskByEpicHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Test 1 desc");
        int epicId = manager.createEpic(epic);
        SubTask subTask = new SubTask("Test 1", "Test 1 desc", 10, LocalDateTime.of(2025, 12, 12, 12, 12, 12), epicId);
        int subTaskId = manager.createSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        String jsonBody = response.body();
        List<SubTask> subTasks = gson.fromJson(jsonBody, new SubTaskListTypeToken());

        Assertions.assertEquals(1, subTasks.size());
        Assertions.assertEquals(subTask.getName(), subTasks.getFirst().getName());
    }

    class EpicListTypeToken extends TypeToken<List<Epic>> {

    }

    class SubTaskListTypeToken extends TypeToken<List<SubTask>> {

    }
}
