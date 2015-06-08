package todo.todoitem;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class UpdateToDoItemTitleCommand {

    @TargetAggregateIdentifier
    private final String todoId;
    private final String title;

    public UpdateToDoItemTitleCommand(String todoId, String title) {
        this.todoId = todoId;
        this.title = title;
    }

    public String getTodoId() {
        return todoId;
    }

    public String getTitle() {
        return title;
    }
}
