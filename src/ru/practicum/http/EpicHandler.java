package ru.practicum.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.TaskOverlapException;
import ru.practicum.manager.TaskManager;
import ru.practicum.model.Epic;
import ru.practicum.model.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {


    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String[] splitPath = httpExchange.getRequestURI().getPath().split("/");
        String requestMethod = httpExchange.getRequestMethod();
        // Путь вида: /epics
        if (splitPath.length == 2) {
            switch (requestMethod) {
                case "GET":
                    // Метод getEpics (Все эпики менеджера)
                    getEpicsHandle(httpExchange);
                    break;
                case "POST":
                    // Метод createEpic
                    postCreateEpicHandle(httpExchange);
                    break;
            }
            // Путь вида: /epics/{id}
        } else if (splitPath.length == 3) {
            // Если id не число возвращаем 400
            Integer id = parseIdOrSendBadRequest(httpExchange, splitPath[2]);
            if (id == null) {
                return;
            }
            switch (requestMethod) {
                case "GET":
                    getEpicByIdHandle(httpExchange, id);
                    break;
                case "DELETE":
                    deleteEpicHandle(httpExchange, id);
                    break;
            }
        } else if (splitPath.length == 4) {
            Integer id = parseIdOrSendBadRequest(httpExchange, splitPath[2]);
            if (id == null) {
                return;
            }
            getSubTaskByEpicHandle(httpExchange, id);
        }
    }

    public void getEpicsHandle(HttpExchange httpExchange) throws IOException {
        List<Epic> epicList = taskManager.getEpics();
        String responseBody = gson.toJson(epicList);
        sendText(httpExchange, 200, responseBody);
        httpExchange.close();
    }

    public void postCreateEpicHandle(HttpExchange httpExchange) throws IOException {
        String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            Epic epic = gson.fromJson(requestBody, Epic.class);
            taskManager.createEpic(epic);
            sendText(httpExchange, 201, "Эпик успешно создан");
        } catch (TaskOverlapException e) {
            // Если менеджер находит пересечение, то отправляем 406
            sendHasOverlaps(httpExchange);
        } finally {
            httpExchange.close();
        }
    }

    public void getEpicByIdHandle(HttpExchange httpExchange, int id) throws IOException {
        try {
            Epic epic = taskManager.getEpicById(id);
            String responseBody = gson.toJson(epic);
            sendText(httpExchange, 200, responseBody);
        } catch (NotFoundException e) {
            // Если эпика нет в менеджере отправляет 404
            sendNotFound(httpExchange);
        } finally {
            httpExchange.close();
        }
    }

    public void deleteEpicHandle(HttpExchange httpExchange, int id) throws IOException {
        try {
            taskManager.removeEpic(id);
            sendText(httpExchange, 200, "Эпик успешно удален");
        } catch (NotFoundException e) {
            sendText(httpExchange, 200, "Такого эпика не существует, удалять нечего");
        } finally {
            httpExchange.close();
        }
    }

    public void getSubTaskByEpicHandle(HttpExchange httpExchange, int id) throws IOException {
        try {
            List<SubTask> subTasks = taskManager.getSubTasksByEpic(id);
            String responseBody = gson.toJson(subTasks);
            sendText(httpExchange, 200, responseBody);
        } catch (NotFoundException e) {
            // Если эпика нет в менеджере отправляет 404
            sendNotFound(httpExchange);
        } finally {
            httpExchange.close();
        }
    }
}
