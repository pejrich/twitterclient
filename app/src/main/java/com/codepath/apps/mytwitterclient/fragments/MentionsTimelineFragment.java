package com.codepath.apps.mytwitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.mytwitterclient.EndlessScrollListener;
import com.codepath.apps.mytwitterclient.TwitterApplication;
import com.codepath.apps.mytwitterclient.TwitterClient;
import com.codepath.apps.mytwitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class MentionsTimelineFragment extends TweetsListFragment {
  private TwitterClient client;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, parent, savedInstanceState);
//    populateTimeline();
    setupScrollListener();
    return v;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    client = TwitterApplication.getRestClient(); // Get singleton client
    populateTimeline();
  }

  private void populateTimeline() {
    progressOn("Fetching Tweets");
    // send api request to get timeline json
    client.getMentionsTimeline(1, new JsonHttpResponseHandler() {
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
        client.getMentionsTimeline(lastTweetID(), new JsonHttpResponseHandler() {
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
