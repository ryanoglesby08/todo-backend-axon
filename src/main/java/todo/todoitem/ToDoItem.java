package todo.todoitem;

public class ToDoItem extends ToDoItemAggregate {
    private final String id;
    private final String description;

    public ToDoItem(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
