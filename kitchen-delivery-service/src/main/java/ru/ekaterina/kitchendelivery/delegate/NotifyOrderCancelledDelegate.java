package ru.ekaterina.kitchendelivery.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.ekaterina.kitchendelivery.kafka.producer.ProcessCommandProducer;

@Component("notifyOrderCancelledDelegate")
public class NotifyOrderCancelledDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(NotifyOrderCancelledDelegate.class);

    private final ProcessCommandProducer commandProducer;

    public NotifyOrderCancelledDelegate(ProcessCommandProducer commandProducer) {
        this.commandProducer = commandProducer;
    }

    @Override
    public void execute(DelegateExecution execution) {
        Long orderId = (Long) execution.getVariable("orderId");
        String reason = (String) execution.getVariable("cancelReason");
        String stage = (String) execution.getVariable("cancelStage");

        logger.warn("ORDER CANCELLED: orderId={}, stage={}, reason={}", orderId, stage, reason);

        commandProducer.sendOrderStatusChanged(orderId, "CANCELLED", reason);
    }
}