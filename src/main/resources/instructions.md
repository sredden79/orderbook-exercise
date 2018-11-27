# Instructions
You are required to implement the functionality of an order for this exercise. It is expected that
you will work in a test-driven (TDD) style by writing the code that will pass the first test, then
the second and so on.

*For simplicity you are only required to implement the bid side of the order book*

- So spend some time thinking about the key areas you will need to consider to implement the order book.

## Task 1
You will start with an empty order book and receive one order. This order needs to be adapted and 
then published.

|BidSize|Bid|Ask|AskSize|Level|
|---:|---:|:---|:---|:---:|
|100|729.0| | |1|
| | | | |2|
| | | | |3|

## Task 2
You will again start with an empty order book and receive one order; this order can be ignored as it
has already been handled in test one. A second order is received with the same price, indicating it
is to be inserted at the same price level as the first order. Once you have adapted the order, the
only detail that needs to be sent back to the client is anything that has changed. In this case only
the `BID_SIZE` has changed and only that field should be published.

|BidSize|Bid|Ask|AskSize|Level|
|---:|---:|:---|:---|:---:|
|**150**|729.0| | |1|
| | | | |2|
| | | | |3|

## Task 3
After receiving your first order a second order is received, this time at a lower price level, i.e. 
a worse price. Again you will only need to publish the detail of what has changed, this time `BID2` 
and `BID_SIZE2`. 

|BidSize|Bid|Ask|AskSize|Level|
|---:|---:|:---|:---|:---:|
|100|729.0| | |1|
|**50**|**728.9**| | |2|
| | | | |3|

## Task 4
This time after receiving your first order a second order will be received at a higher price level.
To adapt the price this time, the original order needs to be moved to a lower price level and the
new order added to the top level of the order book. In this case everything needs to be re-published
as the first order has been moved and the 

|BidSize|Bid|Ask|AskSize|Level|
|---:|---:|:---|:---|:---:|
|**50**|**729.1**| | |1|
|**100**|**729.0**| | |2|
| | | | |3|

## Task 5
After receiving two orders a second version of the order will be received, this time however it will
have a quantity of zero. A zero quantity indicates that the order has been cancelled and this change
needs to be published to the client.

|BidSize|Bid|Ask|AskSize|Level|
|---:|---:|:---|:---|:---:|
|**100**|729.0| | |1|
| | | | |2|
| | | | |3|

## Task 6
Two orders have been received and a cancel is then received for the second order. You need to publish
the removal of the second level of the order book.

|BidSize|Bid|Ask|AskSize|Level|
|---:|---:|:---|:---|:---:|
|**100**|729.0| | |1|
| | | | |2|
| | | | |3|

## Task 7
After receiving three orders, each with a better price than the previous, a cancel is received for
the last order; the current top of the book. By cancelling this order, the two orders previously
received now have to be moved back up to the top of the list and their details published to the client.

|BidSize|Bid|Ask|AskSize|Level|
|---:|---:|:---|:---|:---:|
|**50**|729.1 | | |1|
|**100**|729.0 | | |2|
| | | | |3|


#### Note
If there are any errors in these instructions, please commit a change to this document in its
own commit. This will allow only the change to this file to be cherry-picked back to the master
branch.