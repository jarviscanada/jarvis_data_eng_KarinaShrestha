package ca.jrvs.apps.twitter.dao.helper;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

public class TwitterHttpHelperTest {

  private TwitterHttpHelper twitterHttpHelper;

  @Before
  public void setUp() throws Exception {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);

    twitterHttpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
  }

  @Test
  public void testHttpPost() throws IOException, URISyntaxException {
    HttpResponse response = twitterHttpHelper.httpPost(new URI("https://api.twitter.com/1.1/statuses/update.json?status=junit_helperTest"));
    System.out.println(EntityUtils.toString(response.getEntity()));
  }

  @Test
  public void testHttpGet() throws IOException, URISyntaxException {
    HttpResponse response = twitterHttpHelper.httpPost(new URI("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=Karina01518285"));
    System.out.println(EntityUtils.toString(response.getEntity()));
  }
}