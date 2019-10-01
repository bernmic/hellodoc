package de.b4.hellodoc.service;

import de.b4.hellodoc.model.DocumentType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class DocumentTypeService {

  @Inject
  EntityManager entityManager;

  public DocumentType[] getAll() {
    return entityManager.createNamedQuery("DocumentType.findAll", DocumentType.class).getResultList().toArray(new DocumentType[0]);
  }

  public DocumentType getByExtension(String extension) {
    DocumentType entity = entityManager.find(DocumentType.class, extension);
    return entity;
  }

  @Transactional
  public DocumentType create(DocumentType documentType) {
    entityManager.persist(documentType);
    return documentType;
  }

  @Transactional
  public DocumentType update(DocumentType documentType) {
    DocumentType existingDocumentType = entityManager.find(DocumentType.class, documentType.getExtension());
    existingDocumentType.setName(documentType.getName());
    existingDocumentType.setMimetype(documentType.getMimetype());
    return existingDocumentType;
  }

  @Transactional
  public void delete(DocumentType documentType) {
    entityManager.remove(documentType);
  }
}
