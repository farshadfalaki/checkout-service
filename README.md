# checkout-service
This Spring-Boot application is implemented to provides an API for a checkout system in a
supermarket. It should offer its clients the possibility to register an arbitrary number of items in a shopping cart and
calculate the cart's total price in the end.

### Comment 
I think it was better that prices and offers were completely seprate from the shopping cart. Prices could change independently and
any time we calculate the total amount based on the last prices and the offers.
But anyway in this challenge each time at the beginning of checkout prices and offers are sent to API , then there is a link between the package of prices-offers and the customer's shopping cart.

### Entities
* **PricingRule** : includes product's SKU , price and an **Offer** if exists. 
* **Offer**: indicates the details of an offer: the qunatity and related total price. 
* **Item** : includes product's SKU , and the desired number of that product which the customer wants to purchase.
* **Checkout** : includes </br>
An unique Id as primary key. <br />
A list of **PricingRule** entity which contains prices and offers. <br />
A List of **Item**s indicating products that the customer wants to buy.<br />
###### At first I selected *Map<String,Item>* which the key is SKU and value is an **Item**. Beacause while adding an item to the shopping cart, always we need to traverse the list. I thought using *Map* will be effective. But because there are not so many items in the shopping cart, it will not make a performence issue. Then I prefered to use *List* data structure to have more readable code in this phase. But it can be checked further as a performence improvement case. 
        

### Use Cases:
#### Create-checkout
The aim is to start the checkout process. <br />
Input is a *PricingRuleBundle* which consists of a list of prices and offers.<br />
Output is the created **Checkout** object containing an unique **checkoutId**. It is used in **Scan-arbitary-number-of-items** and **Calculate-the-total-price** use cases.

        
#### Scan-arbitary-number-of-items 
The aim is add one or serveral number of items to the shopping cart.<br />
Inputs are an **Item** and a **checkoutId** which is obtained from the output of **Create-checkout** use case .<br />
Output is a List of the **Item** that currently exists in the shopping cart.

                
        
#### Calculate-the-total-price 
The aim is to calculate the total price.<br />
Input is **checkoutId** which is obtaind from the output of **Create-checkout** use case.
Output is the total price of the shopping cart according to the prices and offers defined in **Create-checkout** use case. 


### Next Improvement:
## Business 
As like a real *supermarket*, after scanning each item, the total amount is displayed to the customer. Then in the output of **Scan-arbitary-number-of-items** we could return whole items with total price for each item and total price of the shopping cart and maybe total discount that customer earns from this shopping.       
By this change, at the end of each **Scan-arbitary-number-of-items** call, total price is calculated, persisted and returned to the client. In this case most of the time there is no need to call **Calculate-the-total-price** as anything is returned in the response of each **Scan-arbitary-number-of-items**. We also should consider the differences between shppoing at a real supermarket and an online supermarket which customer can pay a long time after creating the shopping cart.
