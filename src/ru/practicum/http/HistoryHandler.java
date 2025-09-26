package ru.practicum.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.manager.TaskManager;
import ru.practicum.model.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            List<Task> history = taskManager.getHistory();
            String responseBody = gson.toJson(history);
            sendText(httpExchange, 200, responseBody);
            httpExchange.close();
        }
    }
}
