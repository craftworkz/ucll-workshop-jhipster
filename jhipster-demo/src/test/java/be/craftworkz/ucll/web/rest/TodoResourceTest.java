package be.craftworkz.ucll.web.rest;

import be.craftworkz.ucll.Application;
import be.craftworkz.ucll.domain.Todo;
import be.craftworkz.ucll.repository.TodoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.craftworkz.ucll.domain.enumeration.TodoStatus;

/**
 * Test class for the TodoResource REST controller.
 *
 * @see TodoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TodoResourceTest {

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final TodoStatus DEFAULT_STATUS = TodoStatus.todo;
    private static final TodoStatus UPDATED_STATUS = TodoStatus.inprogress;

    @Inject
    private TodoRepository todoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restTodoMockMvc;

    private Todo todo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TodoResource todoResource = new TodoResource();
        ReflectionTestUtils.setField(todoResource, "todoRepository", todoRepository);
        this.restTodoMockMvc = MockMvcBuilders.standaloneSetup(todoResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        todo = new Todo();
        todo.setTitle(DEFAULT_TITLE);
        todo.setDescription(DEFAULT_DESCRIPTION);
        todo.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createTodo() throws Exception {
        int databaseSizeBeforeCreate = todoRepository.findAll().size();

        // Create the Todo

        restTodoMockMvc.perform(post("/api/todos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(todo)))
                .andExpect(status().isCreated());

        // Validate the Todo in the database
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(databaseSizeBeforeCreate + 1);
        Todo testTodo = todos.get(todos.size() - 1);
        assertThat(testTodo.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTodo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTodo.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoRepository.findAll().size();
        // set the field null
        todo.setTitle(null);

        // Create the Todo, which fails.

        restTodoMockMvc.perform(post("/api/todos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(todo)))
                .andExpect(status().isBadRequest());

        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoRepository.findAll().size();
        // set the field null
        todo.setDescription(null);

        // Create the Todo, which fails.

        restTodoMockMvc.perform(post("/api/todos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(todo)))
                .andExpect(status().isBadRequest());

        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoRepository.findAll().size();
        // set the field null
        todo.setStatus(null);

        // Create the Todo, which fails.

        restTodoMockMvc.perform(post("/api/todos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(todo)))
                .andExpect(status().isBadRequest());

        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTodos() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        // Get all the todos
        restTodoMockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(todo.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

        // Get the todo
        restTodoMockMvc.perform(get("/api/todos/{id}", todo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(todo.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTodo() throws Exception {
        // Get the todo
        restTodoMockMvc.perform(get("/api/todos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

		int databaseSizeBeforeUpdate = todoRepository.findAll().size();

        // Update the todo
        todo.setTitle(UPDATED_TITLE);
        todo.setDescription(UPDATED_DESCRIPTION);
        todo.setStatus(UPDATED_STATUS);
        

        restTodoMockMvc.perform(put("/api/todos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(todo)))
                .andExpect(status().isOk());

        // Validate the Todo in the database
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(databaseSizeBeforeUpdate);
        Todo testTodo = todos.get(todos.size() - 1);
        assertThat(testTodo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTodo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTodo.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteTodo() throws Exception {
        // Initialize the database
        todoRepository.saveAndFlush(todo);

		int databaseSizeBeforeDelete = todoRepository.findAll().size();

        // Get the todo
        restTodoMockMvc.perform(delete("/api/todos/{id}", todo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
