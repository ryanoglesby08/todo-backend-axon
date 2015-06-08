package todo.domain.event;

public class ToDoItemDeletedEvent {
    private final String todoId;

    public ToDoItemDeletedEvent(String todoId) {
        this.todoId = todoId;
    }

    public String getTodoId() {
        return todoId;
    }
}
