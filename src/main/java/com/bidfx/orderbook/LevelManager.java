package com.bidfx.orderbook;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LevelManager {

    // Price ==>> to level container
    private static Map<String, Level> priceLevelMap = new HashMap<>();
    // OrderId ==> Order
    private static Map<String, Order> ActiveOrders = new HashMap<>();
    // Order ==> to current level container
    private static Map<Order, Level> liveOrderLevelMap = new HashMap<>();

    public static Map<String, Object> updateOrderBookWith(Order newOrderDetails)
    {
        Map<String, Object> changesForOrderUpdate = new HashMap<String, Object>();

        Level priceOrderLevel = priceLevelMap.getOrDefault(newOrderDetails.getSPrice(),
                new Level(newOrderDetails.getSPrice()));

        Order liveOrderDetails = null;
        if (ActiveOrders.containsKey(newOrderDetails.getOrderId()))
        {
            liveOrderDetails = ActiveOrders.get(newOrderDetails.getOrderId());
            Level liveLevelForOrder = liveOrderLevelMap.get(liveOrderDetails);
            if (priceOrderLevel != liveLevelForOrder) {
                changesForOrderUpdate.putAll(liveLevelForOrder.removeOrder(liveOrderDetails));
            }
            //TODO : Need to think about when to update the existing order details
            // because you might need to use the differences to know whats changed
            // between orders so that a full recalculation of the current level
            // can be avoided
            // liveOrderDetails.updateOrder(newOrderDetails);
        }
        else
        {
            ActiveOrders.put(newOrderDetails.getOrderId(),liveOrderDetails);
            liveOrderLevelMap.put(newOrderDetails,priceOrderLevel);
        }

        priceOrderLevel.addOrUpdateOrder(liveOrderDetails,newOrderDetails);

        return null;

    }

    private void addOrUpdateOrder(Order currentOrderDetails, Order previousOrderDetails) {



    }

}
