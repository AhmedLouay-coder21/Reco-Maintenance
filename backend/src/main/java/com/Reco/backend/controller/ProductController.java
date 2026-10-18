package com.Reco.backend.controller;

import com.Reco.backend.dto.request.ProductRequest;
import com.Reco.backend.dto.response.ProductResponse;
import com.Reco.backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Product", description = "Catalog browsing and product administration")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Browse products with optional category filter, search and sorting")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> listProducts(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String order
    ) {

        Pageable pageable = PageRequest.of(page, limit,
                Sort.by(Sort.Direction.fromString(order), mapSort(sort)));

        return ResponseEntity.ok(productService.listProducts(categoryId, q, pageable));
    }

    @Operation(summary = "Fetch a single product by id")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @Operation(summary = "Create a new product")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @Operation(summary = "Update an existing product")
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId, @Valid @RequestBody ProductRequest request) {

        return ResponseEntity.ok(productService.updateProduct(request, productId));
    }

    @Operation(summary = "Delete a product and detach it from dependent records")
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long productId) {

        productService.deleteProduct(productId);

        return ResponseEntity.noContent().build();
    }

    private String mapSort(String sort) {
        return switch (sort) {
            case "popularityScore", "popularity_score" -> "popularity_score";
            case "avgRating", "avg_rating", "toprated" -> "avg_rating";
            case "price" -> "price";
            case "createdAt", "created_at" -> "created_at";
            default -> "name";
        };
    }


}
