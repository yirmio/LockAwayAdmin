package com.yirmio.lockawayadmin.DAL;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.yirmio.lockawayadmin.BL.Order;
import com.yirmio.lockawayadmin.BL.OrderStatusEnum;
import com.yirmio.lockawayadmin.BL.RestaurantMenuObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yirmio on 19/07/2015.
 */
public final class ParseConnector {
    //DB Attributes
    private static final String PHOTO_FILE_ATTR = "PhotoFile";
    private static final String MENU_OBJECT_ATTRIBUTE = "MenuObjects";
    private static final String STORE_ID_ATTRIBUTE = "StoreID";
    private static final String ORDERS_ATTR = "UserToOrders";


    private static ParseObject rest;
    private static String TAG = "In ParseConnector Class";
    private static boolean tmpResult = false;

    public static ArrayList getActiveOtdersBL(String resturantID) {
        ArrayList<Order> resBL = new ArrayList();
        ArrayList<ParseObject> resParse = getActiveOrders(resturantID);
        Order tmpOrder;
        if (resParse != null) {
            for (ParseObject pO : resParse) {
                tmpOrder = new Order(pO);
                resBL.add(tmpOrder);
            }
        }
        return resBL;
    }

    public static ArrayList getActiveOrders(String resturantID) {
        ArrayList res = null;
        ParseQuery query = ParseQuery.getQuery(ORDERS_ATTR);
        query.whereEqualTo("ResturantID", resturantID);
        query.whereNotEqualTo("OrderStatus", "done");
        query.whereNotEqualTo("OrderStatus", "Active");
        query.whereNotEqualTo("OrderStatus", "Finish");

        try {
            List<ParseObject> objects = query.find();
            if (objects.size() > 0) {
                res = new ArrayList();
                for (ParseObject p : objects) {
                    res.add(p);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return res;
    }

    public static boolean removeObjectFromOrder(final String orderID, final String objectID) {
        boolean res = true;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("OrderedObjects");
        query.whereEqualTo("MenuObjectID", objectID);
        try {
            String tmpID;
            List<ParseObject> objects = query.find();
            for (ParseObject p : objects) {
                tmpID = p.getString("OrderID");
                if (tmpID.equals(orderID)) {
                    p.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            //Success
                            if (e == null) {
                                Log.i(TAG, "Object " + objectID + " Removed from order " + orderID);

                            } else {
                                Log.e(TAG, "Error reoving object " + objectID + " From order " + orderID);
                                //throw new ParseException(9,"Error reoving object " + objectID + " From order " + orderID);                            }
                            }
                        }
                    });
                }
            }


        } catch (ParseException e) {
            res = false;
        }


        return res;

    }

    public static boolean setRestaurantID(String id) {
        tmpResult = false;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Stores");
        query.whereEqualTo("objectId", id);
        try {
            List<ParseObject> stores = query.find();
            rest = stores.get(0);
            Log.i(TAG, "Get restaurant: " + rest.getString("Name"));
            Log.i(TAG, "Get restaurant: " + rest.getString("Name"));
            tmpResult = true;
        } catch (ParseException e) {
            Log.e(TAG, "Error: " + e.getMessage());
            tmpResult = false;
        }
        /*query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> stores, ParseException e) {
                if (e == null) {
                    rest = stores.get(0);
                    Log.i(TAG, "Get restaurant: " + rest.getString("Name"));
                    tmpResult = true;
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                    tmpResult = false;
                }
            }
        });*/


        return tmpResult;
    }

