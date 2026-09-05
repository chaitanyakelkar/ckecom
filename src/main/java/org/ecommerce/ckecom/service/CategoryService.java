package org.ecommerce.ckecom.service;

import org.ecommerce.ckecom.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void createCategory(Category category);
    String updateCategory(Long categoryId, Category category);
    String deleteCategory(Long categoryId);
}
