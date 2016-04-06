package com.codepath.apps.mytwitterclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
  private String name;
  private String screenName;
  private String tagline;
  private long uid;
  private String profileImageUrl;
  private long followersCount;
  private long followingCount;

  public User() {
  }

  public static User fromJSON(JSONObject jsonObject) {
    User user = new User();
    try {
      user.name               = jsonObject.getString("name");
      user.screenName         = jsonObject.getString("screen_name");
      user.uid                = jsonObject.getLong("id");
      user.profileImageUrl    = jsonObject.getString("profile_image_url");
      user.followersCount     = jsonObject.getLong("followers_count");
      user.followingCount     = jsonObject.getLong("friends_count");
      user.tagline            = jsonObject.getString("description");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return user;
  }

  public String getName() {
    return name;
  }

  public String getScreenName() {
    return screenName;
  }

  public long getUid() {
    return uid;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public long getFollowersCount() {
    return followersCount;
  }

  public long getFollowingCount() {
    return followingCount;
  }

  public String getTagline() {
    return tagline;
  }
}