    public List<ParseFile> getImagesFilesForObject(String objID, final int maxImages) {
        ArrayList<ParseFile> imagesArr = new ArrayList<ParseFile>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MenuPhotos");
        query.whereEqualTo("MenuObjectID", objID);
        try {
            List<ParseObject> objs = query.find();
            if (objs.size() > 0) {
                for (ParseObject obj : objs) {
                    //Up To maxImages files
                    if (imagesArr.size() < maxImages) {
                        imagesArr.add(getParseFileFromParseObject(obj, PHOTO_FILE_ATTR));
                    }
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
       /* query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                //No Error
                if (e == null){
                    //Not empty list
                    if (list.size() > 0){
                        for (ParseObject obj :list) {
                            //Up To maxImages files
                            if (imagesArr.size() < maxImages){
                                imagesArr.add(getParseFileFromParseObject(obj,PHOTO_FILE_ATTR));
                            }
                        }

                    }
                }
            }
        });*/

        return imagesArr;

    }

    private static ParseFile getParseFileFromParseObject(ParseObject object, String attribute) {
        ParseFile res;
        try {
            res = object.getParseFile(attribute);
        } catch (Exception ex) {
            Log.e(TAG, "Cant get file for object: " + object.getObjectId());
            return null;
        }
        return res;
    }

    public static List<RestaurantMenuObject> getObjectsByOrderID(String orderId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("OrderedObjects");//TODO Const
        query.whereEqualTo("OrderID", orderId);//TODO use const
        final List<ParseObject> listRes = new ArrayList<>();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject pObj : objects) {
                            listRes.add(pObj);
                        }
                    }

                }


            }


        });

        if (listRes != null) {
            List<RestaurantMenuObject> menuObjects = new ArrayList<RestaurantMenuObject>();
            ParseObject tmpParseObject = null;
            //Get Full Object
            for (ParseObject pObj : listRes) {
                tmpParseObject = getMenuObjectByID(pObj.getString("MenuObjectID")); //TODO use const
                menuObjects.add(CreateMenuItemFromParseObject(tmpParseObject));
            }
            return menuObjects;

        } else {
            return null;
        }

    }

    private static ParseObject getMenuObjectByID(String menuObjectID) {
        final ParseObject[] tmpParseObject = {null};
        List<ParseObject> tmpRes = null;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("MenuObjects"); //TODO use const
        query.getInBackground(menuObjectID, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    tmpParseObject[0] = object;
                } else {
//                            objectRetrievalFailed();
                }
            }
        });

        return tmpParseObject[0];
    }

    public static void setOrderStatus(String orderID, OrderStatusEnum orderStatusEnum) {
        ParseObject tmpOrder = getOrderByID(orderID);
        tmpOrder.put("OrderStatus", orderStatusEnum.toString());
        tmpOrder.saveInBackground();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("orderID",orderID);

        ParseCloud.callFunctionInBackground("sendPushAfterOrderStatusDone", map);

    }

    private static ParseObject getOrderByID(String orderID) {
        ParseQuery query = ParseQuery.getQuery(ORDERS_ATTR);
        ParseObject resToReturn = null;
        query.whereEqualTo("objectId", orderID);
        try {
            List<ParseObject> res = query.find();
            //TODO - check if first item is good......
            resToReturn = res.get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resToReturn;
    }

    public static String getUserNameFromID(String clientID) {
        ParseQuery query = ParseUser.getQuery();
        ParseObject res = null;
        String username = null;
        query.whereEqualTo("objectId", clientID);
        try {
            List<ParseUser> result = query.find();
            username = result.get(0).getUsername();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return username;
    }

//    public RestaurantMenu getMenu() {
//        final RestaurantMenu restaurantMenuResult = new RestaurantMenu();
//        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(MENU_OBJECT_ATTRIBUTE);
//        query.whereEqualTo(STORE_ID_ATTRIBUTE, rest.getObjectId());
//        try {
//            List<ParseObject> list = query.find();
//            if (list.size() > 0) {
//                for (ParseObject obj : list) {
//                    restaurantMenuResult.addItemToMenu(CreateMenuItemFromParseObject(obj));
//                }
//            }
//            /*query.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> list, ParseException e) {
//                    //No Error
//                    if (e == null){
//                        for (ParseObject obj:list) {
//                            restaurantMenuResult.addItemToMenu(CreateMenuItemFromParseObject(obj));
//                        }
//
//                    }
//                    else {
//                        Log.d(TAG, "Error: " + e.getMessage());
//                    }
//                }
//            });*/
//
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }
//        return restaurantMenuResult;
//    }

    //Create MenuItem From ParseObject
    public static RestaurantMenuObject CreateMenuItemFromParseObject(ParseObject obj) {
        try {
            //TODO use const not strings
            float price = obj.getNumber("Price").floatValue();
            String title = obj.getString("Name");
            int timeToMake = obj.getNumber("TimeToMake").intValue();
            boolean isVeg = obj.getBoolean("Veg");
            boolean isAvaliable = obj.getBoolean("IsAvaliable");
            String storeID = obj.getString("StoreID");
            String description = obj.getString("Description");
            boolean isGlotenFree = obj.getBoolean("GlotenFree");
            String id = obj.getObjectId();
            String type;
            //TODO handle real type
            if (obj.getParseObject("Type") != null) {
                type = (obj.getParseObject("Type")).getString("TypeName");
            } else {
                type = "No Type";
            }
            boolean isReady = false;

//            //TODO - handle more than one image per object
//            List<ParseFile> tmpFilesArray = getImagesFilesForObject(obj.getObjectId(), 1);
//            ParseFile tmpFile = null;
//            if (tmpFilesArray != null) {
//                if (tmpFilesArray.size() > 0) {
//                    tmpFile = getImagesFilesForObject(obj.getObjectId(), 1).get(0);
//                }
//            }
            return new RestaurantMenuObject(id, description, price, title, timeToMake, type, isVeg, isGlotenFree);
//            return new RestaurantMenuObject(id, description, price, title, timeToMake, tmpFile, type, isVeg, isGlotenFree);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }


    }

//    public static boolean addObjectToOrder(String id, String orderID) {
//        ParseObject tmpItem = new ParseObject("OrderedObjects");
//
//        tmpItem.put("MenuObjectID", id);
//        tmpItem.put("OrderID", orderID);
//        tmpItem.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    //TODO - Log...
//                }
//            }
//        });
//        return false;
//    }
}
