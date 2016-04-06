package com.codepath.apps.mytwitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mytwitterclient.EndlessScrollListener;
import com.codepath.apps.mytwitterclient.TwitterApplication;
import com.codepath.apps.mytwitterclient.TwitterClient;
import com.codepath.apps.mytwitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserTimelineFragment extends TweetsListFragment {
  private TwitterClient client;
  private String screenName;


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    client = TwitterApplication.getRestClient(); // Get singleton client
    populateTimeline();
    setupScrollListener();
  }

  public static UserTimelineFragment newInstance(String screenName) {
    UserTimelineFragment userFragment = new UserTimelineFragment();
    Bundle args = new Bundle();
    args.putString("screen_name", screenName);
    userFragment.setArguments(args);
    return userFragment;
  }

  private void populateTimeline() {
    progressOn("Fetching Tweets");
    // send api request to get timeline json
    screenName = getArguments().getString("screen_name");
    client.getUserTimeline(screenName, 0, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
        addAll(Tweet.fromJSONArray(jsonArray));
        progressOff();
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.i("logger", "Failure: " + errorResponse.toString());
        progressOff();
        Toast.makeText(getContext(), "Error loading tweets", Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void setupScrollListener() {
    lvTweets.setOnScrollListener(new EndlessScrollListener() {
      @Override
      public boolean onLoadMore(int page, int totalItemsCount) {
        client.getUserTimeline(screenName, lastTweetID(), new JsonHttpResponseHandler() {
          @Override
          public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
            addAll(Tweet.fromJSONArray(jsonArray));
            progressOff();
          }

          @Override
          public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.i("logger", "Failure: " + errorResponse.toString());
            progressOff();
            Toast.makeText(getActivity(), "Error loading tweets", Toast.LENGTH_SHORT).show();
          }
        });
        return true;
      }
    });
  }
}
