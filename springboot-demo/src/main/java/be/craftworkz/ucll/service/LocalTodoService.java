package be.craftworkz.ucll.service;

import be.craftworkz.ucll.model.Todo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!production")
public class LocalTodoService implements TodoService {
  private List<Todo> todos = new ArrayList<>();

  public LocalTodoService() {
    todos.add(new Todo("UCCL presentation", "Make the presentation awesome"));
    todos.add(new Todo("Order pizzas", "Make sure there is plenty of food"));
  }

  @Override
  public List<Todo> getTodos() {
    return todos;
  }

  @Override
  public Todo createTodo(final Todo newTodo) {
    todos.add(newTodo);
    return newTodo;
  }
}
