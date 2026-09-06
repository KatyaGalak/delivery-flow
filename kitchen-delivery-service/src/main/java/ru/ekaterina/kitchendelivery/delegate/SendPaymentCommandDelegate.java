package ru.ekaterina.kitchendelivery.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.ekaterina.kitchendelivery.kafka.producer.ProcessCommandProducer;

@Component("sendPaymentCommandDelegate")
public class SendPaymentCommandDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(SendPaymentCommandDelegate.class);

    private final ProcessCommandProducer commandProducer;

    public SendPaymentCommandDelegate(ProcessCommandProducer commandProducer) {
        this.commandProducer = commandProducer;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");
        Long userId = (Long) execution.getVariable("userId");
        Double totalAmount = (Double) execution.getVariable("totalAmount");
        String businessKey = execution.getBusinessKey();

        logger.info("Executing SendPaymentCommandDelegate for orderId: {}", orderId);
        commandProducer.sendProcessPaymentCommand(businessKey, orderId, userId, totalAmount);
    }
}