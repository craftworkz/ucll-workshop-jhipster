package be.craftworkz.ucll.web.rest;

import com.codahale.metrics.annotation.Timed;
import be.craftworkz.ucll.domain.Todo;
import be.craftworkz.ucll.repository.TodoRepository;
import be.craftworkz.ucll.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Todo.
 */
@RestController
@RequestMapping("/api")
public class TodoResource {

    private final Logger log = LoggerFactory.getLogger(TodoResource.class);

    @Inject
    private TodoRepository todoRepository;

    /**
     * POST  /todos -> Create a new todo.
     */
    @RequestMapping(value = "/todos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Todo> create(@Valid @RequestBody Todo todo) throws URISyntaxException {
        log.debug("REST request to save Todo : {}", todo);
        if (todo.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new todo cannot already have an ID").body(null);
        }
        Todo result = todoRepository.save(todo);
        return ResponseEntity.created(new URI("/api/todos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("todo", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /todos -> Updates an existing todo.
     */
    @RequestMapping(value = "/todos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Todo> update(@Valid @RequestBody Todo todo) throws URISyntaxException {
        log.debug("REST request to update Todo : {}", todo);
        if (todo.getId() == null) {
            return create(todo);
        }
        Todo result = todoRepository.save(todo);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("todo", todo.getId().toString()))
                .body(result);
    }

    /**
     * GET  /todos -> get all the todos.
     */
    @RequestMapping(value = "/todos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Todo> getAll() {
        log.debug("REST request to get all Todos");
        return todoRepository.findAll();
    }

    /**
     * GET  /todos/:id -> get the "id" todo.
     */
    @RequestMapping(value = "/todos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Todo> get(@PathVariable Long id) {
        log.debug("REST request to get Todo : {}", id);
        return Optional.ofNullable(todoRepository.findOne(id))
            .map(todo -> new ResponseEntity<>(
                todo,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /todos/:id -> delete the "id" todo.
     */
    @RequestMapping(value = "/todos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Todo : {}", id);
        todoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("todo", id.toString())).build();
    }
}
