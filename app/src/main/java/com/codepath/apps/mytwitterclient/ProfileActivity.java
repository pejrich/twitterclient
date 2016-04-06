package com.codepath.apps.mytwitterclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.codepath.apps.mytwitterclient.fragments.FollowersListFragment;
import com.codepath.apps.mytwitterclient.fragments.FriendsListFragment;
import com.codepath.apps.mytwitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.mytwitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
  TwitterClient client;
  User user;
  String screenName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    client = TwitterApplication.getRestClient(); // Get singleton client
    // get the view pager
    ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
    // set the viewpager adapter for the page
    vpPager.setAdapter(new ProfileTabsAdapter(getSupportFragmentManager()));
    // find the sliding tab strip
    PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
    // attach the tabstrip to the viewpager
    tabStrip.setViewPager(vpPager);
    // get string name
    screenName = getIntent().getStringExtra("screen_name");

//    client.getUserInfo(screenName, new JsonHttpResponseHandler() {
//      @Override
//      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//        user = User.fromJSON(response);
//        populateProfileHeader(user);
////        getSupportActionBar().setTitle(user.getScreenName());
//      }
//    });

    client.getUserTimeline(screenName, 0,
            new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
        try {
          JSONObject firstPost = (JSONObject) jsonArray.get(0);
          JSONObject userJSON = (JSONObject) firstPost.getJSONObject("user");
          User user = User.fromJSON(userJSON);
          populateProfileHeader(user);
        } catch (JSONException e) {}
      }
    });

  }

  private void populateProfileHeader(User user) {
    ImageView ivUserImage = (ImageView) findViewById(R.id.ivUserImage);
    TextView tvName = (TextView) findViewById(R.id.tvName);
    TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
    TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
    TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
    TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
    tvName.setText(user.getName());
    tvScreenName.setText(user.getScreenName());
    tvTagline.setText(user.getTagline());
    tvFollowers.setText(user.getFollowersCount() + " Followers");
    tvFollowing.setText(user.getFollowingCount() + " Following");
    Glide.with(getApplicationContext()).load(user.getProfileImageUrl()).into(ivUserImage);
  }

  public class ProfileTabsAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = { "Tweets", "Friends", "Followers" };

    public ProfileTabsAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      if (position == 0) {
        return UserTimelineFragment.newInstance(screenName);
      } else if (position == 1) {
        return FriendsListFragment.newInstance(screenName);
      } else if (position == 2) {
        return FollowersListFragment.newInstance(screenName);
      } else {
        return null;
      }
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return tabTitles[position];
    }

    @Override
    public int getCount() {
      return tabTitles.length;
    }
  }

}
