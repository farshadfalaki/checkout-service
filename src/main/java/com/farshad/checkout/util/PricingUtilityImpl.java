package com.farshad.checkout.util;

import com.farshad.checkout.model.Item;
import com.farshad.checkout.model.PricingRule;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class PricingUtilityImpl implements PricingUtility {
    public BigDecimal calculateItemPrice(Item item, PricingRule pricingRule){
        BigDecimal itemTotalPrice;
        if (pricingRule.getOffer() == null){
            itemTotalPrice =  pricingRule.getPrice().multiply(new BigDecimal(item.getQuantity()));
        }else{
            int inPromotionPackageCount = item.getQuantity() / pricingRule.getOffer().getQuantity();
            int notInPromotionCount = item.getQuantity() % pricingRule.getOffer().getQuantity();
            itemTotalPrice = pricingRule.getPrice().multiply(new BigDecimal(notInPromotionCount)).add(
                pricingRule.getOffer().getTotalPrice().multiply(new BigDecimal(inPromotionPackageCount)));
        }
        return itemTotalPrice;
    }

    public Optional<PricingRule> findPricingRuleForItem(Item item, List<PricingRule> pricingRuleList){
        return pricingRuleList.stream()
            .filter(pricingRule -> pricingRule.getSku().equals(item.getSku()))
            .findFirst();

    }

}
