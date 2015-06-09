package todo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ToDoItem {
    private String id;
    private String title;
    private Boolean completed;
    private Integer order;

    @JsonCreator
    public ToDoItem(@JsonProperty("title") String title,
                    @JsonProperty("completed") Boolean completed,
                    @JsonProperty("order") Integer order) {
        this.title = title;
        this.completed = completed;
        this.order = order;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isCompleted() { return completed; }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToDoItem toDoItem = (ToDoItem) o;

        return id.equals(toDoItem.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
