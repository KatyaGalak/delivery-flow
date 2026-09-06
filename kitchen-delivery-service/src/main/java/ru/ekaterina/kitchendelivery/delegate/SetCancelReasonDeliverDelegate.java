package ru.ekaterina.kitchendelivery.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("setCancelReasonDeliverDelegate")
public class SetCancelReasonDeliverDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(SetCancelReasonDeliverDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        Long orderId = (Long) execution.getVariable("orderId");
        String reason = "Delivery timeout: courier did not complete Deliver Food in time";
        execution.setVariable("cancelReason", reason);
        execution.setVariable("cancelStage", "DELIVER");
        logger.warn("Deliver timeout for orderId={}: {}", orderId, reason);
    }
}
