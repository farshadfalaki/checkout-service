package com.farshad.checkout.model;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingRulesBundle {
    @Valid
    @NotNull
    @NotEmpty
    private List<PricingRule> pricingRules;
}
