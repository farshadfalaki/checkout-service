package com.farshad.checkout.controller;

import com.farshad.checkout.model.PricingRulesBundle;
import com.farshad.checkout.model.Checkout;
import com.farshad.checkout.model.Item;
import com.farshad.checkout.service.CheckoutService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(path = "/checkout")
@AllArgsConstructor
@Slf4j
public class CheckoutController {
    private CheckoutService checkoutService;

    @PostMapping(value = "/create")
    public ResponseEntity<Checkout> createCheckoutWithPricingRules(@Valid @RequestBody PricingRulesBundle pricingRulesBundle){
        log.info("createBasket request {}", pricingRulesBundle);
        ResponseEntity<Checkout> responseEntity = new ResponseEntity<>(checkoutService.createCheckout(pricingRulesBundle), HttpStatus.CREATED);
        log.info("createBasket response {}",responseEntity);
        return responseEntity;
    }

    @PutMapping(value = "/{checkoutId}/items/{sku}")
    public ResponseEntity<List<Item>> addOneNumberOfItem(@PathVariable String checkoutId, @PathVariable String sku){
        log.info("addOneNumberOfItem request checkoutId: {} sku: {} ",checkoutId,sku);
        ResponseEntity<List<Item>> responseEntity = checkoutService.addItems(checkoutId,new Item(sku,1))
            .map(checkout -> new ResponseEntity<>(checkout.getItems(), HttpStatus.OK))
            .orElse(new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND));
        log.info("addOneNumberOfItem response checkoutId: {} items: {}",checkoutId,responseEntity);
        return responseEntity;
    }

    @PutMapping(value = "/{checkoutId}/items")
    public ResponseEntity<List<Item>> addArbitraryNumberOfItem(@PathVariable String checkoutId,@Valid @RequestBody Item item){
        log.info("addArbitraryNumberOfItem request checkoutId: {} sku: {} ",checkoutId,item);
        ResponseEntity<List<Item>> responseEntity = checkoutService.addItems(checkoutId,item)
            .map(checkout -> new ResponseEntity<>(checkout.getItems(), HttpStatus.OK))
            .orElse(new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND));
        log.info("addArbitraryNumberOfItem response checkoutId: {} items: {}",checkoutId,responseEntity);
        return responseEntity;

    }

    @GetMapping(value = "/{checkoutId}/total-price")
    public ResponseEntity<BigDecimal> calculateTotalPrice(@PathVariable String checkoutId){
        log.info("calculateTotalPrice request checkoutId: {}",checkoutId);
        ResponseEntity<BigDecimal> responseEntity = checkoutService.calculateTotalPrice(checkoutId)
            .map(totalPrice -> new ResponseEntity<>(totalPrice, HttpStatus.OK))
            .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        log.info("calculateTotalPrice response checkoutId: {} total price: {}",checkoutId,responseEntity);
        return  responseEntity;
    }

}
