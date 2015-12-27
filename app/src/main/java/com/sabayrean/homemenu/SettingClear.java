package com.sabayrean.homemenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * Created by LAYLeangsros on 27/06/2015.
 */
public class SettingClear extends DialogPreference {
    protected Context context;
    public SettingClear(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        if(which == DialogInterface.BUTTON_POSITIVE){
            //Clear database
            context.deleteDatabase("khmerLearning");
            AlertDialog dialogDone = new AlertDialog.Builder(context)
                    .setTitle("Success")
                    .setInverseBackgroundForced(true)
                    .setMessage("Data cleared").show();
        }
    }
}
