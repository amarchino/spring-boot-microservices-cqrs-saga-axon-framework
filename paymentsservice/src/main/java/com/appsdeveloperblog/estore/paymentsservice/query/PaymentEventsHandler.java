package com.appsdeveloperblog.estore.paymentsservice.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.estore.core.events.PaymentProcessedEvent;
import com.appsdeveloperblog.estore.paymentsservice.data.PaymentEntity;
import com.appsdeveloperblog.estore.paymentsservice.data.PaymentsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@ProcessingGroup("payment-group")
@Slf4j
public class PaymentEventsHandler {
	private final PaymentsRepository paymentsRepository;
	
	@EventHandler
	public void on(PaymentProcessedEvent paymentProcessedEvent) {
		log.info("PaymentProcessedEvent is called for orderId: {}", paymentProcessedEvent.getOrderId());
		PaymentEntity paymentEntity = new PaymentEntity();
		BeanUtils.copyProperties(paymentProcessedEvent, paymentEntity);
		paymentsRepository.save(paymentEntity);
	}
}
