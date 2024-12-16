package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;

import java.util.List;

public interface ProductService {
    // Create product
    ProductDto createProduct(ProductDto productDto);

    // Create product with category
    ProductDto createProductWithCategory(ProductDto productDto, String categoryId);

    // Update product
    ProductDto updateProduct(ProductDto productWithUpdatedDetails, String productId);

    // Update Category of a product
    ProductDto updateCategoryOfProduct(String productId, String categoryId);

    // search all products
    PagableResponse<ProductDto> getAllProducts(Integer pageNumber,
                                               Integer pageSize,
                                               String sortBy,
                                               String sortDir);

    // Get all the products of a given Category (category ID)
    PagableResponse<ProductDto> getAllProductsOfCategory(
            String categoryId,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir);

    // search product by ID
    ProductDto getProductByID(String productId);

    // Delete product
    void deleteProduct(String productId);

    // Search products by name
    PagableResponse<ProductDto> searchProductsByName(String productNameFragment,
                                          Integer pageNumber,
                                          Integer pageSize,
                                          String sortBy,
                                          String sortDir);

    // Search products that are active
    PagableResponse<ProductDto> searchActiveProducts(Integer pageNumber,
                                                     Integer pageSize,
                                                     String sortBy,
                                                     String sortDir);

    //Self implemented custom finder method's service functionality
    // Search products by Brand name
    PagableResponse<ProductDto> searchProductsByBrandName(String brandNameFragment,
                                               Integer pageNumber,
                                               Integer pageSize,
                                               String sortBy,
                                               String sortDir);

    // Search products with product rating and brand Name
    PagableResponse<ProductDto> searchProductsByRatingAndBrand(Integer productRating,
                                                               String brandName,
                                                               String ratingMoreLessFlag,
                                                                Integer pageNumber,
                                                                Integer pageSize,
                                                                String sortBy,
                                                                String sortDir);

    // Search product(single) with given Sku ID
    ProductDto getProductBySku(String sku);

    // Search products with given Sku ID fragment
    PagableResponse<ProductDto> searchProductsBySku(String skuFragment,
                                                    Integer pageNumber,
                                                    Integer pageSize,
                                                    String sortBy,
                                                    String sortDir);

    // TODO: 12/13/2024 : Implements other methods after creating mappings for the category of the products

}
