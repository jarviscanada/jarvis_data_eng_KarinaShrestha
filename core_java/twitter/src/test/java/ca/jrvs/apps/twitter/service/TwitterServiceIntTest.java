package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterServiceIntTest {

  private TwitterDao tdao;
  private TwitterService service;
  private Tweet postedTweet;
  private Double lng;
  private Double lat;
  private String text;

  @Before
  public void setUp() throws Exception {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    //set up dependency
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
    //pass dependency
    this.tdao = new TwitterDao(httpHelper);
    this.service = new TwitterService(tdao);
    //build a tweet
    String hashTag = "#abcd";
    text = "Karina ServiceIntTest xyz " + hashTag + " " + System.currentTimeMillis();
    lat = 1d;
    lng = -1d;

  }

  @Test
  public void postTweet() {
    Tweet postTweets = TweetUtil.buildTweet(text, lng, lat);
    postedTweet = service.postTweet(postTweets);
    //check if tweet is valid
    assertNotNull(postedTweet);
    assertEquals(lng, postedTweet.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, postedTweet.getCoordinates().getCoordinates().get(1));
    assertEquals(text, postedTweet.getText());
  }

  @Test
  public void showTweet() {
    Tweet postTweets = TweetUtil.buildTweet(text, lng, lat);
    postedTweet = service.postTweet(postTweets);
    String[] testFields = {"created_at", "id", "id_str", "text", "favorited"};
    Tweet displayTweet = service.showTweet(postedTweet.getIdStr(), testFields);

    assertNotNull(displayTweet);
    assertEquals(lng, postedTweet.getCoordinates().getCoordinates().get(0));
    assertNull(displayTweet.getFavoriteCount());
    assertNull(displayTweet.getCoordinates());
  }

  @Test
  public void deleteTweets() {
    String text = "to be deleted.. " + " " + System.currentTimeMillis();
    Tweet postTweet1 = TweetUtil.buildTweet(text, lng, lat);
    Tweet postedTweet1 = service.postTweet(postTweet1);
    Tweet postTweet2 = TweetUtil.buildTweet("to be deleted2. ", lng, lat);
    Tweet postedTweet2 = service.postTweet(postTweet2);
    String[] ids = new String[] {postedTweet1.getIdStr(), postedTweet2.getIdStr()};

    List<Tweet> delTweets = service.deleteTweets(ids);
    assertNotNull(delTweets);
    assertEquals(2, delTweets.size());
    assertEquals(lng, postedTweet1.getCoordinates().getCoordinates().get(0));
    assertEquals(lng, postedTweet2.getCoordinates().getCoordinates().get(0));
  }
}