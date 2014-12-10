package com.michaelmeluso.grad_lib;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class EditStoryActivity extends ActionBarActivity {
	
	private PageDataSource datasource;
	
	private static final String TAG = "EditStoryActivity";
	private Button mNounButton;
	private Button mVerbButton;
	//private Button mAdjectiveButton;
	private Button mDoneButton;
	private EditText mRandomWordOneEditText;
	private EditText mRandomWordTwoEditText;
	//private EditText mRandomWordThreeEdi+tText;
	private int pageId = 1;
	
    public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_story);
       
        System.out.println("The database is being created");
    	datasource = new PageDataSource(this);
    	System.out.println("The database is being opened");
    	datasource.open();
    	datasource.initDB();
    	
    	Page[] pages = new Page[10];
    	pages = datasource.getAllPages();
     
    	System.out.println("The noun on page 0 is " + pages[0].getNoun());
    	System.out.println("The verb on page 0 is " + pages[0].getVerb());
        
        mRandomWordOneEditText = (EditText)findViewById(R.id.random_word_one_edit_text);
        mRandomWordTwoEditText = (EditText)findViewById(R.id.random_word_two_edit_text);
        //mRandomWordThreeEditText = (EditText)findViewById(R.id.random_word_three_edit_text);
        
        mNounButton = (Button)findViewById(R.id.noun_button);
        mNounButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkConnection()) {
					getRandomWord("noun");
				} else {
					mRandomWordOneEditText.setText("lorem ipsum");
				}
			}
		});
        
        mVerbButton = (Button)findViewById(R.id.verb_button);
        mVerbButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkConnection()) {
					getRandomWord("verb");
				} else {
					mRandomWordOneEditText.setText("lorem ipsum");
				}
			}
		});
        
        /*mAdjectiveButton = (Button)findViewById(R.id.adjective_button);
        mAdjectiveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkConnection()) {
					getRandomWord("adjective");
				} else {
					mRandomWordThreeEditText.setText("lorem ipsum");
				}
			}
		});*/
        
        mDoneButton = (Button)findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datasource.setNounAndVerb(mRandomWordOneEditText.getText().toString().trim(), 
										  mRandomWordTwoEditText.getText().toString().trim(), 
										  pageId);
				Intent i = new Intent(EditStoryActivity.this, ViewStoryActivity.class);
				i.putExtra(ViewStoryActivity.EXTRA_PAGE_ID, pageId);
				Log.d(TAG, "Starting second activity");
				startActivity(i);
			}
		});
    }
    
    private boolean checkConnection() {
    	ConnectivityManager connMgr = (ConnectivityManager) 
    	        getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	if (networkInfo != null && networkInfo.isConnected()) {
    		return true;
    	} else {
    	    return false;
    	}
    }
    
    // Uses AsyncTask to create a task away from the main UI thread. This task takes a 
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
       @Override
       protected String doInBackground(String... urls) {
             
           // params comes from the execute() call: params[0] is the url.
           try {
               return downloadUrl(urls[0]);
           } catch (IOException e) {
               return "Unable to retrieve web page. URL may be invalid.";
           }
       }
       // onPostExecute displays the results of the AsyncTask.
       @Override
       protected void onPostExecute(String result) {
    	   String word = result.substring(
    			   (result.indexOf("<span class='crux'>") + 19), 
    			   result.indexOf("</span>", result.indexOf("<span class='crux'>")));
    	   
    	   if (result.contains("<title>Generate Random Nouns - Noun List</title>")) {
    		   setRandomNoun(word);
    	   } else if (result.contains("<title>Random Verb Generator</title>")) {
    		   setRandomVerb(word);
    	   } else if (result.contains("<title>Random Adjective Generator - Adjective List</title>")) {
    		   //setRandomAdjective(word); 
    	   } else {
    		   Log.d(TAG, "Somehow got other part of speech. Check URL and response source.");
    	   }
      }
    }
    
	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	private String downloadUrl(String myurl) throws IOException {
		InputStream is = null;
	    // Only display the first 3500 characters of the retrieved
	    // web page content.
	    int len = 3500;
	         
	    try {
	    	URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        // Starts the query
	        conn.connect();
	        int response = conn.getResponseCode();
	        Log.d(TAG, "The response is: " + response);
	        is = conn.getInputStream();
	
	        // Convert the InputStream into a string
	        String contentAsString = readIt(is, len);
	        return contentAsString;
	         
	     // Makes sure that the InputStream is closed after the app is
	     // finished using it.
	     } finally {
	         if (is != null) {
	             is.close();
	         } 
	     }
	}
	
	// Reads an InputStream and converts it to a String.
	public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	    Reader reader = null;
	    reader = new InputStreamReader(stream, "UTF-8");        
	    char[] buffer = new char[len];
	    reader.read(buffer);
	    return new String(buffer);
	}
    
    private void getRandomWord(String type) {
    	final String baseURL = "http://www.randomlists.com/";
    	String wordsURL;
    	
    	if (type == "noun") {
    		wordsURL = baseURL + "random-nouns/";
    		new DownloadWebpageTask().execute(wordsURL);
    	} else if (type == "verb") {
    		wordsURL = baseURL + "random-verbs/";
    		new DownloadWebpageTask().execute(wordsURL);
    	} else if (type == "adjective") {
    		wordsURL = baseURL + "random-adjectives/";
    		new DownloadWebpageTask().execute(wordsURL);
    	} else if (type == "adverb") {
    		wordsURL = baseURL + "random-adverbs/";
    		new DownloadWebpageTask().execute(wordsURL);
    	}
    }
    
    private void setRandomNoun(String result) {
    	mRandomWordOneEditText.setText(result);
    }
    
    private void setRandomVerb(String result) {
    	mRandomWordTwoEditText.setText(result);
    }
    
    /*private void setRandomAdjective(String result) {
    	mRandomWordThreeEditText.setText(result);
    }*/
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.story, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

