package edu.towson.cis.cosc490.jdehlinger.reddit.parsers;
import java.util.List;

import edu.towson.cis.cosc490.jdehlinger.reddit.main.RedditItem;

public interface FeedParser {
	List<RedditItem> parse();
}
