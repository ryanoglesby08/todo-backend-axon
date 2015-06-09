package todo.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import todo.ToDoEventHandler;
import todo.domain.ToDoItem;
import todo.domain.command.CreateToDoItemCommand;
import todo.domain.command.DeleteToDoItemCommand;
import todo.domain.command.UpdateToDoItemCommand;
import todo.persistance.TodoList;
import todo.view.ToDoItemViewFactory;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class ToDoControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CommandGateway commandGateway;

    @Mock
    private TodoList todoList;

    @Mock
    private ToDoEventHandler eventHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        ToDoItemViewFactory toDoItemViewFactory = new ToDoItemViewFactory("http://test.host/todos");
        ToDoController toDoController = new ToDoController(commandGateway, todoList, eventHandler, toDoItemViewFactory);

        mockMvc = MockMvcBuilders.standaloneSetup(toDoController).build();
    }

    @Test
    public void index_rendersViewOfAllTodos() throws Exception {
        ToDoItem todo1 = new ToDoItem("do something", false, 1);
        todo1.setId("123abc");
        ToDoItem todo2 = new ToDoItem("do something else", false, 2);
        todo2.setId("456def");

        when(todoList.all()).thenReturn(Arrays.asList(todo1, todo2));

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("123abc"))
                .andExpect(jsonPath("$[0].title").value("do something"))
                .andExpect(jsonPath("$[0].completed").value(false))
                .andExpect(jsonPath("$[0].url").value("http://test.host/todos/123abc"))
                .andExpect(jsonPath("$[0].order").value(1))
                .andExpect(jsonPath("$[1].id").value("456def"));
    }

    @Test
    public void create_issuesACommandToCreateATodo() throws Exception {
        ToDoItem todo = new ToDoItem("do something", null, 1);

        ObjectMapper objectMapper = new ObjectMapper();
        String todoJson = objectMapper.writeValueAsString(todo);

        mockMvc.perform(post("/todos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(todoJson))
                .andExpect(status().isOk());

        ArgumentCaptor<CreateToDoItemCommand> commandCaptor = ArgumentCaptor.forClass(CreateToDoItemCommand.class);
        verify(commandGateway).send(commandCaptor.capture());

        CreateToDoItemCommand command = commandCaptor.getValue();
        assertThat(command.getTodoId(), is(not(nullValue())));
        assertThat(command.getTodo().getTitle(), is("do something"));
        assertThat(command.getTodo().getOrder(), is(1));
        assertThat(command.getTodo().isCompleted(), is(false));
    }

    @Test
    public void show_rendersViewOfASingleTodo() throws Exception {
        ToDoItem todo = new ToDoItem("do something", true, 2);
        todo.setId("123abc");

        when(todoList.get("123abc")).thenReturn(todo);

        mockMvc.perform(get("/todos/{id}", "123abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123abc"))
                .andExpect(jsonPath("$.title").value("do something"))
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.url").value("http://test.host/todos/123abc"))
                .andExpect(jsonPath("$.order").value(2));
    }

    @Test
    public void update_issuesACommandToUpdateATodo() throws Exception {
        String todoUpdateJson = "{\"completed\": \"true\"}";

        mockMvc.perform(patch("/todos/{id}", "123abc")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(todoUpdateJson))
                .andExpect(status().isOk());

        ArgumentCaptor<UpdateToDoItemCommand> commandCaptor = ArgumentCaptor.forClass(UpdateToDoItemCommand.class);
        verify(commandGateway).send(commandCaptor.capture());

        UpdateToDoItemCommand command = commandCaptor.getValue();
        assertThat(command.getTodoUpdates().isCompleted(), is(true));
    }

    @Test
    public void delete_issuesACommandToUpdateATodo() throws Exception {
        mockMvc.perform(delete("/todos/{id}", "123abc"))
                .andExpect(status().isOk());

        verify(commandGateway).send(any(DeleteToDoItemCommand.class));
    }
}