package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.payload.ApiResponseMessage;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/product-api/")
public class ProductController {
    private Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    // Create
    @PostMapping("/create-product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }

    // update
    @PutMapping("/update-product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto updatedProductDto,@PathVariable String productId) {
        return new ResponseEntity<>(productService.updateProduct(updatedProductDto, productId), HttpStatus.OK);
    }

    // get all products
    @GetMapping("/get-all-products")
    public ResponseEntity<PagableResponse<ProductDto>> getAllProducts(
        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
        @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
        @RequestParam(value = "sortBy", defaultValue = "productName", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }
    // get single by id
    @GetMapping("/get-by-id/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId) {
        return new ResponseEntity<>(productService.getProductByID(productId), HttpStatus.FOUND);
    }
    // delete
    @DeleteMapping("/delete-product/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);

        // Create the response Message
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Product Deleted Successfully")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    // Searching Products by Product Name
    @GetMapping("/search-products-by-name/{fragment}")
    public ResponseEntity<PagableResponse<ProductDto>> searchProductsByName(
            @PathVariable String fragment,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "productName", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return new ResponseEntity<>(productService.searchProductsByName(fragment, pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    // Searching Products that are active
    @GetMapping("/search-active-products")
    public ResponseEntity<PagableResponse<ProductDto>> searchActiveProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "productName", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return new ResponseEntity<>(productService.searchActiveProducts(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    // Searching Products by Brand Name
    @GetMapping("/search-products-by-brand/{fragment}")
    public ResponseEntity<PagableResponse<ProductDto>> searchProductsByBrand(
            @PathVariable String fragment,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "productName", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return new ResponseEntity<>(productService.searchProductsByBrandName(fragment, pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    // Searching Products by rating and brand name
    // There are different variations for this on the basis of rating more/less flag
    @GetMapping("/search-products-by-rating-brand/{rating}/{brandFragment}")
    public ResponseEntity<PagableResponse<ProductDto>> searchProductsByRatingAndBrand(
            @PathVariable Integer rating,
            @PathVariable String brandFragment,
            @RequestParam(value = "ratingMoreLessFlag", required = false) String ratingMoreLessFlag,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "productName", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return new ResponseEntity<>(productService.searchProductsByRatingAndBrand(
                rating, brandFragment, ratingMoreLessFlag,
                pageNumber, pageSize, sortBy, sortDir
        ), HttpStatus.OK);
    }

    // Search single product by particular SKU code
    @GetMapping("/search-by-sku/{sku}")
    public ResponseEntity<ProductDto> searchProductBySku(@PathVariable String sku) {
        return new ResponseEntity<>(productService.getProductBySku(sku), HttpStatus.FOUND);
    }

    // Search products by given SKU code fragment
    @GetMapping("/search-products-by-sku/{fragment}")
    public ResponseEntity<PagableResponse<ProductDto>> searchProductsBySkuFragment(
            @PathVariable String fragment,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "productName", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return new ResponseEntity<>(productService.searchProductsBySku(
                fragment,
                pageNumber, pageSize, sortBy, sortDir
        ), HttpStatus.OK);
    }
}
