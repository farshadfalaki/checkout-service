package com.farshad.checkout.repository;

import com.farshad.checkout.model.Checkout;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutRepository extends MongoRepository<Checkout, String> {

}
