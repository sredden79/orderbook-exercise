/*
 * Copyright (c) 2018. BidFX Systems Limited. All rights reserved.
 */

package com.bidfx.orderbook;


import java.util.HashMap;
import java.util.Map;

/**
 * Represents an order coming in from a remote price source. This order can be
 * either a buy or sell order.
 *
 * @author BidFX Systems Limited
 */

public class Order {
    private final String orderId;
    private final double price;
    private final long size;
    private final Side side;

    private long executedSize =0;
    private static long ID_COUNTER = 0;
    private final long id;


    Order(String orderId, double price, long size, Side side) {
        this.orderId = orderId;
        this.price = price;
        this.size = size;
        this.side = side;

        this.id = getNextID();
    }

    private synchronized long getNextID()
    {
        return ++ID_COUNTER;
    }


    public String getOrderId() {
        return orderId;
    }

    public long getGeneratedID()
    {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public long getSize() {
        return size;
    }

    public long getRemainingSize() {
        return size - executedSize;
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
