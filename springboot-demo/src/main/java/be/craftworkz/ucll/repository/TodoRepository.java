package be.craftworkz.ucll.repository;

import be.craftworkz.ucll.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
