package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.exceptions.general.ResourseNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.Helper;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;
import com.lcwd.electronic.store.ElectronicStore.repositories.CategoryRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.lcwd.electronic.store.ElectronicStore.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${product.cover.image.path}")
    private String productImageUploadPath;


    @Override
    public ProductDto createProduct(ProductDto productDto) {
        //  generate a unique ID
        String uniqueId = UUID.randomUUID().toString();
        productDto.setProductId(uniqueId);

        // TODO: 12/15/2024 :  generate a unique SKU
//        String sku = Helper.generateSKU(productDto);
//        logger.info("Sku : {}",sku);
//        productDto.setSku(sku);

        Product product = mapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {
        // We will find the category using the category repository
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourseNotFoundException("Category with given Id not found"));
        // map the incoming Product DTO object to Product entity object, to set various fields..
        Product product = mapper.map(productDto, Product.class);
        // Set productId
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setProductAddedDate(new Date());
        product.setCategory(category);

        // Set the SKU
        String sku = Helper.generateSKU(product);
        logger.info("SKU : {}",sku);
        product.setSku(sku);

        // Save the product in DB
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
//        product.setSku(productWithUpdatedDetails.getSku());
        // Say if there are any changes then new SKU can be generated and assigned
        String sku = Helper.generateSKU(product);
        logger.info("SKU : {}",sku);
        product.setSku(sku);

        product.setProductAddedDate(productWithUpdatedDetails.getProductAddedDate());
        product.setProductImage(productWithUpdatedDetails.getProductImage());

        // Save updated product to DB
        Product updatedProduct = productRepository.save(product);
        return mapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateCategoryOfProduct(String productId, String categoryId) {
        //Find the product and category associated to the given IDs
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourseNotFoundException("Product with given Id not found"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourseNotFoundException("Category with given Id not found"));
        // Set the category
        product.setCategory(category);
        // Set new SKU
        product.setSku(Helper.generateSKU(product));
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        // Without image deletion logic
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourseNotFoundException("Product with given Id not found !!"));

        // Delete user image from the images folder
        // 1. We get the full path to delete the image
        String fullPath = productImageUploadPath + product.getProductImage();
        // 2. We need to create a path then pass that path to Delete function in Files to delete the file.

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("Category Cover image not found in Folder");
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    public PagableResponse<ProductDto> getAllProductsOfCategory(String categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourseNotFoundException("Category with given ID not found"));
        Sort sort = (sortDir.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending())
                :(Sort.by(sortBy).ascending());

        Pageable pagable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> pageObject = productRepository.findByCategory(category, pagable);
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
