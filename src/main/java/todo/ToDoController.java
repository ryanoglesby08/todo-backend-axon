package todo;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import todo.todoitem.CreateToDoItemCommand;
import todo.todoitem.DeleteToDoItemCommand;
import todo.todoitem.ToDoItem;
import todo.todoitem.UpdateToDoItemCommand;
import todo.view.ToDoItemView;
import todo.view.ToDoUrlBuilder;

import javax.servlet.http.HttpServletRequest;
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
    private final ToDoUrlBuilder toDoUrlBuilder;

    @Autowired
    public ToDoController(CommandGateway commandGateway, TodoList list, ToDoEventHandler eventHandler, ToDoUrlBuilder toDoUrlBuilder) {
        this.commandGateway = commandGateway;
        this.list = list;
        this.eventHandler = eventHandler;
        this.toDoUrlBuilder = toDoUrlBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ToDoItemView> index(HttpServletRequest request) {
        List<ToDoItemView> todos = new ArrayList<ToDoItemView>();
        for (ToDoItem todo : list.all()) {
            todos.add(ToDoItemView.build(todo, toDoUrlBuilder));
        }

        return todos;
    }

    @RequestMapping(method = RequestMethod.POST)
    public DeferredResult<ToDoItemView> create(@RequestBody ToDoItem todo) {
        DeferredResult<ToDoItemView> result = new DeferredResult<ToDoItemView>();
        String id = UUID.randomUUID().toString();

        eventHandler.linkResultWithEvent(id, result);
        commandGateway.send(new CreateToDoItemCommand(id, todo));

        return result;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String clear() {
        list.clear();
        return "Deleted";
    }

    @RequestMapping(value = TODO_URL, method = RequestMethod.GET)
    public ToDoItemView show(@PathVariable String id) {
        return ToDoItemView.build(list.get(id), toDoUrlBuilder);
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
