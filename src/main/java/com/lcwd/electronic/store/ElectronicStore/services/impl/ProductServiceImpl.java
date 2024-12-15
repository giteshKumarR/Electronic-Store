package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourseNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.Helper;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.lcwd.electronic.store.ElectronicStore.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;


    @Override
    public ProductDto createProduct(ProductDto productDto) {
        //  generate a unique ID
        String uniqueId = UUID.randomUUID().toString();
        productDto.setProductId(uniqueId);

        // TODO: 12/15/2024 :  generate a unique SKU
        

        Product product = mapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productWithUpdatedDetails, String productId) {
        // Check if the product present or not..
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourseNotFoundException("Product with given ID not found"));

        // Set the new values
        product.setProductName(productWithUpdatedDetails.getProductName());
        product.setProductDescription(productWithUpdatedDetails.getProductDescription());
        product.setProductBrandName(productWithUpdatedDetails.getProductBrandName());
        product.setProductWarrantyPeriod(productWithUpdatedDetails.getProductWarrantyPeriod());
        product.setProductRating(productWithUpdatedDetails.getProductRating());
        product.setProductPrice(productWithUpdatedDetails.getProductPrice());
        product.setProductDiscountedPrice(productWithUpdatedDetails.getProductDiscountedPrice());
        product.setProductStockQuantity(productWithUpdatedDetails.getProductStockQuantity());
        product.setProductLive(productWithUpdatedDetails.isProductLive());
        product.setProductOutOfStock(productWithUpdatedDetails.isProductOutOfStock());
        product.setSku(productWithUpdatedDetails.getSku());
        product.setProductAddedDate(productWithUpdatedDetails.getProductAddedDate());
        product.setProductImage(productWithUpdatedDetails.getProductImage());

        // Save updated product to DB
        Product updatedProduct = productRepository.save(product);
        return mapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        // Without image deletion logic
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourseNotFoundException("Product with given Id not found !!"));
        productRepository.delete(product);
    }

    @Override
    public PagableResponse<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending())
                :(Sort.by(sortBy).ascending());
        Pageable pagable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> pageObject = productRepository.findAll(pagable);
        PagableResponse<ProductDto> pagableResponse = Helper.getPagableResponse(pageObject, ProductDto.class);

        return pagableResponse;
    }

    @Override
    public ProductDto getProductByID(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourseNotFoundException("Product with given productId not found"));
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PagableResponse<ProductDto> searchProductsByName(String productNameFragment, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending())
                :(Sort.by(sortBy).ascending());
        Pageable pagable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> pageObject = productRepository.findByProductNameContaining(productNameFragment, pagable);
        PagableResponse<ProductDto> pagableResponse = Helper.getPagableResponse(pageObject, ProductDto.class);

        return pagableResponse;
    }

    @Override
    public PagableResponse<ProductDto> searchActiveProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending())
                :(Sort.by(sortBy).ascending());
        Pageable pagable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> pageObject = productRepository.findByIsProductLiveTrue(pagable);
        PagableResponse<ProductDto> pagableResponse = Helper.getPagableResponse(pageObject, ProductDto.class);

        return pagableResponse;
    }

    @Override
    public PagableResponse<ProductDto> searchProductsByBrandName(String brandNameFragment, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending())
                :(Sort.by(sortBy).ascending());
        Pageable pagable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> pageObject = productRepository.findByProductBrandNameContaining(brandNameFragment, pagable);
        PagableResponse<ProductDto> pagableResponse = Helper.getPagableResponse(pageObject, ProductDto.class);

        return pagableResponse;
    }

    @Override
    public PagableResponse<ProductDto> searchProductsByRatingAndBrand(Integer productRating, String brandName,String ratingMoreLessFlag, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending())
                :(Sort.by(sortBy).ascending());
        Pageable pagable = PageRequest.of(pageNumber, pageSize, sort);
        // Calling different search filters on the basis of the more or less flag
        // 1. value = more then repo method called is "searchEqualGreaterRatingProductsByBrand"
        // 2. value = less then repo method called is "searchSmallerEqualRatingProductsByBrand"
        // 3. if the string is empty then repo method called is "findByProductRatingAndProductBrandName"

        PagableResponse<ProductDto> pagableResponse = new PagableResponse<>();
        System.out.println("rating flg value : {}" + ratingMoreLessFlag);
        if(ratingMoreLessFlag.equalsIgnoreCase("more")) {
            logger.info("More than rating called");
            Page<Product> pageObject = productRepository.findByProductRatingGreaterThanEqualAndProductBrandNameContaining(productRating,brandName, pagable);
            pagableResponse = Helper.getPagableResponse(pageObject, ProductDto.class);
        } else if(ratingMoreLessFlag.equalsIgnoreCase("less")) {
            logger.info("Less than rating called");
            Page<Product> pageObject = productRepository.findByProductRatingLessThanEqualAndProductBrandNameContaining(productRating,brandName, pagable);
            pagableResponse = Helper.getPagableResponse(pageObject, ProductDto.class);
        } else if(ratingMoreLessFlag.isEmpty()) {
            logger.info("Equal rating called");
            Page<Product> pageObject = productRepository.findByProductRatingAndProductBrandNameContaining(productRating,brandName, pagable);
            pagableResponse = Helper.getPagableResponse(pageObject, ProductDto.class);
        }

        return pagableResponse;
    }

    @Override
    public ProductDto getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku).orElseThrow(() -> new ResourseNotFoundException("Product with given SKU not found !!"));
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PagableResponse<ProductDto> searchProductsBySku(String skuFragment, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending())
                :(Sort.by(sortBy).ascending());
        Pageable pagable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> pageObject = productRepository.findBySkuContainingIgnoreCase(skuFragment, pagable);
        PagableResponse<ProductDto> pagableResponse = Helper.getPagableResponse(pageObject, ProductDto.class);

        return pagableResponse;
    }
}
