package de.b4.hellodoc.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.*;

@Entity
@Table(name = "category")
@Cacheable
public class Category extends PanacheEntity {
  @Column(length = 100, unique = true, nullable = false)
  public String name;
  @Column(nullable = true)
  public String description;
}
