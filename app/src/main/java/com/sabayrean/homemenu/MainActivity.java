package com.sabayrean.homemenu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sabayrean.homemenu.app.MyApplication;
import com.sabayrean.homemenu.article.Detail;
import com.sabayrean.homemenu.article.TousArticles;
import com.sabayrean.homemenu.database.Download;
import com.sabayrean.homemenu.vocabulary.MainCategory;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {
    ImageButton btnVocabulary, btnSetting, btnGrammar;
    ProgressDialog PD;
    private int contentID;
    Typeface face;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVocabulary = (ImageButton) findViewById(R.id.btnVocabulary);
        btnVocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainCategory.class);
                startActivity(intent);
            }
        });
        btnSetting = (ImageButton) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Setting.class);
                startActivity(intent);
            }
        });
        btnGrammar = (ImageButton) findViewById(R.id.btnGrammar);
        btnGrammar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Grammar.class);
                startActivity(intent);
            }
        });

        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);

        TextView moreArticle = (TextView) findViewById(R.id.moreArticle);
        face = Typeface.createFromAsset(moreArticle.getContext().getAssets(), "fonts/DaunPenh.ttf");
        moreArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TousArticles.class);
                startActivity(intent);
            }
        });
        ImageView alaune = (ImageView) findViewById(R.id.articleImage);
        alaune.setClickable(true);
        alaune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Detail.class);
                intent.putExtra("id", contentID);
                startActivity(intent);
            }
        });


        HighlightArticle();
        LoadPreferences();



        
    }
    private void HighlightArticle() {
        //PD.show();
        final String URL;
        URL = "http://news.learnkh.com/article.php";
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
                                contentID = object.getInt("id");
                                String title = object.getString("title");
                                String tag = object.getString("tag");
                                String content = object.getString("content");
                                TextView tvTitle = (TextView) findViewById(R.id.articleTitle);
                                tvTitle.setText(title);
                                tvTitle.setTypeface(face);
                                ImageView imvArticle = (ImageView) findViewById(R.id.articleImage);
                                Picasso.with(getBaseContext()).load(object.getString("image")).into(imvArticle);

                                imvArticle.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //PD.dismiss();
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

    private void LoadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String download = sharedPreferences.getString("downloadType", "B") ;

        if(download.equals("3")){

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("dd\ndddddddddddddddddddddddsdafsadfsd\nsadfsdfffffffffffff")
                    .setInverseBackgroundForced(true)
                    .setPositiveButton("Yes", dialogOnClickListener)
                    .setNegativeButton("No",  dialogOnClickListener)
                    .setMessage("You selected download " + download + ". \nDo you to download data now?").show();
        }else{
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("dd\ndddddddddddddddddddddddsdafsadfsd\nsadfsdfffffffffffff")
                    .setInverseBackgroundForced(true)
                    .setMessage("You selected download " + download).show();
        }
    }
    DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    /*AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("dd\ndddddddddddddddddddddddsdafsadfsd\nsadfsdfffffffffffff")
                            .setInverseBackgroundForced(true)
                            .setMessage("Downloading").show();*/
                    Download downloadData = new Download(MainActivity.this);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

}

