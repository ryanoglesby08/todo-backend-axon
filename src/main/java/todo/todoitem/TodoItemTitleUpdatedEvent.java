package todo.todoitem;

public class TodoItemTitleUpdatedEvent {
    private final String todoId;
    private final String title;

    public TodoItemTitleUpdatedEvent(String todoId, String title) {
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
