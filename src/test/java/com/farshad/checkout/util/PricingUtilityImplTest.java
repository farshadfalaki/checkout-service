package com.farshad.checkout.util;

import static org.junit.Assert.*;
import com.farshad.checkout.model.Item;
import com.farshad.checkout.model.Offer;
import com.farshad.checkout.model.PricingRule;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

public class PricingUtilityImplTest {
    PricingUtilityImpl pricingUtility = new PricingUtilityImpl();
    @Test
    public void calculateItemPrice_withNoOffer_shouldReturnTotalPriceForItem() {
        //given
        PricingRule pricingRule = new PricingRule("A",new BigDecimal("1.1"),null);
        Item itemInShoppingCart = new Item("A",2);
        //when
        BigDecimal totalItemPrice = pricingUtility.calculateItemPrice(itemInShoppingCart,pricingRule);
        //then
        assertEquals(new BigDecimal("2.2"),totalItemPrice);
    }
    @Test
    public void calculateItemPrice_withLessThanOfferQuantity_shouldReturnTotalPriceForItem() {
        //given
        PricingRule pricingRule = new PricingRule("A",new BigDecimal("1.14"),new Offer(10,new BigDecimal("11.20")));
        Item itemInShoppingCart = new Item("A",1);
        //when
        BigDecimal totalItemPrice = pricingUtility.calculateItemPrice(itemInShoppingCart,pricingRule);
        //then
        assertEquals(new BigDecimal("1.14"),totalItemPrice);
    }

    @Test
    public void calculateItemPrice_withEqualOfferQuantity_shouldReturnTotalPriceForItem() {
        //given
        PricingRule pricingRule = new PricingRule("A",new BigDecimal("1.14"),new Offer(10,new BigDecimal("11.20")));
        Item itemInShoppingCart = new Item("A",10);
        //when
        BigDecimal totalItemPrice = pricingUtility.calculateItemPrice(itemInShoppingCart,pricingRule);
        //then
        assertEquals(new BigDecimal("11.20"),totalItemPrice);
    }

    @Test
    public void calculateItemPrice_withGreaterThanOfferQuantity_shouldReturnTotalPriceForItem() {
        //given
        PricingRule pricingRule = new PricingRule("A",new BigDecimal("1.14"),new Offer(10,new BigDecimal("11.20")));
        Item itemInShoppingCart = new Item("A",39);
        //when
        BigDecimal totalItemPrice = pricingUtility.calculateItemPrice(itemInShoppingCart,pricingRule);
        //then
        assertEquals(new BigDecimal("43.86"),totalItemPrice);
    }

    @Test
    public void findPricingRuleForItem_withNoPricingRuleForItem_shouldReturnEmptyOptional() {
        //given
        List<PricingRule> pricingRuleList = Arrays.asList(
            new PricingRule("A",new BigDecimal("1"),null),
            new PricingRule("B",new BigDecimal("2.2"),new Offer(5,new BigDecimal("10.90")))
        );
        Item itemInShoppingCart = new Item("C",5);
        //when
        Optional<PricingRule> pricingRule = pricingUtility.findPricingRuleForItem(itemInShoppingCart,pricingRuleList);
        //then
        assertFalse(pricingRule.isPresent());
    }

    @Test
    public void findPricingRuleForItem_withPricingRuleForItem_shouldReturnOptionalPricingRule() {
        //given
        PricingRule pricingRuleProductA = new PricingRule("A",new BigDecimal("1"),null);
        PricingRule pricingRuleProductB = new PricingRule("B",new BigDecimal("2.2"),new Offer(5,new BigDecimal("10.90")));
        List<PricingRule> pricingRuleList = Arrays.asList(pricingRuleProductA,pricingRuleProductB);
        Item itemInShoppingCart = new Item("B",5);
        //when
        Optional<PricingRule> actualPricingRule = pricingUtility.findPricingRuleForItem(itemInShoppingCart,pricingRuleList);
        //then
        assertTrue(actualPricingRule.isPresent());
        assertEquals(Optional.of(pricingRuleProductB),actualPricingRule);
    }
}