package com.appsdeveloperblog.estore.ordersservice.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<OrderEntity, String> {

	OrderEntity findByOrderId(String orderId);
}
