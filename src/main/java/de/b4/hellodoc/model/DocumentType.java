package de.b4.hellodoc.model;

import javax.persistence.*;

@Entity
@Table(name = "documenttype")
@NamedQuery(name = "DocumentType.findAll",
        query = "SELECT d FROM DocumentType d ORDER BY d.name",
        hints = @QueryHint(name = "org.hibernate.cacheable", value = "true") )
@Cacheable
public class DocumentType {
  @Id
  String extension;
  @Column(nullable = false)
  String name;
  @Column(nullable = false)
  String mimetype;

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMimetype() {
    return mimetype;
  }

  public void setMimetype(String mimetype) {
    this.mimetype = mimetype;
  }
}
