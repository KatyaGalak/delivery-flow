package ru.ekaterina.kitchendelivery.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("setCancelReasonPrepareDelegate")
public class SetCancelReasonPrepareDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(SetCancelReasonPrepareDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        Long orderId = (Long) execution.getVariable("orderId");
        String reason = "Preparation timeout: restaurant did not complete Prepare Food in time";
        execution.setVariable("cancelReason", reason);
        execution.setVariable("cancelStage", "PREPARE");
        logger.warn("Prepare timeout for orderId={}: {}", orderId, reason);
    }
}

