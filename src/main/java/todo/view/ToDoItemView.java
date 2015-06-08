package todo.view;

import todo.todoitem.ToDoItem;

public class ToDoItemView {
    private final ToDoItem todo;
    private final String todoUrl;

    public static ToDoItemView build(ToDoItem todo, ToDoUrlBuilder urlBuilder) {
        String todoUrl = urlBuilder.build(todo.getId());
        return new ToDoItemView(todo, todoUrl);
    }

    private ToDoItemView(ToDoItem todo, String todoUrl) {
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


}
