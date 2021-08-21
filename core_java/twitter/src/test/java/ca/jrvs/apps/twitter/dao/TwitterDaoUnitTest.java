package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

  @Mock
  HttpHelper mockHelper;

  @InjectMocks
  TwitterDao tdao;

  Tweet expectedTweet;
  String tweetJsonStr;

  @Before
  public void setUp() throws Exception {
    String tweetJsonStr = "{\n"
        + "   \"created_at\":\"Mon Feb 18 21:24:39 + 0000 2019\",\n"
        + "   \"id\":1097607853932564480,\n"
        + "   \"id_str\":\"1097607853932564480\", \n"
        + "   \"text\":\"test with loc223\",\n"
        + "   \"entities\":{\n"
        + "       \"hashtags\":[],"
        + "       \"user_mentions\":[]"
        + "   },\n"
        + "   \"coordinates\":null,"
        + "   \"retweet_count\":0,\n"
        + "   \"favorite_count\":0,\n"
        + "   \"favorited\":false,\n"
        + "   \"retweeted\":false\n"
        + "}";

    expectedTweet = JsonUtil.toObjectFromJson(tweetJsonStr, Tweet.class);
  }

  @Test
  public void showTweet() throws IOException {

    //test failed request
    String hashTag = "abc";
    String text = "@someone some text " + hashTag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lng = -1d;
    //exception is expected here
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      tdao.create(TweetUtil.buildTweet(text, lng, lat));
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    //create a spyDao that can fake parseResponseBody return value
    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(tdao);
    //mock parseResponseBody
    Tweet expectedTweet = JsonUtil.toObjectFromJson(tweetJsonStr, Tweet.class);
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
    Tweet tweet = spyDao.create(TweetUtil.buildTweet(text, lng, lat));
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void postTweet() {
    //test failed request - exception is expected here
    when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      tdao.findById("");
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    //test successful request - create a spyDao that can fake parseResponseBody return value
    when(mockHelper.httpGet(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(tdao);
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
    Tweet tweet = spyDao.findById(expectedTweet.getIdStr());
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void deleteTweet() {
    //test failed request - exception is expected here
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      tdao.deleteById("");
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    //test successful request - create a spyDao that can fake parseResponseBody return value
    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(tdao);
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
    Tweet tweet = spyDao.deleteById(expectedTweet.getIdStr());
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }
}