package com.sabayrean.homemenu.article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.sabayrean.homemenu.helper.Article;
import com.sabayrean.homemenu.helper.SwipeListAdapterArticle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LAYLeangsros on 14/06/2015.
 */
public class TousArticles extends Activity implements SwipeRefreshLayout.OnRefreshListener{
    private ListView listView;
    private SwipeListAdapterArticle adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Article> articleList;
    private String URL;
    private int offSet=1;
    private int numContent;
    private boolean firstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tous_articles);

        listView = (ListView) findViewById(R.id.listView);
        Log.d("fetchingx:01" , "fethx01");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        Log.d("fetchingx:11" , "fethx11");
        articleList = new ArrayList<>();
        Log.d("fetchingx:12" , "fethx12");
        adapter = new SwipeListAdapterArticle(this, articleList);

        Log.d("fetchingx:1c" , "fethx1c");
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TousArticles.this , Detail.class);
                intent.putExtra("id", articleList.get(i).id);
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                Log.d("fetchingx:" , "fethx");
                fetchArticle();
            }
        });
    }

    private void fetchArticle() {
        swipeRefreshLayout.setRefreshing(true);

        URL = "http://news.learnkh.com/article.php?id=" + offSet;
        Log.d("Imaged", "RULLL");
        JsonArrayRequest req = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("Imageda", "LoadImagea");
                        if (jsonArray.length() > 0) {
                            Log.d("Imaged", "LoadImage");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String title = object.getString("title");
                                    int id = object.getInt("id");
                                    if(firstLoad){
                                        numContent = id;
                                        firstLoad=false;
                                    }
                                    Article article = new Article(id, title);
                                    articleList.add(0,article);

                                    // updating offset value to highest value
                                    if (numContent-id + 2 >= offSet) {
                                        Log.d("xxxx1", "xxxx2" + id);
                                        offSet = numContent  - id + 2;
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                        //PD.dismiss();
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Apx", "Server Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        MyApplication.getInstance().addToRequestQueue(req);



    }

    @Override
    public void onRefresh() {
        fetchArticle();
    }
}
