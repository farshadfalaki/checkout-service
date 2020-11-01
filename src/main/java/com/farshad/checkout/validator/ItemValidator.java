package com.farshad.checkout.validator;

import com.farshad.checkout.model.Item;
import com.farshad.checkout.model.PricingRule;
import java.util.List;

public interface ItemValidator {
    boolean isValidItem(Item item, List<PricingRule> pricingRuleList);
}
