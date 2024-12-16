package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourseNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.Helper;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;
import com.lcwd.electronic.store.ElectronicStore.repositories.CategoryRepository;
import com.lcwd.electronic.store.ElectronicStore.services.CategoryService;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ProductService productService;

    @Value("${category.cover.image.path}")
    private String categoryCoverImagesUploadPath;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        // Set a unique string category Id
        String uniqueCategoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(uniqueCategoryId);

        // This means that map the categoryDTO object to Category entity class
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        // Convert savedCategory entity object to Category Dto
        return mapper.map(savedCategory, CategoryDto.class);
    }




    @Override
    public CategoryDto updateCategory(CategoryDto categoryDtoWithUpdatedDetails, String categoryId) {
        // See if the category present or not for given ID
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourseNotFoundException("Category Not found !!"));

        // Update the category details
        category.setCategoryTitle(categoryDtoWithUpdatedDetails.getCategoryTitle());
        category.setCategoryDescription(categoryDtoWithUpdatedDetails.getCategoryDescription());
        category.setCategoryCoverImage(categoryDtoWithUpdatedDetails.getCategoryCoverImage());

        Category updatedCategory = categoryRepository.save(category);

        return mapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        // See if the category present or not for given ID
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourseNotFoundException("Category Not found !!"));

        // Delete user image from the images folder
        // 1. We get the full path to delete the image
        String fullPath = categoryCoverImagesUploadPath + category.getCategoryCoverImage();
        // 2. We need to create a path then pass that path to Delete function in Files to delete the file.

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("Category Cover image not found in Folder");
        } catch (IOException e) {
            e.printStackTrace();
        }

        categoryRepository.delete(category);
    }

    @Override
    public PagableResponse<CategoryDto> getAllCategories(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    ) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))
                ? (Sort.by(sortBy).descending())
                :(Sort.by(sortBy).ascending());
        Pageable pagable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> pageObject = categoryRepository.findAll(pagable);
        PagableResponse<CategoryDto> pagableResponse = Helper.getPagableResponse(pageObject, CategoryDto.class);

        return pagableResponse;
    }

    @Override
    public CategoryDto getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourseNotFoundException("Category with given categoryId Not found !!"));
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        List<Category> byCategoryNameContaining = categoryRepository.findByCategoryTitleContaining(keyword);
        return byCategoryNameContaining.stream()
                .map(category -> {
                    return new ModelMapper().map(category, CategoryDto.class);
                }).collect(Collectors.toList());
    }
}
