package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.lcwd.electronic.store.ElectronicStore.payload.ApiResponseMessage;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/category-api/")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    // Create
    @PostMapping("/create-category")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }
    // update
    @PutMapping("/update-category/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDtoWithUpdatedDetails,
                                                      @PathVariable String categoryId){
        return new ResponseEntity<>(categoryService.updateCategory(categoryDtoWithUpdatedDetails, categoryId), HttpStatus.OK);
    }
    // delete
    @DeleteMapping("/delete-category/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);

        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Category Deleted Successfully..")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
    // getAll
    @GetMapping("/get-all-categories")
    public ResponseEntity<PagableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "categoryTitle", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

    ) {
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }
    // getSingle
    @GetMapping("/get-by-id/{categoryId}")
    public ResponseEntity<CategoryDto> getUserById(@PathVariable String categoryId) {
        return new ResponseEntity<>(categoryService.getCategoryById(categoryId), HttpStatus.FOUND);
    }

    // search category by Keyword
    @GetMapping("/search-category/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchCategories(@PathVariable String keyword) {
        return new ResponseEntity<>(categoryService.searchCategory(keyword), HttpStatus.FOUND);
    }
}
