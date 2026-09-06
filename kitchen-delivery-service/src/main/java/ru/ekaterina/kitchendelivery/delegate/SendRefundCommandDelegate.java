package ru.ekaterina.kitchendelivery.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.ekaterina.kitchendelivery.kafka.producer.ProcessCommandProducer;

@Component("sendRefundCommandDelegate")
public class SendRefundCommandDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(SendRefundCommandDelegate.class);

    private final ProcessCommandProducer commandProducer;

    public SendRefundCommandDelegate(ProcessCommandProducer commandProducer) {
        this.commandProducer = commandProducer;
    }

    @Override
    public void execute(DelegateExecution execution) {
        Long orderId = (Long) execution.getVariable("orderId");
        Long userId = (Long) execution.getVariable("userId");
        Double totalAmount = (Double) execution.getVariable("totalAmount");
        String reason = (String) execution.getVariable("cancelReason");
        String businessKey = execution.getBusinessKey();

        logger.info("Requesting refund for orderId={}, amount={}, reason={}", orderId, totalAmount, reason);
        commandProducer.sendRefundCommand(businessKey, orderId, userId, totalAmount, reason);
    }
}
