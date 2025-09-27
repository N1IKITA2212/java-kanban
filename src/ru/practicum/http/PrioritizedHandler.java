package ru.practicum.http;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.manager.TaskManager;
import ru.practicum.model.Task;

import java.io.IOException;
import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            Set<Task> prioritized = taskManager.getPrioritizedTasks();
            String responseBody = gson.toJson(prioritized);
            sendText(exchange, 200, responseBody);
            exchange.close();
        } else {
            sendMethodNotAllowed(exchange);
        }
    }
}
