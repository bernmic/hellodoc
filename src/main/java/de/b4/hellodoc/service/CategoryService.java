package de.b4.hellodoc.service;

import de.b4.hellodoc.model.Category;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class CategoryService {
/*
  @Inject
  AgroalDataSource defaultDataSource;
*/
  @Inject
  EntityManager entityManager;

  public Category[] getAll() {
    return entityManager.createNamedQuery("Category.findAll", Category.class).getResultList().toArray(new Category[0]);
  }

  public Category getById(Long id) {
    Category entity = entityManager.find(Category.class, id);
    return entity;
  }

  @Transactional
  public Category create(Category category) {
    entityManager.persist(category);
    return category;
  }

  @Transactional
  public Category update(Category category) {
    Category existingCategory = entityManager.find(Category.class, category.getId());
    existingCategory.setName(category.getName());
    existingCategory.setDescription(category.getDescription());
    return existingCategory;
  }

  @Transactional
  public void delete(Category category) {
    entityManager.remove(category);
  }
}
