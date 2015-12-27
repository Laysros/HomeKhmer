package com.sabayrean.homemenu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

/**
 * Created by LAYLeangsros on 25/06/2015.
 */
public class Grammar extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_grammar);
        LoadPreferences();


    }
    private void LoadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String  name = sharedPreferences.getString("yourname", "A") ;
        Boolean update = sharedPreferences.getBoolean("applicationUpdates", false);
        String download = sharedPreferences.getString("downloadType", "B") ;

        TextView tvName = (TextView) findViewById(R.id.name);
        TextView tvTick = (TextView) findViewById(R.id.tick);
        TextView tvType = (TextView) findViewById(R.id.type);
        tvName.setText(name);
        tvTick.setText(update + "");
        tvType.setText(download);
    }
}
