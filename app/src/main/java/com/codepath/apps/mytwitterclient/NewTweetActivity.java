package com.codepath.apps.mytwitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class NewTweetActivity extends AppCompatActivity {

  public int TWITTER_MAX_POST_LENGTH = 140;

  private EditText etText;
  private Button btnPostTweet;
  private Button btnCancel;
  private TwitterClient client;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_tweet);
    setVars();
  }

  private void setVars() {
    client = TwitterApplication.getRestClient(); // Get singleton client
    etText = (EditText) findViewById(R.id.etTweetText);
    btnPostTweet = (Button) findViewById(R.id.btnPostTweet);
    btnPostTweet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        postTweet();
      }
    });
    btnCancel = (Button) findViewById(R.id.btnCancel);
    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setResult(1);
        finish();
      }
    });
  }

  private void postTweet() {
    String text = etText.getText().toString();
    if (text.length() > TWITTER_MAX_POST_LENGTH) {
      Toast.makeText(this, "Tweet is too long", Toast.LENGTH_SHORT).show();
    } else if (text.length() == 0) {
      Toast.makeText(this, "Tweet is too short", Toast.LENGTH_SHORT).show();
    } else {
      client.postTweet(text, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
          Toast.makeText(getApplicationContext(), "Tweet posted successfully", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent();
          intent.putExtra("responseString", jsonObject.toString());
          setResult(0, intent);
          finish();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          Toast.makeText(getApplicationContext(), "Issue posting tweet", Toast.LENGTH_SHORT).show();
          Log.i("error", "Tweet failure: " + errorResponse.toString());
        }
      });
    }
  }

}
