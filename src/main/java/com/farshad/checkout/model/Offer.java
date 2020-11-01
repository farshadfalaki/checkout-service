package com.farshad.checkout.model;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offer {
    @Positive
    private int quantity;
    @NotNull
    @Positive
    private BigDecimal totalPrice;
}
