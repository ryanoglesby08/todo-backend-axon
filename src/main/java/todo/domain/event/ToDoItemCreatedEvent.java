package todo.domain.event;

import todo.domain.ToDoItem;

public class ToDoItemCreatedEvent {
    private final String todoId;
    private final ToDoItem todo;

    public ToDoItemCreatedEvent(String todoId, ToDoItem todo) {
        this.todoId = todoId;
        this.todo = todo;
    }

    public String getTodoId() {
        return todoId;
    }

    public ToDoItem getTodo() {
        return todo;
    }
}
