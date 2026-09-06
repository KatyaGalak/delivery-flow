package ru.ekaterina.kitchendelivery.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("notifyOrderCompletedDelegate")
public class NotifyOrderCompletedDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(NotifyOrderCompletedDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");
        logger.info("ORDER SUCCESS PROCESS COMPLETED: Order ID {} successfully delivered!", orderId);
    }
}