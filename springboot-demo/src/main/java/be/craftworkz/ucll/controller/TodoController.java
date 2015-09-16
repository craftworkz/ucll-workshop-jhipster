package be.craftworkz.ucll.controller;

import be.craftworkz.ucll.model.Todo;
import be.craftworkz.ucll.service.TodoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/todos")
public class TodoController {
  @Autowired
  private TodoService todoService;

  @RequestMapping
  public List<Todo> getTodos() {
    return todoService.getTodos();
  }

  @RequestMapping(method = RequestMethod.POST)
  public Todo todos(@RequestBody Todo newTodo) {
    return todoService.createTodo(newTodo);
  }
}
