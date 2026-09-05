package org.ecommerce.ckecom.service;

import org.ecommerce.ckecom.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    final private List<Category> categories = new ArrayList<>();
    private Long idCounter = 1L;

    @Override
    public List<Category> getAllCategories(){
        return categories;
    }

    @Override
    public void createCategory(Category category){
        category.setCategoryId(idCounter++);
        categories.add(category);
    }

    @Override
    public String updateCategory(Long categoryId, Category category){
        Optional<Category> categoryOptional = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst();
        if (categoryOptional.isPresent()) {
            Category existingCategory = categoryOptional.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return "Category with categoryId : " + categoryId + " Updated Successfully!";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
    }

    @Override
    public String deleteCategory(Long categoryId){
        Optional<Category> categoryOptional = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst();
        if (categoryOptional.isPresent()) {
            categories.remove(categoryOptional.get());
            return "Category with categoryId : " + categoryId + " Deleted Successfully!";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
    }
}
