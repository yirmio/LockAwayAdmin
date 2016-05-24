package com.yirmio.lockawayadmin.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yirmio.lockawayadmin.BL.RestaurantMenuObject;
import com.yirmio.lockawayadmin.DAL.ParseConnector;
import com.yirmio.lockawayadmin.R;

/**
 * Created by oppenhime on 24/05/2016.
 */
public class EditItemActivity extends Activity implements View.OnClickListener{

    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextTimeToMake;
    private EditText editTextPrice;

    private CheckBox checkBoxIsGlotenFree;
    private CheckBox checkBoxIsVeg;
    private CheckBox checkBoxIsAvalible;
    private CheckBox checkBoxIsOnSale;

    private Button btnSend;
    private Button btnImg;

    private ImageView imgView;

    private TextView txtViewTitle;

    private Spinner spnrItemType;
    private ArrayAdapter mTypeSpnrAdptr;
    private RestaurantMenuObject itemToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String itemIDToEdit;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                itemIDToEdit= null;
            } else {
                itemIDToEdit= extras.getString("ItemID");
            }
        } else {
            itemIDToEdit= null;
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);
        attachUI();
        loadInfo(itemIDToEdit);
    }

    private void loadInfo(String itemIDToEdit) {
        itemToEdit = ParseConnector.getResturantMenuObjectByID(itemIDToEdit);
        editTextName.setText(itemToEdit.getTitle());
        editTextDescription.setText(itemToEdit.getDescription());
        editTextTimeToMake.setText(String.valueOf(itemToEdit.getTimeToMake()));
        editTextPrice.setText(String.valueOf(itemToEdit.getPrice()));
        checkBoxIsGlotenFree.setChecked(itemToEdit.isGlootenFree());
        checkBoxIsVeg.setChecked(itemToEdit.isVeg());
        checkBoxIsAvalible.setChecked(itemToEdit.isAvaliable());
        checkBoxIsOnSale.setChecked(itemToEdit.isOnSale());
        spnrItemType.setSelection(mTypeSpnrAdptr.getPosition(itemToEdit.getType()));

    }

    private void attachUI() {

        //Checkboxes
        this.checkBoxIsAvalible = (CheckBox) findViewById(R.id.insertItemChckBxAvalibleInMemu);
        this.checkBoxIsGlotenFree = (CheckBox) findViewById(R.id.insertItemChckBxIsGlotenFree);
        this.checkBoxIsVeg = (CheckBox) findViewById(R.id.insertItemChckBxIsVeg);
        this.checkBoxIsOnSale = (CheckBox)findViewById(R.id.insertItemChckBxIsSale);

        //Edit Text
        this.editTextDescription = (EditText) findViewById(R.id.insertItemEditTextDescription);
        this.editTextName = (EditText) findViewById(R.id.insertItemEditTextName);
        this.editTextTimeToMake = (EditText) findViewById(R.id.insertItemEditTextTimeToMake);
        this.editTextPrice = (EditText) findViewById(R.id.insertItemEditTextPrice);

        //ImageView - To Hide
        this.imgView = (ImageView) findViewById(R.id.insertItemImgViewPhoto);
        this.imgView.setVisibility(View.GONE);

        //Buttons
        this.btnSend = (Button) findViewById(R.id.insertItemBtnSend);
        this.btnSend.setOnClickListener(this);
        //To Hide
        this.btnImg = (Button)findViewById(R.id.insertItemBtnAddImages);
        this.btnImg.setVisibility(View.GONE);

        //Spinner
        this.spnrItemType = (Spinner)findViewById(R.id.addItemTypeSpinner);
        String[] items = ParseConnector.getAllMenuObjectTypeNames();
        this.mTypeSpnrAdptr = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items);
        this.spnrItemType.setAdapter(this.mTypeSpnrAdptr);

        //TextViews
        this.txtViewTitle = (TextView) findViewById(R.id.txtVw_AddMenuItem_Title);
        this.txtViewTitle.setText(R.string.edit_item_lable);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.insertItemBtnSend:
                if (updateItemInModel()) {
                    SaveEditedItemTask saveTask = new SaveEditedItemTask(itemToEdit, this);
                    saveTask.execute();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.check_item, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private boolean updateItemInModel() {
        boolean res = true;
        if ((editTextName.getText() == null || editTextName.getText().length() <= 0) ||
                (editTextDescription.getText() == null || editTextDescription.getText().length() <= 0) ||
                (editTextTimeToMake.getText() == null || editTextTimeToMake.getText().length() <= 0) ||
                (editTextPrice.getText() == null || editTextPrice.getText().length() <= 0)) {
            res = false;
        }
        else {
            itemToEdit.setAvaliable(checkBoxIsAvalible.isChecked());
            itemToEdit.setDescription(editTextDescription.getText().toString());
            itemToEdit.setIsGlootenFree(checkBoxIsGlotenFree.isChecked());
            itemToEdit.setIsVeg(checkBoxIsVeg.isChecked());
            itemToEdit.setOnSale(checkBoxIsOnSale.isChecked());
            itemToEdit.setPrice(Float.valueOf(editTextPrice.getText().toString()));
            itemToEdit.setTimeToMake(Math.round(Float.valueOf(editTextTimeToMake.getText().toString())));
            itemToEdit.setTitle(editTextName.getText().toString());
            itemToEdit.setType(spnrItemType.getSelectedItem().toString());
        }


        return res;
    }

    private class SaveEditedItemTask extends AsyncTask{
        RestaurantMenuObject item;
        boolean result = false;
        ProgressDialog dialog;
        EditItemActivity activity;
        public SaveEditedItemTask(RestaurantMenuObject itemToEdit, EditItemActivity editItemActivity) {
            item = itemToEdit;
            activity = editItemActivity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            result = ParseConnector.saveEditedObject(item);
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getResources().getString(R.string.wait_saving_item));
            dialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            if (result){
                Toast.makeText(activity,R.string.object_saved,Toast.LENGTH_LONG);
            }
            else {
                Toast.makeText(activity, R.string.error_saving_object,Toast.LENGTH_LONG);
            }
            activity.finish();
        }
    }
}
