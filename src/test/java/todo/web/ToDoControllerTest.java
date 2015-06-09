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
import todo.persistance.TodoList;
import todo.view.ToDoItemViewFactory;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(jsonPath("$[1].id").value("456def"));
    }

    @Test
    public void create_sendsACommandToCreateATodo() throws Exception {
        ToDoItem todo = new ToDoItem("do something", false, 1);

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
}