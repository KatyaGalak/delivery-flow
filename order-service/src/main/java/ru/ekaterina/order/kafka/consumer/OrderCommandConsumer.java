package ru.ekaterina.order.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ekaterina.order.dto.CreateOrderCommand;
import ru.ekaterina.order.dto.OrderItemDto;
import ru.ekaterina.order.dto.GetOrderCommand;
import ru.ekaterina.order.kafka.message.OrderResponse;
import ru.ekaterina.order.kafka.producer.OrderEventProducer;
import ru.ekaterina.order.kafka.producer.OrderResponseProducer;
import ru.ekaterina.order.model.Order;
import ru.ekaterina.order.service.OrderService;

import java.util.List;

@Service
public class OrderCommandConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OrderCommandConsumer.class);

    private final OrderService orderService;
    private final OrderEventProducer eventProducer;
    private final OrderResponseProducer responseProducer;
    private final ObjectMapper objectMapper;

    public OrderCommandConsumer(OrderService orderService,
                                OrderEventProducer eventProducer,
                                OrderResponseProducer responseProducer,
                                ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.eventProducer = eventProducer;
        this.responseProducer = responseProducer;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "create-order-requests", groupId = "order-service-group")
    public void consumeCreateOrderRequest(String message) {
        try {
            CreateOrderCommand command = objectMapper.readValue(message, CreateOrderCommand.class);
            logger.info("Received create-order command with correlationId: {}", command.correlationId);

            Order order = orderService.createOrder(command);
            eventProducer.sendOrderCreatedEvent(order);

            List<OrderItemDto> itemDtos = order.getItems().stream()
                    .map(i -> new OrderItemDto(i.getMenuItemId(), i.getName(), i.getQuantity(), i.getPrice()))
                    .toList();

            OrderResponse response = OrderResponse.builder()
                    .correlationId(command.correlationId)
                    .orderId(order.getId())
                    .userId(order.getUserId())
                    .restaurantId(order.getRestaurantId())
                    .status(order.getStatus().name())
                    .totalAmount(order.getTotalAmount())
                    .deliveryAddress(order.getDeliveryAddress())
                    .items(itemDtos)
                    .build();

            responseProducer.sendCreateOrderResponse(command.correlationId, response);
        } catch (Exception e) {
            logger.error("Error processing create order command", e);
        }
    }

    @KafkaListener(topics = "get-order-requests", groupId = "order-service-group")
    public void consumeGetOrderRequest(String message) {
        String correlationId = null;
        try {
            GetOrderCommand command = objectMapper.readValue(message, GetOrderCommand.class);
            correlationId = command.correlationId;
            logger.info("Received get-order command with correlationId: {}, orderId: {}",
                    command.correlationId, command.orderId);

            Order order = orderService.getOrderById(command.orderId);

            responseProducer.sendGetOrderResponse(
                    command.correlationId,
                    toOrderResponse(command.correlationId, order)
            );
        } catch (Exception e) {
            logger.error("Error processing get order command", e);
            if (correlationId != null) {
                OrderResponse errorResponse = OrderResponse.builder()
                        .correlationId(correlationId)
                        .error(e.getMessage() != null ? e.getMessage() : "Order not found")
                        .build();
                responseProducer.sendGetOrderResponse(correlationId, errorResponse);
            }
        }
    }

    private OrderResponse toOrderResponse(String correlationId, Order order) {
        List<OrderItemDto> itemDto = order.getItems().stream()
                .map(i -> new OrderItemDto(i.getMenuItemId(), i.getName(), i.getQuantity(), i.getPrice()))
                .toList();

        return OrderResponse.builder()
                .correlationId(correlationId)
                .orderId(order.getId())
                .userId(order.getUserId())
                .restaurantId(order.getRestaurantId())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .deliveryAddress(order.getDeliveryAddress())
                .items(itemDto)
                .build();
    }
}