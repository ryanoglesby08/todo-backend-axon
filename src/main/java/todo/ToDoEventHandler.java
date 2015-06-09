package todo;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import todo.persistance.TodoList;
import todo.domain.ToDoItem;
import todo.domain.event.ToDoItemCreatedEvent;
import todo.domain.event.ToDoItemDeletedEvent;
import todo.domain.event.TodoItemUpdatedEvent;
import todo.view.ToDoItemView;
import todo.view.ToDoItemViewFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class ToDoEventHandler {

    private final TodoList todos;
    private final ToDoItemViewFactory toDoItemViewFactory;
    private Map<String, DeferredResult<ToDoItemView>> httpResults;

    @Autowired
    public ToDoEventHandler(TodoList todos, ToDoItemViewFactory toDoItemViewFactory) {
        this.todos = todos;
        this.toDoItemViewFactory = toDoItemViewFactory;
        this.httpResults = new HashMap<String, DeferredResult<ToDoItemView>>();
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
        ToDoItem todo = todos.get(event.getTodoId());
        ToDoItem updates = event.getTodoUpdates();

        String title = updates.getTitle();
        if( title != null ) {
            todo.setTitle(title);
        }

        Boolean completed = updates.isCompleted();
        if( completed != null ) {
            todo.setCompleted(completed);
        }

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
        result.setResult(toDoItemViewFactory.build(todo));

        httpResults.remove(todo.getId());
    }
}
