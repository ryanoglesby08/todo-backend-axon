package todo.todoitem;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

public class ToDoItemAggregate extends AbstractAnnotatedAggregateRoot {
    @AggregateIdentifier
    private String id;

    public ToDoItemAggregate() {
    }

    @CommandHandler
    public ToDoItemAggregate(CreateToDoItemCommand command) {
        apply(new ToDoItemCreatedEvent(command.getTodoId(), command.getTitle()));
    }

    @CommandHandler
    public void markCompleted(MarkCompletedCommand command) {
        apply(new ToDoItemCompletedEvent(id));
    }

    @CommandHandler
    public void updateTitle(UpdateToDoItemTitleCommand command) {
        apply(new TodoItemTitleUpdatedEvent(id, command.getTitle()));
    }

    @EventHandler
    public void on(ToDoItemCreatedEvent event) {
        this.id = event.getTodoId();
    }
}
