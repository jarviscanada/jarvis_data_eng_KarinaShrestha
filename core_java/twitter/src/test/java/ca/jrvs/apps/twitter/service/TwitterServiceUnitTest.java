package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

  @Mock
  CrdDao dao;

  @InjectMocks
  TwitterService service;

  @Test
  public void postTweet() {
    when(dao.create(any())).thenReturn(new Tweet());

    String text = "hello #testing_postTweet " + System.currentTimeMillis();
    Tweet testTweet = service.postTweet(TweetUtil.buildTweet(text, 87.689, 14.790053));

    testTweet.setFavorited(false);
    assertNotNull(testTweet);
    assertFalse(testTweet.isFavorited());

    Tweet tweetTooLong = new Tweet();
    tweetTooLong.setText("When you post a tweet, the service layer is responsible to check if the tweet text exceeds 140 characters and if lon/lat is out of range some text some text some more text text text text");
    try {
      service.postTweet(tweetTooLong);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void showTweet() {
    //Tweet tweet = new Tweet();
    when(dao.findById(any())).thenReturn((new Tweet()));
    String[] fields = {"created_at", "id", "id_str", "text"};
//    String text = "today is a good day! " + System.currentTimeMillis();
    Tweet tweet = service.showTweet("1001", fields);

    assertNotNull(tweet);
    assertNull(tweet.getFavoriteCount());
    String[] invalidFields = {"created_at", "ids"};
    //test fail case
    try {
      service.showTweet("30398592a", invalidFields);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void deleteTweets() {
    String text = "abc " + System.currentTimeMillis();
    Tweet tweet = new Tweet();
    tweet.setIdStr("12345");
    when(dao.deleteById(any())).thenReturn(tweet);
    String[] ids = {"12345"};
    List<Tweet> delTweets = service.deleteTweets(ids);
    assertNotNull(delTweets);

    try {
      String[] testIds = {"090790", "7384232x"};
      service.deleteTweets(testIds);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

  }
}