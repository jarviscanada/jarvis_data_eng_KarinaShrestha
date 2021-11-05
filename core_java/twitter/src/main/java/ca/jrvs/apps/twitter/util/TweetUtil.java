package ca.jrvs.apps.twitter.util;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.List;

public class TweetUtil {

  /**
   * Build a tweet using the given text and coordinates
   * @param text
   * @param lng
   * @param lat
   * @return tweet object
   */
  public static Tweet buildTweet(String text, Double lng, Double lat) {
    Tweet tweet = new Tweet();
    tweet.setText(text);

    Coordinates coordinates = new Coordinates();
    coordinates.setType("Point");
    List<Double> coordinatesObj = new ArrayList<>();
    // add coordinates: [longitude, latitude]
    coordinatesObj.add(lng);
    coordinatesObj.add(lat);
    coordinates.setCoordinates(coordinatesObj);
    tweet.setCoordinates(coordinates);

    return tweet;
  }

}
