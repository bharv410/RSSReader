package edu.towson.cis.cosc490.jdehlinger.reddit;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.towson.cis.cosc490.jdehlinger.reddit.main.RedditMainActivity;

public class MainActivity extends ListActivity {
	ListView lv;
	HashMap<String, String> nameAndUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nameAndUrl = new HashMap<String, String>();
		nameAndUrl.put("Wired", "http://feeds.wired.com/wired/index");
		nameAndUrl.put("CNN", "http://rss.cnn.com/rss/cnn_topstories.rss");
		nameAndUrl.put("US. Dept Of Education", "http://www2.ed.gov/rss/edgov.xml");
		

		ArrayList<String> list = new ArrayList<String>();
		for (String titles : nameAndUrl.keySet()) {
			list.add(titles);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		setListAdapter(adapter);
		lv = getListView();
		

	}
	 @Override
	 public void onListItemClick(ListView l, View v, int position, long id) {
	     String site= lv.getAdapter().getItem(position).toString();
	     Intent i = new Intent(getApplicationContext(), RedditMainActivity.class);
	     i.putExtra("url",nameAndUrl.get(site));
	     startActivity(i);

	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
