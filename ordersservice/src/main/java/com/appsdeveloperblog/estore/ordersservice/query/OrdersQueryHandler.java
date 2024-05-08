package com.appsdeveloperblog.estore.ordersservice.query;

import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.estore.ordersservice.core.data.OrdersRepository;
import com.appsdeveloperblog.estore.ordersservice.query.rest.OrderRestModel;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrdersQueryHandler {
	private final OrdersRepository ordersRepository;
	
	@QueryHandler
	public List<OrderRestModel> findOrders(FindOrdersQuery query) {
		return ordersRepository.findAll()
			.stream()
			.map(pe -> {
				OrderRestModel prm = new OrderRestModel();
				BeanUtils.copyProperties(pe, prm);
				return prm;
			})
			.toList();
	}
}
