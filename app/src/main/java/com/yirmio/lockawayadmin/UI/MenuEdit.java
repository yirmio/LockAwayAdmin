package com.yirmio.lockawayadmin.UI;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yirmio.lockawayadmin.R;

public class MenuEdit extends Activity implements View.OnClickListener {
    private Button mAddItemBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_edit);

        attachUI();
    }

    private void attachUI() {
        this.mAddItemBtn = (Button)findViewById(R.id.menu_edit_activity_Btn_add);
        this.mAddItemBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_edit_activity_Btn_add:
                Intent attItemIntent = new Intent(MenuEdit.this,AddItemActivity.class);
                startActivity(attItemIntent);
                break;
        }
    }
}
