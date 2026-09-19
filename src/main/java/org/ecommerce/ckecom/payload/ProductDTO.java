package org.ecommerce.ckecom.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    @NotEmpty
    private String productName;
    private String image;
    @NotEmpty
    private String description;
    @NotNull
    private Integer quantity;
    @NotNull
    private Double price;
    @NotNull
    private Double discount;
}
