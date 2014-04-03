package edu.towson.cis.cosc490.jdehlinger.reddit.data;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
   public static final String TABLE_NAME = "Reddits";

   // Columns in the Reddits database
   public static final String TITLE = "title";
   public static final String DES = "des";
   public static final String LINK = "link";
   public static final String PUBDATE = "pubDate";
}
