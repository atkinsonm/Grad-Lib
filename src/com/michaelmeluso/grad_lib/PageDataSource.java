package com.michaelmeluso.grad_lib;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PageDataSource {
	// Database fields
	  private SQLiteDatabase database;
	  private DataBaseHelper dbHelper;
	  private String[] allColumns = { 
		  DataBaseHelper.COLUMN_ID,
		  DataBaseHelper.COLUMN_BEGINNING,
	      DataBaseHelper.COLUMN_MIDDLE,
	      DataBaseHelper.COLUMN_END,
	      DataBaseHelper.COLUMN_NOUN,
	      DataBaseHelper.COLUMN_VERB };

	  public PageDataSource(Context context) {
	    dbHelper = new DataBaseHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public Page[] getAllPages() {

	    Cursor cursorSize = database.query(DataBaseHelper.TABLE_PAGES,
	        allColumns, null, null, null, null, null);
	    
	    int size = 1;
	    
	    if(cursorSize.moveToFirst()) {
	    	do {
	    		size++;
	    		System.out.println("The size of the pages array will be " + size);
			} while (cursorSize.moveToNext());
	    }
	    
	    Page[] pages = new Page[size];
	    
	    Cursor cursor = database.query(DataBaseHelper.TABLE_PAGES,
		        allColumns, null, null, null, null, null);
	    
	    int ii = 0;
	    
	    if(cursor.moveToFirst()) {
    		do {
    			System.out.println(cursor.getLong(0));
    	    	System.out.println(cursor.getString(1));
    	    	System.out.println(cursor.getString(2));
    	    	System.out.println(cursor.getString(3));
    	    	System.out.println(cursor.getString(4));
    	    	System.out.println(cursor.getString(5));
    	    	
    	    	Page page = new Page();
    		    page.setID(cursor.getLong(0));
    		    page.setBeginning(cursor.getString(1));
    		    page.setMiddle(cursor.getString(2));
    		    page.setEnd(cursor.getString(3));
    		    page.setNoun(cursor.getString(4));
    		    page.setVerb(cursor.getString(5));
    	    	
    	    	pages[ii] = page;
    	    	ii++;
  
    		} while (cursor.moveToNext());
    	}
	  
	    // make sure to close the cursor
	    cursor.close();
	    return pages;
	  }
	  
	  public void setNounAndVerb(String noun, String verb, int pageNumber) {
		  String sql = "UPDATE " + DataBaseHelper.TABLE_PAGES + "\n"
				  + " SET " + DataBaseHelper.COLUMN_NOUN + " = '" + noun + "', " + "\n"
				  + DataBaseHelper.COLUMN_VERB + " = '" + verb + "' " + "\n"
				  + "WHERE " + DataBaseHelper.COLUMN_ID + " = " + pageNumber 
				  + ";";
		  
		  database.execSQL(sql);
	  }
	  
	  public void initDB() {
		  database.execSQL("DROP TABLE IF EXISTS " + DataBaseHelper.TABLE_PAGES);
		  System.out.println(DataBaseHelper.DATABASE_CREATE);
		  database.execSQL(DataBaseHelper.DATABASE_CREATE);
		  database.execSQL(DataBaseHelper.DATABASE_DEFAULT_PAGE);
		  database.execSQL(DataBaseHelper.DATABASE_FIRST_PAGE);
	  }
	
}
