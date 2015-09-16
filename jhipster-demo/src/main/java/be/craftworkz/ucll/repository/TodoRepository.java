package be.craftworkz.ucll.repository;

import be.craftworkz.ucll.domain.Todo;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Todo entity.
 */
public interface TodoRepository extends JpaRepository<Todo,Long> {

    @Query("select todo from Todo todo where todo.user.login = ?#{principal.username}")
    List<Todo> findByUserIsCurrentUser();

}
