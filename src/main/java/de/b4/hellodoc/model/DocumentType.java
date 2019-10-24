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
  @Column(nullable = false)
  public String mimetype;
  @Column(nullable = false, unique = true)
  public String extension;
  @Column(nullable = false)
  public String name;
}
