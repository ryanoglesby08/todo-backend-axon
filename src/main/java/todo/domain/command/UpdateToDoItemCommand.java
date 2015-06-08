package todo.domain.command;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;
import todo.domain.ToDoItem;

public class UpdateToDoItemCommand {

    @TargetAggregateIdentifier
    private final String todoId;
    private final ToDoItem todoUpdates;

    public UpdateToDoItemCommand(String todoId, ToDoItem todoUpdates) {
        this.todoId = todoId;
        this.todoUpdates = todoUpdates;
    }

    public ToDoItem getTodoUpdates() {
        return todoUpdates;
    }
}
