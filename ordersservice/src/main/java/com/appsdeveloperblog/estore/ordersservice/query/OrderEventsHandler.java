package com.appsdeveloperblog.estore.ordersservice.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.estore.ordersservice.data.OrderEntity;
import com.appsdeveloperblog.estore.ordersservice.data.OrdersRepository;
import com.appsdeveloperblog.estore.ordersservice.events.OrderApprovedEvent;
import com.appsdeveloperblog.estore.ordersservice.events.OrderCreatedEvent;
import com.appsdeveloperblog.estore.ordersservice.events.OrderRejectedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@ProcessingGroup("order-group")
public class OrderEventsHandler {
	private final OrdersRepository ordersRepository;

	@EventHandler
	public void on(OrderCreatedEvent orderCreatedEvent) {
		OrderEntity orderEntity = new OrderEntity();
		BeanUtils.copyProperties(orderCreatedEvent, orderEntity);
		ordersRepository.save(orderEntity);
	}

	@EventHandler
	public void on(OrderApprovedEvent orderApprovedEvent) {
		OrderEntity orderEntity = ordersRepository.findByOrderId(orderApprovedEvent.getOrderId());
		if(orderEntity == null) {
			// TOOD: do something about it
			return;
		}
		orderEntity.setOrderStatus(orderApprovedEvent.getOrderStatus());
		ordersRepository.save(orderEntity);
	}
	
	@EventHandler
	public void on(OrderRejectedEvent orderRejectedEvent) {
		OrderEntity orderEntity = ordersRepository.findByOrderId(orderRejectedEvent.getOrderId());
		orderEntity.setOrderStatus(orderRejectedEvent.getOrderStatus());
		ordersRepository.save(orderEntity);
	}

	@ExceptionHandler(resultType = IllegalArgumentException.class)
	public void handle(IllegalArgumentException exception) {
		// Log error message
	}
	@ExceptionHandler(resultType = Exception.class)
	public void handle(Exception exception) throws Exception {
		throw exception;
	}
}
