package ru.practicum.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.http.adapters.DurationAdapter;
import ru.practicum.http.adapters.LocalDateTimeAdapter;
import ru.practicum.manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler implements HttpHandler {
    protected Gson gson;
    protected TaskManager taskManager;

    protected BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter()).setPrettyPrinting().serializeNulls().create();
    }

    protected void sendText(HttpExchange httpExchange, int code, String text) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        httpExchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(bytes);
        }
        httpExchange.close();
    }


    protected void sendNotFound(HttpExchange httpExchange) throws IOException {
        final String responseBody = "Запрашиваемый объект не найден";
        OutputStream os = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(404, 0);
        os.write(responseBody.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    protected void sendHasOverlaps(HttpExchange httpExchange) throws IOException {
        final String responseBody = "Ваша задача пересекается с существующей";
        OutputStream os = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(406, 0);
        os.write(responseBody.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    protected void sendBadRequest(HttpExchange httpExchange) throws IOException {
        final String responseBody = "Ошибка в запросе";
        OutputStream os = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(400, 0);
        os.write(responseBody.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    // Метод для обработки id в случае путей /tasks/{id}, если в id не число возвращаем 400
    protected Integer parseIdOrSendBadRequest(HttpExchange httpExchange, String idString) throws IOException {
        try {
            return Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            sendBadRequest(httpExchange);
            return null;
        }
    }

    protected void sendMethodNotAllowed(HttpExchange httpExchange) throws IOException {
        final String responseBody = "Method not allowed";
        OutputStream os = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(405, 0);
        os.write(responseBody.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
