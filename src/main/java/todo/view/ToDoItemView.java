package todo.view;

import todo.domain.ToDoItem;

public class ToDoItemView {
    private final ToDoItem todo;
    private final String todoUrl;

    public ToDoItemView(ToDoItem todo, String todoUrl) {
        this.todo = todo;
        this.todoUrl = todoUrl;
    }

    public String getId() {
        return todo.getId();
    }

    public String getTitle() {
        return todo.getTitle();
    }

    public boolean isCompleted() {
        return todo.isCompleted();
    }

    public String getUrl() {
        return todoUrl;
    }

    public Integer getOrder() { return todo.getOrder(); }
}
