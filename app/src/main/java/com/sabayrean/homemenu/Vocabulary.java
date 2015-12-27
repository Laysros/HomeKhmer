package com.sabayrean.homemenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by LAYLeangsros on 13/04/2015.
 */
public class Vocabulary extends Activity{
    GridView gridView;
    ArrayList<VocabularyItem> gridArray = new ArrayList<VocabularyItem>();
    VocabularyAdapter vocabularyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabulary_layout);

        Bitmap categoryOne = BitmapFactory.decodeResource(this.getResources(), R.drawable.voc_c1);
        gridArray.add(new VocabularyItem(categoryOne, "Home"));
        gridArray.add(new VocabularyItem(categoryOne, "School"));
        gridView = (GridView) findViewById(R.id.vocabularyGridView);
        vocabularyAdapter = new VocabularyAdapter(this, R.layout.item_category_vocabulary, gridArray);
        gridView.setAdapter(vocabularyAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getBaseContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
                view.setSelected(true);
                Intent intent = new Intent(getBaseContext(), VocabularyList.class);

            }
        });
    }
}
