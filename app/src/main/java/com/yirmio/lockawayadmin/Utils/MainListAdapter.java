package com.yirmio.lockawayadmin.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yirmio.lockawayadmin.BL.Order;
import com.yirmio.lockawayadmin.BL.OrderStatusEnum;
import com.yirmio.lockawayadmin.DAL.ParseConnector;
import com.yirmio.lockawayadmin.R;

import java.util.ArrayList;
import java.util.Arrays;

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
        final int pos = position;

        //First time view
        if (convertView == null) {
            viewToUse = mInflater.inflate(R.layout.single_row_layout, null);
            holder = new ViewHolder();
            setViewItems(holder, viewToUse, parent, position);    //Connect UI to holder properties


            viewToUse.setTag(holder);
        }
        //Not first time
        else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        putDataInViewHolder(holder, item);
        return viewToUse;
    }

    private void putDataInViewHolder(ViewHolder holder, OrdersListRawItem item) {
        if (item.getId() != null) {
            holder.orderID = item.getId();
            if (item.getPrice() > 0) {

            }
            if (item.getInfo() != null) {

            }
            if (item.getTimeToMake() > 0) {
                holder.txtVwTotalTimeToMakeValue.setText(String.valueOf(item.getTimeToMake()));
            }
            if (item.getTotalItems() > 0) {
                holder.txtVwTotalItemsValue.setText(String.valueOf(item.getTotalItems()));
            }
            if (item.getUserName() != null){
                holder.txtVwUserName.setText(item.getUserName());
            }
            if (item.getOrderStatusEnum() != null) {
                switch (item.getOrderStatusEnum()) {
                    case Active:
                        holder.imgVwStatusIcon.setImageResource(R.drawable.ic_action_cart);
                        break;
                    case OnHold:
                        holder.imgVwStatusIcon.setImageResource(R.drawable.ic_pause);
                        break;
                    case OnDelay:
                        holder.imgVwStatusIcon.setImageResource(R.drawable.ic_timedelay);
                        break;
                    case OnMake:
                        holder.imgVwStatusIcon.setImageResource(R.drawable.ic_build);
                        break;
                    case Ready:
                        holder.imgVwStatusIcon.setImageResource(R.drawable.ic_done);
                        break;
                    case WaitingToStart:
                        holder.imgVwStatusIcon.setImageResource(R.drawable.ic_av_waiting);
                        break;
                    case Finish:
                        holder.imgVwStatusIcon.setImageResource(R.drawable.ic_done_all);
                        break;
                    default:
                        holder.imgVwStatusIcon.setImageResource(R.drawable.ic_android);
                }
            }
        }
    }

    private void setViewItems(ViewHolder holder, View viewToUse, ViewGroup parent, int pos) {
        final int tmpPos = pos;
        //Connecting
        holder.btnDone = (Button) viewToUse.findViewById(R.id.buttonDone);
        holder.txtVwETAValue = (TextView) viewToUse.findViewById(R.id.textViewETAValue);
        holder.txtVwTotalItemsValue = (TextView) viewToUse.findViewById(R.id.textViewTotalItemsInOrderValue);
        holder.txtVwTotalTimeToMakeValue = (TextView) viewToUse.findViewById(R.id.textViewTimeToMakeValue);
        holder.txtVwTimeToStartMakingOrder = (TextView) viewToUse.findViewById(R.id.textViewTimeToStartMakingOrderValue);
        holder.imgVwStatusIcon = (ImageView) viewToUse.findViewById(R.id.imageViewUserStatus);
        holder.txtVwUserName = (TextView) viewToUse.findViewById(R.id.textViewUserName);

        //Setting Actions
        holder.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeOrderStatus(tmpPos);

            }
        });
    }

    private void changeOrderStatus(final int tmpPos) {
        final Order tmpOrder = (Order) this.ordersList.get(tmpPos);
        //Dialog
//Dialog dialog = new Dialog(this.context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pick Status");//TODO - strings.xml
        builder.setItems(OrderStatusEnum.getAllValues(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Change BL
                tmpOrder.setOrderStatusEnum(OrderStatusEnum.fromInt(i));
                //Change DAL
                ParseConnector.setOrderStatus(tmpOrder.getOrderID(), tmpOrder.getOrderStatusEnum());
                //Update UI
                notifyDataSetChanged();
            }
        });
        builder.create();
        builder.show();
    }


    private class ViewHolder {
        Button btnDone;
        TextView txtVwETAValue;
        TextView txtVwTotalItemsValue;
        TextView txtVwTotalTimeToMakeValue;
        TextView txtVwTimeToStartMakingOrder;
        TextView txtVwUserName;
        ImageView imgVwStatusIcon;
        String orderID;
    }
}