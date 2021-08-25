package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterControllerIntTest {

  private TwitterDao tdao;
  private TwitterController controller;
  private TwitterService service;
  private Tweet tweet;
  private Double lng;
  private Double lat;
  private String hashTag;
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
    this.controller = new TwitterController(service);

    //build a tweet
    String hashTag = "#abcd";
    text = "Karina ServiceIntTest_2 " + hashTag + " " + System.currentTimeMillis();
    lng = 1d;
    lat = -1d;
  }

  @Test
  public void postTweet() {
    String[] args = {"post", text, lng + ":" + lat};
    Tweet postedTweet = controller.postTweet(args);
    assertNotNull(postedTweet);
    assertEquals(text, postedTweet.getText());
  }

  @Test
  public void showTweet() {
    String[] args = new String[] {"post", text, lng + ":" + lat};
    Tweet postedTweet = controller.postTweet(args);
    String fields = "created_at, id, id_str, favorited";
    String[] showArgs = new String[] {"show", postedTweet.getIdStr(), fields};
    Tweet displayTweet = controller.showTweet(showArgs);
    assertNotNull(displayTweet);
  }

  @Test
  public void deleteTweet() {
    String[] args = {"post", text, lng + ":" + lat};
    Tweet postedTweet = controller.postTweet(args);
    String[] deleteArgs = {"delete", postedTweet.getIdStr()};
    List<Tweet> output = controller.deleteTweet(deleteArgs);
    assertNotNull(output);
    assertEquals(postedTweet.getId(), output.get(0).getId());
  }
}