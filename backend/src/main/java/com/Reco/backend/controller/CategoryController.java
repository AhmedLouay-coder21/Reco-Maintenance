package com.Reco.backend.controller;

import com.Reco.backend.dto.request.CategoryRequest;
import com.Reco.backend.dto.request.CategoryUpdateRequest;
import com.Reco.backend.dto.response.CategoryResponse;
import com.Reco.backend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category", description = "Product categories such as Ritual, Form and Signal")
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @Operation(summary = "List every category")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listCategories(){
        return ResponseEntity.ok(categoryService.listCategories());
    }

    @Operation(summary = "Fetch a single category by id")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long categoryId){
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @Operation(summary = "Create a new category")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest category){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(category));
    }

    @Operation(summary = "Update an existing category")
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @RequestBody CategoryUpdateRequest category, @PathVariable Long categoryId){
        return ResponseEntity.ok(categoryService.updateCategory(categoryId,category));
    }

    @Operation(summary = "Delete a category, reassigning its products to Uncategorized")
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId){

         categoryService.deleteCategory(categoryId);

         return ResponseEntity.noContent().build();
    }
}
