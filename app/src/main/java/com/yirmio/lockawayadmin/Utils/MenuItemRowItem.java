package com.yirmio.lockawayadmin.Utils;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by oppenhime on 23/05/2016.
 */
public class MenuItemRowItem {
    private ImageView mImageViewObjectPhoto;
    private TextView mTxtViewTitle;
    private TextView mTxtViewPrice;
    private Button mBtnEditItem;
    private Button mBtnDelItem;
    private LinearLayout mBtnsLayout;

    public ImageView getmImageViewObjectPhoto() {
        return mImageViewObjectPhoto;
    }

    public void setmImageViewObjectPhoto(ImageView mImageViewObjectPhoto) {
        this.mImageViewObjectPhoto = mImageViewObjectPhoto;
    }

    public TextView getmTxtViewTitle() {
        return mTxtViewTitle;
    }

    public void setmTxtViewTitle(TextView mTxtViewTitle) {
        this.mTxtViewTitle = mTxtViewTitle;
    }

    public TextView getmTxtViewPrice() {
        return mTxtViewPrice;
    }

    public void setmTxtViewPrice(TextView mTxtViewPrice) {
        this.mTxtViewPrice = mTxtViewPrice;
    }

    public Button getmBtnEditItem() {
        return mBtnEditItem;
    }

    public void setmBtnEditItem(Button mBtnEditItem) {
        this.mBtnEditItem = mBtnEditItem;
    }

    public Button getmBtnDelItem() {
        return mBtnDelItem;
    }

    public void setmBtnDelItem(Button mBtnDelItem) {
        this.mBtnDelItem = mBtnDelItem;
    }

    public LinearLayout getmBtnsLayout() {
        return mBtnsLayout;
    }

    public void setmBtnsLayout(LinearLayout mBtnsLayout) {
        this.mBtnsLayout = mBtnsLayout;
    }
}
