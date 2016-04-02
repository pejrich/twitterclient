package com.codepath.apps.mytwitterclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mytwitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {

  private TwitterClient client;
  private ArrayList<Tweet> tweets;
  private TweetsArrayAdapter aTweets;
  @Bind(R.id.lvTweets) ListView lvTweets;
  private ProgressDialog progress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);
    client = TwitterApplication.getRestClient(); // Get singleton client
    ButterKnife.bind(this);
    initVars();
    populateTimeline();
    lvTweets.setOnScrollListener(new EndlessScrollListener() {
      @Override
      public boolean onLoadMore(int page, int totalItemsCount) {
        client.getHomeTimeline(tweets.get(tweets.size() - 1).getUid(), new JsonHttpResponseHandler() {
          @Override
          public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
            aTweets.addAll(Tweet.fromJSONArray(jsonArray));
            progress.hide();
          }

          @Override
          public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.i("logger", "Failure: " + errorResponse.toString());
            progress.hide();
            Toast.makeText(getApplicationContext(), "Error loading tweets", Toast.LENGTH_SHORT).show();
          }
        });
        return true;
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.activity_timeline_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.new_tweet:
        Intent i = new Intent(this, NewTweetActivity.class);
        startActivityForResult(i, 1);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    // Check which request we're responding to
    if (requestCode == 1) {
      // Make sure the request was successful
      if (resultCode == 0) {
        String tweetStr = (String) data.getSerializableExtra("responseString");
        try {
          JSONObject obj = new JSONObject(tweetStr);
          Tweet newTweet = Tweet.fromJSON(obj);
          tweets.add(0, newTweet);
          aTweets.notifyDataSetChanged();
        } catch (JSONException e) { e.printStackTrace(); }
      } else if (resultCode == 1) {}
    }
  }

  private void initVars() {
    tweets = new ArrayList<>();
    aTweets = new TweetsArrayAdapter(this, tweets);
    lvTweets.setAdapter(aTweets);
  }

  private void populateTimeline() {
    progress = new ProgressDialog(this);
    progress.setMessage("Fetching tweets");
    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progress.setIndeterminate(true);
    progress.show();
    // send api request to get timeline json
    client.getHomeTimeline(1, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
        aTweets.addAll(Tweet.fromJSONArray(jsonArray));
        progress.hide();
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.i("logger", "Failure: " + errorResponse.toString());
        progress.hide();
        Toast.makeText(getApplicationContext(), "Error loading tweets", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
