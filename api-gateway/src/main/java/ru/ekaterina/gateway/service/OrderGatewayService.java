package ru.ekaterina.gateway.service;

import ru.ekaterina.gateway.dto.CreateOrderRequest;
import ru.ekaterina.gateway.dto.OrderResponse;
import ru.ekaterina.gateway.kafka.producer.Producer;
import ru.ekaterina.gateway.kafka.consumer.Consumer;
import ru.ekaterina.gateway.kafka.message.CreateOrderMessage;
import ru.ekaterina.gateway.kafka.message.CreateOrderResponse;
import ru.ekaterina.gateway.kafka.message.GetOrderResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderGatewayService {

    private final Producer producer;
    private final Consumer consumer;

    public OrderGatewayService(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        String correlationId = UUID.randomUUID().toString();

        CreateOrderMessage message = new CreateOrderMessage(
                correlationId,
                userId,
                request.restaurantId(),
                request.deliveryAddress(),
                request.items()
        );

        CompletableFuture<CreateOrderResponse> future = consumer.waitForCreateOrderResponse(correlationId);
        producer.sendCreateOrderRequest(message);

        try {
            CreateOrderResponse response = future.get();
            if (response == null) {
                throw new RuntimeException("Timeout waiting for order creation response");
            }
            if (response.error != null) {
                throw new RuntimeException(response.error);
            }

            return new OrderResponse(
                    response.orderId,
                    response.userId,
                    response.restaurantId,
                    response.status,
                    response.totalAmount,
                    response.deliveryAddress,
                    response.items
            );
        } catch (Exception e) {
            throw new RuntimeException("Error processing create order", e);
        }
    }

    public OrderResponse getOrderById(Long orderId) {
        String correlationId = UUID.randomUUID().toString();

        CompletableFuture<GetOrderResponse> future = consumer.waitForGetOrderResponse(correlationId);
        producer.sendGetOrderRequest(correlationId, orderId);

        try {
            GetOrderResponse response = future.get();
            if (response == null) {
                throw new RuntimeException("Timeout waiting for order response");
            }
            if (response.error != null) {
                throw new RuntimeException(response.error);
            }

            return new OrderResponse(
                    response.orderId,
                    response.userId,
                    response.restaurantId,
                    response.status,
                    response.totalAmount,
                    response.deliveryAddress,
                    response.items
            );
        } catch (Exception e) {
            throw new RuntimeException("Error fetching order", e);
        }
    }
}
