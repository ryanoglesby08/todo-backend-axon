package todo.todoitem;

public class ToDoItemCreatedEvent {
    private final String todoId;
    private final String title;

    public ToDoItemCreatedEvent(String todoId, String title) {
        this.todoId = todoId;
        this.title = title;
    }

    public String getTodoId() {
        return todoId;
    }

    public String getTitle() {
        return title;
    }
}
