package com.lcwd.electronic.store.ElectronicStore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    private String productId;
    private String productName;
    @Column(length = 10000) // We kept it this large as we will integrate a Rich
    // text editor for product Description so that we can put the full
    // HTML in the rich text editor
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


    // these two fields fare for tracking purposes
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}