package com.sabayrean.homemenu.vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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


public class MainCategory extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = MainCategory.class.getSimpleName();
    private int offSet = 0;
    private String key = "";
    private int language = 1; // else =Englsih, 2=French
    private String URL;
    private Database database;
    private boolean loaded = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private SwipeListAdapterCategory adapter;
    private List<Category> categoryList;

    // initially offset will be 0, later will be updated while parsing the json


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);

        listView = (ListView) findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        database = new Database(this);
        categoryList = new ArrayList<>();
        adapter = new SwipeListAdapterCategory(this, categoryList);

        adapter.setLaguage(language);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainCategory.this , MainVocabulary.class);
                intent.putExtra("itemVocabulary", categoryList.get(i).en);
                startActivity(intent);
            }
        });
        /*listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/


        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        fetchCategories();
                                    }
                                }
        );

    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
          fetchCategories();
    }

    /**
     * Fetching movies json by making http call
     */
    private void fetchCategories() {

        // showing refresh animation before making http call
        int sizeTmp;
        if((sizeTmp=database.getAllCategories().size())>0){
            swipeRefreshLayout.setRefreshing(false);
            if(!loaded){
                categoryList.addAll(database.getAllCategories());//.subList(offSet, offSet + 5));
                adapter.notifyDataSetChanged();
                loaded = true;
            }
        }else {
            swipeRefreshLayout.setRefreshing(true);
            // appending offset to url
            // Volley's json array request object
            URL = "http://news.learnkh.com/index.php?offset=" + offSet + "&key=" + key + "&id=" + language;
            Log.d("URL", "OFFSETx:" + offSet);
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

                                        int id = CategoryObj.getInt("id");
                                        String kh = CategoryObj.getString("KH");
                                        String fr = "", en = "";

                                        fr = CategoryObj.getString("FR");
                                        en = CategoryObj.getString("EN");

                                        Log.d("test", fr + ":" + en);

                                        String imageURL = "http://icons.iconarchive.com/icons/harwen/simple/256/JPEG-Image-icon.png";

                                        Category v = new Category(id, kh, fr, en, imageURL);

                                        categoryList.add(0, v);
                                        Log.d("IDD:", "offset:" + offSet + "-i:" + id);
                                        // updating offset value to highest value
                                        if (id >= offSet) {
                                            Log.d("xxxx1", "xxxx2" + id);
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