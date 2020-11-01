package com.farshad.checkout.model;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class PricingRulesBundleTest {
    static Validator validator;

    @BeforeClass
    public static void init(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validation_pricingRulesBundle_shouldNotHaveEmptyList() {
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(Collections.emptyList());
        Set<ConstraintViolation<PricingRulesBundle>> violations = validator.validate(pricingRulesBundle);
        assertNotEquals(0,violations.size());
    }
    @Test
    public void validation_pricingRulesBundle_shouldNotHaveNullList() {
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle();
        Set<ConstraintViolation<PricingRulesBundle>> violations = validator.validate(pricingRulesBundle);
        assertNotEquals(0,violations.size());
    }

}