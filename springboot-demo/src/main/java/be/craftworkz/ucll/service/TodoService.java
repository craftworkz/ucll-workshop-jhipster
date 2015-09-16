package be.craftworkz.ucll.service;

import be.craftworkz.ucll.model.Todo;
import java.util.List;

public interface TodoService {
  List<Todo> getTodos();

  Todo createTodo(Todo newTodo);
}
