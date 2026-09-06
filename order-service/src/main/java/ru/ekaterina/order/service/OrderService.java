package ru.ekaterina.order.service;

import ru.ekaterina.order.dto.CreateOrderCommand;
import ru.ekaterina.order.dto.OrderItemDto;
import ru.ekaterina.order.model.Order;
import ru.ekaterina.order.model.OrderItem;
import ru.ekaterina.order.model.OrderStatus;
import ru.ekaterina.order.repository.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(CreateOrderCommand command) {
        double total = command.items.stream()
                .mapToDouble(i -> i.price() * i.quantity())
                .sum();

        Order order = Order.builder()
                .userId(command.userId)
                .restaurantId(command.restaurantId)
                .deliveryAddress(command.deliveryAddress)
                .totalAmount(total)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        for (OrderItemDto itemDto : command.items) {
            OrderItem item = OrderItem.builder()
                    .menuItemId(itemDto.menuItemId())
                    .name(itemDto.name())
                    .quantity(itemDto.quantity())
                    .price(itemDto.price())
                    .build();
            order.addItem(item);
        }

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {

        return orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    }

    @Transactional
    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        order.setStatus(newStatus);

        return orderRepository.save(order);
    }
}