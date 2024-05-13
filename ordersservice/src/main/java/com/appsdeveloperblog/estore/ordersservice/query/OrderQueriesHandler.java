package com.appsdeveloperblog.estore.ordersservice.query;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.estore.ordersservice.data.OrderEntity;
import com.appsdeveloperblog.estore.ordersservice.data.OrdersRepository;
import com.appsdeveloperblog.estore.ordersservice.model.OrderSummary;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderQueriesHandler {
	private final OrdersRepository ordersRepository;
	
	@QueryHandler
	public OrderSummary findOrder(FindOrderQuery query) {
		OrderEntity orderEntity = ordersRepository.findByOrderId(query.getOrderId());
		return new OrderSummary(orderEntity.getOrderId(), orderEntity.getOrderStatus(),"");
	}
}
