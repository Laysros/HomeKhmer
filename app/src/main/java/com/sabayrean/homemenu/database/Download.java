package com.sabayrean.homemenu.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sabayrean.homemenu.app.MyApplication;
import com.sabayrean.homemenu.vocabulary.Category;
import com.sabayrean.homemenu.vocabulary.Vocabulary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LAYLeangsros on 25/06/2015.
 */
public class Download {
    private String URL;
    private List<Vocabulary> vocabularyList;
    private List<Category> categoryList;
    private Database database;
    private boolean d=false;

    ProgressDialog PD;

    public Download(Context context){
        database = new Database(context);
        PD = new ProgressDialog(context);
        PD.setMessage("Downloading.....");
        PD.setCancelable(true);

        categoryList = new ArrayList<>();
        fetchCategories();
        categoryList.addAll(database.getAllCategories());
        fetchVocabularies();



    }
    private void fetchCategories() {

        // showing refresh animation before making http call

        // appending offset to url
        // Volley's json array request object
        PD.show();
        URL = "http://news.learnkh.com/index.php?download=1";
        JsonArrayRequest req = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("SuccessResq", response.toString());

                        if (response.length() > 0) {

                            // looping through json and adding to Categories list
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject CategoryObj = response.getJSONObject(i);

                                    String kh = CategoryObj.getString("KH");
                                    String fr="", en="", icon;
                                    icon = CategoryObj.getString("ICON");
                                    fr = CategoryObj.getString("FR");
                                    en = CategoryObj.getString("EN");

                                    Category category = new Category(i, kh, fr, en, icon);

                                     database.addCategory(category);





                                } catch (JSONException e) {

                                }
                            }


                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }
    private void fetchVocabularies() {

        // showing refresh animation before making http call
        for (final Category category: categoryList) {
            String url = "http://news.learnkh.com/index.php?download=2&key=" + category.en;
            JsonArrayRequest req = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("SuccessResq", response.toString());

                            if (response.length() > 0) {

                                // looping through json and adding to Vocabularies list
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject VocabularyObj = response.getJSONObject(i);

                                        int id = VocabularyObj.getInt("id");
                                        String kh = VocabularyObj.getString("KH");
                                        String fr = "", en = "";
                                        String sound = VocabularyObj.getString("sound");

                                        Vocabulary voc = new Vocabulary(id,sound ,kh, fr, en);
                                          database.addVocabulary(voc, category.en);


                                    } catch (JSONException e) {

                                    }
                                }
                                PD.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(req);
        }
    }
}
