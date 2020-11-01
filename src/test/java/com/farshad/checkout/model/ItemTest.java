package com.farshad.checkout.model;

import static org.junit.Assert.*;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class ItemTest {
    static Validator validator;

    @BeforeClass
    public static void init(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    public void validation_item_shouldNotHaveNullSku() {
        Item item = new Item(null,5);
        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertNotEquals(0,violations.size());
    }

    @Test
    public void validation_item_shouldNotHaveNegativeQuantity() {
        Item item = new Item("A",-5);
        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertNotEquals(0,violations.size());
    }

    @Test
    public void validation_item_shouldNotHaveZeroQuantity() {
        Item item = new Item("A",-5);
        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertNotEquals(0,violations.size());
    }

    @Test
    public void addQuantity_withAddedQuantity_shouldIncreaseQuantity(){
        Item item = new Item("A",5);
        item.addQuantity(7);
        assertEquals(12,item.getQuantity());
    }
}