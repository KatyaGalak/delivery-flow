package ru.ekaterina.kitchendelivery.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("notifyOrderFailedDelegate")
public class NotifyOrderFailedDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(NotifyOrderFailedDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");
        String reason = (String) execution.getVariable("failReason");
        
        logger.warn("ORDER FAILED PROCESS COMPLETED: Order ID {} failed. Reason: {}", orderId, reason);
    }
}