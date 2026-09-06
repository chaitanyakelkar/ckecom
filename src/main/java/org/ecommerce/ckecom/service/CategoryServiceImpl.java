package org.ecommerce.ckecom.service;

import org.ecommerce.ckecom.model.Category;
import org.ecommerce.ckecom.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    // final private List<Category> categories = new ArrayList<>();
    // private Long idCounter = 1L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category){
        // category.setCategoryId(idCounter++);
        categoryRepository.save(category);
    }

    @Override
    public String updateCategory(Long categoryId, Category category){
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            Category oldCategory = optionalCategory.get();
            oldCategory.setCategoryName(category.getCategoryName());
            categoryRepository.save(oldCategory);
            return "Category with categoryId : " + categoryId + " Updated Successfully!";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
    }

    @Override
    public String deleteCategory(Long categoryId){
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
            return "Category with categoryId : " + categoryId + " Deleted Successfully!";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
    }
}
