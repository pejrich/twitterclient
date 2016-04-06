package com.codepath.apps.mytwitterclient;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
  public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
  public static final String REST_URL = "https://api.twitter.com/1.1";
  public static final String REST_CONSUMER_KEY = "jMQ1T7s0DBLEq0bsnCbqylUGQ";
  public static final String REST_CONSUMER_SECRET = "293G4hdIqRQzg9WUW0CmeLc2RN2FvSHUzmI9BEkJZ71RxSh5k9";
  public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets";

  public TwitterClient(Context context) {
    super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
  }

  // CHANGE THIS
  // DEFINE METHODS for different API endpoints here
  public void getInterestingnessList(AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
    // Can specify query string params directly or through RequestParams.
    RequestParams params = new RequestParams();
    params.put("format", "json");
    client.get(apiUrl, params, handler);
  }

  // METHOD => ENDPOINT

  // HomeTimeline - Get us the home timeline
  public void getHomeTimeline(long since, AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/home_timeline.json");
    // Specify the params
    RequestParams params = new RequestParams();
    params.put("count", 25);
    params.put("since_id", since);
    // Execute the request
    getClient().get(apiUrl, params, handler);
  }

  public void getMentionsTimeline(long since, AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/mentions_timeline.json");
    RequestParams params = new RequestParams();
    params.put("count", 25);
    params.put("since_id", since);
    // Execute the request
    getClient().get(apiUrl, params, handler);
  }

  public void postTweet(String tweet, AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/update.json");
    RequestParams params = new RequestParams();
    params.put("status", tweet);
    getClient().post(apiUrl, params, handler);
  }

  public void getUserTimeline(String screenName, long since, AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/user_timeline.json");
    RequestParams params = new RequestParams();
    params.put("count", 25);
    params.put("screen_name", screenName);
    params.put("since_id", since);
    getClient().get(apiUrl, params, handler);
  }

  public void getFriendsList(String screenName, final AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("friends/ids.json");
    RequestParams params = new RequestParams();
    params.put("screen_name", screenName);
    params.put("count", 20);
    getClient().get(apiUrl, params, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
        try {
          JSONArray ids = jsonObject.getJSONArray("ids");
          String apiUrl = getApiUrl("users/lookup.json");
          RequestParams params = new RequestParams();
          params.put("user_id", ids);
          getClient().get(apiUrl, params, handler);
        } catch (JSONException e) {}
      }
    });
  }

  public void getFollowersList(String screenName, final AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("followers/ids.json");
    RequestParams params = new RequestParams();
    params.put("screen_name", screenName);
    params.put("count", 20);
    getClient().get(apiUrl, params, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
        try {
          JSONArray ids = jsonObject.getJSONArray("ids");
          String apiUrl = getApiUrl("users/lookup.json");
          RequestParams params = new RequestParams();
          params.put("user_id", ids);
          getClient().get(apiUrl, params, handler);
        } catch (JSONException e) {}
      }
    });
  }

  /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
   *    i.e getApiUrl("statuses/home_timeline.json");
   * 2. Define the parameters to pass to the request (query or body)
   *    i.e RequestParams params = new RequestParams("foo", "bar");
   * 3. Define the request method and make a call to the client
   *    i.e client.get(apiUrl, params, handler);
   *    i.e client.post(apiUrl, params, handler);
   */
}