package ru.practicum.manager;

import ru.practicum.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> history = new HashMap<>();
    private Node first;
    private Node last;

    private void linkLast(Task task) { //Добавляем элемент в конец списка и меняем ссылки last и first(при отсутствии first ранее)
        Node newLast = new Node(task, last, null);
        if (first == null) { // Если список пустой, то добавляется так
            first = newLast;
        } else {
            last.next = newLast;
        }
        last = newLast;
        history.put(task.getId(), last);
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node next = first;
        while (next != null) {
            tasks.add(next.task);
            next = next.next;
        }
        return tasks;
    }

    private void removeNode(Node node) {
        /*
        Здесь нужна проверка задачи на существование в истории, если мы просто создадим задачу и не будем к ней
        обращаться, то она не попадет в историю, а при вызове метода removeTask в InMemoryHistoryManager мы удаляем
        задачу из истории, для этого нужна проверка на Null
         */
        if (node == null) {
            return;
        }
        if (first == last) { //Проверяем случай, когда весь список состоит из одного элемента
            first = null;
            last = null;
        } else if (first == node) { //Проверяем, что первый элемент и переданный в метод ссылаются на один узел
            first = first.next;
            first.prev = null;
        } else if (last == node) { //Аналогично проверяем последний
            last = last.prev;
            last.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        history.remove(node.task.getId());
    }

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            removeNode(history.get(task.getId()));
        }
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
    }
}
