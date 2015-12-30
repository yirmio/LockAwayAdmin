package com.yirmio.lockawayadmin.BL;

import android.text.format.Time;

import com.parse.ParseObject;
import com.yirmio.lockawayadmin.DAL.ParseConnector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by oppenhime on 07/12/2015.
 */
public class Order {
    private String clientName;
    private String clientID;
    private float totalPrice;
    private List<RestaurantMenuObject> itemsByOrderToMake;
    private Time timeToBeReady;
    private float timeToMakeAllOrder;
    private String orderId;

    public Order(ParseObject parseOrder) {
        this.orderId = parseOrder.getObjectId();
        this.clientID = parseOrder.getString("UserID");
        this.itemsByOrderToMake = this.convertParseMenuObjectsToLocalBL(ParseConnector.getObjectsByOrderID(this.orderId));

        //TODO - implement time converting from parse to string or android time


    }

    private List<RestaurantMenuObject> convertParseMenuObjectsToLocalBL(List<ParseObject> parseObjects) {
//TODO implement converting
        return null;
    }

    public OrderStatusEnum getOrderStatusEnum() {
        return orderStatusEnum;
    }

    public void setOrderStatusEnum(OrderStatusEnum orderStatusEnum) {
        this.orderStatusEnum = orderStatusEnum;
    }

    private OrderStatusEnum orderStatusEnum;


    public Order(String clientName, Time timeToBeReady) {
        this.clientName = clientName;
        this.timeToBeReady = timeToBeReady;
        this.totalPrice = 0;
        this.itemsByOrderToMake = new ArrayList<RestaurantMenuObject>();
        this.timeToMakeAllOrder = 0;

    }
    public Order(String orderID){

    }





    public String getClientName() {
        return clientName;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public List<RestaurantMenuObject> getItemsByOrderToMake() {
        return itemsByOrderToMake;
    }

    public Time getTimeToBeReady() {
        return timeToBeReady;
    }

    public float getTimeToMakeAllOrder() {
        return timeToMakeAllOrder;
    }
    


}
