package ru.ekaterina.gateway.kafka.consumer;

import ru.ekaterina.gateway.kafka.message.CreateOrderResponse;
import ru.ekaterina.gateway.kafka.message.GetOrderResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final ObjectMapper objectMapper;

    private final ConcurrentHashMap<String, CompletableFuture<GetOrderResponse>> getOrderResponses = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CompletableFuture<CreateOrderResponse>> createOrderResponses = new ConcurrentHashMap<>();

    public Consumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "get-order-responses", groupId = "api-gateway-group")
    public void consumeGetOrderResponse(String message) {
        try {
            GetOrderResponse response = objectMapper.readValue(message, GetOrderResponse.class);

            logger.info("Received GetOrderResponse with correlation id: {}", response.correlationId);

            CompletableFuture<GetOrderResponse> future = getOrderResponses.remove(response.correlationId);
            if (future != null) {
                future.complete(response);
            }

        } catch (Exception e) {
            logger.error("Error processing GetOrderResponse", e);
        }
    }

    @KafkaListener(topics = "create-order-responses", groupId = "api-gateway-group")
    public void consumeCreateOrderResponse(String message) {
        try {
            CreateOrderResponse response = objectMapper.readValue(message, CreateOrderResponse.class);

            logger.info("Received CreateOrderResponse with correlation id: {}", response.correlationId);

            CompletableFuture<CreateOrderResponse> future = createOrderResponses.remove(response.correlationId);
            if (future != null) {
                future.complete(response);
            }

        } catch (Exception e) {
            logger.error("Error processing CreateOrderResponse", e);
        }
    }

    public CompletableFuture<GetOrderResponse> waitForGetOrderResponse(String correlationId) {

        CompletableFuture<GetOrderResponse> future = new CompletableFuture<>();
        getOrderResponses.put(correlationId, future);
        future.completeOnTimeout(null, 5, TimeUnit.SECONDS);

        return future;
    }

    public CompletableFuture<CreateOrderResponse> waitForCreateOrderResponse(String correlationId) {

        CompletableFuture<CreateOrderResponse> future = new CompletableFuture<>();
        createOrderResponses.put(correlationId, future);
        future.completeOnTimeout(null, 5, TimeUnit.SECONDS);

        return future;
    }
}
