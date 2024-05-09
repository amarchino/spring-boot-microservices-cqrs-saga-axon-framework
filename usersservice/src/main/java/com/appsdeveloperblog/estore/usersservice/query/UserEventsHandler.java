package com.appsdeveloperblog.estore.usersservice.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.estore.core.model.PaymentDetails;
import com.appsdeveloperblog.estore.core.model.User;
import com.appsdeveloperblog.estore.core.query.FetchUserPaymentDetailsQuery;

@Component
@ProcessingGroup("user-group")
public class UserEventsHandler {

	@QueryHandler
	public User findUserPaymentDetails(FetchUserPaymentDetailsQuery query) {
		PaymentDetails paymentDetails = PaymentDetails.builder()
			.cardNumber("123Card")
			.cvv("123")
			.name("SERGEY KARGOPOLOV")
			.validUntilMonth(12)
			.validUntilYear(2030)
			.build();
				 
		User userRest = User.builder()
			.firstName("Sergey")
			.lastName("Kargopolov")
			.userId(query.getUserId())
			.paymentDetails(paymentDetails)
			.build();
		return userRest;
	}
}
