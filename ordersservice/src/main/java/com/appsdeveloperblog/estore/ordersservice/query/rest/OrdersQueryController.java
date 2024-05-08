package com.appsdeveloperblog.estore.ordersservice.query.rest;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.estore.ordersservice.query.FindOrdersQuery;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersQueryController {
	private final QueryGateway queryGateway;

	@GetMapping
	public List<OrderRestModel> getProducts() {
		FindOrdersQuery findOrdersQuery = new FindOrdersQuery();
		List<OrderRestModel> orders = queryGateway.query(findOrdersQuery, ResponseTypes.multipleInstancesOf(OrderRestModel.class)).join();
		return orders;
	}
}
