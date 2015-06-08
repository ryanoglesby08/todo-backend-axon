package todo.todoitem;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class CreateToDoItemCommand {

    @TargetAggregateIdentifier
    private final String todoId;
    private final ToDoItem todo;

    public CreateToDoItemCommand(String todoId, ToDoItem todo) {
        this.todoId = todoId;
        this.todo = todo;
    }

    public String getTodoId() {
        return todoId;
    }

    public ToDoItem getTodo() {
        return todo;
    }
}
