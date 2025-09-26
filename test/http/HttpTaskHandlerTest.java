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
import ru.practicum.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter()).setPrettyPrinting();
    Gson gson = gsonBuilder.create();
    Task task;
    Task task1;

    @BeforeEach
    public void setUp() throws IOException {
        manager.deleteTasks();
        manager.deleteSubTasks();
        manager.deleteEpics();
        taskServer.start();
        task = new Task("Test 1", "Test 1 desc", 10, LocalDateTime.of(2025, 12, 12, 12, 12, 12));
        task1 = new Task("Test 2", "Test 2 desc", 10, LocalDateTime.of(2024, 11, 12, 12, 12, 12));
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void postCreateTaskHandleTest() throws IOException, InterruptedException {

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

        List<Task> tasks = manager.getTasks();

        Assertions.assertNotNull(tasks, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals("Test 1", tasks.get(0).getName());
    }

    @Test
    public void getTasksHandleTest() throws IOException, InterruptedException {

        manager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        String jsonBody = response.body();

        List<Task> tasks = gson.fromJson(jsonBody, new ListTypeToken().getType());

        Assertions.assertEquals(task.getName(), tasks.getFirst().getName());
        Assertions.assertEquals(task.getDescription(), tasks.getFirst().getDescription());


    }

    @Test
    public void getTaskByIdHandleTest() throws IOException, InterruptedException {
        int id = manager.createTask(task);
        int id2 = manager.createTask(task1);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        String jsonBody = response.body();

        Task task1FromServer = gson.fromJson(jsonBody, Task.class);

        Assertions.assertEquals(task.getName(), task1FromServer.getName());


    }

    @Test
    public void deleteTaskHandleTest() throws IOException, InterruptedException {
        int id = manager.createTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        Assertions.assertEquals(0, manager.getTasks().size());
    }

    @Test
    public void getHistoryHandleTest() throws IOException, InterruptedException {
        int task1Id = manager.createTask(task);
        int task2Id = manager.createTask(task1);

        manager.getTaskById(task1Id);
        manager.getTaskById(task2Id);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        String jsonBody = response.body();
        List<Task> history = gson.fromJson(jsonBody, new ListTypeToken());

        Assertions.assertEquals(task.getName(), history.getFirst().getName());
    }

    @Test
    public void getPrioritizedTasksHandleTest() throws IOException, InterruptedException {
        int task1Id = manager.createTask(task);
        int task2Id = manager.createTask(task1);

        manager.getTaskById(task1Id);
        manager.getTaskById(task2Id);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());

        String jsonBody = response.body();
        List<Task> history = gson.fromJson(jsonBody, new ListTypeToken());

        Assertions.assertEquals(task1.getName(), history.getFirst().getName());
    }

    class ListTypeToken extends TypeToken<List<Task>> {

    }

}
