package com.bidfx.orderbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Level {

    // orders to be filled from position 0;
    private List<Order> orderMatchingOrder = new ArrayList<>();
    public final String pricePoint;
    private long totalRemainingSize;

    Level(String pricePoint) {
        this.pricePoint = pricePoint;
    }

    /**
     * TODO : Write Java Doc for previousOrderDetails
     * @param previousOrderDetails
     */
    public void removeOrder(Order previousOrderDetails) {
    }


    /**
     * TODO : Write Java Doc
     *
     * @param liveOrderDetails
     * @param currentOrderDetails
     * @return the updated Order
     */
    public Order addOrUpdateOrder(Order liveOrderDetails, Order currentOrderDetails) {
        Order processingOrder = null;

        if (currentOrderDetails != null)
        {
            // handle diffs

            // Then
            currentOrderDetails.updateOrder(liveOrderDetails);
            processingOrder = currentOrderDetails;
        }
        else
        {
            processingOrder = currentOrderDetails;
        }

        // TODO : ? Processing

        return processingOrder;
    }

    // Might use for determining what the current level is for
    private double getSignedPrice(Order order) {
        int signMultipler = 1;
        if (order.getSide() == Side.BID)
        {
            signMultipler = -1;
        }
        return order.getPrice() * signMultipler;
    }

}
