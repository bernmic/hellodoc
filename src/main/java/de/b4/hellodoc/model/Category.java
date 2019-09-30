package de.b4.hellodoc.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "category")
@NamedQuery(name = "Category.findAll",
        query = "SELECT c FROM Category c ORDER BY c.name",
        hints = @QueryHint(name = "org.hibernate.cacheable", value = "true") )
@Cacheable
public class Category {
  @Id
  @GeneratedValue(generator = "category-sequence-generator")
  @GenericGenerator(
          name = "category-sequence-generator",
          strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
          parameters = {
                  @org.hibernate.annotations.Parameter(name = "sequence_name", value = "category_sequence"),
                  @org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
                  @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
          }
  )
  Long id;
  @Column(length = 100, unique = true)
  String name;
  String description;

  public Category() {}

  public Category(Long id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
