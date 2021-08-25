package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

  @Mock
  TwitterService service;

  @InjectMocks
  TwitterController controller;

  private Tweet postTweets;
  Double lng = 15d;
  Double lat = 25d;
  String coordinates = "15:25";

  @Before
  public void setUp() throws Exception {
    String text = "Controller test.. ";
    postTweets = TweetUtil.buildTweet(text, lng, lat);
  }

  @Test
  public void postTweet() {
    when(service.postTweet(any())).thenReturn(postTweets);
    String[] args = {"post", postTweets.getText(), coordinates};

    Tweet posted = controller.postTweet(args);
    assertNotNull(posted);
    assertEquals(lng, posted.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, posted.getCoordinates().getCoordinates().get(1));

    try {
      String[] invalidArgs = {"post", "random text", "-20:5s"};
      controller.postTweet(invalidArgs);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

  }

  @Test
  public void showTweet() {
    String fields = "created_at, id, retweeted, favorite_count ";
    when(service.showTweet(any(), any())).thenReturn(postTweets);
    String[] args = {"show", postTweets.getIdStr(), fields};
    Tweet displayTweet = controller.showTweet(args);

    assertNotNull(displayTweet);
    assertNotNull(displayTweet.getText());

    try {
      String[] invalidArgs = {"show", "093282982y", ""};
      controller.postTweet(invalidArgs);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void deleteTweet() {
    List<Tweet> delTweets = new ArrayList<Tweet>();
    delTweets.add(postTweets);

    Tweet testTweet = new Tweet();
    testTweet.setText("delete test tweet");
    testTweet.setIdStr("88239920");

    when(service.deleteTweets(any())).thenReturn(delTweets);
    String[] args = {"delete", testTweet.getIdStr()};
    List<Tweet> deleted = controller.deleteTweet(args);
    assertNotNull(deleted);
  }
}