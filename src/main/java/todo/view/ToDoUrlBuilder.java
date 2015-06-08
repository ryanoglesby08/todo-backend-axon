package todo.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import todo.ToDoController;

@Component
public class ToDoUrlBuilder {
    @Value("${api.root}")
    private String apiRoot;

    public String build(String id) {
        return apiRoot + ToDoController.TODO_URL.replace("{id}", id);
    }
}
