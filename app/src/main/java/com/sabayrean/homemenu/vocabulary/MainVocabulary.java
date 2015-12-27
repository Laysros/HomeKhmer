package com.sabayrean.homemenu.vocabulary;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sabayrean.homemenu.R;
import com.sabayrean.homemenu.app.MyApplication;
import com.sabayrean.homemenu.database.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainVocabulary extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = MainVocabulary.class.getSimpleName();
    private int offSet = 0;
    private String key = "";
    private int language = 1; // else =Englsih, 2=French
    private boolean loaded = false;

    private Database database;



    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private SwipeListAdapterVocabulary adapter;
    private List<Vocabulary> vocabularyList;

    // initially offset will be 0, later will be updated while parsing the json


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);

        listView = (ListView) findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        database = new Database(this);
        vocabularyList = new ArrayList<>();
        adapter = new SwipeListAdapterVocabulary(this, vocabularyList);

        adapter.setLangauge(language);
        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);


        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            key = extras.getString("itemVocabulary");
            Log.d("KEY GTTT=", "getting" + key);
        }

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        fetchVocabularies();
                                    }
                                }
        );

    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
         fetchVocabularies();
    }

    /**
     * Fetching movies json by making http call
     */
    private void fetchVocabularies() {

        // showing refresh animation before making http call

        int sizeTmp;
        if(database.getAllCategories().size()>0){
            swipeRefreshLayout.setRefreshing(false);
            if(!loaded ) {
                vocabularyList.addAll(database.getAllVocabularies(key));
                adapter.notifyDataSetChanged();
                loaded = true;
            }
        }else {
            swipeRefreshLayout.setRefreshing(true);
            // appending offset to url
            String url = "http://news.learnkh.com/index.php?offset=" + offSet + "&key=" + key + "&id=" + language;
            Log.d("URL", url);
            // Volley's json array request object
            Log.d("vvv", "yyy");
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

                                        if (language == 2) {
                                            fr = VocabularyObj.getString("FR");
                                        } else {
                                            en = VocabularyObj.getString("EN");
                                        }
                                        Log.d("test", fr + ":" + en);
                                        String sound = VocabularyObj.getString("sound");
                                        Vocabulary v = new Vocabulary(id, sound, kh, fr, en);

                                        vocabularyList.add(0, v);

                                        // updating offset value to highest value
                                        if (id >= offSet) {
                                            offSet = id;
                                        }

                                    } catch (JSONException e) {
                                        Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                    }
                                }

                                adapter.notifyDataSetChanged();
                            }

                            // stopping swipe refresh
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Server Error: " + error.getMessage());

                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                    // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(req);
        }
    }

}