package com.appsdeveloperblog.estore.ordersservice.query;

import lombok.Value;

@Value
public class FindOrdersQuery {
	private final String orderId;
}
