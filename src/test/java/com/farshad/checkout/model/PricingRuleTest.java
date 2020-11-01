package com.farshad.checkout.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class PricingRuleTest {
    static Validator validator;

    @BeforeClass
    public static void init(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    public void validation_pricingRule_shouldNotHaveNullSku() {
        PricingRule pricingRule = new PricingRule(null,new BigDecimal("12"),new Offer(1,new BigDecimal("11")));
        Set<ConstraintViolation<PricingRule>> violations = validator.validate(pricingRule);
        assertNotEquals(0,violations.size());
    }

    @Test
    public void validation_pricingRule_shouldNotHaveNullPrice() {
        PricingRule pricingRule = new PricingRule("A",null,new Offer(1,new BigDecimal("11")));
        Set<ConstraintViolation<PricingRule>> violations = validator.validate(pricingRule);
        assertNotEquals(0,violations.size());
    }

    @Test
    public void validation_pricingRule_shouldNotHaveZeroPrice() {
        PricingRule pricingRule = new PricingRule("A",new BigDecimal("0"),new Offer(1,new BigDecimal("11")));
        Set<ConstraintViolation<PricingRule>> violations = validator.validate(pricingRule);
        assertNotEquals(0,violations.size());
    }

    @Test
    public void validation_pricingRule_shouldNotHaveNegativePrice() {
        PricingRule pricingRule = new PricingRule("A",new BigDecimal("-0.50"),new Offer(1,new BigDecimal("11")));
        Set<ConstraintViolation<PricingRule>> violations = validator.validate(pricingRule);
        assertNotEquals(0,violations.size());
    }


    @Test
    public void validation_pricingRule_couldHaveNullOffer() {
        PricingRule pricingRule = new PricingRule("A",new BigDecimal("11"),null);
        Set<ConstraintViolation<PricingRule>> violations = validator.validate(pricingRule);
        assertEquals(0,violations.size());
    }

}