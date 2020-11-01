package com.farshad.checkout.service;

import com.farshad.checkout.exception.CheckoutException;
import com.farshad.checkout.model.Checkout;
import com.farshad.checkout.model.Item;
import com.farshad.checkout.model.PricingRulesBundle;
import com.farshad.checkout.repository.CheckoutRepository;
import com.farshad.checkout.util.PricingUtility;
import com.farshad.checkout.validator.ItemValidator;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class CheckoutServiceImpl implements CheckoutService{
    private CheckoutRepository checkoutRepository;
    private ItemValidator itemValidator;
    private PricingUtility pricingUtility;

    @Override
    public Checkout createCheckout(PricingRulesBundle pricingRulesBundle) {
        Checkout checkout = Checkout.builder().id(UUID.randomUUID().toString()).pricingRules(pricingRulesBundle.getPricingRules()).items(Collections.emptyList()).build();
        return checkoutRepository.save(checkout);
    }

    @Override
    public Optional<Checkout> addItems(String checkoutId,Item newItem) {
        Checkout updatedCheckout = null;
        Optional<Checkout> optionalCheckout = checkoutRepository.findById(checkoutId);
        if (optionalCheckout.isPresent()){
            Checkout fetchedCheckout = optionalCheckout.get();
            if(itemValidator.isValidItem(newItem,fetchedCheckout.getPricingRules())) {
                Optional<Item> optionalItemInShoppingCart = fetchedCheckout.getItems().stream()
                    .filter(item -> item.getSku().equals(newItem.getSku()))
                    .findFirst();

                if (optionalItemInShoppingCart.isPresent()) {
                    optionalItemInShoppingCart.get().addQuantity(newItem.getQuantity());
                } else {
                    fetchedCheckout.getItems().add(newItem);
                }
                updatedCheckout = checkoutRepository.save(fetchedCheckout);
            }
        }
        return Optional.ofNullable(updatedCheckout);
    }

    @Override
    public Optional<BigDecimal> calculateTotalPrice(String checkoutId) {
        return checkoutRepository.findById(checkoutId)
            .map(this::calculateTotalPrice);
    }

    BigDecimal calculateTotalPrice(Checkout checkout) {
        return checkout.getItems().stream()
            .map(item ->
                    pricingUtility.findPricingRuleForItem(item,checkout.getPricingRules())
                    .map(pricingRule -> pricingUtility.calculateItemPrice(item,pricingRule))
                    .orElseThrow(() -> new CheckoutException("No pricing rule found for Sku " + item.getSku() ))
            )
            .reduce(BigDecimal::add).orElse(BigDecimal.valueOf(0));
    }

}
