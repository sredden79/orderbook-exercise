/*
 * Copyright (c) 2018. BidFX Systems Limited. All rights reserved.
 */

package com.bidfx.orderbook;


/**
 * Represents an order coming in from a remote price source. This order can be
 * either a buy or sell order.
 *
 * @author BidFX Systems Limited
 */
@SuppressWarnings("all")
public class Order {
    private final String orderId;
    private final double price;
    private final long size;
    private final Side side;

    Order(String orderId, double price, long size, Side side) {
        this.orderId = orderId;
        this.price = price;
        this.size = size;
        this.side = side;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getPrice() {
        return price;
    }

    public long getSize() {
        return size;
    }

    public Side getSide() {
        return side;
    }

    @Override
    public String toString() {
        return "Order{" +
            "orderId='" + orderId + '\'' +
            ", price=" + price +
            ", size=" + size +
            ", side=" + side +
            '}';
    }
}
