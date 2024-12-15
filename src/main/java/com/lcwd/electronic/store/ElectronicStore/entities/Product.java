package com.lcwd.electronic.store.ElectronicStore.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

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
    private Integer productStockQuantity;
    private boolean isProductLive;
    private boolean isProductOutOfStock;
    private String sku; //Stock Keeping Unit for Inventory Management
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date productAddedDate;
    private String productImage;


    // these two fields fare for tracking purposes
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
    public void onPrePersist() {
        // This will run only when the entity will be first created
        this.createdOn = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedOn = LocalDateTime.now();
    }
}
