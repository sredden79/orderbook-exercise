/*
 * Copyright (c) 2018. BidFX Systems Limited. All rights reserved.
 */

package com.bidfx.orderbook;

import java.util.Map;

/**
 * This class adapts orders coming from a remote price service and builds the
 * {@link OrderBook}. On receipt of a new order it publishes changes to the
 * order book as a map.
 *
 * @author BidFX Systems Limited
 */
@SuppressWarnings("all")
public class OrderHandler {
    private OrderBook orderBook = new OrderBook();

    public Map<String, Object> handleOrder(Order order) {
        // TODO: Handle incoming order
        return publishChangedLevels();
    }

    private Map<String, Object> publishChangedLevels() {
        // TODO: Publish a price map containing what price levels have changed
        return null;
    }
}
