package todo;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.request.async.DeferredResult;
import todo.domain.ToDoItem;
import todo.domain.event.ToDoItemCreatedEvent;
import todo.domain.event.ToDoItemDeletedEvent;
import todo.domain.event.TodoItemUpdatedEvent;
import todo.persistance.TodoList;
import todo.view.ToDoItemView;
import todo.view.ToDoItemViewFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ToDoEventHandlerTest {
    private static final String TODO_ID = "123abc";

    private TodoList todoList;
    private ToDoEventHandler toDoEventHandler;

    @Before
    public void setUp() throws Exception {
        todoList = new TodoList();
        ToDoItemViewFactory toDoItemViewFactory = new ToDoItemViewFactory("http://test.host/todos");

        toDoEventHandler = new ToDoEventHandler(todoList, toDoItemViewFactory);
    }

    @Test
    public void receivingACreatedEventAddsATodo() throws Exception {
        ToDoItem createTodo = new ToDoItem("do something", false, 10);
        ToDoItemCreatedEvent createdEvent = new ToDoItemCreatedEvent(TODO_ID, createTodo);

        DeferredResult<ToDoItemView> result = linkResultAndTodo(TODO_ID);
        toDoEventHandler.handle(createdEvent);

        ToDoItem fetchedTodo = todoList.get(TODO_ID);
        assertThat(fetchedTodo.getId(), is(TODO_ID));
        assertThat(fetchedTodo.getTitle(), is("do something"));

        assertThatResultIsFinished(result, TODO_ID);
    }

    @Test
    public void receivingAnUpdatedEventCanUpdateATodosTitleOnly() throws Exception {
        ToDoItem todo = addTodoToList(TODO_ID);

        ToDoItem todoUpdates = new ToDoItem("do something else", null, null);
        TodoItemUpdatedEvent updatedEvent = new TodoItemUpdatedEvent(TODO_ID, todoUpdates);

        DeferredResult<ToDoItemView> result = linkResultAndTodo(TODO_ID);
        toDoEventHandler.handle(updatedEvent);

        ToDoItem updatedTodo = todoList.get(TODO_ID);
        assertThat(updatedTodo.getTitle(), is("do something else"));
        assertThat(updatedTodo.isCompleted(), is(todo.isCompleted()));
        assertThat(updatedTodo.getOrder(), is(todo.getOrder()));

        assertThatResultIsFinished(result, TODO_ID);
    }

    @Test
    public void receivingAnUpdatedEventCanUpdateATodosCompletedStatusOnly() throws Exception {
        ToDoItem todo = addTodoToList(TODO_ID);

        ToDoItem todoUpdates = new ToDoItem(null, true, null);
        TodoItemUpdatedEvent updatedEvent = new TodoItemUpdatedEvent(TODO_ID, todoUpdates);

        DeferredResult<ToDoItemView> result = linkResultAndTodo(TODO_ID);
        toDoEventHandler.handle(updatedEvent);

        ToDoItem updatedTodo = todoList.get(TODO_ID);
        assertThat(updatedTodo.getTitle(), is(todo.getTitle()));
        assertThat(updatedTodo.isCompleted(), is(true));
        assertThat(updatedTodo.getOrder(), is(todo.getOrder()));

        assertThatResultIsFinished(result, TODO_ID);
    }

    @Test
    public void receivingAnUpdatedEventCanUpdateATodosOrderOnly() throws Exception {
        ToDoItem todo = addTodoToList(TODO_ID);
        int newOrder = todo.getOrder() + 1;

        ToDoItem todoUpdates = new ToDoItem(null, null, newOrder);
        TodoItemUpdatedEvent updatedEvent = new TodoItemUpdatedEvent(TODO_ID, todoUpdates);

        DeferredResult<ToDoItemView> result = linkResultAndTodo(TODO_ID);
        toDoEventHandler.handle(updatedEvent);

        ToDoItem updatedTodo = todoList.get(TODO_ID);
        assertThat(updatedTodo.getTitle(), is(todo.getTitle()));
        assertThat(updatedTodo.isCompleted(), is(todo.isCompleted()));
        assertThat(updatedTodo.getOrder(), is(newOrder));

        assertThatResultIsFinished(result, TODO_ID);
    }

    @Test
    public void receivingADeletedEventRemovesATodo() throws Exception {
        addTodoToList(TODO_ID);

        ToDoItem anotherTodo = new ToDoItem("another task", false, 2);
        anotherTodo.setId("789ghi");
        todoList.add(anotherTodo);

        ToDoItemDeletedEvent deletedEvent = new ToDoItemDeletedEvent(TODO_ID);

        DeferredResult<ToDoItemView> result = linkResultAndTodo(TODO_ID);
        toDoEventHandler.handle(deletedEvent);

        assertThat(todoList.all().size(), is(1));
        assertThat(todoList.get(TODO_ID), is(nullValue()));

        assertThatResultIsFinished(result, TODO_ID);
    }

    private ToDoItem addTodoToList(String todoId) {
        ToDoItem todo = new ToDoItem("do something", false, 1);
        todo.setId(todoId);
        todoList.add(todo);
        return todo;
    }

    private DeferredResult<ToDoItemView> linkResultAndTodo(String todoId) {
        DeferredResult<ToDoItemView> result = new DeferredResult<ToDoItemView>();
        toDoEventHandler.linkResultWithEvent(todoId, result);
        return result;
    }

    private void assertThatResultIsFinished(DeferredResult<ToDoItemView> result, String todoId) {
        assertThat(result.hasResult(), is(true));
        ToDoItemView todoView = (ToDoItemView) result.getResult();
        assertThat(todoView.getId(), is(todoId));
    }
}