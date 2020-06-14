/*
 * Copyright (c) 2018. BidFX Systems Limited. All rights reserved.
 */

package com.bidfx.orderbook;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** 
 * This class adapts orders coming from a remote price service and builds the
 * {@link OrderBook}. On receipt of a new order it publishes changes to the
 * order book as a map.
 *
 * @author BidFX Systems Limited
 */

public class OrderHandler {

    private OrderBook orderBook = new OrderBook();
    private Set<String> numberOfActiveOrders = new HashSet<>();

    /**
     * Updates the orderBook for the order details provided
     * @param order the order to update the book with
     * @return a Map containing the delta changes to the order book
     */
    public Map<String, Object> handleOrder(Order order) {
        // Handle incoming order
    	if(order.getSize() == 0){
    		orderBook.remove(order);
            numberOfActiveOrders.remove(order.getOrderId());
    	}
    	else{
    		orderBook.add(order);
            numberOfActiveOrders.add(order.getOrderId());
    	}
    	
        return publishChangedLevels();
    }

    /**
     * Returns the delta (of the changes) or
     * null if no orders have been entered at the time the function has been called
     *
     * @return The updated delta variable
     */
    public Map<String, Object> publishChangedLevels() {
        // Publish a price map containing what price levels have changed
        return orderBook.getChangedLevels();
    }

    /**
     * Returns the current active order count for this handler
     * @return
     */
    public long currentActiveOrderCount() {
        return numberOfActiveOrders.size();
    }
}
