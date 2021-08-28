package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class TwitterService implements Service {

  private CrdDao dao;

  @Autowired
  public TwitterService(CrdDao dao) {
    this.dao = dao;
  }

  @Override
  public Tweet postTweet(Tweet tweet) {
    //Business logic:
    //e.g. text length, lat/lng range, id format
    validatePostTweet(tweet);

    //create tweet via dao
    return (Tweet) dao.create(tweet);
  }

  /**
   * Validate tweet based on length of text and longitude/latitude values
   * @param tweet
   */
  private void validatePostTweet(Tweet tweet) {
    if (tweet.getText().length() > 140) {
      throw new IllegalArgumentException("Text exceeds the 140 characters limit");
    }
    if (tweet.getCoordinates() != null) {
      Double lng = tweet.getCoordinates().getCoordinates().get(0);
      Double lat = tweet.getCoordinates().getCoordinates().get(1);
      if (lng < -180 || lng > 180) {
        throw new IllegalArgumentException("Longitude value out of range");
      }
      if (lat < -90 || lat > 90) {
        throw new IllegalArgumentException("Latitude value out of range");
      }
    }
  }

  @Override
  public Tweet showTweet(String id, String[] fields) {
    String[] tweetData = new String[] {"created_at", "id", "id_str", "text", "entities", "coordinates", "retweet_count", "favorite_count", "favorited", "retweeted"};
    List<String> jsonFields = Arrays.asList(tweetData);
    List<String> allFields = Arrays.asList(fields);
    validateId(id);
    Tweet tweet = (Tweet) dao.findById(id);
    Tweet displayTweet = new Tweet();

    // set fields not in list to null
    if (fields != null) {
      for (String field : allFields) {
//        if (!allFields.contains(field)) {
//          throw new IllegalArgumentException("Invalid field.");
//        }
//      }
        if (!field.equals("created_at")) {
          displayTweet.setCreatedAt(null);
        }
        if (!field.equals("id")) {
          displayTweet.setId(null);
        }
        if (!field.equals("id_str")) {
          displayTweet.setIdStr(null);
        }
        if (!field.equals("text")) {
          displayTweet.setText(null);
        }
        if (!field.equals("entities")) {
          displayTweet.setEntities(null);
        }
        if (!field.equals("coordinates")) {
          displayTweet.setCoordinates(null);
        }
        if (!field.equals("retweet_count")) {
          displayTweet.setRetweetCount(null);
        }
        if (!field.equals("favorite_count")) {
          displayTweet.setFavoriteCount(null);
        }
        if (!field.equals("favorited")) {
          displayTweet.setFavorited(null);
        }
        if (!field.equals("retweeted")) {
          displayTweet.setRetweeted(null);
        }
      }
      return displayTweet;
    }
    return tweet;
  }

  private void validateId(String id) {
    if (!id.matches("[0-9]+") || id == null) {
      throw new IllegalArgumentException("Tweet Id must only contain digits");
    }
    //Long.parseLong(id);
  }

  @Override
  public List<Tweet> deleteTweets(String[] ids) {
    List<Tweet> deleteTweets = new ArrayList<>();
    Arrays.stream(ids).forEach(i -> validateId(i));
    Arrays.stream(ids).forEach(i -> deleteTweets.add((Tweet) dao.deleteById(i)));
    return deleteTweets;
  }

}
