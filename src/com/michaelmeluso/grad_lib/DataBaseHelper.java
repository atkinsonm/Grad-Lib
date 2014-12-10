package com.michaelmeluso.grad_lib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

  public static final String TABLE_PAGES = "TPages";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_BEGINNING = "beginning";
  public static final String COLUMN_MIDDLE = "middle";
  public static final String COLUMN_END = "end";
  public static final String COLUMN_NOUN = "noun";
  public static final String COLUMN_VERB = "verb";
  public static final String DATABASE_DEFAULT_PAGE = "INSERT INTO TPages VALUES(0, 'this', 'that', 'the other', 'it', 'has');";
  public static final String DATABASE_FIRST_PAGE = "INSERT INTO TPages VALUES(1, 'Today in Eickhoff, I was surprised to find a', '. I think it was because of the theme of Eick this week. Before I went to class, I remembered I had to go to my room to', 'my homework. I was really glad I did not forget to do that!', 'it', 'has');";
  private static final String DATABASE_NAME = "pages.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  public static final String DATABASE_CREATE = "create table "
      + TABLE_PAGES + "(" + 
      COLUMN_ID + " integer primary key autoincrement, " + 
      COLUMN_BEGINNING + " text, " + 
      COLUMN_MIDDLE + " text, " + 
      COLUMN_END + " text, " + 
      COLUMN_NOUN + " text, " + 
      COLUMN_VERB + " text);";
  
  public DataBaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(DataBaseHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGES);
    onCreate(db);
  }
  

} 