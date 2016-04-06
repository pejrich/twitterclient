package com.codepath.apps.mytwitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mytwitterclient.TwitterApplication;
import com.codepath.apps.mytwitterclient.TwitterClient;
import com.codepath.apps.mytwitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsListFragment extends UserListFragment {
  private TwitterClient client;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    client = TwitterApplication.getRestClient(); // Get singleton client
    populateTimeline();
  }

  public static FriendsListFragment newInstance(String screenName) {
    FriendsListFragment userFragment = new FriendsListFragment();
    Bundle args = new Bundle();
    args.putString("screen_name", screenName);
    userFragment.setArguments(args);
    return userFragment;
  }

  private void populateTimeline() {
    progressOn("Fetching Users");
    // send api request to get timeline json
    String screenName = getArguments().getString("screen_name");
    Log.i("logger", "screenName is " + screenName);
    client.getFriendsList(screenName, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
        progressOff();
        for (int i = 0; i < jsonArray.length(); i++) {
          try {
            JSONObject userJSON = jsonArray.getJSONObject(i);
            add(User.fromJSON(userJSON));
          } catch (JSONException e) {}
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        progressOff();
        Toast.makeText(getContext(), "Error loading tweets", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
