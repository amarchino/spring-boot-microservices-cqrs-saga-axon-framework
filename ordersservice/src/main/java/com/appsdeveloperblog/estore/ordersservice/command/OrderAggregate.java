package com.appsdeveloperblog.estore.ordersservice.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.appsdeveloperblog.estore.ordersservice.command.commands.ApproveOrderCommand;
import com.appsdeveloperblog.estore.ordersservice.command.commands.CreateOrderCommand;
import com.appsdeveloperblog.estore.ordersservice.command.commands.RejectOrderCommand;
import com.appsdeveloperblog.estore.ordersservice.events.OrderApprovedEvent;
import com.appsdeveloperblog.estore.ordersservice.events.OrderCreatedEvent;
import com.appsdeveloperblog.estore.ordersservice.events.OrderRejectedEvent;
import com.appsdeveloperblog.estore.ordersservice.model.OrderStatus;

import lombok.NoArgsConstructor;

@Aggregate
@NoArgsConstructor
public class OrderAggregate {

	@AggregateIdentifier
	private String orderId;
	private String productId;
	private String userId;
	private Integer quantity;
	private String addressId;
	private OrderStatus orderStatus;
	
	@CommandHandler
	public OrderAggregate(CreateOrderCommand createOrderCommand) throws Exception {
		OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
		BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
		AggregateLifecycle.apply(orderCreatedEvent);
	}
	
	@CommandHandler
	public void handle(ApproveOrderCommand approveOrderCommand) throws Exception {
		// Create and publish the OrderApprovedEvent
		OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(approveOrderCommand.getOrderId());
		AggregateLifecycle.apply(orderApprovedEvent);
	}
	
	@CommandHandler
	public void handle(RejectOrderCommand rejectOrderCommand) throws Exception {
		// Create and publish the OrderRejectedEvent
		OrderRejectedEvent orderRejectedEvent = new OrderRejectedEvent(rejectOrderCommand.getOrderId(), rejectOrderCommand.getReason());
		AggregateLifecycle.apply(orderRejectedEvent);
	}

	@EventSourcingHandler
	public void on(OrderCreatedEvent orderCreatedEvent) {
		this.orderId = orderCreatedEvent.getOrderId();
		this.productId = orderCreatedEvent.getProductId();
		this.userId = orderCreatedEvent.getUserId();
		this.quantity = orderCreatedEvent.getQuantity();
		this.addressId = orderCreatedEvent.getAddressId();
		this.orderStatus = orderCreatedEvent.getOrderStatus();
	}
	
	@EventSourcingHandler
	public void on(OrderApprovedEvent orderApprovedEvent) {
		this.orderStatus = orderApprovedEvent.getOrderStatus();
	}
	@EventSourcingHandler
	public void on(OrderRejectedEvent orderRejectedEvent) {
		this.orderStatus = orderRejectedEvent.getOrderStatus();
	}
}
