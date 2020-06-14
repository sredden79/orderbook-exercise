package com.bidfx.orderbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceLevelDetails {

    // Price => to level container
    private static Map<Double, PriceLevelDetails> processingOrderLevel = new HashMap<>();
    private static Map<Long,Order> ActiveOrders = new HashMap<>();
    // Order Id to Level
    private static Map<Order, PriceLevelDetails> previousOrderLevelMap = new HashMap<>();

    // orders to be filled from position 0;
    private List<Order> orderMatchingOrder = new ArrayList<>();
    public final Double pricePoint;
    private long totalRemainingSize;

    private PriceLevelDetails(Double pricePoint) {
        this.pricePoint = pricePoint;
    }

    public static Map<String, Object> updateOrderBookWith(Order currentOrderDetails)
    {

        Order previousOrderDetails = null;
        if (ActiveOrders.containsKey(currentOrderDetails.getGeneratedID()))
        {
            previousOrderDetails = ActiveOrders.get(currentOrderDetails.getGeneratedID());
        }
        else
        {
            ActiveOrders.put(currentOrderDetails.getGeneratedID(),currentOrderDetails);
        }

        Double orderPrice = currentOrderDetails.getPrice();
        PriceLevelDetails latestPricePoint = processingOrderLevel.getOrDefault(orderPrice, new PriceLevelDetails(orderPrice));
        latestPricePoint.addOrUpdateOrder(currentOrderDetails, previousOrderDetails);





    }

    private void addOrUpdateOrder(Order currentOrderDetails, Order previousOrderDetails) {

        if (previousOrderDetails != null)
        {
            if (previousOrderDetails.getPrice() != currentOrderDetails.getPrice())
            {
                PriceLevelDetails previousLevelForOrder = previousOrderLevelMap.get(previousOrderDetails);
                previousLevelForOrder.removeOrder(previousOrderDetails);
            }
        }
        else
        {
            orderMatchingOrder.add(currentOrderDetails);
            this.totalRemainingSize = this.totalRemainingSize + currentOrderDetails.getSize();
            previousOrderLevelMap.put(currentOrderDetails,this);

        }

    }

    private static void removeOrder(Order previousOrderDetails) {
    }

    private double getSignedPrice(Order order) {
        int signMultipler = 1;
        if (order.getSide() == Side.BID)
        {
            signMultipler = -1;
        }
        return order.getPrice() * signMultipler;
    }


}
