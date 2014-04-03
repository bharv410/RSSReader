package edu.towson.cis.cosc490.jdehlinger.reddit.main;

import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.towson.cis.cosc490.jdehlinger.reddit.R;
import edu.towson.cis.cosc490.jdehlinger.reddit.data.Constants;
import edu.towson.cis.cosc490.jdehlinger.reddit.data.RedditData;
import edu.towson.cis.cosc490.jdehlinger.reddit.parsers.AndroidSaxFeedParser;

public class RedditMainActivity extends ListActivity {
	
	private List<RedditItem> reddits;
	private RedditData redditdata;
	private boolean serviceFlag;
	AlarmManager alarms;
	private String feedURL;
    
	@Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
        setContentView(R.layout.main);
        Bundle extras = getIntent().getExtras();
        feedURL = extras.getString("url");
        redditdata = new RedditData(this);
        loadFeed();
        setAlarm();  

    } 
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent viewMessage = new Intent(Intent.ACTION_VIEW, Uri.parse(reddits.get(position).getLink().toExternalForm()));
		startActivity(viewMessage);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater(); 
		inflater.inflate(R.menu.options_menu, menu); 
		return true;
	}
 
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		super.onMenuItemSelected(featureId, item);
		switch (item.getItemId()) 
		{
		case R.id.Refresh:
			loadFeed();
		break;
		case R.id.Start: 
	          if(!serviceFlag) setAlarm();
	    break;
		case R.id.Stop: 
	          if(serviceFlag) cancelAlarm();
	    break;
		} 
		return true;
	}
	
	private void setAlarm(){
		serviceFlag = true;
	    Intent downloader = new Intent(this, AlarmReceiver.class);
	    PendingIntent recurringDownload = PendingIntent.getBroadcast(this, 0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
	    
	    alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	    alarms.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 60000, recurringDownload);
	    System.out.println("Setting alarm...");
	}
	
	private void cancelAlarm(){
		serviceFlag = false;
		Intent downloader = new Intent(this, AlarmReceiver.class);
	    PendingIntent recurringDownload = PendingIntent.getBroadcast(this, 0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
		
	    alarms.cancel(recurringDownload);
	}
	
	private void loadFeed(){
    	try{
    	
	    	AndroidSaxFeedParser parser = new AndroidSaxFeedParser(feedURL);
	       	reddits = parser.parse();
	    	
	    	List<String> titles = new ArrayList<String>(reddits.size());
	    	
	    	for (RedditItem rd : reddits){
	    		titles.add(rd.getTitle());
	    		
	    		try{
	    			addFarkItem(rd.getDescription(), "" + rd.getTitle(), rd.getLink().toString(), rd.getDate());
	    		}
	    		catch(Exception e){e.printStackTrace();}
	    	}
	    	
	    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.reddit_item,titles);
	    	this.setListAdapter(adapter);
    	} catch (Throwable t){}
    }
		
	private void addFarkItem(String des,String title,String link,String pubDate){
        SQLiteDatabase db = redditdata.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.DES, des);
        values.put(Constants.TITLE, title);
        values.put(Constants.LINK, link);
        values.put(Constants.PUBDATE, pubDate);
        db.insertOrThrow(Constants.TABLE_NAME, null, values);
     }	
}