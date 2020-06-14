package com.bidfx.orderbook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;
import java.util.TreeMap;

import com.bidfx.TestStatusTags;
import org.junit.jupiter.api.*;

/**
 * Unit test for {@link OrderHandler} class.
 *
 * @author BidFX Systems Limited
 */
class OrderHandlerTest {
    private OrderHandler orderHandler;

    @BeforeEach
    void setUp() {
        orderHandler = new OrderHandler();
    }

    @AfterEach
    void cleanUp() {
        orderHandler = null;
    }

    @Test
    @DisplayName("Add an order to the order book")
    void testOne() {
        Order order = new Order("1", 729.0, 100L, Side.BID);

        Map<String, Object> changedLevels = orderHandler.handleOrder(order);
        Map<Object, Object> expected = new TreeMap<>();
        expected.put(PriceFields.BID, 729.0);
        expected.put(PriceFields.BID_SIZE, 100L);
        assertEquals(expected, changedLevels);
        assertEquals(1,orderHandler.currentActiveOrderCount());
    }

    @Test
    @DisplayName("Add a second order with the same price")
    void testTwo() {
        Order order1 = new Order("1", 729.0, 100L, Side.BID);
        Order order2 = new Order("2", 729.0, 50L, Side.BID);

        orderHandler.handleOrder(order1);
        Map<String, Object> changedLevels = orderHandler.handleOrder(order2);
        Map<Object, Object> expected = new TreeMap<>();
        expected.put(PriceFields.BID_SIZE, 150L);
        assertEquals(expected, changedLevels);
        assertEquals(2,orderHandler.currentActiveOrderCount());
    }

    @Test
    @DisplayName("Add a second order with a worse price")
    void testThree() {
        Order order1 = new Order("1", 729.0, 100L, Side.BID);
        Order order2 = new Order("2", 728.9, 50L, Side.BID);

        orderHandler.handleOrder(order1);
        Map<String, Object> changedLevels = orderHandler.handleOrder(order2);
        Map<Object, Object> expected = new TreeMap<>();
        expected.put(PriceFields.BID2, 728.9);
        expected.put(PriceFields.BID_SIZE2, 50L);
        assertEquals(expected, changedLevels);
        assertEquals(2,orderHandler.currentActiveOrderCount());
    }


    @Test
    @DisplayName("Add a second order with a better price")
    void testFour() {
        Order order1 = new Order("1", 729.0, 100L, Side.BID);
        Order order2 = new Order("2", 729.1, 50L, Side.BID);

        orderHandler.handleOrder(order1);
        Map<String, Object> changedLevels = orderHandler.handleOrder(order2);
        Map<Object, Object> expected = new TreeMap<>();
        expected.put(PriceFields.BID, 729.1);
        expected.put(PriceFields.BID_SIZE, 50L);
        expected.put(PriceFields.BID2, 729.0);
        expected.put(PriceFields.BID_SIZE2, 100L);
        assertEquals(expected, changedLevels);
        assertEquals(2,orderHandler.currentActiveOrderCount());
    }

    @Test
    @DisplayName("Removing an order from the best level")
    void testFive() {
        Order order1 = new Order("1", 729.0, 100L, Side.BID);
        Order order2 = new Order("2", 729.0, 50L, Side.BID);
        Order order2delete = new Order("2", 729.0, 0L, Side.BID);

        orderHandler.handleOrder(order1);
        orderHandler.handleOrder(order2);
        Map<String, Object> changedLevels = orderHandler.handleOrder(order2delete);
        Map<Object, Object> expected = new TreeMap<>();
        expected.put(PriceFields.BID_SIZE, 100L);
        assertEquals(expected, changedLevels);
        assertEquals(1,orderHandler.currentActiveOrderCount());
    }

    @Test
    @DisplayName("Removing the last order from the worst price level")
    void testSix() {
        Order order1 = new Order("1", 729.0, 100, Side.BID);
        Order order2 = new Order("2", 728.9, 50, Side.BID);
        Order order2remove = new Order("2", 728.9, 0, Side.BID);

        orderHandler.handleOrder(order1);
        orderHandler.handleOrder(order2);
        Map<String, Object> changedLevels = orderHandler.handleOrder(order2remove);
        Map<String, Object> expected = new TreeMap<>();
        expected.put(PriceFields.BID2, null);
        expected.put(PriceFields.BID_SIZE2, null);
        assertEquals(expected, changedLevels);
        assertEquals(1,orderHandler.currentActiveOrderCount());
    }

    @Test
    @DisplayName("Removing the last order from the best price level")
    void testSeven() {
        Order order1 = new Order("1", 729.0, 100, Side.BID);
        Order order2 = new Order("2", 729.1, 50, Side.BID);
        Order order3 = new Order("3", 729.2, 25, Side.BID);
        Order order3delete = new Order("3", 729.2, 0, Side.BID);

        orderHandler.handleOrder(order1);
        orderHandler.handleOrder(order2);
        orderHandler.handleOrder(order3);
        assertEquals(3,orderHandler.currentActiveOrderCount());
        Map<String, Object> changedLevels = orderHandler.handleOrder(order3delete);
        Map<String, Object> expected = new TreeMap<>();
        expected.put(PriceFields.BID, 729.1);
        expected.put(PriceFields.BID_SIZE, 50L);
        expected.put(PriceFields.BID2, 729.0);
        expected.put(PriceFields.BID_SIZE2, 100L);
        // TODO : Query if BID3, and BID_SIZE3 should be set to null in this check
        assertEquals(expected, changedLevels);
        assertEquals(2,orderHandler.currentActiveOrderCount());
    }

