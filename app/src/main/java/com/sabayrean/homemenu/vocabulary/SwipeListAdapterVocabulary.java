package com.sabayrean.homemenu.vocabulary;

/**
 * Created by LAYLeangsros on 05/06/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
public class SwipeListAdapterVocabulary extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Vocabulary> vocabularyList;
    private String[] bgColors;
    private int language;

    public SwipeListAdapterVocabulary(Activity activity, List<Vocabulary> vocabularyList) {
        this.activity = activity;
        this.vocabularyList = vocabularyList;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
    }

    @Override
    public int getCount() {
        return vocabularyList.size();
    }

    @Override
    public Object getItem(int location) {
        return vocabularyList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setLangauge(int language) {
        this.language = language;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_vocabulary, null);

        TextView serial = (TextView) convertView.findViewById(R.id.serial);
        TextView kh = (TextView) convertView.findViewById(R.id.kh);
        TextView translation = (TextView) convertView.findViewById(R.id.translation);
        Typeface face = Typeface.createFromAsset(kh.getContext().getAssets(), "fonts/DaunPenh.ttf");
        kh.setTypeface(face);
        serial.setText(String.valueOf(vocabularyList.get(position).id));
        kh.setText(vocabularyList.get(position).kh);

        if(vocabularyList.get(position).id <10){
            serial.setText("0" + vocabularyList.get(position).id);
        }else{
            serial.setText(String.valueOf(vocabularyList.get(position).id));
        }

        if(language==2){
            translation.setText(vocabularyList.get(position).fr);
        }else{
            translation.setText(vocabularyList.get(position).en);
        }
        String color = bgColors[position % bgColors.length];
        serial.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }

}