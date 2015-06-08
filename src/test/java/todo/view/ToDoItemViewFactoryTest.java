package todo.view;

import org.junit.Before;
import org.junit.Test;
import todo.todoitem.ToDoItem;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ToDoItemViewFactoryTest {

    private ToDoItemViewFactory toDoItemViewFactory;

    @Before
    public void setUp() throws Exception {
        toDoItemViewFactory = new ToDoItemViewFactory("http://test.host/todos");
    }

    @Test
    public void createsAToDoItemView() {
        ToDoItem todo = new ToDoItem("feed the dog", false, 1);
        todo.setId("abc123");

        ToDoItemView toDoItemView = toDoItemViewFactory.build(todo);

        assertThat(toDoItemView.getId(), is("abc123"));
        assertThat(toDoItemView.getTitle(), is("feed the dog"));
        assertThat(toDoItemView.isCompleted(), is(false));
        assertThat(toDoItemView.getOrder(), is(1));
        assertThat(toDoItemView.getUrl(), is("http://test.host/todos/abc123"));
    }
}