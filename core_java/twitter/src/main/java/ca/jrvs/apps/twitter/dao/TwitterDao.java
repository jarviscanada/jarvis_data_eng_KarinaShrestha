package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.example.JSONParser;
import ca.jrvs.apps.twitter.model.Tweet;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterDao implements CrdDao<Tweet, String> {

  static final Logger logger = LoggerFactory.getLogger(TwitterDao.class);

  //URI constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";

  //URI symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";

  //Response code
  private static final int HTTP_OK = 200;

  private HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  @Override
  public Tweet create(Tweet tweet) {
    //Construct URI
    URI uri;
    uri = getPostUri(tweet);

    //Execute HTTP Request and deserialize response
    HttpResponse response = httpHelper.httpPost(uri);
    return parseResponseBody(response, HTTP_OK);
  }

  private URI getPostUri(Tweet tweet) {
    URI uri;

    PercentEscaper percentEscaper = new PercentEscaper("", false);
    String longitude = tweet.getCoordinates().getCoordinates().get(0).toString();
    String latitude = tweet.getCoordinates().getCoordinates().get(1).toString();

    try {
      uri = new URI(API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL
            + percentEscaper.escape(tweet.getText()) + AMPERSAND + "long" + EQUAL + longitude
            + AMPERSAND + "lat" + EQUAL + latitude);
      return uri;
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid input. Unable to generate URI");
    }
  }

  /**
   * Check response status code - Convert Response Entity to Tweet
   */
  public Tweet parseResponseBody(HttpResponse response, Integer expectedStatusCode) {
    Tweet tweet = null;

    //Check response status
    int status = response.getStatusLine().getStatusCode();
    if (status != expectedStatusCode) {
      try {
        logger.error(EntityUtils.toString(response.getEntity()));
      } catch (IOException e) {
        logger.error("Response has no entity");
      }
      throw new RuntimeException("Unexpected HTTP status: " + status);
    }

    if(response.getEntity() == null) {
      throw new RuntimeException("Empty response body");
    }

    //Convert Response Entity to str
    String jsonStr;
    try {
      jsonStr = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      throw new RuntimeException("Failed to convert entity to String", e);
    }

    //Deserialize JSON String to Tweet object
    try {
      tweet = JSONParser.toObjectFromJson(jsonStr, Tweet.class);
    } catch (IOException e) {
      throw new RuntimeException("Unable to convert JSON str to Object", e);
    }

    return tweet;
  }

  @Override
  public Tweet findById(String s) {
    URI uri;
    try {
      uri = new URI(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + s);
      HttpResponse response = httpHelper.httpGet(uri);
      return parseResponseBody(response, HTTP_OK);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Unable to execute HTTP request: Invalid input", e);
    }
  }

  @Override
  public Tweet deleteById(String s) {
    URI uri;
    try {
      uri = new URI(API_BASE_URI + DELETE_PATH + "/" + s + ".json");
      HttpResponse response = httpHelper.httpPost(uri);
      return parseResponseBody(response, HTTP_OK);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid input id. Unable to execute HTTP request", e);
    }
  }
}







































