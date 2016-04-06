package com.codepath.apps.mytwitterclient.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.mytwitterclient.ProfileActivity;
import com.codepath.apps.mytwitterclient.R;
import com.codepath.apps.mytwitterclient.TweetsAdapter;
import com.codepath.apps.mytwitterclient.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetsListFragment extends Fragment {

  private ArrayList<Tweet> tweets;
  private TweetsAdapter aTweets;
  ListView lvTweets;
  private ProgressDialog progress;
//  @Bind(R.id.lvTweets);

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
//    ButterKnife.bind(v);
    lvTweets = (ListView) v.findViewById(R.id.lvTweets);
    lvTweets.setAdapter(aTweets);
    lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Tweet t = (Tweet) lvTweets.getItemAtPosition(position);
        String screenName = t.getUser().getScreenName();
        Intent i = new Intent(getActivity(), ProfileActivity.class);
        i.putExtra("screen_name", screenName);
        startActivity(i);
      }
    });
    return v;
  }

  // creation lifecyle event

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    tweets = new ArrayList<>();
    aTweets = new TweetsAdapter(getActivity(), tweets);
  }

  public void addAll(List<Tweet> list) {
    aTweets.addAll(list);
  }

  public void addToBeginning(Tweet tweet) {
    tweets.add(0, tweet);
    aTweets.notifyDataSetChanged();
  }

  public long lastTweetID() {
    return tweets.get(tweets.size() - 1).getUid();
  }

  public void progressOn(String str) {
    progress = new ProgressDialog(getActivity());
    progress.setMessage(str);
    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progress.setIndeterminate(true);
    progress.show();
  }

  public void progressOff() {
    progress.hide();
  }
}
