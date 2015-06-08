package todo.domain;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class UpdateToDoItemCommand {

    @TargetAggregateIdentifier
    private final String todoId;
    private final ToDoItem todoUpdates;

    public UpdateToDoItemCommand(String todoId, ToDoItem todoUpdates) {
        this.todoId = todoId;
        this.todoUpdates = todoUpdates;
    }

    public String getTodoId() {
        return todoId;
    }

    public ToDoItem getTodoUpdates() {
        return todoUpdates;
    }
}
