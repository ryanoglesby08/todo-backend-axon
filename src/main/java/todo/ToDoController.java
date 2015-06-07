package todo;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import todo.todoitem.ToDoItem;

import java.util.List;

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
    public String index() {
        List<ToDoItem> items = list.all();

        return items.get(0).getTitle();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ToDoItem create(@RequestBody ToDoItem todoItem) {
//        commandGateway.send(new CreateToDoItemCommand(UUID.randomUUID().toString(), "Learn Spring + Axon"));
        return todoItem;
//        return "Created";
    }
}
