package org.ecommerce.ckecom.repository;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.ecommerce.ckecom.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName(@NotEmpty(message = "Category Name cannot be empty") @Size(min = 5, message = "Category Name must have atleast 5 characters") String categoryName);
}
