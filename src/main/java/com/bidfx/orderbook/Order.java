/*
 * Copyright (c) 2018. BidFX Systems Limited. All rights reserved.
 */

package com.bidfx.orderbook;


import java.math.BigDecimal;

/**
 * Represents an order coming in from a remote price source. This order can be
 * either a buy or sell order.
 *
 * @author BidFX Systems Limited
 */

public class Order {
    private final String orderId;
    private final Side side;

    private String price;
    private long size;
    private long executedSize =0;
    private static long idCounter = 0;


    Order(String price, long size, Side side) {
        this.orderId = Long.toString(this.getNextID());
        this.price = price;
        this.size = size;
        this.side = side;
    }

    Order(String orderId, double price, long size, Side side) {
        this.orderId = orderId;
        this.size = size;
        this.side = side;

        // Converted price to string for better memory management
        this.price = Double.toString(price);;
    }

    private synchronized long getNextID()
    {
        return ++idCounter;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getSPrice()
    {
        return price;
    }

    public double getPrice() {
        return Double.valueOf(price);
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


    public void updateOrder(Order currentOrderDetails) {
        this.price = currentOrderDetails.price;
        this.size = currentOrderDetails.size;
    }
}
