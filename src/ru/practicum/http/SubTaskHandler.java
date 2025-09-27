package ru.practicum.http;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.TaskOverlapException;
import ru.practicum.manager.TaskManager;
import ru.practicum.model.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubTaskHandler extends BaseHttpHandler {

    public SubTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] splitPath = httpExchange.getRequestURI().getPath().split("/");
        String requestMethod = httpExchange.getRequestMethod();
        // Путь вида /subtasks
        try {
            if (splitPath.length == 2) {
                switch (requestMethod) {
                    case "GET":
                        getSubTasksHandle(httpExchange);
                        break;
                    case "POST":
                        postCreateOrUpdateSubTaskHandle(httpExchange);
                        break;
                    default:
                        sendMethodNotAllowed(httpExchange);
                }
                // // Путь вида /subtasks/{id}
            } else if (splitPath.length == 3) {
                Integer id = parseIdOrSendBadRequest(httpExchange, splitPath[2]);
                if (id == null) {
                    return;
                }
                switch (requestMethod) {
                    case "GET":
                        getSubTaskByIdHandle(httpExchange, id);
                        break;
                    case "DELETE":
                        deleteSubTaskHandle(httpExchange, id);
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

    public void getSubTasksHandle(HttpExchange httpExchange) throws IOException {
        List<SubTask> subTaskList = taskManager.getSubTasks();
        String responseBody = gson.toJson(subTaskList);
        sendText(httpExchange, 200, responseBody);
        httpExchange.close();
    }

    public void postCreateOrUpdateSubTaskHandle(HttpExchange httpExchange) throws IOException {
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (requestBody.isEmpty()) {
            sendBadRequest(httpExchange);
            return;
        }
        JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
        try {
            if (!jsonObject.has("id")) {
                taskManager.createSubTask(gson.fromJson(jsonObject, SubTask.class));
                sendText(httpExchange, 201, "Подзадача успешно создана");
            } else {
                SubTask subTask = gson.fromJson(jsonObject, SubTask.class);
                taskManager.updateSubTask(subTask);
                sendText(httpExchange, 201, "Подзадача успешно обновлена");
            }
        } catch (TaskOverlapException e) {
            sendHasOverlaps(httpExchange);
        } finally {
            httpExchange.close();
        }
    }

    public void getSubTaskByIdHandle(HttpExchange httpExchange, int id) throws IOException {
        SubTask subTask = taskManager.getSubTaskById(id);
        String responseBody = gson.toJson(subTask);
        sendText(httpExchange, 200, responseBody);
    }

    public void deleteSubTaskHandle(HttpExchange httpExchange, int id) throws IOException {
        taskManager.removeSubTask(id);
        sendText(httpExchange, 200, "Подзадача успешно удалена");
    }
}