    @Test
    @DisplayName("Removing non-last with same price")
    void testEight() {
        Order order1 = new Order("1", 729.0, 100, Side.BID);
        Order order2 = new Order("2", 729.0, 50, Side.BID);
        Order order3 = new Order("3", 729.2, 25, Side.BID);
        Order order4 = new Order("4", 728.1, 30, Side.BID);
        Order order3delete = new Order("2", 729.0, 0, Side.BID);

        orderHandler.handleOrder(order1);
        orderHandler.handleOrder(order2);
        orderHandler.handleOrder(order3);
        orderHandler.handleOrder(order4);
        assertEquals(4,orderHandler.currentActiveOrderCount());
        Map<String, Object> changedLevels = orderHandler.handleOrder(order3delete);
        Map<String, Object> expected = new TreeMap<>();

        expected.put(PriceFields.BID_SIZE2, 100L);
        expected.put(PriceFields.BID3, 728.1);
        expected.put(PriceFields.BID_SIZE3, 30L);

        assertEquals(expected, changedLevels);
        assertEquals(3,orderHandler.currentActiveOrderCount());
    }

    @Tags({@Tag(TestStatusTags.TO_BE_COMPLETED),@Tag(TestStatusTags.WAITING_REQUIREMENTS)})
    @Disabled
    @Test
    @DisplayName("Amend the second order quantity to be greater")
    void testNine() {
        Order order1 = new Order("1", 729.0, 100L, Side.BID);
        Order order2 = new Order("2", 728.9, 50L, Side.BID);
        // Same Level price as order 2
        Order order3 = new Order("3", 728.9, 200L, Side.BID);
        Order order2update = new Order("2", 728.9, 150L, Side.BID);

        orderHandler.handleOrder(order1);
        orderHandler.handleOrder(order2);
        orderHandler.handleOrder(order3);
        assertEquals(3,orderHandler.currentActiveOrderCount());

        Map<String, Object> changedLevels = orderHandler.handleOrder(order2update);
        Map<Object, Object> expected = new TreeMap<>();
        expected.put(PriceFields.BID_SIZE2, 350L);
        assertEquals(3,orderHandler.currentActiveOrderCount());

        // TODO : Failing with Expected :{BidSize2=350}; Actual   :{Bid2=728.9, BidSize2=150} due to code not dealing with order size updates

        assertEquals(expected, changedLevels);
    }

    @Test
    @DisplayName("Check behaviour when no orders have yet to be sent")
    void testTen() {

        Order order1 = new Order("1", 729.0, 100L, Side.BID);
        Order order2 = new Order("2", 728.9, 50L, Side.BID);
        Order removeOrder1 = new Order("1", 729.0, 0L, Side.BID);
        Order removeOrder2 = new Order("2", 728.9, 0L, Side.BID);

        assertNull(orderHandler.publishChangedLevels());
        assertEquals(0,orderHandler.currentActiveOrderCount());

        orderHandler.handleOrder(order1);
        orderHandler.handleOrder(order2);
        orderHandler.handleOrder(removeOrder1);
        orderHandler.handleOrder(removeOrder2);

        Map<String, Object> expected = new TreeMap<>();
        expected.put(PriceFields.BID, null);
        expected.put(PriceFields.BID_SIZE, null);
        assertEquals(expected, orderHandler.publishChangedLevels());
        assertEquals(0,orderHandler.currentActiveOrderCount());

    }

    @Tag(TestStatusTags.WAITING_REQUIREMENTS)
    @Test
    @DisplayName("Attempt to remove the same order")
    void testEleven() {

        Order order1 = new Order("1", 729.0, 100L, Side.BID);
        Order order2 = new Order("2", 728.9, 50L, Side.BID);
        Order removeOrder1 = new Order("1", 729.0, 0L, Side.BID);

        orderHandler.handleOrder(order1);
        orderHandler.handleOrder(order2);
        Map<String, Object> deltaWhenOrderExisted = orderHandler.handleOrder(removeOrder1);

        Map<String, Object> expected = new TreeMap<>();
        expected.put(PriceFields.BID, 728.9);
        expected.put(PriceFields.BID_SIZE, 50L);
        // TODO : Confirm req if lower levels need to be cleared, I would say so, or just leave the gaps null without shuffling
//        expected.put(PriceFields.BID2, null);
//        expected.put(PriceFields.BID_SIZE2, null);
        assertEquals(expected, orderHandler.publishChangedLevels());

        Map<String, Object> deltaWhenOrderAlreadyDeleted = orderHandler.handleOrder(removeOrder1);
        assertEquals(0,deltaWhenOrderAlreadyDeleted.size());
    }


}