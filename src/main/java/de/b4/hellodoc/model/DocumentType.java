package de.b4.hellodoc.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

;

@Entity
@Table(name = "documenttype")
@Cacheable
public class DocumentType extends PanacheEntity {
  @Column(name = "mimetype", nullable = false)
  public String mimetype;
  @Column(name = "extension", nullable = false, unique = true)
  public String extension;
  @Column(name = "name", nullable = false)
  public String name;
}
