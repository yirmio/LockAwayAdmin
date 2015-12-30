package com.yirmio.lockawayadmin.Utils;

import com.parse.ParseFile;
import com.yirmio.lockawayadmin.BL.Order;

/**
 * Created by oppenhime on 07/12/2015.
 */
public class OrdersListRawItem {


    private float price;
    private String lable;
    private int timeToMake;
    private ParseFile photoParseFile;
    private String info;
    private boolean veg;
    private boolean glotenFree;
    private String id;

    public OrdersListRawItem(Order item) {
        //todo implement

    }


    public float getPrice() {
        return price;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public int getTimeToMake() {
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
}
