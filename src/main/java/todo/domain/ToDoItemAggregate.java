package todo.domain;

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
        apply(new ToDoItemCreatedEvent(command.getTodoId(), command.getTodo()));
    }

    @CommandHandler
    public void update(UpdateToDoItemCommand command) {
        apply(new TodoItemUpdatedEvent(id, command.getTodoUpdates()));
    }

    @CommandHandler
    public void delete(DeleteToDoItemCommand command) {
        apply(new ToDoItemDeletedEvent(id));
    }

    @EventHandler
    public void on(ToDoItemCreatedEvent event) {
        this.id = event.getTodoId();
    }
}
