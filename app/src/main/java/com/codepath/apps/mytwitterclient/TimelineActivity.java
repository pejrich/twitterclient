package com.codepath.apps.mytwitterclient;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mytwitterclient.fragments.HomeTimelineFragment;
import com.codepath.apps.mytwitterclient.fragments.MentionsTimelineFragment;
import com.codepath.apps.mytwitterclient.fragments.TweetsListFragment;
import com.codepath.apps.mytwitterclient.models.Tweet;

import org.json.JSONException;
import org.json.JSONObject;

public class TimelineActivity extends AppCompatActivity {

  private TweetsListFragment fragmentTweets;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);

    if (isOnline(this)) {
      // get the view pager
      ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
      // set the viewpager adapter for the page
      vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
      // find the sliding tab strip
      PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
      // attach the tabstrip to the viewpager
      tabStrip.setViewPager(vpPager);
      if (savedInstanceState == null) {
//      fragmentTweets = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
      }
    } else {
      Toast.makeText(this, "No Internet Connection. This app requires Internet", Toast.LENGTH_LONG).show();
    }
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

  public void onProfileView(MenuItem mi) {
    Intent i = new Intent(this, ProfileActivity.class);
    startActivity(i);
  }

  @Override
  protected void onActivityResult ( int requestCode, int resultCode, Intent data){

    // Check which request we're responding to
    if (requestCode == 1) {
      // Make sure the request was successful
      if (resultCode == 0) {
        String tweetStr = (String) data.getSerializableExtra("responseString");
        try {
          JSONObject obj = new JSONObject(tweetStr);
          Tweet newTweet = Tweet.fromJSON(obj);
          fragmentTweets.addToBeginning(newTweet);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      } else if (resultCode == 1) {
      }
    }
  }

  // return the order of the fragments in the view pager

  public class TweetsPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = { "Home", "Mentions" };

    public TweetsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      if (position == 0) {
        return new HomeTimelineFragment();
      } else if (position == 1) {
        return new MentionsTimelineFragment();
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

  public boolean isOnline(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
  }
}
