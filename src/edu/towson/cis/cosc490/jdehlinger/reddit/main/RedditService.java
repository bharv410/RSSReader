package edu.towson.cis.cosc490.jdehlinger.reddit.main;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import edu.towson.cis.cosc490.jdehlinger.reddit.R;
import edu.towson.cis.cosc490.jdehlinger.reddit.data.RedditData;
import edu.towson.cis.cosc490.jdehlinger.reddit.parsers.AndroidSaxFeedParser;

public class RedditService extends Service {

	public static final String REDDIT_SERVICE = "edu.towson.cis.cosc490.jdehlinger.reddit.main.RedditService.SERVICE";
	private String feedURL = "http://www.reddit.com/.rss";
	private List<RedditItem> reddits;
	private RedditData redditdata;

	@Override
	public void onCreate() {
		super.onCreate();
	    redditdata = new RedditData(this);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		doServiceStart(intent, startId);
	    return Service.START_REDELIVER_INTENT;
	}
	
	public void doServiceStart(Intent intent, int startID) {
		checkFeed();
		System.out.println("Running service...");
	}
	
	private void checkFeed(){
    	try{
    	
	    	AndroidSaxFeedParser parser = new AndroidSaxFeedParser(feedURL);
	       	reddits = parser.parse();
	    	
	       	String link = reddits.get(0).getLink().toString();
	        System.out.println("The title" + link);
	        
	        SQLiteDatabase db = redditdata.getReadableDatabase();
	        
	        Cursor cursor = db.rawQuery("select 1 from Reddits where link=?", new String[] {link});
	        boolean exists = (cursor.getCount() > 0);
	        cursor.close();
	        db.close();
	           
	        if (!exists){
	        	doNotification();
	        }
    	} catch (Throwable t){}
    }
	
	private void doNotification(){
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		
		int icon = R.drawable.reddit_notify;
		CharSequence tickerText = "New Reddit articles!";
		long when = System.currentTimeMillis();
		
		Notification notification = new Notification(icon, tickerText, when);

		Context context = getApplicationContext();
		CharSequence contentTitle = "New Reddit news!";
		CharSequence contentText = "Reddit: The front page of the internet.";
		Intent notificationIntent = new Intent(this, RedditMainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		mNotificationManager.notify(100, notification);
	}
	
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
