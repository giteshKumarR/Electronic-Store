package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;

import java.util.List;

public interface CategoryService {
    // Create Category
    CategoryDto createCategory(CategoryDto categoryDto);

    // Update Category
    CategoryDto updateCategory(CategoryDto categoryDtoWithUpdatedDetails, String categoryId);

    // Delete Category
    void deleteCategory(String categoryId);

    // Get All Categories
    PagableResponse<CategoryDto> getAllCategories(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    );

    // Get single category by Id
    CategoryDto getCategoryById(String categoryId);

    // Search (Implement If needed)
    List<CategoryDto> searchCategory(String keyword);
}
