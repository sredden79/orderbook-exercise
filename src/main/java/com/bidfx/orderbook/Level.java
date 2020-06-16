package com.bidfx.orderbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Level {

    private Side currentlevelSide;
    private final String SIZE_TAG = "_SIZE";

    // orders to be filled from position 0;
    private List<Order> orderMatchingOrder = new ArrayList<>();
    public final String pricePoint;
    private long totalRemainingSize=0;
    private int levelNumber =0;

    private Map<String, Object> changesToLevel;

    Level(String pricePoint) {

        this.pricePoint = pricePoint;
        // TODO : Think about determining dynamically based on remaining size
        // ie BID = - size, ASK = + size
        currentlevelSide = Side.BID;
    }

    /**
     * TODO : Write Java Doc for previousOrderDetails
     * @param orderToRemove
     */
    public synchronized  Map<String, Object> removeOrder(Order orderToRemove) {

        changesToLevel = new HashMap<>();

        if (orderMatchingOrder.contains(orderToRemove))
        {
            orderMatchingOrder.remove(orderToRemove);
        }
        else
        {
            return changesToLevel;
        }

        long updatedRemainingSize = totalRemainingSize - orderToRemove.getRemainingSize();

        if (updatedRemainingSize > 0)
        {
            changesToLevel.put(currentlevelSide+SIZE_TAG+levelNumber,updatedRemainingSize);
        }

        if (updatedRemainingSize == 0)
        {
            changesToLevel.put(currentlevelSide.toString()+levelNumber,null);
            changesToLevel.put(currentlevelSide+SIZE_TAG+levelNumber,null);
        }

        totalRemainingSize = updatedRemainingSize;

        return changesToLevel;

    }


    /**
     * TODO : Write Java Doc
     *
     * @param liveOrderDetails
     * @param currentOrderDetails
     * @return the updated Order
     */
    public Map<String, Object> addOrUpdateOrder(Order liveOrderDetails, Order currentOrderDetails) {
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

        return null;
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
