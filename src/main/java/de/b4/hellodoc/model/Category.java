package de.b4.hellodoc.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "category")
@Cacheable
public class Category extends PanacheEntity {
  @Column(name = "name", length = 256, unique = true, nullable = false)
  public String name;
  @Column(name = "description", nullable = true)
  public String description;
}
