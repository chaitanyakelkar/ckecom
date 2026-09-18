package org.ecommerce.ckecom.service;

import org.ecommerce.ckecom.exception.APIException;
import org.ecommerce.ckecom.exception.ResourceNotFoundException;
import org.ecommerce.ckecom.model.Category;
import org.ecommerce.ckecom.payload.CategoryDTO;
import org.ecommerce.ckecom.payload.CategoryResponse;
import org.ecommerce.ckecom.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    // final private List<Category> categories = new ArrayList<>();
    // private Long idCounter = 1L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException{
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("ascending")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<Category> categoryList = categoryPage.getContent();
        if (categoryList.isEmpty()){
            throw new APIException("No Categories exist");
        }
        List<CategoryDTO> categoryDTOList = categoryList.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOList);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) throws APIException {
        // category.setCategoryId(idCounter++);
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory != null){
            throw new APIException("Category with the name " + savedCategory.getCategoryName() + " already exists!");
        }
        category = categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) throws ResourceNotFoundException{
        Category category = modelMapper.map(categoryDTO, Category.class);
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            Category oldCategory = optionalCategory.get();
            oldCategory.setCategoryName(category.getCategoryName());
            oldCategory = categoryRepository.save(oldCategory);
            return modelMapper.map(oldCategory, CategoryDTO.class);
        } else {
            throw new ResourceNotFoundException("Category", "Id", categoryId);
        }
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) throws ResourceNotFoundException{
        Optional<Category> categoryToDelete = categoryRepository.findById(categoryId);
        if (categoryToDelete.isPresent()) {
            categoryRepository.deleteById(categoryId);
            return modelMapper.map(categoryToDelete.get(), CategoryDTO.class);
        } else {
            throw new ResourceNotFoundException("Category", "Id", categoryId);
        }
    }
}
