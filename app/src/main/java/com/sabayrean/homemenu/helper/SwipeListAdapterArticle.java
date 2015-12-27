package com.sabayrean.homemenu.helper;

/**
 * Created by LAYLeangsros on 05/06/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sabayrean.homemenu.R;

import java.util.List;


/**
 * Created by Ravi on 13/05/15.
 */
public class SwipeListAdapterArticle extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Article> articleList;
    private String[] bgColors;
    private int language;

    public SwipeListAdapterArticle(Activity activity, List<Article> articleList) {
        Log.e("xxxd:+", "efd");
        this.activity = activity;
        this.articleList = articleList;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
        Log.e("xxx0:+", "ef0");
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public Object getItem(int location) {
        return articleList.get(location);
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
        Log.e("xxx:+", "ef");

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.tous_articles_items, null);

        Log.e("xxx1:+", "ef1");

        TextView serial = (TextView) convertView.findViewById(R.id.serial);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        Typeface face = Typeface.createFromAsset(title.getContext().getAssets(), "fonts/DaunPenh.ttf");

        title.setTypeface(face);
        title.setText(articleList.get(position).title);

        Log.d("resultx", "+" + articleList.get(position).title);


        if(articleList.get(position).id <10){
            serial.setText("0" + articleList.get(position).id);
        }else{
            serial.setText(String.valueOf(articleList.get(position).id));
        }



        String color = bgColors[position % bgColors.length];
        serial.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }

}