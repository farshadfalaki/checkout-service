package com.farshad.checkout.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.farshad.checkout.model.Checkout;
import com.farshad.checkout.model.Item;
import com.farshad.checkout.model.Offer;
import com.farshad.checkout.model.PricingRule;
import com.farshad.checkout.model.PricingRulesBundle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest()
public class CheckoutControllerTest {
    @Autowired
    private MockMvc mockMvc;
    static ObjectMapper objectMapper = new ObjectMapper();

    public static final String CREATE_CHECKOUT_URL = "http://localhost:8080/checkout/create";
    public static final String CHECKOUT_URL = "http://localhost:8080/checkout";
    public static final String ADD_ITEM_CONTEXT = "/items";
    public static final String TOTAL_PRICE_CONTEXT = "/total-price";

    @BeforeClass
    public static void init(){
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }
    @Test
    public void createCheckout_withEmptyBody_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    public void createCheckout_withNoPricingRule_shouldReturnBadRequest() throws Exception {
        List<PricingRule> pricingRuleList = Collections.emptyList();
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    public void createCheckout_withPricingRuleEmptyPrice_shouldReturnBadRequest() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", null, null));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    public void createCheckout_withPricingRuleZeroPrice_shouldReturnBadRequest() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("0"), null));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    public void createCheckout_withPricingRuleNegativePrice_shouldReturnBadRequest() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("-10.90"), null));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    public void createCheckout_withPricingRuleNullSku_shouldReturnBadRequest() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule(null, new BigDecimal("10.90"), null));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    public void createCheckout_withPricingRuleEmptySku_shouldReturnBadRequest() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("", new BigDecimal("10.90"), null));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    public void createCheckout_withPricingRuleWithOfferNullPrice_shouldReturnBadRequest() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.90"), new Offer(1,null)));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    public void createCheckout_withPricingRuleWithOfferZeroPrice_shouldReturnBadRequest() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.90"), new Offer(1,new BigDecimal("0"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    public void createCheckout_withPricingRuleWithOfferNegativePrice_shouldReturnBadRequest() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.90"), new Offer(1,new BigDecimal("-10"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    public void createCheckout_withPricingRuleWithOfferZeroQuantity_shouldReturnBadRequest() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.90"), new Offer(0,new BigDecimal("10"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    public void createCheckout_withPricingRuleWithOfferNegativeQuantity_shouldReturnBadRequest() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.90"), new Offer(-10,new BigDecimal("10"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    public void createCheckout_withPricingRuleWithoutOffer_shouldReturnCreatedCheckout() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.9"), null));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id",notNullValue()))
            .andExpect(jsonPath("$.items",hasSize(0)))
            .andExpect(jsonPath("$.pricing_rules",hasSize(1)))
            .andExpect(jsonPath("$.pricing_rules[0].sku",is("A")))
            .andExpect(jsonPath("$.pricing_rules[0].price",is(10.9)))
            .andExpect(jsonPath("$.pricing_rules[0].offer",nullValue()));
    }
    @Test
    public void createCheckout_withPricingRuleWithOffer_shouldReturnCreatedCheckout() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.94"), new Offer(10,new BigDecimal("108.2"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id",notNullValue()))
            .andExpect(jsonPath("$.items",hasSize(0)))
            .andExpect(jsonPath("$.pricing_rules",hasSize(1)))
            .andExpect(jsonPath("$.pricing_rules[0].sku",is("A")))
            .andExpect(jsonPath("$.pricing_rules[0].price",is(10.94)))
            .andExpect(jsonPath("$.pricing_rules[0].offer.quantity",is(10)))
            .andExpect(jsonPath("$.pricing_rules[0].offer.total_price",is(108.2)));
    }
    @Test
    public void createCheckout_calledTwice_shouldReturnDifferentCheckoutIds() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.94"), new Offer(10,new BigDecimal("108.2"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);

        Checkout checkout1 = createCheckout(pricingRulesBundle);
        Checkout checkout2 = createCheckout(pricingRulesBundle);
        assertNotEquals(checkout1.getId(),checkout2.getId());
    }

    @Test
    public void addOneNumberOfItem_withInvalidCheckoutId_shouldReturnNotFound() throws Exception {
        addOneNumberItem("INVALID-ID","A").andExpect(status().isNotFound());
    }
    @Test
    public void addOneNumberOfItem_anInvalidSku_shouldReturnNotFound() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.94"), new Offer(10,new BigDecimal("108.2"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        Checkout checkout = createCheckout(pricingRulesBundle);
        addOneNumberItem(checkout.getId(),"X").andExpect(status().isNotFound());
    }
    @Test
    public void addOneNumberOfItem_toNotExistingItemInEmptyShoppingCart_shouldAddToShoppingCart() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.94"), new Offer(10,new BigDecimal("108.2"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        Checkout checkout = createCheckout(pricingRulesBundle);
        addOneNumberItem(checkout.getId(),"A")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$",hasSize(1)))
            .andExpect(jsonPath("$[0].sku",is("A")))
            .andExpect(jsonPath("$[0].quantity",is(1)));
    }
    @Test
    public void addOneNumberOfItem_toExistingItemInShoppingCart_shouldAddOneNumberToItem() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.94"), new Offer(10,new BigDecimal("108.2"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        Checkout checkout = createCheckout(pricingRulesBundle);
        addOneNumberItem(checkout.getId(),"A")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$",hasSize(1)))
            .andExpect(jsonPath("$[0].sku",is("A")))
            .andExpect(jsonPath("$[0].quantity",is(1)));

        addOneNumberItem(checkout.getId(),"A")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$",hasSize(1)))
            .andExpect(jsonPath("$[0].sku",is("A")))
            .andExpect(jsonPath("$[0].quantity",is(2)));
    }

    @Test
    public void addArbitraryNumberOfItem_withInvalidCheckoutId_shouldReturnNotFound() throws Exception {
        addArbitraryNumberItem("INVALID-ID",new Item("A",5))
            .andExpect(status().isNotFound());
    }
    @Test
    public void addArbitraryNumberOfItem_anInvalidSku_shouldReturnNotFound() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.94"), new Offer(10,new BigDecimal("108.2"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        Checkout checkout = createCheckout(pricingRulesBundle);
        addArbitraryNumberItem(checkout.getId(),new Item("X",5))
            .andExpect(status().isNotFound());
    }
    @Test
    public void addArbitraryNumberOfItem_withZeroQuantity_shouldReturnBadRequest() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.94"), new Offer(10,new BigDecimal("108.2"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        Checkout checkout = createCheckout(pricingRulesBundle);
        addArbitraryNumberItem(checkout.getId(),new Item("A",0))
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    public void addArbitraryNumberOfItem_withNegativeQuantity_shouldReturnBadRequest() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.94"), new Offer(10,new BigDecimal("108.2"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        Checkout checkout = createCheckout(pricingRulesBundle);
        addArbitraryNumberItem(checkout.getId(),new Item("A",-10))
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    public void addArbitraryNumberOfItem_toNotExistingItemInEmptyShoppingCart_shouldAddToShoppingCart() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.94"), new Offer(10,new BigDecimal("108.2"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        Checkout checkout = createCheckout(pricingRulesBundle);
        addArbitraryNumberItem(checkout.getId(),new Item("A",5))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$",hasSize(1)))
            .andExpect(jsonPath("$[0].sku",is("A")))
            .andExpect(jsonPath("$[0].quantity",is(5)));
    }
    @Test
    public void addArbitraryNumberOfItem_toExistingItemInShoppingCart_shouldAddOneNumberToItem() throws Exception {
        List<PricingRule> pricingRuleList = Collections.singletonList(new PricingRule("A", new BigDecimal("10.94"), new Offer(10,new BigDecimal("108.2"))));
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        Checkout checkout = createCheckout(pricingRulesBundle);
        addArbitraryNumberItem(checkout.getId(),new Item("A",5))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$",hasSize(1)))
            .andExpect(jsonPath("$[0].sku",is("A")))
            .andExpect(jsonPath("$[0].quantity",is(5)));

        addArbitraryNumberItem(checkout.getId(),new Item("A",4))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$",hasSize(1)))
            .andExpect(jsonPath("$[0].sku",is("A")))
            .andExpect(jsonPath("$[0].quantity",is(9)));
    }
    @Test
    public void addItem_severalTimesAddingItemSomeWithInvalidSkuOrQuantity_shouldAddItemsCorrectly() throws Exception {
        List<PricingRule> pricingRuleList = Arrays.asList(
            new PricingRule("A", new BigDecimal("10.94"), new Offer(10,new BigDecimal("108.2"))),
            new PricingRule("B", new BigDecimal("4"), new Offer(3,new BigDecimal("11.90"))),
            new PricingRule("C", new BigDecimal("7.2"), new Offer(5,new BigDecimal("35.22")))
        );
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        Checkout checkout = createCheckout(pricingRulesBundle);

        addOneNumberItem(checkout.getId(),"A")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$",hasSize(1)))
            .andExpect(jsonPath("$[0].sku",is("A")))
            .andExpect(jsonPath("$[0].quantity",is(1)));

        addArbitraryNumberItem(checkout.getId(),new Item("B",2))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$",hasSize(2)))
            .andExpect(jsonPath("$[0].sku",is("A")))
            .andExpect(jsonPath("$[0].quantity",is(1)))
            .andExpect(jsonPath("$[1].sku",is("B")))
            .andExpect(jsonPath("$[1].quantity",is(2)));

        addArbitraryNumberItem(checkout.getId(),new Item("A",4))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$",hasSize(2)))
            .andExpect(jsonPath("$[0].sku",is("A")))
            .andExpect(jsonPath("$[0].quantity",is(5)))
            .andExpect(jsonPath("$[1].sku",is("B")))
            .andExpect(jsonPath("$[1].quantity",is(2)));

        addOneNumberItem(checkout.getId(),"XX").andExpect(status().isNotFound());
        addArbitraryNumberItem(checkout.getId(),new Item("C",-13)).andExpect(status().isBadRequest());
        addArbitraryNumberItem(checkout.getId(),new Item("XX",13)).andExpect(status().isNotFound());
        addArbitraryNumberItem(checkout.getId(),new Item("C",3))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$",hasSize(3)))
            .andExpect(jsonPath("$[0].sku",is("A")))
            .andExpect(jsonPath("$[0].quantity",is(5)))
            .andExpect(jsonPath("$[1].sku",is("B")))
            .andExpect(jsonPath("$[1].quantity",is(2)))
            .andExpect(jsonPath("$[2].sku",is("C")))
            .andExpect(jsonPath("$[2].quantity",is(3)));

        addOneNumberItem(checkout.getId(),"XX").andExpect(status().isNotFound());

        addOneNumberItem(checkout.getId(),"A")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$",hasSize(3)))
            .andExpect(jsonPath("$[0].sku",is("A")))
            .andExpect(jsonPath("$[0].quantity",is(6)))
            .andExpect(jsonPath("$[1].sku",is("B")))
            .andExpect(jsonPath("$[1].quantity",is(2)))
            .andExpect(jsonPath("$[2].sku",is("C")))
            .andExpect(jsonPath("$[2].quantity",is(3)));
    }

    @Test
    public void totalPrice_withInvalidCheckoutId_shouldReturnNotFound() throws Exception {
        totalPrice("INVALID-ID")
            .andExpect(status().isNotFound());
    }

    @Test
    public void totalPrice_withValidCheckoutId_shouldReturnTotalPrice() throws Exception {
        List<PricingRule> pricingRuleList = Arrays.asList(
            new PricingRule("A", new BigDecimal("10.94"), new Offer(10,new BigDecimal("108.2"))),
            new PricingRule("B", new BigDecimal("4"),null),
            new PricingRule("C", new BigDecimal("7.2"), new Offer(5,new BigDecimal("35.22"))),
            new PricingRule("D", new BigDecimal("2.5"), new Offer(4,new BigDecimal("9.99")))
        );
        PricingRulesBundle pricingRulesBundle = new PricingRulesBundle(pricingRuleList);
        Checkout checkout = createCheckout(pricingRulesBundle);

        addOneNumberItem(checkout.getId(),"A").andExpect(status().isOk());
        addArbitraryNumberItem(checkout.getId(),new Item("B",4)).andExpect(status().isOk());
        addArbitraryNumberItem(checkout.getId(),new Item("C",15)).andExpect(status().isOk());
        addArbitraryNumberItem(checkout.getId(),new Item("D",9)).andExpect(status().isOk());
        totalPrice(checkout.getId()).andExpect(status().isOk())
            .andExpect(jsonPath("$",is(155.08)));
    }

    public Checkout createCheckout(PricingRulesBundle pricingRulesBundle) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(CREATE_CHECKOUT_URL).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(pricingRulesBundle)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id",notNullValue()))
            .andExpect(jsonPath("$.items",hasSize(0)))
            .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, Checkout.class);
    }

    public ResultActions addOneNumberItem(String checkoutId,String sku) throws Exception {
        return mockMvc.perform(put(CHECKOUT_URL + "/" + checkoutId + ADD_ITEM_CONTEXT + "/" + sku).contentType(MediaType.APPLICATION_JSON))
            .andDo(print());
    }
    public ResultActions addArbitraryNumberItem(String checkoutId,Item newItem) throws Exception {
        return mockMvc.perform(put(CHECKOUT_URL + "/" + checkoutId + ADD_ITEM_CONTEXT).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(newItem)))
            .andDo(print());
    }
    public ResultActions totalPrice(String checkoutId) throws Exception {
        return mockMvc.perform(get(CHECKOUT_URL + "/" + checkoutId + TOTAL_PRICE_CONTEXT ).contentType(MediaType.APPLICATION_JSON))
            .andDo(print());
    }
}