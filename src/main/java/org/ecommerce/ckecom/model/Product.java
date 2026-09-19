package org.ecommerce.ckecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String productName;
    private String image;
    private String description;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;

    @ManyToOne
    private Category category;

    public void updateProduct(Product newProduct) {
        this.setProductName(newProduct.getProductName());
        this.setDescription(newProduct.getDescription());
        this.setQuantity(newProduct.getQuantity());
        this.setPrice(newProduct.getPrice());
        this.setDiscount(newProduct.getDiscount());
        this.setSpecialPrice(this.getPrice() - this.getPrice() * this.getDiscount() * 0.01);
    }
}
