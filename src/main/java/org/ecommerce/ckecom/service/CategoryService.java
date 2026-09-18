package org.ecommerce.ckecom.service;

import org.ecommerce.ckecom.payload.CategoryDTO;
import org.ecommerce.ckecom.payload.CategoryResponse;


public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO category);
    CategoryDTO updateCategory(Long categoryId, CategoryDTO category);
    CategoryDTO deleteCategory(Long categoryId);
}
