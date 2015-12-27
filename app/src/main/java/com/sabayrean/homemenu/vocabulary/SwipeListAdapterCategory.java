package com.sabayrean.homemenu.vocabulary;

/**
 * Created by LAYLeangsros on 05/06/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sabayrean.homemenu.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Ravi on 13/05/15.
 */
public class SwipeListAdapterCategory extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Category> categoryList;
    private String[] bgColors;
    private int language;

    public SwipeListAdapterCategory(Activity activity, List<Category> categoryList) {
        this.activity = activity;
        this.categoryList = categoryList;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int location) {
        return categoryList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setLaguage(int language) {
        this.language = language;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_category_vocabulary, null);


        TextView item = (TextView) convertView.findViewById(R.id.item_text);
        ImageView image = (ImageView) convertView.findViewById(R.id.item_image);

        Picasso.with(activity).load(categoryList.get(position).icon).into(image);

        if(language==2){
            item.setText(categoryList.get(position).fr);
        }else{
            item.setText(categoryList.get(position).en);
        }

        return convertView;
    }

}