package com.appsdeveloperblog.estore.paymentsservice.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<PaymentEntity, String> {

}
