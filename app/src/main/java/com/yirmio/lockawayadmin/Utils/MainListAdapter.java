package com.yirmio.lockawayadmin.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yirmio.lockawayadmin.BL.Order;
import com.yirmio.lockawayadmin.BL.OrderStatusEnum;
import com.yirmio.lockawayadmin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oppenhime on 07/12/2015.
 */
public class MainListAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList ordersList;

    public MainListAdapter(Context context, int resource, ArrayList ordersList) {
        super(context, resource, ordersList);
        this.context = context;
        this.ordersList = ordersList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View viewToUse = null;
        OrdersListRawItem item = new OrdersListRawItem((Order) getItem(position));
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        //First time view
        if (convertView == null){
            viewToUse = mInflater.inflate(R.layout.menu_item_row_layout, null);
            holder = new ViewHolder();
            setViewItems(holder, viewToUse, parent);    //Connect UI to holder properties
            //TODO - buttons action here

            viewToUse.setTag(holder);
        }
        //Not first time
        else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        putDataInViewHolder(holder, item);




        //return super.getView(position, convertView, parent);
        return viewToUse;
    }

    private void putDataInViewHolder(ViewHolder holder, OrdersListRawItem item) {
        //TODO - put data in view
        if (item.getId() != null){
            holder.orderID = item.getId();
            if (item.getPrice() > 0){

            }
            if (item.getInfo() != null){

            }
            if (item.getTimeToMake() > 0){
                holder.txtVwTotalTimeToMakeValue.setText(String.valueOf(item.getTimeToMake()));
            }
            if (item.getTotalItems() > 0){
                holder.txtVwTotalItemsValue.setText(item.getTotalItems());
            }
            if (item.getOrderStatusEnum() != null) {
                switch (item.getOrderStatusEnum()) {
                    //TODO =- set icons
                    case Active:
                        break;
                    case OnHold:
                        break;
                    case OnDelay:
                        break;
                    case OnMake:
                        break;
                    case Ready:
                        break;
                    case WaitingToStart:
                        break;
                    case Finish:
                        break;
                }
            }
        }
    }

    private void setViewItems(ViewHolder holder, View viewToUse, ViewGroup parent) {
        holder.btnDone = (Button) viewToUse.findViewById(R.id.buttonDone);
        holder.txtVwETAValue = (TextView) viewToUse.findViewById(R.id.textViewETAValue);
        holder.txtVwTotalItemsValue = (TextView) viewToUse.findViewById(R.id.textViewTotalItemsInOrderValue);
        holder.txtVwTotalTimeToMakeValue = (TextView) viewToUse.findViewById(R.id.textViewTimeToMakeValue);
        holder.txtVwTimeToStartMakingOrder = (TextView) viewToUse.findViewById(R.id.textViewTimeToStartMakingOrderValue);
        holder.imgVwStatusIcon = (ImageView) viewToUse.findViewById(R.id.imageViewUserStatus);
    }

    private class ViewHolder {
        Button    btnDone;
        TextView  txtVwETAValue;
        TextView  txtVwTotalItemsValue;
        TextView  txtVwTotalTimeToMakeValue;
        TextView  txtVwTimeToStartMakingOrder;
        ImageView imgVwStatusIcon;
        String orderID;
    }
}





//    LayoutInflater inflater = (LayoutInflater) context
//            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
//    TextView textView = (TextView) rowView.findViewById(R.id.label);
//    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
//textView.setText(values[position]);
//        // change the icon for Windows and iPhone
//        String s = values[position];
//        if (s.startsWith("iPhone")) {
//        imageView.setImageResource(R.drawable.no);
//        } else {
//        imageView.setImageResource(R.drawable.ok);
//        }
//
//        return rowView;