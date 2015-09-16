package be.craftworkz.ucll.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Todo {
  @Id
  @GeneratedValue
  private Long id;
  private String title;
  private String description;

  public Todo() {
  }

  public Todo(final String title, final String description) {
    this.description = description;
    this.title = title;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.title, this.description);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }
    final Todo other = (Todo) obj;
    return Objects.equals(this.title, other.title)
      && Objects.equals(this.description, other.description);
  }
}
