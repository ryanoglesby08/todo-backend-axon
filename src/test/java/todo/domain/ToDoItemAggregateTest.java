package todo.domain;

import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;
import todo.domain.command.CreateToDoItemCommand;
import todo.domain.command.DeleteToDoItemCommand;
import todo.domain.command.UpdateToDoItemCommand;
import todo.domain.event.ToDoItemCreatedEvent;
import todo.domain.event.ToDoItemDeletedEvent;
import todo.domain.event.TodoItemUpdatedEvent;

public class ToDoItemAggregateTest {
    private FixtureConfiguration fixture;
    private ToDoItem todo;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(ToDoItemAggregate.class);
        todo = new ToDoItem("do something", false, 2);
    }

    @Test
    public void creatingTodoItemsEmitsCreatedEvents() throws Exception {
        fixture.given()
                .when(new CreateToDoItemCommand("1", todo))
                .expectEvents(new ToDoItemCreatedEvent("1", todo));

    }

    @Test
    public void updatingTodoItemsEmitsUpdatedEvents() throws Exception {
        ToDoItem updates = new ToDoItem(null, true, null);

        fixture.given(new ToDoItemCreatedEvent("1", todo))
                .when(new UpdateToDoItemCommand("1", updates))
                .expectEvents(new TodoItemUpdatedEvent("1", updates));
    }

    @Test
    public void deletingTodoItemsEmitsDeletedEvents() throws Exception {
        fixture.given(new ToDoItemCreatedEvent("1", todo))
                .when(new DeleteToDoItemCommand("1"))
                .expectEvents(new ToDoItemDeletedEvent("1"));
    }
}