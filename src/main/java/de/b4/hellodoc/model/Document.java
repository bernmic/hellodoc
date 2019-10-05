package de.b4.hellodoc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "document")
public class Document extends PanacheEntity {
    @Column(nullable = false, length = 100)
    public String name;
    @Column(nullable = false, unique = true)
    public String path;
    @ManyToOne
    @JoinColumn(name = "documenttype_id", nullable = false)
    public DocumentType documentType;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    public Category category;
}