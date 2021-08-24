package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
import org.springframework.util.StringUtils;

public class TwitterController implements Controller {

  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";

  private Service service;

  //@Autowired
  public TwitterController(Service service) {
    this.service = service;
  }

  /**
   * Parse user argument and post a tweet by calling service classes
   *
   * @param args
   * @return a posted tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet postTweet(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException(
          "USAGE: TwitterCLIApp post \"tweet_text\"\"latitude:longitude\" ");
    }
    String text = args[1];
    String coord = args[2];
    String[] coordArray = coord.split(COORD_SEP);
    if (coordArray.length != 2 || StringUtils.isEmpty(text)) {
      throw new IllegalArgumentException(
          "Invalid location format\nUSAGE: TwitterCLIApp post \"tweet_text\"\"latitude:longitude\"");
    }
    Double lat = 1d;
    Double lng = -1d;
    try {
      lng = Double.parseDouble(coordArray[0]);
      lat= Double.parseDouble(coordArray[1]);
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Invalid location format\nUSAGE: TwitterCLIApp post \"tweet_text\"\"latitude:longitude\"");
    }
    Tweet postTweet = TweetUtil.buildTweet(text, lng, lat);
    return service.postTweet(postTweet);
  }

  /**
   * Parse user argument and search a tweet by calling service classes
   *
   * @param args
   * @return a tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet showTweet(String[] args) {
    Tweet displayTweet;
    if (args.length != 3) {
      throw new IllegalArgumentException("View Correct Usage: TwitterCLIApp show [tweet_id] [field1, field2..] ");
    }
    String id = args[1];
    String[] fields = args[2].split(COMMA);
    displayTweet = service.showTweet(id, fields);

    return displayTweet;
  }

  /**
   * Parse user argument and delete tweets by calling service classes
   *
   * @param args
   * @return a list of deleted tweets
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public List<Tweet> deleteTweet(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException("Error. View correct usage: TwitterCLIApp delete [id1, id2, ..]");
    }
    String[] ids = args[1].split(COMMA);
    return service.deleteTweets(ids);
  }
}
