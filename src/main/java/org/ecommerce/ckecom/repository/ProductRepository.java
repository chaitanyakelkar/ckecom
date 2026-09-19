package org.ecommerce.ckecom.repository;

import org.ecommerce.ckecom.model.Category;
import org.ecommerce.ckecom.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByCategory(Category category,
                                    Pageable pageable);

    Page<Product> findAllByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String productName, String description, Pageable pageable);

    Optional<Product> findByProductName(String productName);
}
