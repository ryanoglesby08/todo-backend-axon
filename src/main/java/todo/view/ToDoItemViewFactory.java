package todo.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import todo.ToDoController;
import todo.domain.ToDoItem;

@Component
public class ToDoItemViewFactory {
    private String apiRoot;

    @Autowired
    public ToDoItemViewFactory(@Value("${api.root}") String apiRoot) {
        this.apiRoot = apiRoot;
    }

    public ToDoItemView build(ToDoItem todo) {
        return new ToDoItemView(todo, todoUrlFor(todo.getId()));
    }

    private String todoUrlFor(String id) {
        return apiRoot + ToDoController.TODO_URL.replace("{id}", id);
    }
}
