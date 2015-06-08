package todo;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import todo.todoitem.ToDoItem;
import todo.todoitem.ToDoItemCreatedEvent;
import todo.todoitem.TodoItemUpdatedEvent;
import todo.view.ToDoItemView;

import java.util.HashMap;
import java.util.Map;

@Service
public class ToDoEventHandler {

    private final TodoList todos;
    private Map<String, DeferredResult<ToDoItemView>> httpResults;

    @Autowired
    public ToDoEventHandler(TodoList todos) {
        this.todos = todos;
        httpResults = new HashMap<String, DeferredResult<ToDoItemView>>();
    }

    public void linkResultWithEvent(String todoId, DeferredResult<ToDoItemView> result) {
        httpResults.put(todoId, result);
    }

    @EventHandler
    public void handle(ToDoItemCreatedEvent event) {
        System.out.println("Something else to do! " + event.getTitle() + " (" + event.getTodoId() + ")");


        ToDoItem todo = new ToDoItem(event.getTodoId(), event.getTitle());
        todos.add(todo);

        finishHttpRequestFor(todo);
    }

    @EventHandler
    public void handle(TodoItemUpdatedEvent event) {
        System.out.println("Updated! " + event.getTodoId());

        ToDoItem todo = todos.get(event.getTodoId());
        ToDoItem updates = event.getTodoUpdates();

        String title = updates.getTitle();
        if(title != null) {
            todo.setTitle(title);
        }

        boolean completed = updates.isCompleted();
        todo.setCompleted(completed);

        finishHttpRequestFor(todo);
    }

    private void finishHttpRequestFor(ToDoItem todo) {
        DeferredResult<ToDoItemView> result = httpResults.get(todo.getId());
        result.setResult(ToDoItemView.build(todo));

        httpResults.remove(todo.getId());
    }
}
