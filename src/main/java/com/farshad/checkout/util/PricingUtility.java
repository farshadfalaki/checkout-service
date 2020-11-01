package com.farshad.checkout.util;

import com.farshad.checkout.model.Item;
import com.farshad.checkout.model.PricingRule;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PricingUtility {
     BigDecimal calculateItemPrice(Item item, PricingRule pricingRule);

     Optional<PricingRule> findPricingRuleForItem(Item item, List<PricingRule> pricingRuleList);
}
