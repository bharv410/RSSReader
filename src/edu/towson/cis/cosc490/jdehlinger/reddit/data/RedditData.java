package edu.towson.cis.cosc490.jdehlinger.reddit.data;

import static android.provider.BaseColumns._ID;
import static edu.towson.cis.cosc490.jdehlinger.reddit.data.Constants.DES;
import static edu.towson.cis.cosc490.jdehlinger.reddit.data.Constants.LINK;
import static edu.towson.cis.cosc490.jdehlinger.reddit.data.Constants.PUBDATE;
import static edu.towson.cis.cosc490.jdehlinger.reddit.data.Constants.TABLE_NAME;
import static edu.towson.cis.cosc490.jdehlinger.reddit.data.Constants.TITLE;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RedditData extends SQLiteOpenHelper {
   private static final String DATABASE_NAME = "Reddits.db";
   private static final int DATABASE_VERSION = 1;

   /** Create a helper object for the Reddits database */
   public RedditData(Context ctx) { 
      super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase db) { 
      db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
    		  + DES + " TEXT," + TITLE + " TEXT NOT NULL," 
    		  + LINK + " TEXT, "
    		  + PUBDATE + " TEXT);");
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, 
         int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
      onCreate(db);
   }
}