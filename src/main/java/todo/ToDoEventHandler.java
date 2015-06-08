package todo;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import todo.todoitem.ToDoItem;
import todo.todoitem.ToDoItemCreatedEvent;
import todo.todoitem.ToDoItemDeletedEvent;
import todo.todoitem.TodoItemUpdatedEvent;
import todo.view.ToDoItemView;
import todo.view.ToDoUrlBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class ToDoEventHandler {

    private final TodoList todos;
    private final ToDoUrlBuilder toDoUrlBuilder;
    private Map<String, DeferredResult<ToDoItemView>> httpResults;

    @Autowired
    public ToDoEventHandler(TodoList todos, ToDoUrlBuilder toDoUrlBuilder) {
        this.todos = todos;
        this.toDoUrlBuilder = toDoUrlBuilder;
        httpResults = new HashMap<String, DeferredResult<ToDoItemView>>();
    }

    public void linkResultWithEvent(String todoId, DeferredResult<ToDoItemView> result) {
        httpResults.put(todoId, result);
    }

    @EventHandler
    public void handle(ToDoItemCreatedEvent event) {
        ToDoItem todo = event.getTodo();

        todo.setId(event.getTodoId());
        todos.add(todo);

        finishHttpRequestFor(todo);
    }

    @EventHandler
    public void handle(TodoItemUpdatedEvent event) {
        System.out.println("Updated! " + event.getTodoId());

        ToDoItem todo = todos.get(event.getTodoId());
        ToDoItem updates = event.getTodoUpdates();

        String title = updates.getTitle();
        if( title != null ) {
            todo.setTitle(title);
        }

        boolean completed = updates.isCompleted();
        todo.setCompleted(completed);

        Integer order = updates.getOrder();
        if( order != null ) {
            todo.setOrder(order);
        }

        finishHttpRequestFor(todo);
    }

    @EventHandler
    public void handle(ToDoItemDeletedEvent event) {
        ToDoItem todo = todos.remove(event.getTodoId());

        finishHttpRequestFor(todo);
    }

    private void finishHttpRequestFor(ToDoItem todo) {
        DeferredResult<ToDoItemView> result = httpResults.get(todo.getId());
        result.setResult(ToDoItemView.build(todo, toDoUrlBuilder));

        httpResults.remove(todo.getId());
    }
}
