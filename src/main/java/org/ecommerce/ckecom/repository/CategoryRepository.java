package org.ecommerce.ckecom.repository;

import org.ecommerce.ckecom.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
