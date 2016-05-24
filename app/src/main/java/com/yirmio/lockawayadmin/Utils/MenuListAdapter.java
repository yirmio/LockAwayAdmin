package com.yirmio.lockawayadmin.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yirmio.lockawayadmin.BL.RestaurantMenuObject;
import com.yirmio.lockawayadmin.R;
import com.yirmio.lockawayadmin.UI.MenuEditActivity;

import java.util.ArrayList;
import java.util.List;

public class MenuListAdapter extends ArrayAdapter implements Filterable, View.OnClickListener {
    private LayoutInflater mLayoutInflater;
    private List<RestaurantMenuObject> allObjects;
    private List<RestaurantMenuObject> filteredObjects;
    private ObjectsFilter objectsFilter;
    private Context mContext;
    private MenuEditActivity parentActivity;


    public MenuListAdapter(Context context, int resource, List objects,MenuEditActivity pActivity) {
        super(context, resource, objects);
        this.allObjects = objects;
        this.filteredObjects = objects;
        mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.parentActivity = pActivity;

    }

    @Override
    public int getCount() {
        return allObjects.size();
    }

    @Override
    public RestaurantMenuObject getItem(int position) {
        return allObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View updateView;
        ViewHolder viewHolder;
        if (view == null){
            updateView = mLayoutInflater.inflate(R.layout.signle_menu_row_layout,null);
            viewHolder = new ViewHolder();
            viewHolder.mImageViewObjectPhoto = (ImageView) updateView.findViewById(R.id.rowItem_Object_imageView);
            viewHolder.mTxtViewTitle = (TextView)updateView.findViewById(R.id.rowItem_Object_textViewTitle);
            viewHolder.mTxtViewPrice = (TextView)updateView.findViewById(R.id.rowItem_Object_textViewPrice);
            viewHolder.mBtnEditItem = (Button)updateView.findViewById(R.id.rowItem_Object_BtnEdit);
            viewHolder.mBtnEditItem.setOnClickListener(this);
            viewHolder.mBtnEditItem.setTag(position);
            viewHolder.mBtnDelItem = (Button)updateView.findViewById(R.id.rowItem_Object_BtnDel);
            viewHolder.mBtnDelItem.setTag(position);
            viewHolder.mBtnDelItem.setOnClickListener(this);
            viewHolder.mBtnsLayout = (LinearLayout)updateView.findViewById(R.id.rowItem_Object_BtnsLayout);
            viewHolder.mBtnsLayout.setVisibility(View.GONE);

            updateView.setTag(viewHolder);
        }
        else {
            updateView = view;
            viewHolder = (ViewHolder) updateView.getTag();
        }

        final RestaurantMenuObject item = getItem(position);
        viewHolder.updateContent(item);

        return updateView;
    }

    @Override
    public Filter getFilter() {
        if (objectsFilter == null){
            objectsFilter = new ObjectsFilter();
        }
        return objectsFilter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rowItem_Object_BtnDel:
                String idToDelete = getItem(Integer.valueOf(view.getTag().toString())).getId();
                parentActivity.delItem(idToDelete);
                break;
            case R.id.rowItem_Object_BtnEdit:
                //TODO - edit item
                break;

        }

    }

    private class ObjectsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String typeToFilterBy = String.valueOf(charSequence);
            FilterResults results = new FilterResults();
            if (typeToFilterBy != null && typeToFilterBy.length() > 0) {
                ArrayList<RestaurantMenuObject> filteredList = new ArrayList<RestaurantMenuObject>();
                for (int i = 0; i < filteredObjects.size(); i++) {
                    if (filteredObjects.get(i).getType() == typeToFilterBy){
                        RestaurantMenuObject menuObject = filteredObjects.get(i);
                        filteredList.add(menuObject);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            } else {
                results.count = filteredObjects.size();
                results.values = filteredObjects;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            allObjects = (ArrayList<RestaurantMenuObject>)results.values;
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        ImageView mImageViewObjectPhoto;
        TextView mTxtViewTitle;
        TextView mTxtViewPrice;
        Button mBtnEditItem;
        Button mBtnDelItem;
        LinearLayout mBtnsLayout;






        public void updateContent(RestaurantMenuObject item) {
            //mImageViewObjectPhoto = item.getImage();
            mTxtViewTitle.setText(item.getTitle());
            mTxtViewPrice.setText(String.valueOf(item.getPrice()));
        }
    }
}
