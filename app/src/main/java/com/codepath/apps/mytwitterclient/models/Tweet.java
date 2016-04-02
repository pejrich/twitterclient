package com.codepath.apps.mytwitterclient.models;

import android.util.Log;

import com.codepath.apps.mytwitterclient.DateFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Tweet implements Serializable {
  String body;
  long uid; // Unique ID for tweet
  User user; // Store embedded user object
  String createdAt;

  public Tweet() {
  }

  // Deserialize JSON
  public static Tweet fromJSON(JSONObject jsonObject) {
    Tweet tweet = new Tweet();

    try {
      tweet.body        = jsonObject.getString("text");
      tweet.uid         = jsonObject.getLong("id");
      tweet.createdAt   = jsonObject.getString("created_at");
      tweet.user        = User.fromJSON(jsonObject.getJSONObject("user"));
    } catch (JSONException e) { e.printStackTrace(); }

    return tweet;
  }

  public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
    ArrayList<Tweet> tweets = new ArrayList<>();
    for (int i = 0; i < jsonArray.length(); i++) {
      try {
        JSONObject obj = (JSONObject) jsonArray.get(i);
        Tweet tweet = Tweet.fromJSON(obj);
        if (tweet != null) {
          tweets.add(tweet);
        }
      } catch (JSONException e) {
        e.printStackTrace();
        continue;
      }
    }
    return tweets;
  }

  public String getBody() {
    return body;
  }

  public long getUid() {
    return uid;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getCreatedAtInWords() {
    Date date = null;
    final String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    SimpleDateFormat format = new SimpleDateFormat(TWITTER);
    try {
      date = format.parse(createdAt);
      Log.i("logger", "\nn\ne\nw\n");
      Log.i("logger", "Created " + this.createdAt + "   " + this.getUser().getScreenName() + "    " + this.body);
      Log.i("logger", "parsed date is " + date);
    } catch (ParseException e) { e.printStackTrace(); }
    if (date != null) {
      return DateFormatter.timeAgoInWords(date);
    } else {
      return "";
    }
  }

  public User getUser() {
    return user;
  }
}
