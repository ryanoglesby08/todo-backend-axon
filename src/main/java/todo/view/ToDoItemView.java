package todo.view;

import todo.ToDoController;
import todo.todoitem.ToDoItem;

import javax.servlet.http.HttpServletRequest;

public class ToDoItemView {
    private final ToDoItem todo;
    private final String todoUrl;

    public static ToDoItemView build(ToDoItem todo, HttpServletRequest request) {
//        String todoUrl = request.getRequestURL() + "/" + todo.getId();
        String todoUrl = ("http://localhost:8080/todos" + ToDoController.TODO_URL).replace("{id}", todo.getId());
        return new ToDoItemView(todo, todoUrl);
    }

    public static ToDoItemView build(ToDoItem todo) {
        return build(todo, null);
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
