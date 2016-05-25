package com.yirmio.lockawayadmin.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.yirmio.lockawayadmin.DAL.ParseConnector;
import com.yirmio.lockawayadmin.R;
import com.yirmio.lockawayadmin.Utils.LockAwayAdminApplication;
import com.yirmio.lockawayadmin.Utils.MainListAdapter;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {


    private ArrayList mOrderList;
    private MainListAdapter mAdapter;
    private ListView mOrdersListView;
    private Button mBtnRefreshList;
    private Button mBtnMenuEdit;
    private Button mBtnUsers;
    private Button mBtnCloseStore;

    private boolean isStoreOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOrderList = ParseConnector.getActiveOtdersBL(LockAwayAdminApplication.getRestID());
        mAdapter = new MainListAdapter(this, R.layout.single_orders_row_layout, mOrderList);
        mOrdersListView = (ListView) findViewById(R.id.listView_Orders);
        mOrdersListView.setAdapter(mAdapter);

        isStoreOpen = LockAwayAdminApplication.isStoreOpen();


        this.mBtnRefreshList = (Button) findViewById(R.id.btnRefreshList);
        this.mBtnRefreshList.setOnClickListener(this);
        this.mBtnMenuEdit = (Button) findViewById(R.id.btnGoToMenuEdit);
        this.mBtnMenuEdit.setOnClickListener(this);
        this.mBtnUsers = (Button) findViewById(R.id.btnGoToCustomersArea);
        this.mBtnUsers.setOnClickListener(this);
        this.mBtnCloseStore = (Button) findViewById(R.id.btnSetStoreStatus);
        this.mBtnCloseStore.setOnClickListener(this);
        updateCloseOpenBtn();

    }

    private void updateCloseOpenBtn() {
        if (this.isStoreOpen){
            this.mBtnCloseStore.setText(R.string.close_store);
            this.mBtnCloseStore.setBackgroundColor(Color.RED);
        }
        else {
            this.mBtnCloseStore.setText(R.string.open_store);
            this.mBtnCloseStore.setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGoToCustomersArea:
                Intent custIntent = new Intent(MainActivity.this, UsersActivity.class);
                startActivity(custIntent);
                break;
            case R.id.btnGoToMenuEdit:
                Intent intent = new Intent(MainActivity.this, MenuEditActivity.class);
                startActivity(intent);
                break;
            case R.id.btnRefreshList:
                new UpdtaeOrdersListTask(this).execute("");
                break;
            case R.id.btnSetStoreStatus:
                openCloseStore();
                break;

        }
    }

    private void openCloseStore() {

        final boolean newStoreStatus;
        isStoreOpen = ParseConnector.getStoreStatus(LockAwayAdminApplication.getRestID());
        String msgToShow = null;

        if (isStoreOpen) {
            msgToShow = getResources().getString(R.string.want_close_store);
            newStoreStatus = false;
        } else {
            msgToShow = getResources().getString(R.string.want_open_store);
            newStoreStatus = true;
        }

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        ParseConnector.setStoreStatus(LockAwayAdminApplication.getRestID(), newStoreStatus);
                        isStoreOpen = newStoreStatus;
                        updateCloseOpenBtn();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msgToShow).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();
    }


    private class UpdtaeOrdersListTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        public UpdtaeOrdersListTask(MainActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        ;

        @Override
        protected String doInBackground(String... strings) {
            mOrderList.addAll(ParseConnector.getActiveOtdersBL(LockAwayAdminApplication.getRestID()));
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            mAdapter.notifyDataSetChanged();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }


        @Override
        protected void onPreExecute() {
            dialog.setMessage(getResources().getString(R.string.wait_refreshing_list));
            dialog.show();
            mOrderList.clear();
        }
    }
}


//    private void refreshOrders(){
////        LockAwayAdminApplication.refreshOrdersList();
//
//        this.mOrderList = ParseConnector.getActiveOtdersBL(LockAwayAdminApplication.getRestID());
////        if (newOrders){
//
//            mAdapter.notifyDataSetChanged();
//        mOrdersListView.invalidateViews();
//
////        }
//    }