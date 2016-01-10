package com.yirmio.lockawayadmin.Utils;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.yirmio.lockawayadmin.BL.Order;
import com.yirmio.lockawayadmin.BL.OrderStatusEnum;
import com.yirmio.lockawayadmin.DAL.ParseConnector;

import java.util.ArrayList;

/**
 * Created by oppenhime on 30/12/2015.
 */
public class LockAwayAdminApplication extends Application {

    private static String restID = "g1bzMQEXoj";
    private static ArrayList<Order> allOrders;
    //public static OrderStatusEnum staticOrderStatusEnum;

    public static String getRestID() {
        return restID;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);


        // Add your initialization code here
        Parse.initialize(this, "i6hUPoLJOlkH8j0p4nB3q1r2x18Kbr2SHlKocuua", "32VPtaO5T2Rg62uZWO3i9x4jr9mfxOKePlSL0rlW");
        ParseInstallation.getCurrentInstallation().saveInBackground();


        ParseUser usr = null;
        try {
            usr = ParseUser.logIn("Yirmi", "Z69Hus&&");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL(usr);
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


        allOrders = new ArrayList<Order>();
//        this.refreshOrders();
        //TODO - log to server
    }

    private void refreshOrders() {
        ArrayList<ParseObject> orders = ParseConnector.getActiveOrders(restID);
        for (ParseObject order : orders) {
            allOrders.add(new Order(order));

        }
    }

    public static Boolean refreshOrdersList() {
        //TODO do it better
        boolean hasNewOrders = true;
        ArrayList<ParseObject> orders = ParseConnector.getActiveOrders(restID);
        allOrders.clear();
        for (ParseObject order : orders) {
            allOrders.add(new Order(order));

        }





        return hasNewOrders;
    }
}
