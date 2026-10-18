package com.Reco.backend.controller;

import com.Reco.backend.dto.request.CartItemAddRequest;
import com.Reco.backend.dto.request.CartItemUpdateRequest;
import com.Reco.backend.dto.response.CartResponse;
import com.Reco.backend.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart", description = "Shopping cart contents for the authenticated customer")
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Get the cart of the authenticated customer")
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @Operation(summary = "Add a product to the cart")
    @PostMapping("/items")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CartResponse> addItem(@Valid @RequestBody CartItemAddRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(request));
    }

    @Operation(summary = "Change the quantity of a cart item")
    @PutMapping("/items/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CartResponse> updateItem(@PathVariable Long productId,
                                                   @Valid @RequestBody CartItemUpdateRequest request) {
        return ResponseEntity.ok(cartService.updateItem(productId, request));
    }

    @Operation(summary = "Remove a single product from the cart")
    @DeleteMapping("/items/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> removeItem(@PathVariable Long productId) {
        cartService.removeItem(productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Empty the cart")
    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}
