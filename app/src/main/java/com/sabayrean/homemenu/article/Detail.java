package com.sabayrean.homemenu.article;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sabayrean.homemenu.R;
import com.sabayrean.homemenu.app.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LAYLeangsros on 14/06/2015.
 */
public class Detail extends Activity{
    private String title, content, URL, tag;
    private int id;
    ProgressDialog PD;
    String voc[] = new String[30];
    Typeface face;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);

        Bundle extras = getIntent().getExtras();
        if(extras !=null){
            id = extras.getInt("id");
        }
        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);

        showDetail();

    }

    private void showDetail() {
        PD.show();
        URL = "http://news.learnkh.com/article.php?id=" + id + "&title=1";
        Log.d("Imaged", "RULLL");
        JsonArrayRequest req = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("Imageda", "LoadImagea");
                        if (jsonArray.length() > 0) {
                            Log.d("Imaged", "LoadImage");
                            try {
                                JSONObject object = jsonArray.getJSONObject(0);

                                title = object.getString("title");
                                tag = object.getString("tag");
                                content = object.getString("content");
                                TextView tTitle = (TextView) findViewById(R.id.title);
                                TextView tTag = (TextView) findViewById(R.id.tag);
                                final TextView tContent = (TextView) findViewById(R.id.content);
                                face = Typeface.createFromAsset(tContent.getContext().getAssets(), "fonts/DaunPenh.ttf");

                                tTitle.setText(title);
                                tTitle.setTypeface(face);
                                tTag.setText(tag);
                                tTag.setTypeface(face);
                                ImageView image = (ImageView) findViewById(R.id.image);
                                Picasso.with(getBaseContext()).load(object.getString("image")).into(image);
                                ////
                                //Create content clickable
                                ////

                                SpannableString sp = new SpannableString(content);

                                //Get sub json array
                                final JSONObject subObject = object.getJSONObject("sub");

                                for(int j=0; j< subObject.length(); j++){
                                    voc[j] = subObject.getString("vocabulary" + j);
                                    Log.d("vocc", "cm:" + voc[j] + "-" + j);
                                }
                                int pos;
                                for(int indexOfVoc=0; indexOfVoc<subObject.length(); indexOfVoc++) {
                                    pos = content.indexOf(voc[indexOfVoc].substring(0,voc[indexOfVoc].indexOf("=")));
                                    sp.setSpan( new ClickableSpan() {
                                        @Override
                                        public void onClick(View widget) {
                                            int start = tContent.getSelectionStart();
                                            int end = tContent.getSelectionEnd();
                                            String selected = tContent.getText().toString().substring(start, end);
                                            for(int i=0; i<subObject.length(); i++){
                                                if(voc[i].indexOf(selected + "=")==0){
                                                    selected = voc[i];
                                                    break;
                                                }
                                            }

                                            AlertDialog dialog = new AlertDialog.Builder(Detail.this)
                                                    .setTitle("dd\ndddddddddddddddddddddddsdafsadfsd\nsadfsdfffffffffffff")
                                                    .setInverseBackgroundForced(true)
                                                    .setMessage(selected).show();

                                            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                                            textView.setTextSize(40);



                                            textView.setTypeface(face);
                                        }
                                    }, pos, pos + voc[indexOfVoc].substring(0,voc[indexOfVoc].indexOf("=")).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                                Log.d("content:", "xxl"+ content);
                                tContent.setLinksClickable(true);
                                tContent.setMovementMethod(LinkMovementMethod.getInstance());

                                tContent.setText(sp, TextView.BufferType.SPANNABLE);
                                tContent.setTypeface(face);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        PD.dismiss();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Apx", "Server Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MyApplication.getInstance().addToRequestQueue(req);



    }
}
