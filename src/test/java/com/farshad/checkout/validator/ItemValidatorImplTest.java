package com.farshad.checkout.validator;

import static org.junit.Assert.*;
import com.farshad.checkout.model.Item;
import com.farshad.checkout.model.Offer;
import com.farshad.checkout.model.PricingRule;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class ItemValidatorImplTest {
    ItemValidatorImpl itemValidator = new ItemValidatorImpl();
    @Test
    public void isValidItem_withPricingRuleForItem_shouldReturnTrue() {
        //given
        List<PricingRule> pricingRuleList = Arrays.asList(
            new PricingRule("A",new BigDecimal("1"),null),
            new PricingRule("B",new BigDecimal("2.2"),new Offer(5,new BigDecimal("10.90")))
        );
        Item itemInShoppingCart = new Item("C",5);
        //when
        boolean actualResult = itemValidator.isValidItem(itemInShoppingCart,pricingRuleList);
        //then
        assertFalse(actualResult);

    }

    @Test
    public void isValidItem_withNoPricingRuleForItem_shouldReturnFalse() {
        //given
        List<PricingRule> pricingRuleList = Arrays.asList(
            new PricingRule("A",new BigDecimal("1"),null),
            new PricingRule("B",new BigDecimal("2.2"),new Offer(5,new BigDecimal("10.90")))
        );
        Item itemInShoppingCart = new Item("C",5);
        //when
        boolean actualResult = itemValidator.isValidItem(itemInShoppingCart,pricingRuleList);
        //then
        assertFalse(actualResult);
    }

}