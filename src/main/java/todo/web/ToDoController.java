package todo.web;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import todo.ToDoEventHandler;
import todo.persistance.TodoList;
import todo.domain.command.CreateToDoItemCommand;
import todo.domain.command.DeleteToDoItemCommand;
import todo.domain.ToDoItem;
import todo.domain.command.UpdateToDoItemCommand;
import todo.view.ToDoItemView;
import todo.view.ToDoItemViewFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/todos")
public class ToDoController {
    public static final String TODO_URL = "/{id}";

    private final CommandGateway commandGateway;
    private final TodoList list;
    private final ToDoEventHandler eventHandler;
    private final ToDoItemViewFactory toDoItemViewFactory;

    @Autowired
    public ToDoController(CommandGateway commandGateway, TodoList list, ToDoEventHandler eventHandler, ToDoItemViewFactory toDoItemViewFactory) {
        this.commandGateway = commandGateway;
        this.list = list;
        this.eventHandler = eventHandler;
        this.toDoItemViewFactory = toDoItemViewFactory;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ToDoItemView> index() {
        List<ToDoItemView> todoViews = new ArrayList<ToDoItemView>();
        for (ToDoItem todo : list.all()) {
            todoViews.add(toDoItemViewFactory.build(todo));
        }

        return todoViews;
    }

    @RequestMapping(method = RequestMethod.POST)
    public DeferredResult<ToDoItemView> create(@RequestBody ToDoItem todo) {
        DeferredResult<ToDoItemView> result = new DeferredResult<ToDoItemView>();
        String id = UUID.randomUUID().toString();

        todo.setCompleted(false);

        eventHandler.linkResultWithEvent(id, result);
        commandGateway.send(new CreateToDoItemCommand(id, todo));

        return result;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String clear() {
        list.clear();
        return "Deleted all todos";
    }

    @RequestMapping(value = TODO_URL, method = RequestMethod.GET)
    public ToDoItemView show(@PathVariable String id) {
        return toDoItemViewFactory.build(list.get(id));
    }

    @RequestMapping(value = TODO_URL, method = RequestMethod.PATCH)
    public DeferredResult<ToDoItemView> update(@PathVariable String id, @RequestBody ToDoItem todoUpdates) {
        DeferredResult<ToDoItemView> result = new DeferredResult<ToDoItemView>();

        eventHandler.linkResultWithEvent(id, result);
        commandGateway.send(new UpdateToDoItemCommand(id, todoUpdates));

        return result;
    }

    @RequestMapping(value = TODO_URL, method = RequestMethod.DELETE)
    public DeferredResult<ToDoItemView> delete(@PathVariable String id) {
        DeferredResult<ToDoItemView> result = new DeferredResult<ToDoItemView>();

        eventHandler.linkResultWithEvent(id, result);
        commandGateway.send(new DeleteToDoItemCommand(id));

        return result;
    }
}
