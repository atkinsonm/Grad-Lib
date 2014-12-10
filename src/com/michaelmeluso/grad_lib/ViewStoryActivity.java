package com.michaelmeluso.grad_lib;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

public class ViewStoryActivity extends ActionBarActivity {
	private PageDataSource datasource;
	
	private static final String TAG = "ViewStoryActivity";
	
	private TextView mStoryTextView;
	private String story = "";
	
	public static final String EXTRA_PAGE_ID =
			"com.michaelmeluso.android.grad-lib.page_id";
	
	private int pageId;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);
        
        pageId = getIntent().getIntExtra(EXTRA_PAGE_ID, 0);
        
        mStoryTextView = (TextView)findViewById(R.id.story_text_view);
        
    	datasource = new PageDataSource(this);
    	System.out.println("The database is being opened");
    	datasource.open();
    	
    	Page[] pages = new Page[10];
    	pages = datasource.getAllPages();
    	
    	Log.d(TAG, (pages[pageId].getBeginning() + " " + pages[pageId].getNoun()));
    	story += pages[pageId].getBeginning() + " " + pages[pageId].getNoun();
    	story += pages[pageId].getMiddle() + " " + pages[pageId].getVerb();
    	story += " " + pages[pageId].getEnd();
        mStoryTextView.setText(story);
	}
}
