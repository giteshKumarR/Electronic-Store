package com.lcwd.electronic.store.ElectronicStore.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private String productId;

    @NotBlank(message = "Please provide product name !!")
    @Size(min = 6, max = 26, message = "Invalid Name !!")
    private String productName;
    @NotBlank(message = "Please Provide a description")
    private String productDescription;
    private String productBrandName;
    private Integer productWarrantyPeriod;
    private Integer productRating;
    private Double productPrice;
    private Double productDiscountedPrice;
    private Integer productStockQuantity;
    private boolean isProductLive;
    private boolean isProductOutOfStock;
    private String sku; //Stock Keeping Unit for Inventory Management
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date productAddedDate;
    private String productImage;

    private CategoryDto category;
}
