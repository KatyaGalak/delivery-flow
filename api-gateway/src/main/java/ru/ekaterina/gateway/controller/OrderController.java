package ru.ekaterina.gateway.controller;

import ru.ekaterina.gateway.dto.CreateOrderRequest;
import ru.ekaterina.gateway.dto.OrderResponse;
import ru.ekaterina.gateway.dto.UserDTO;
import ru.ekaterina.gateway.service.AuthService;
import ru.ekaterina.gateway.service.OrderGatewayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderGatewayService orderGatewayService;
    private final AuthService authService;

    public OrderController(OrderGatewayService orderGatewayService, AuthService authService) {
        this.orderGatewayService = orderGatewayService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UserDTO currentUser = authService.findByUsername(username);

            OrderResponse response = orderGatewayService.createOrder(currentUser.id(), request);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable @Positive(message = "id must be > 0") Long id) {
        try {
            OrderResponse response = orderGatewayService.getOrderById(id);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
