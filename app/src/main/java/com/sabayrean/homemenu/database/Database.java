package com.sabayrean.homemenu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sabayrean.homemenu.vocabulary.Category;
import com.sabayrean.homemenu.vocabulary.Vocabulary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LAYLeangsros on 25/06/2015.
 */
public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "khmerLearning";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_VOCABULARY = "vocabulary";
    private static final String KEY_ID = "id";
    private static final String KEY_KH = "khmer";
    private static final String KEY_EN = "english";
    private static final String KEY_FR = "french";
    private static final String KEY_VOICE = "voice";
    private static final String KEY_ICON = "icon";
    private static final String KEY_CATEGORY = "category";


    private SQLiteDatabase database;
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        database=sqLiteDatabase;
        String sqlCategory = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY + " ( "
                + KEY_ID + " INTEGER, "
                + KEY_KH + " TEXT, "
                + KEY_EN + " TEXT PRIMARY KEY, "
                + KEY_FR +" TEXT, "
                + KEY_ICON+" TEXT)";

        String sqlVocabulary = "CREATE TABLE IF NOT EXISTS " + TABLE_VOCABULARY + " ( "
                + KEY_ID + " INTEGER, "
                + KEY_KH + " TEXT, "
                + KEY_EN+ " TEXT, "
                + KEY_FR +" TEXT, "
                + KEY_VOICE+" TEXT,"
                + KEY_CATEGORY + " TEXT NOT NULL, "
                + " FOREIGN KEY ("+KEY_CATEGORY+") REFERENCES "+ TABLE_CATEGORY+" ("+KEY_EN+"))";


        database.execSQL(sqlCategory);
        database.execSQL(sqlVocabulary);


    }

    protected void addVocabulary(Vocabulary vocabulary, String category) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, vocabulary.id);
        values.put(KEY_VOICE, vocabulary.voice);
        values.put(KEY_KH, vocabulary.kh);
        values.put(KEY_FR, vocabulary.fr);
        values.put(KEY_EN, vocabulary.en);
        values.put(KEY_CATEGORY, category);
        database.insertWithOnConflict(TABLE_VOCABULARY, null,  values,SQLiteDatabase.CONFLICT_REPLACE);
    }
    protected  void addCategory(Category category){
        ContentValues values = new ContentValues();
        values.put(KEY_ID, category.id);
        values.put(KEY_KH, category.kh);
        values.put(KEY_FR, category.fr);
        values.put(KEY_EN, category.en);
        values.put(KEY_ICON, category.icon);
        database.insertWithOnConflict(TABLE_CATEGORY, null,  values,SQLiteDatabase.CONFLICT_REPLACE);
    }


    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<Category>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;
        database=this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setKh(cursor.getString(0));
                category.setFr(cursor.getString(1));
                category.setEn(cursor.getString(2));
                category.setIcon(cursor.getString(3));
                categoryList.add(category);
                Log.d("Database getting", "Show:" + category.en);
            } while (cursor.moveToNext());
        }
        // return quest list
        return categoryList;
    }
    public List<Vocabulary> getAllVocabularies(String category) {
        List<Vocabulary> vocabularyList = new ArrayList<Vocabulary>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_VOCABULARY + " WHERE " + KEY_CATEGORY + "=\"" + category + "\"";
        database=this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Vocabulary vocabulary = new Vocabulary();
                vocabulary.setId(cursor.getInt(0));
                vocabulary.setKh(cursor.getString(1));
                vocabulary.setFr(cursor.getString(2));
                vocabulary.setEn(cursor.getString(3));
                vocabulary.setVoice(cursor.getString(4));
                vocabularyList.add(vocabulary);
                Log.d("Database getting", "Show:" + category);
            } while (cursor.moveToNext());
        }
        // return quest list
        return vocabularyList;
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_VOCABULARY);
        // Create tables again
        onCreate(database);
    }
}
