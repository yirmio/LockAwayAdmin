package com.yirmio.lockawayadmin.Utils;

import com.parse.ParseFile;
import com.yirmio.lockawayadmin.BL.Order;
import com.yirmio.lockawayadmin.BL.OrderStatusEnum;

/**
 * Created by oppenhime on 07/12/2015.
 */
public class OrdersListRawItem {


    private final int totalItems;
    private float price;
    private String lable;
    private float timeToMake;
    private ParseFile photoParseFile;
    private String info;
    private boolean veg;
    private boolean glotenFree;
    private String id;
    private OrderStatusEnum orderStatusEnum;
    private String userName;
    private String clientETA;

    public OrdersListRawItem(Order item) {
        this.id = item.getOrderID();
        this.info = item.getInfo();
        this.timeToMake = item.getTimeToMakeAllOrder();
        this.price = item.getTotalPrice();
        this.totalItems = item.getTotalItems();
        this.orderStatusEnum = item.getOrderStatusEnum();
        this.userName = item.getUserName();
        this.clientETA = item.getClientETA();
    }

public OrderStatusEnum getOrderStatusEnum(){return this.orderStatusEnum;}
    public float getPrice() {
        return price;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public float getTimeToMake() {
        return timeToMake;
    }

    public void setTimeToMake(int timeToMake) {
        this.timeToMake = timeToMake;
    }

    public ParseFile getPhotoParseFile() {
        return photoParseFile;
    }

    public void setPhotoParseFile(ParseFile photoParseFile) {
        this.photoParseFile = photoParseFile;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isVeg() {
        return veg;
    }

    public void setVeg(boolean veg) {
        this.veg = veg;
    }

    public boolean isGlotenFree() {
        return glotenFree;
    }

    public void setGlotenFree(boolean glotenFree) {
        this.glotenFree = glotenFree;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalItems() {
        return this.totalItems;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getClientETA() {
        return this.clientETA;
    }
}
