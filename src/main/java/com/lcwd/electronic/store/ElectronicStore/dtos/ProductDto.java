package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private String productId;
    private String productName;
    private String productDescription;
    private String productBrandName;
    private Integer productWarrantyPeriod;
    private Integer productRating;
    private Double productPrice;
    private Double productDiscountedPrice;
    private Integer stockQuantity;
    private boolean isProductLive;
    private boolean isProductOutOfStock;
    private String sku; //Stock Keeping Unit for Inventory Management
    private LocalDateTime productAddedDate;
    private String productImage;
}
