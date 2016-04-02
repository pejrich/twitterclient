package com.codepath.apps.mytwitterclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mytwitterclient.models.Tweet;
import com.codepath.apps.mytwitterclient.models.User;

import java.util.ArrayList;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

  public TweetsArrayAdapter(Context context, ArrayList<Tweet> tweets) {
    super(context, android.R.layout.simple_list_item_1 ,tweets);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Tweet tweet = getItem(position);
    User user = tweet.getUser();
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item, parent, false);
    }
    TextView tvBody = (TextView) convertView.findViewById(R.id.tvTweetBody);
    tvBody.setText(tweet.getBody());
    TextView tvUserName = (TextView) convertView.findViewById(R.id.tvTweetUserName);
    tvUserName.setText(user.getName() + " (" + user.getScreenName() + ")");
    TextView tvCreatedAt = (TextView) convertView.findViewById(R.id.tvTweetCreatedAt);
    tvCreatedAt.setText(tweet.getCreatedAtInWords());
    ImageView ivUser = (ImageView) convertView.findViewById(R.id.ivTweetUserImage);
    Glide.with(getContext()).load(user.getProfileImageUrl()).into(ivUser);
    return convertView;
  }
}
