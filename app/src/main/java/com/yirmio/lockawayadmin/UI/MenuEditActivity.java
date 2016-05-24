package com.yirmio.lockawayadmin.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.yirmio.lockawayadmin.BL.RestaurantMenuObject;
import com.yirmio.lockawayadmin.DAL.ParseConnector;
import com.yirmio.lockawayadmin.R;
import com.yirmio.lockawayadmin.Utils.MenuListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuEditActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private Button mAddItemBtn;
    private Spinner mSectionsSpinner;
    private ListView mItemsListView;


    private MenuListAdapter listAdapter;
    private List<RestaurantMenuObject> menuObjectsList = new ArrayList<RestaurantMenuObject>();
    private String[] typesItemsToSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_edit);

        attachUI();

    }

    private void attachUI() {
        //Buttons
        this.mAddItemBtn = (Button)findViewById(R.id.menu_edit_activity_Btn_add);
        this.mAddItemBtn.setOnClickListener(this);

        //Spinner
        this.mSectionsSpinner = (Spinner)findViewById(R.id.mnuEditActivity_spinner);
        typesItemsToSpinner = ParseConnector.getAllMenuObjectTypeNames();
        this.mSectionsSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,typesItemsToSpinner));
        this.mSectionsSpinner.setOnItemSelectedListener(this);

        //ListView
        this.mItemsListView = (ListView)findViewById(R.id.mnuEditActivity_listView);
        this.menuObjectsList = ParseConnector.getAllMenuItems();
        this.listAdapter = new MenuListAdapter(this,R.layout.single_menu_row_layout,menuObjectsList,this);
        this.mItemsListView.setAdapter(this.listAdapter);
        this.mItemsListView.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_edit_activity_Btn_add:
                Intent attItemIntent = new Intent(MenuEditActivity.this,AddItemActivity.class);
                startActivity(attItemIntent);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String type = typesItemsToSpinner[position];
        listAdapter.getFilter().filter(type, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int i) {

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        LinearLayout btns = (LinearLayout)view.findViewById(R.id.rowItem_Object_BtnsLayout);
        if (btns.getVisibility() != View.VISIBLE){
            btns.setVisibility(View.VISIBLE);
        }
        else {
            btns.setVisibility(View.GONE);
        }

    }

    public void delItem(final String idToDelete) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //TODO - refresh
                        doDeleteItem(idToDelete);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.are_you_sure).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();    }

    private void doDeleteItem(String idToDelete) {
//        ParseConnector.deleteObjectItem(idToDelete);
        DeleteInBackgroundTask delTask = new DeleteInBackgroundTask(this,idToDelete);
        delTask.execute();
//        Toast.makeText(this,getResources().getString(R.string.item_deleted),Toast.LENGTH_SHORT).show();
    }

    public void editItem(String idToEdit) {
        Intent goToEditItemIntent = new Intent(MenuEditActivity.this,EditItemActivity.class);
        goToEditItemIntent.putExtra("ItemID",idToEdit);
        startActivity(goToEditItemIntent);

    }

    private class DeleteInBackgroundTask extends AsyncTask <Void,Void,Void>{
        ProgressDialog dialog;
        String itemIDToDel;
        MenuEditActivity mMenuEditActivity;

        public DeleteInBackgroundTask(MenuEditActivity menuEditActivity,String IDToDel){
            dialog = new ProgressDialog(menuEditActivity);
            itemIDToDel = IDToDel;
            mMenuEditActivity = menuEditActivity;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getResources().getString(R.string.wait_deleting_item));
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            Toast.makeText(mMenuEditActivity,getResources().getString(R.string.item_deleted),Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParseConnector.deleteObjectItem(itemIDToDel);
            return null;
        }
    }
}
