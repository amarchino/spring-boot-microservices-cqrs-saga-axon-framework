package com.appsdeveloperblog.estore.ordersservice;

import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrdersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersServiceApplication.class, args);
	}

	@Autowired
	void configure(EventProcessingConfigurer configurer) {
		configurer.registerListenerInvocationErrorHandler("order-group", conf -> PropagatingErrorHandler.INSTANCE);
	}
}
