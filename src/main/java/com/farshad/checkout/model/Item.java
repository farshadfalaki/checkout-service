package com.farshad.checkout.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    @NotBlank
    private String sku;
    @Positive
    private int quantity;

    public void addQuantity(int quantity){
        this.quantity += quantity;
    }
}
