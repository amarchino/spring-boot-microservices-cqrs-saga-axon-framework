package com.appsdeveloperblog.estore.ordersservice.command.rest;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.estore.ordersservice.command.commands.CreateOrderCommand;
import com.appsdeveloperblog.estore.ordersservice.model.OrderStatus;
import com.appsdeveloperblog.estore.ordersservice.model.OrderSummary;
import com.appsdeveloperblog.estore.ordersservice.query.FindOrderQuery;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/orders")
@RestController
@RequiredArgsConstructor
public class OrdersCommandController {
	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;

	@PostMapping
	public OrderSummary createOrder(@RequestBody @Valid CreateOrderRestModel createOrderRestModel) {
		String orderId = UUID.randomUUID().toString();
		CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
			.orderId(orderId)
			.userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
			.productId(createOrderRestModel.getProductId())
			.quantity(createOrderRestModel.getQuantity())
			.addressId(createOrderRestModel.getAddressId())
			.orderStatus(OrderStatus.CREATED)
			.build();
		
		try(SubscriptionQueryResult<OrderSummary, OrderSummary> queryResult = queryGateway.subscriptionQuery(
				new FindOrderQuery(orderId),
				ResponseTypes.instanceOf(OrderSummary.class),
				ResponseTypes.instanceOf(OrderSummary.class))) {
			commandGateway.sendAndWait(createOrderCommand);
			return queryResult.updates().blockFirst();
		}
	}
}
