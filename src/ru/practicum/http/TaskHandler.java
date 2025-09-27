package ru.practicum.http;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.TaskOverlapException;
import ru.practicum.manager.TaskManager;
import ru.practicum.model.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {


    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] splitPath = httpExchange.getRequestURI().getPath().split("/");
        String requestMethod = httpExchange.getRequestMethod();
        // путь вида: /tasks
        try {
            if (splitPath.length == 2) {
                switch (requestMethod) {
                    case "GET":
                        getTasksHandle(httpExchange); // Метод getTasks (Все задачи менеджера)
                        break;
                    case "POST":
                        // В случае если указан id вызывается updateTask, если нет то createTask
                        postCreateUpdateTaskHandle(httpExchange);
                        break;
                    default:
                        sendMethodNotAllowed(httpExchange);
                }
                // Путь вида /tasks/{id}
            } else if (splitPath.length == 3) {
                // Если в {id} не число, то отправляем 400
                Integer id = parseIdOrSendBadRequest(httpExchange, splitPath[2]);
                if (id == null) {
                    return;
                }
                switch (requestMethod) {
                    case "GET":
                        getTaskByIdHandle(httpExchange, id); // getTaskById
                        break;

                    case "DELETE":
                        deleteTaskHandle(httpExchange, id); // deleteTask (по id)
                        break;
                    default:
                        sendMethodNotAllowed(httpExchange);
                }
            } else {
                sendMethodNotAllowed(httpExchange);
            }
        } catch (NotFoundException e) {
            sendNotFound(httpExchange);
        } finally {
            httpExchange.close();
        }
    }

    public void getTasksHandle(HttpExchange httpExchange) throws IOException {
        List<Task> tasks = taskManager.getTasks();
        String responseBody = gson.toJson(tasks);
        sendText(httpExchange, 200, responseBody);
        httpExchange.close();
    }

    public void postCreateUpdateTaskHandle(HttpExchange httpExchange) throws IOException {
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (requestBody.isEmpty()) {
            sendBadRequest(httpExchange);
            return;
        }
        JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
        try {
            if (!jsonObject.has("id")) {
                taskManager.createTask(gson.fromJson(requestBody, Task.class));
                sendText(httpExchange, 201, "Задача успешно создана.");
            } else {
                Task task = gson.fromJson(requestBody, Task.class);
                taskManager.updateTask(task);
                sendText(httpExchange, 201, "Задача успешно обновлена.");
            }
        } catch (TaskOverlapException e) {
            sendHasOverlaps(httpExchange); // Если менеджер находит пересечение отправляем 406
        } finally {
            httpExchange.close();
        }
    }

    public void getTaskByIdHandle(HttpExchange httpExchange, int id) throws IOException {
            Task task = taskManager.getTaskById(id);
            String responseBody = gson.toJson(task);
            sendText(httpExchange, 200, responseBody);
    }

    public void deleteTaskHandle(HttpExchange httpExchange, int id) throws IOException {
            taskManager.removeTask(id);
            sendText(httpExchange, 200, "Задача успешно удалена");
    }
}