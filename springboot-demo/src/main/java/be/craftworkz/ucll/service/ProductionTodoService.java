package be.craftworkz.ucll.service;

import be.craftworkz.ucll.model.Todo;
import be.craftworkz.ucll.repository.TodoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("production")
public class ProductionTodoService implements TodoService {
  @Autowired
  private TodoRepository todoRepository;

  @Override
  public List<Todo> getTodos() {
    return todoRepository.findAll();
  }

  @Override
  public Todo createTodo(final Todo newTodo) {
    return todoRepository.save(newTodo);
  }
}
