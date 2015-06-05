package todo;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todo.todoitem.CreateToDoItemCommand;
import todo.todoitem.ToDoItem;

import java.util.List;
import java.util.UUID;

@RestController
public class ToDoController {
    private final CommandGateway commandGateway;
    private final TodoList list;

    @Autowired
    public ToDoController(CommandGateway commandGateway, TodoList list) {
        this.commandGateway = commandGateway;
        this.list = list;
    }

    @RequestMapping("/")
    String home() {
        List<ToDoItem> items = list.all();

        return items.get(0).getDescription();
    }

    @RequestMapping("/create")
    String create() {
        commandGateway.send(new CreateToDoItemCommand(UUID.randomUUID().toString(), "Learn Spring + Axon"));

        return "Created";
    }
}
