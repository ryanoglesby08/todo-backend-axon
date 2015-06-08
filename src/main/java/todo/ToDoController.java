package todo;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import todo.todoitem.CreateToDoItemCommand;
import todo.todoitem.ToDoItem;
import todo.todoitem.UpdateToDoItemTitleCommand;

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

    @Autowired
    public ToDoController(CommandGateway commandGateway, TodoList list) {
        this.commandGateway = commandGateway;
        this.list = list;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ToDoItemView> index(HttpServletRequest request) {
        List<ToDoItemView> todos = new ArrayList<ToDoItemView>();
        for (ToDoItem todo : list.all()) {
            todos.add(ToDoItemView.build(todo, request));
        }

        return todos;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ToDoItemView create(@RequestBody ToDoItem todo, HttpServletRequest request) {
        String id = UUID.randomUUID().toString();
        commandGateway.send(new CreateToDoItemCommand(id, todo.getTitle()));

        todo.setId(id);
        return ToDoItemView.build(todo, request);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String delete() {
        list.clear();
        return "Deleted";
    }

    @RequestMapping(value = TODO_URL, method = RequestMethod.GET)
    public ToDoItem show(@PathVariable String id) {
        return list.get(id);
    }

    @RequestMapping(value = TODO_URL, method = RequestMethod.PATCH)
    public ToDoItem update(@PathVariable String id, @RequestBody ToDoItem todo) {
        commandGateway.send(new UpdateToDoItemTitleCommand(id, todo.getTitle()));

        return null;
    }
}
