package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.junit.Before;
import org.junit.Test;

public class TwitterDaoIntTest {

  private TwitterDao tdao;

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
  }

  @Test
  public void create() {
    String hashTag = "#abc";
    String text = "@Karina testing " + hashTag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lng = -1d;
    Tweet postTweet = TweetUtil.buildTweet(text, lng, lat);

    Tweet tweet = tdao.create(postTweet);

    assertEquals(text, tweet.getText());
    assertNotNull(tweet.getCoordinates());
    assertEquals(2, tweet.getCoordinates().getCoordinates().size());
    assertEquals(lng, tweet.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));
    assertTrue(hashTag.contains(tweet.getEntities().getHashtags().get(0).getText()));
  }

  @Test
  public void findById() {
    String hashTag = "findById";
    String text = "@Karina test2 " + hashTag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lng = -1d;
    Tweet postTweet = TweetUtil.buildTweet(text, lng, lat);

    Tweet tweet = tdao.create(postTweet);
    Tweet tweetFound = tdao.findById(tweet.getIdStr());

    assertEquals(text, tweetFound.getText());
    assertNotNull(tweetFound.getCoordinates());
    assertEquals(lng, tweetFound.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, tweetFound.getCoordinates().getCoordinates().get(1));
  }

  @Test
  public void deleteById() {
    String hashTag = "deleting";
    String text = "@Karina test3 " + hashTag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lng = -1d;
    Tweet postTweet = TweetUtil.buildTweet(text, lng, lat);

    Tweet tweet = tdao.create(postTweet);
    Tweet tweetDel = tdao.deleteById(tweet.getIdStr());

    assertEquals(text, tweetDel.getText());
    assertNotNull(tweetDel.getCoordinates());
    assertEquals(lng, tweetDel.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, tweetDel.getCoordinates().getCoordinates().get(1));
  }
}