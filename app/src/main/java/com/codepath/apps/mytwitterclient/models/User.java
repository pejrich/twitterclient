package com.codepath.apps.mytwitterclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
  private String name;
  private String screenName;
  private long uid;
  private String profileImageUrl;
  private long followersCount;

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
}
