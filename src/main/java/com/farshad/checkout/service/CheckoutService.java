package com.farshad.checkout.service;

import com.farshad.checkout.model.Checkout;
import com.farshad.checkout.model.Item;
import com.farshad.checkout.model.PricingRulesBundle;
import java.math.BigDecimal;
import java.util.Optional;

public interface CheckoutService {
    Checkout createCheckout(PricingRulesBundle pricingRulesBundle);
    Optional<Checkout> addItems(String checkoutId,Item newItem);
    Optional<BigDecimal> calculateTotalPrice(String checkoutId);
}
