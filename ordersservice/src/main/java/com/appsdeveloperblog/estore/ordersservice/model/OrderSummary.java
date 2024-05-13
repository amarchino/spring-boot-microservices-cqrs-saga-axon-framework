package com.appsdeveloperblog.estore.ordersservice.model;

import lombok.Value;

@Value
public class OrderSummary {
	private final String orderId;
	private final OrderStatus orderStatus;
}
