# Orderbook exercise
## Introduction
This programming exercise is based on a real-world pricing example. When people trade shares on an exchange they have two options, to buy or sell. If they are buying shares, they specify a price that they would be willing to buy at and an amount they want to buy (bid price and bid size). If they are selling shares they specify the price that they want to sell at and an amount that they want to sell (ask price and ask size). An example bid might be 2000 shares at $729.5 per share. These prices always come in discrete levels with a minimum size between the two such as 729.5, 729.4, etc.

When you have multiple people buying or selling, these prices can be put into a table, sorted with the best price first. For a sell, this will be the lowest ask price and for a buy this will be the highest bid. If more than one person places an order (bid or ask) at the same price, the total quantity of the two orders will be summed. In this way we build up an order book, where each level shows the price and total amount of shares at that price.

An example is given below:

|BidSize|Bid|Ask|AskSize|Level|
|:------|:-------|-------:|------:|:---:|
|13879|729.50|729.55|27629|1|
|29792|729.45|729.60|59545|2|
|74755|729.40|729.65|103330|3|
|80173|729.35|729.70|99191|4|
|59933|729.30|729.75|75027|5|
|30281|729.25|729.80|49785|6|
|23425|729.20|729.85|29216|7|
|66460|729.15|729.90|16043|8|
|61481|729.10|||9|
|15331|729.05|||10|

The object of this exercise is to build and then publish an order book based on the messages we receive from our price provider. In this case, the provider sends message in the form of a map. An example of the message is shown below:
```
"TimeOfUpdate":1379003683275L, "OrderID": 1, "BidSize": 46, "Bid": 729.40
```
In this example the message contains a bid price of 729.40 and size of 46 along with an order ID, that is unique and will not be re-used by another order and an epoch long timestamp.

When publishing price updates we use a delta approach, where only change information is sent to the client on an update. If we received these two messages, then we would publish both bid size and price after processing the first message
### Pricing update from provider
```
"TimeOfUpdate":1379003683275L, "OrderID": 1, "BidSize": 46, "Bid": 729.40
"TimeOfUpdate":1379003683276L, "OrderID": 2, "BidSize": 20, "Bid": 729.40
```
### Price update to client
```java
    new Map<>("BidSize", "46", "Bid", "729.40:);
    new Map<>("BidSize", "66"); // Add bid sizes together and report to client
```
