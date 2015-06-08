package todo.todoitem;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class DeleteToDoItemCommand {
    @TargetAggregateIdentifier
    private final String todoId;

    public DeleteToDoItemCommand(String todoId) {
        this.todoId = todoId;
    }

    public String getTodoId() {
        return todoId;
    }
}
