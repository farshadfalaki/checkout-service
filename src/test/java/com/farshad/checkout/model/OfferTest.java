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

public class OfferTest {
    static Validator validator;

    @BeforeClass
    public static void init(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    public void validation_offer_shouldNotHaveNullPrice() {
        Offer offer = new Offer(5,null);
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertNotEquals(0,violations.size());
    }
    @Test
    public void validation_offer_shouldNotHaveZeroPrice() {
        Offer offer = new Offer(5,new BigDecimal("0"));
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertNotEquals(0,violations.size());
    }
    @Test
    public void validation_offer_shouldNotHaveNegativePrice() {
        Offer offer = new Offer(5,new BigDecimal("-11"));
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertNotEquals(0,violations.size());
    }
    @Test
    public void validation_offer_shouldNotHaveZeroQuantity() {
        Offer offer = new Offer(0,new BigDecimal("11"));
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertNotEquals(0,violations.size());
    }
    @Test
    public void validation_offer_shouldNotHaveNegativeQuantity() {
        Offer offer = new Offer(0,new BigDecimal("11"));
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertNotEquals(0,violations.size());
    }


}