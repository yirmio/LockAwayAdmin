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
    private String extraInfo;
    private float totalPrice;
    private List<RestaurantMenuObject> itemsByOrderToMake;
    private Time timeToBeReady;
    private float timeToMakeAllOrder;
    private String orderId;
    private OrderStatusEnum orderStatusEnum;

    public Order(ParseObject parseOrder) {
        this.orderId = parseOrder.getObjectId();
        this.clientID = parseOrder.getString("UserID");
        this.clientName = ParseConnector.getUserNameFromID(this.clientID);
        this.extraInfo = parseOrder.getString("ExtraInfo");
//        this.itemsByOrderToMake = this.convertParseMenuObjectsToLocalBL(ParseConnector.getObjectsByOrderID(this.orderId));
        this.itemsByOrderToMake = ParseConnector.getObjectsByOrderID(this.orderId);
        this.orderStatusEnum = OrderStatusEnum.valueOf(parseOrder.getString("OrderStatus"));
        //TODO - implement time converting from parse to string or android time

        if (this.itemsByOrderToMake != null) {
            this.calcDetails();
        }
    }

    private void calcDetails() {
        float tmpSum = 0;
        float tmpTimeToMake = 0;
        for (RestaurantMenuObject o : this.itemsByOrderToMake) {
            tmpSum += o.getPrice();
            tmpTimeToMake += o.getTimeToMake();
        }
        this.totalPrice = tmpSum;
        this.timeToMakeAllOrder = tmpTimeToMake;
    }

    private List<RestaurantMenuObject> convertParseMenuObjectsToLocalBL(List<ParseObject> parseObjects) {

        RestaurantMenuObject tmpRestObj;
        if (parseObjects != null) {
            for (ParseObject o : parseObjects) {
                tmpRestObj = new RestaurantMenuObject(o.getObjectId(), o.getString("Description"), o.getNumber("Price").floatValue(), o.getString("Name")
                        , o.getNumber("TimeToMake").intValue(), o.getString("Type"), o.getBoolean("Veg"), o.getBoolean("GlotenFree"));
                this.itemsByOrderToMake.add(tmpRestObj);
            }
            return this.itemsByOrderToMake;
        }
        return null;


    }

    public OrderStatusEnum getOrderStatusEnum() {
        return orderStatusEnum;
    }

    public void setOrderStatusEnum(OrderStatusEnum orderStatusEnum) {
        this.orderStatusEnum = orderStatusEnum;
    }


    public Order(String clientName, Time timeToBeReady) {
        this.clientName = clientName;
        this.timeToBeReady = timeToBeReady;
        this.totalPrice = 0;
        this.itemsByOrderToMake = new ArrayList<RestaurantMenuObject>();
        this.timeToMakeAllOrder = 0;

    }

    public Order(String orderID) {

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


    public String getOrderID() {
        return this.orderId;
    }

    public String getInfo() {
        return this.extraInfo;
    }


    public int getTotalItems() {
        int ret = 0;
        if (this.itemsByOrderToMake != null){
            ret =  this.itemsByOrderToMake.size();
        }
        return ret;
    }

    public String getUserName() {
        return this.clientName;
    }
}
