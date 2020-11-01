# checkout-service
This application is implemented to provide an API for a checkout system in a
supermarket. It should offer its clients the possibility to register an arbitrary number of items in a shopping cart and
calculate the cart's total price in the end.

The application uses JDK 8, Spring-Boot, Mongodb.  


#### Prerequisites
+ JDK 8+
+ Maven
+ Docker , Docker-compose
### Installation
After pulling the code, make *install.sh* script file executable.
```
chmod 770 install.sh
```
Now to run the application execute script *install.sh*
```
./install.sh
```
### Service API
Endpoint API details are available at swagger url http://localhost:8080/swagger-ui.html

### Note
I think it was better that prices and offers were completely separate from the shopping cart. Prices could change independently and
any time we calculate the total amount based on the last prices and the offers.
But anyway in this challenge each time at the beginning of checkout prices and offers are sent to API , then there is a link between the package of prices-offers and the customer's shopping cart.

### Entities
* **PricingRule** : includes product's SKU , price and an **Offer** if exists. 
* **Offer**: indicates the details of an offer: the quantity and related total price. 
* **Item** : includes product's SKU , and the desired number of that product which the customer wants to purchase.
* **Checkout** : includes </br>
An unique Id as primary key. <br />
A list of **PricingRule** entities which contain prices and offers. <br />
A List of **Item**s indicating products that the customer wants to buy.<br />
###### At first I selected *Map<String,Item>* which the key is SKU and value is an **Item**. Because while adding an item to the shopping cart, we always need to traverse the list. I thought using *Map* will be effective. But because there are not so many items in the shopping cart, it will not make a performance issue. Then I prefered to use *List* data structure to have more readable code in this phase. But it can be checked further as a performance  improvement case. 
        

### Use Cases
#### Create-checkout
The aim is to start the checkout process. <br />
Input is a *PricingRuleBundle* which consists of a list of prices and offers.<br />
Output is the created **Checkout** object containing an unique **checkoutId**. It is used in **Scan-arbitary-number-of-items** and **Calculate-the-total-price** use cases.

##### Example
Request
```
POST /checkout/create HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
    "pricing_rules" :[
        {
            "sku" : "A",
            "price" : "1",
            "offer" : {"quantity":5,"total_price": 4.5}
        },{
            "sku" : "B",
            "price" : "10"
        }
    ]
}
```
Response
```
POST /checkout/create HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
    "id": "d129b0f8-7ca1-46ff-ad26-48b4647210aa",
    "items": [],
    "pricing_rules" :[
        {
            "sku" : "A",
            "price" : "1",
            "offer" : {"quantity":5,"total_price": 4.5}
        },
        {
            "sku" : "B",
            "price" : "10"
         
        }
    ]
}
```
#### Scan-arbitary-number-of-items 
The aim is add one or several number of items to the shopping cart.<br />
Inputs are an **Item** and a **checkoutId** which is obtained from the output of **Create-checkout** use case .<br />
Output is a List of the **Item** that currently exists in the shopping cart.
##### Example
Add one number of an item <br />
Request
```
PUT /checkout/d129b0f8-7ca1-46ff-ad26-48b4647210aa/items/B 
HTTP/1.1
Host: localhost:8080
```
Response
```
[
    {
        "sku": "B",
        "quantity": 2
    }
]
```
Add arbitrary number of an item <br />
Request
```
PUT /checkout/d129b0f8-7ca1-46ff-ad26-48b4647210aa/items/ HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{"sku":"A", "quantity":10}
```
Response
```
[
    {
        "sku": "B",
        "quantity": 2
    },
    {
        "sku": "A",
        "quantity": 10
    }
]
```
        
#### Calculate-the-total-price 
The aim is to calculate the total price.<br />
Input is **checkoutId** which is obtained from the output of **Create-checkout** use case.
Output is the total price of the shopping cart according to the prices and offers defined in **Create-checkout** use case. 
##### Example
Request
```
GET /checkout/d129b0f8-7ca1-46ff-ad26-48b4647210aa/total-price 
HTTP/1.1
Host: localhost:8080
```
Response
```
29.0
```
###

### Next Improvements
#### Business related 
+ Return whole shopping cart items with total price in the response of **Calculate-the-total-price**
+ As like a real *supermarket*, after scanning each item, the total amount is displayed to the customer. Then in the output of **Scan-arbitary-number-of-items** we could return whole items with total price for each item and total price of the shopping cart and maybe total discount that customer earns from this shopping.       
By this change, at the end of each **Scan-arbitary-number-of-items** call, total price is calculated, persisted and returned to the client. In this case most of the time there is no need to call **Calculate-the-total-price** as anything is returned in the response of each **Scan-arbitary-number-of-items**. We also should consider the differences between shopping at a real supermarket and an online supermarket which customers can pay a long time after creating the shopping cart.<br />
+ Add endpoint in order to remove items from then shopping cart.


#### Technical related
+ Seprating completely request and response dto from model and add correspondings mappers.
+ Use more specific error message in responses beisdes http status codes.
+ Add more logs.
+ More clear validation error messages to distinguish invalid checkoutId and invalid SKU.
+ Cleanup db before and after running integration tests.
+ Checking duplicated products in pricingRules.
+ Defining some error message, context urls in Constant classes.
