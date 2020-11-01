package com.farshad.checkout.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Checkout {
    @Id
    private String id;
    private List<Item> items ;
    private List<PricingRule> pricingRules;
}
