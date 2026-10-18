package com.Reco.backend.controller;

import com.Reco.backend.dto.request.OrderStatusUpdateRequest;
import com.Reco.backend.dto.request.PaymentProcessRequest;
import com.Reco.backend.dto.response.OrderItemResponse;
import com.Reco.backend.dto.response.OrderResponse;
import com.Reco.backend.dto.response.PaymentResponse;
import com.Reco.backend.model.OrderStatus;
import com.Reco.backend.service.OrderService;
import com.Reco.backend.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Order", description = "Order lifecycle, order items and order scoped payment")
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;


    @Operation(summary = "Place an order from the current cart")
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponse> createOrder() {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder());
    }

    @Operation(summary = "List the authenticated customer's orders, optionally filtered by status")
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@RequestParam(required = false) OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByUser(status));
    }

    @Operation(summary = "Fetch a single order by id")
    @GetMapping("/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @Operation(summary = "List the line items of an order")
    @GetMapping("/{orderId}/items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderItemResponse>> getOrderItems(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderItems(orderId));
    }

    @Operation(summary = "Pay for an order through the mock gateway")
    @PostMapping("/{orderId}/payment")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable Long orderId,
                                                          @Valid @RequestBody PaymentProcessRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.processPayment(orderId, request));
    }

    @Operation(summary = "Get the payment recorded for an order")
    @GetMapping("/{orderId}/payment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @Operation(summary = "List every order across all customers")
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    
    @Operation(summary = "Update the status of an order")
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long orderId,
                                                           @Valid @RequestBody OrderStatusUpdateRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request.getStatus()));
    }

    @Operation(summary = "Delete an order")
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
