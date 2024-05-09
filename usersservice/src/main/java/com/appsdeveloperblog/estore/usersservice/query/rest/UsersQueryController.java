package com.appsdeveloperblog.estore.usersservice.query.rest;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.estore.core.model.User;
import com.appsdeveloperblog.estore.core.query.FetchUserPaymentDetailsQuery;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersQueryController {

	private final QueryGateway queryGateway;

	@GetMapping("/{userId}/payment-details")
	public User getMethodName(@PathVariable("userId") String userId) {
		FetchUserPaymentDetailsQuery query = new FetchUserPaymentDetailsQuery(userId);
		return queryGateway.query(query, ResponseTypes.instanceOf(User.class)).join();
	}
	
}
