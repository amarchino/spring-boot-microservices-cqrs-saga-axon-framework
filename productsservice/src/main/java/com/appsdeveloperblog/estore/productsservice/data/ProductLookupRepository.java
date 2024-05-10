package com.appsdeveloperblog.estore.productsservice.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String> {

	ProductLookupEntity findByProductIdOrTitle(String productId, String title);
}
