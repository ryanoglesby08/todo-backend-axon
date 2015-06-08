package todo.domain;

public class TodoItemUpdatedEvent {
    private final String todoId;
    private final ToDoItem todoUpdates;

    public TodoItemUpdatedEvent(String todoId, ToDoItem todoUpdates) {
        this.todoId = todoId;
        this.todoUpdates = todoUpdates;
    }

    public String getTodoId() {
        return todoId;
    }

    public ToDoItem getTodoUpdates() {
        return todoUpdates;
    }
}
