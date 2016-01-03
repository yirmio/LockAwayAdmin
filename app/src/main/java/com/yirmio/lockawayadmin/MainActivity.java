package com.yirmio.lockawayadmin;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.yirmio.lockawayadmin.DAL.ParseConnector;
import com.yirmio.lockawayadmin.Utils.LockAwayAdminApplication;
import com.yirmio.lockawayadmin.Utils.MainListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {


    private ArrayList mOrderList;
    private MainListAdapter mAdapter;
    private ListView mOrdersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOrderList = ParseConnector.getActiveOtdersBL(LockAwayAdminApplication.getRestID());
        mAdapter = new MainListAdapter(this,R.layout.single_row_layout,mOrderList);
        mOrdersListView = (ListView) findViewById(R.id.listView_Orders);
        mOrdersListView.setAdapter(mAdapter);

    }
}
