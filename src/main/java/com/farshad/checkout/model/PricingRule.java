package com.farshad.checkout.model;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingRule {
    @NotBlank
    private String sku;
    @NotNull
    @Positive
    private BigDecimal price;
    @Valid
    private Offer offer;
}
