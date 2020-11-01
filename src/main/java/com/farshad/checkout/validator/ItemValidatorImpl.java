package com.farshad.checkout.validator;

import com.farshad.checkout.model.Item;
import com.farshad.checkout.model.PricingRule;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ItemValidatorImpl implements ItemValidator {

    public boolean isValidItem(Item item, List<PricingRule> pricingRuleList){
        return pricingRuleList.stream().anyMatch(pricingRule -> pricingRule.getSku().equals(item.getSku()));
    }
}
