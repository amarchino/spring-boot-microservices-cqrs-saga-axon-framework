package com.appsdeveloperblog.estore.ordersservice.events;

import com.appsdeveloperblog.estore.ordersservice.model.OrderStatus;

import lombok.Data;

@Data
public class OrderRejectedEvent {
	private final String orderId;
	private final String reason;
	private final OrderStatus orderStatus = OrderStatus.REJECTED;
}
