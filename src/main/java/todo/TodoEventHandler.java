package todo;

import org.axonframework.eventhandling.annotation.EventHandler;
import todo.todoitem.ToDoItemCompletedEvent;
import todo.todoitem.ToDoItemCreatedEvent;

public class TodoEventHandler {
    @EventHandler
    public void handle(ToDoItemCreatedEvent event) {
        System.out.println("Something else to do! " + event.getTitle() + " (" + event.getTodoId() + ")");
    }

    @EventHandler
    public void handle(ToDoItemCompletedEvent event) {
        System.out.println("Yeah done with that one! " + event.getTodoId());
    }
}
