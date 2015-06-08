package todo.todoitem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ToDoItem {
    private String id;
    private String title;
    private final boolean completed;

    @JsonCreator
    public ToDoItem(@JsonProperty("title")  String title) {
        this.title = title;
        this.completed = false;
    }

    public ToDoItem(String id, String title) {
        this(title);
        this.id = id;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() { return completed; }
}
