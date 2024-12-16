package com.lcwd.electronic.store.ElectronicStore.repositories;

import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    // search product by product name
    // SINGLE (Not needed)
    // Product findByProductName(String productName);

    // MULTIPLE (Note: Also implemented in the course lecture)
    Page<Product> findByProductNameContaining(String productNameFragment, Pageable pageable);
    // Search products that are active/live
    Page<Product> findByIsProductLiveTrue(Pageable pageable);


    // OTHER FINDER METHODS

    // search product by brand name
    // SINGLE(Not Needed)
    // Product findByProductBrandName(String brandName);

    // MULTIPLE
    Page<Product> findByProductBrandNameContaining(String brandNameFragment, Pageable pageable);

    // search by product rating and Brand Name
    Page<Product> findByProductRatingAndProductBrandNameContaining(
            Integer productRating,
            String brandNameFragment,
            Pageable pageable);

    // Search products with ratings greater than or equal and brand name containing suffix
    Page<Product> findByProductRatingGreaterThanEqualAndProductBrandNameContaining(
            Integer productRating,
            String brandNameFragment,
            Pageable pageable
    );

    // Search products with ratings less than or equal and brand name containing suffix
    Page<Product> findByProductRatingLessThanEqualAndProductBrandNameContaining(
            Integer productRating,
            String brandNameFragment,
            Pageable pageable
    );

    // search by SKU
    // SINGLE
    Optional<Product> findBySku(String sku);

    // MULTIPLE
    Page<Product> findBySkuContainingIgnoreCase(String skuFragment, Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);

    // search all Products that are active/Live products under a brand containing under a category containing

    // search all Products that are Inactive products under a brand containing under a category containing



    // search all products that are Out of stock under a brand containing under a category containing

    // search all products that are In stock under a brand containing under a category containing
}
