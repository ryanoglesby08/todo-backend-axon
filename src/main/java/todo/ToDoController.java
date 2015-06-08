package todo;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import todo.todoitem.CreateToDoItemCommand;
import todo.todoitem.ToDoItem;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/todos")
public class ToDoController {
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
    public ToDoItemView create(@RequestBody ToDoItem todoItem, HttpServletRequest request) {
        String id = UUID.randomUUID().toString();
        commandGateway.send(new CreateToDoItemCommand(id, todoItem.getTitle()));

        todoItem.setId(id);
        return ToDoItemView.build(todoItem, request);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String delete() {
        list.clear();
        return "Deleted";
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ToDoItem show(@PathVariable String id) {
        return list.get(id);
    }
}
