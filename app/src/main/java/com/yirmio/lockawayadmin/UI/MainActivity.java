package com.yirmio.lockawayadmin.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOrderList = ParseConnector.getActiveOtdersBL(LockAwayAdminApplication.getRestID());
        mAdapter = new MainListAdapter(this, R.layout.single_row_layout, mOrderList);
        mOrdersListView = (ListView) findViewById(R.id.listView_Orders);
        mOrdersListView.setAdapter(mAdapter);


        this.mBtnRefreshList = (Button) findViewById(R.id.btnRefreshList);
        this.mBtnRefreshList.setOnClickListener(this);
        this.mBtnMenuEdit = (Button) findViewById(R.id.btnGoToMenuEdit);
        this.mBtnMenuEdit.setOnClickListener(this);
        this.mBtnUsers = (Button)findViewById(R.id.btnGoToCustomersArea);
        this.mBtnUsers.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGoToCustomersArea:
                Intent custIntent = new Intent(MainActivity.this,UsersActivity.class);
                startActivity(custIntent);
                break;
            case R.id.btnGoToMenuEdit:
                Intent intent = new Intent(MainActivity.this,MenuEdit.class);
                startActivity(intent);
                break;
            case R.id.btnRefreshList:
                new UpdtaeOrdersListTask().execute("");
                break;

        }
    }


    private class UpdtaeOrdersListTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            mOrderList.addAll(ParseConnector.getActiveOtdersBL(LockAwayAdminApplication.getRestID()));
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
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