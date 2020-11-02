package com.farshad.checkout.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.farshad.checkout.exception.CheckoutException;
import com.farshad.checkout.model.Checkout;
import com.farshad.checkout.model.Item;
import com.farshad.checkout.model.Offer;
import com.farshad.checkout.model.PricingRule;
import com.farshad.checkout.model.PricingRulesBundle;
import com.farshad.checkout.repository.CheckoutRepository;
import com.farshad.checkout.util.PricingUtilityImpl;
import com.farshad.checkout.validator.ItemValidatorImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutServiceImplTest {
    @Mock
    CheckoutRepository checkoutRepository;
    @Mock
    ItemValidatorImpl itemValidator;
    @Mock
    PricingUtilityImpl pricingUtility;
    @InjectMocks
    CheckoutServiceImpl checkoutService;
    @Test
    public void createCheckout_withTwoPricingRules_shouldCreateCheckout() {
        //given
        List<PricingRule> pricingRuleList = Arrays.asList(
            new PricingRule("A",new BigDecimal("1"),null),
            new PricingRule("B",new BigDecimal("2.2"),new Offer(5,new BigDecimal("10.90")))
        );
        when(checkoutRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        //when
        Checkout actualCheckout = checkoutService.createCheckout(new PricingRulesBundle(pricingRuleList));
        //then
        assertNotNull(actualCheckout.getId());
        assertEquals(pricingRuleList,actualCheckout.getPricingRules());
        assertEquals(0,actualCheckout.getItems().size());
        verify(checkoutRepository).save(any());
    }

    @Test
    public void addItems_withNotExistingCheckoutId_shouldReturnEmptyOptional() {
        //given
        String checkoutId = "NOT-EXISTING_ID";
        Item newItem = new Item("A",5);
        when(checkoutRepository.findById(checkoutId)).thenReturn(Optional.empty());
        //when
        Optional<Checkout> optionalCheckout = checkoutService.addItems(checkoutId,newItem);
        //then
        assertFalse(optionalCheckout.isPresent());
        verify(checkoutRepository).findById(checkoutId);
    }

    @Test
    public void addItems_withInvalidSKU_shouldReturnEmptyOptional() {
        //given
        String checkoutId = "asdf-1234";
        Item newItem = new Item("INVALID-SKU",5);
        Checkout checkout = new Checkout(checkoutId,Collections.emptyList(),Collections.emptyList());
        when(checkoutRepository.findById(checkoutId)).thenReturn(Optional.of(checkout));
        when(itemValidator.isValidItem(newItem, checkout.getPricingRules())).thenReturn(false);
        //when
        Optional<Checkout> optionalCheckout = checkoutService.addItems(checkoutId,newItem);
        //then
        assertFalse(optionalCheckout.isPresent());
        verify(checkoutRepository).findById(checkoutId);
        verify(itemValidator).isValidItem(newItem, Collections.emptyList());
    }

    @Test
    public void addItems_withNotExistingItemInShoppingCart_shouldAddItemToShoppingCart() {
        //given
        String checkoutId = "asdf-1234";
        List<PricingRule> pricingRuleList = Arrays.asList(
            new PricingRule("A",new BigDecimal("1"),null),
            new PricingRule("B",new BigDecimal("2.2"),new Offer(5,new BigDecimal("10.90")))
        );
        Item newItem = new Item("A",5);
        Checkout checkout = new Checkout(checkoutId,new ArrayList<>(),pricingRuleList);
        when(checkoutRepository.findById(checkoutId)).thenReturn(Optional.of(checkout));
        when(checkoutRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(itemValidator.isValidItem(newItem, checkout.getPricingRules())).thenReturn(true);
        //when
        Optional<Checkout> optionalCheckout = checkoutService.addItems(checkoutId,newItem);
        //then
        assertTrue(optionalCheckout.isPresent());
        assertEquals(newItem,optionalCheckout.get().getItems().get(0));
        verify(checkoutRepository).findById(checkoutId);
        verify(checkoutRepository).save(any());
        verify(itemValidator).isValidItem(newItem, pricingRuleList);
    }

    @Test
    public void addItems_withExistingItemInShoppingCart_shouldAddItemToShoppingCart() {
        //given
        String checkoutId = "asdf-1234";
        List<PricingRule> pricingRuleList = Arrays.asList(
            new PricingRule("A",new BigDecimal("1"),null),
            new PricingRule("B",new BigDecimal("2.2"),new Offer(5,new BigDecimal("10.90")))
        );
        Item newItem = new Item("B",5);
        Checkout checkout = new Checkout(checkoutId,Arrays.asList(new Item("A",5),new Item("B",3)),pricingRuleList);
        when(checkoutRepository.findById(checkoutId)).thenReturn(Optional.of(checkout));
        when(checkoutRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(itemValidator.isValidItem(newItem, checkout.getPricingRules())).thenReturn(true);
        //when
        Optional<Checkout> optionalCheckout = checkoutService.addItems(checkoutId,newItem);
        //then
        assertTrue(optionalCheckout.isPresent());
        assertEquals(new Item("B",8),optionalCheckout.get().getItems().get(1));
        verify(checkoutRepository).findById(checkoutId);
        verify(checkoutRepository).save(any());
        verify(itemValidator).isValidItem(newItem, pricingRuleList);
    }

    @Test
    public void calculateTotalPrice_withNotExistingCheckoutId_shouldReturnEmptyOptional() {
        //given
        String checkoutId = "NOT-EXISTING_ID";
        when(checkoutRepository.findById(checkoutId)).thenReturn(Optional.empty());
        //when
        Optional<BigDecimal> optionalTotalPrice = checkoutService.calculateTotalPrice(checkoutId);
        //then
        assertFalse(optionalTotalPrice.isPresent());
        verify(checkoutRepository).findById(checkoutId);
    }

    @Test(expected = CheckoutException.class)
    public void calculateTotalPrice_withNoPricingRuleForItem_shouldThrowCheckoutException() {
        //given
        String checkoutId = "asdf-1234";
        List<PricingRule> pricingRuleList = Arrays.asList(
            new PricingRule("A",new BigDecimal("1"),null),
            new PricingRule("B",new BigDecimal("2.2"),new Offer(5,new BigDecimal("10.90")))
        );
        Item itemInShoppingCart = new Item("C",5);
        Checkout checkout = new Checkout(checkoutId, Collections.singletonList(itemInShoppingCart),pricingRuleList);
        when(checkoutRepository.findById(checkoutId)).thenReturn(Optional.of(checkout));
        when(pricingUtility.findPricingRuleForItem(any(),any())).thenReturn(Optional.empty());
        //when
        checkoutService.calculateTotalPrice(checkoutId);
    }

    @Test
    public void calculateTotalPrice_withExistingCheckoutId_shouldReturnTotalPrice() {
        //given
        String checkoutId = "asdf-1234";

        PricingRule pricingRuleA = new PricingRule("A",new BigDecimal("1.1"),new Offer(4,new BigDecimal("4.20")));
        PricingRule pricingRuleB = new PricingRule("C",new BigDecimal("0.5"),null);
        PricingRule pricingRuleC = new PricingRule("B",new BigDecimal("2.2"),new Offer(5,new BigDecimal("10.90")));

        List<PricingRule> pricingRuleList = Arrays.asList(pricingRuleA,pricingRuleB,pricingRuleC);
        Item itemInShoppingCartC = new Item("C",11);
        Item itemInShoppingCartA = new Item("A",4);
        Item itemInShoppingCartB = new Item("B",9);
        List<Item> itemsInInShoppingCart = Arrays.asList(itemInShoppingCartA,itemInShoppingCartB,itemInShoppingCartC);
        Checkout checkout = new Checkout(checkoutId, itemsInInShoppingCart ,pricingRuleList);
        when(checkoutRepository.findById(checkoutId)).thenReturn(Optional.of(checkout));
        when(pricingUtility.findPricingRuleForItem(itemInShoppingCartA,pricingRuleList)).thenReturn(Optional.of(pricingRuleA));
        when(pricingUtility.findPricingRuleForItem(itemInShoppingCartB,pricingRuleList)).thenReturn(Optional.of(pricingRuleB));
        when(pricingUtility.findPricingRuleForItem(itemInShoppingCartC,pricingRuleList)).thenReturn(Optional.of(pricingRuleC));
        when(pricingUtility.calculateItemPrice(itemInShoppingCartA,pricingRuleA)).thenReturn(new BigDecimal("4.20"));
        when(pricingUtility.calculateItemPrice(itemInShoppingCartB,pricingRuleB)).thenReturn(new BigDecimal("24.0"));
        when(pricingUtility.calculateItemPrice(itemInShoppingCartC,pricingRuleC)).thenReturn(new BigDecimal("5.50"));
        //when
        Optional<BigDecimal> optionalTotalPrice = checkoutService.calculateTotalPrice(checkoutId);
        //then
        assertTrue(optionalTotalPrice.isPresent());
        assertEquals(Optional.of(new BigDecimal("33.70")),optionalTotalPrice);
        verify(checkoutRepository).findById(checkoutId);
        verify(pricingUtility,times(itemsInInShoppingCart.size())).calculateItemPrice(any(),any());
        verify(pricingUtility,times(itemsInInShoppingCart.size())).findPricingRuleForItem(any(),any());

    }

}