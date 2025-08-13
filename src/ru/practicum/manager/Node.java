package ru.practicum.manager;

import ru.practicum.model.Task;

class Node {
    public Node prev;
    public Node next;
    public Task task;

    public Node(Task task, Node prev, Node next) {
        this.task = task;
        this.prev = prev;
        this.next = next;
    }
}
