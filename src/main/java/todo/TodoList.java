package todo;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.stereotype.Service;
import todo.todoitem.ToDoItem;
import todo.todoitem.ToDoItemCreatedEvent;
import todo.todoitem.TodoItemTitleUpdatedEvent;

import java.util.ArrayList;
import java.util.List;

@Service
public class TodoList {
    private List<ToDoItem> todos;

    public TodoList() {
        todos = new ArrayList<ToDoItem>();
    }

    public List<ToDoItem> all() {
        return todos;
    }

    public ToDoItem get(String id) {
        for (ToDoItem todo : todos) {
            if(todo.getId().equals(id)) {
                return todo;
            }
        }

        return null;
    }

    public void clear() {
        todos.clear();
    }

    @EventHandler
    public void addItem(ToDoItemCreatedEvent event) {
        todos.add(new ToDoItem(event.getTodoId(), event.getTitle()));
    }

    @EventHandler
    public void updateItemTitle(TodoItemTitleUpdatedEvent event) {
        ToDoItem todo = get(event.getTodoId());
        todo.setTitle(event.getTitle());
    }
}
