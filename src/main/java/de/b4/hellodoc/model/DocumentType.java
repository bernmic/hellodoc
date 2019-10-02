package de.b4.hellodoc.model;

import javax.persistence.*;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;;

@Entity
@Table(name = "documenttype")
@Cacheable
public class DocumentType extends PanacheEntityBase {
  @Id
  public String extension;
  @Column(nullable = false)
  public String name;
  @Column(nullable = false)
  public String mimetype;
}
